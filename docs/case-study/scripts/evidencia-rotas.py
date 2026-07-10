#!/usr/bin/env python3
"""Evidência primária da PP2: ausência da rota no OpenAPI e no atendimento HTTP.

Para cada variante de produção, sobe o contêiner, extrai o documento OpenAPI e
requisita as rotas de negócio dos três modos, registrando o status HTTP.

Esta é a evidência **primária** da PP2: ela independe das convenções internas do
Quarkus (`_Bean`, `$quarkusrestinvoker$`) sobre as quais repousa a evidência
corroborativa da inspeção de bytecode. Ver AVALIACAO.md, seção 7.2.

Nenhum alvo de escrita é fixo. Antes das requisições, o script **lista os pedidos**
da própria variante e deriva dela tudo o que precisa:

  - para o PATCH, um pedido e uma entrega existentes, mais um avanço de status
    legítimo (`OrderService.updateStatus` só aceita status de `id` maior que o
    atual);
  - para o POST, um `NewOrderDTO` válido: cliente real, um endereço e um contato
    seus, e um SKU cujo `unitPrice` coincide com o preço do catálogo.

Corpo vazio produziria `400`, e alvo inexistente produziria `404` — o primeiro
prova apenas que o manipulador executou; o segundo é ambíguo, pois tanto pode
significar "não há rota" quanto "a rota existe e o recurso não". Com recursos
reais, as rotas expostas respondem `201 Created` e `202 Accepted`: evidência
positiva de que a operação se completa.

A coleta MUTA o estado do banco — é o que prova que as rotas funcionam. A cada
execução, a entrega alvo avança dois estados e dois pedidos são criados.

Pré-requisitos:
  make deps/up/populated                                   # Postgres migrado e populado
  make container/build/variants && make container/build    # as quatro imagens

Uso e leitura da saída: docs/case-study/scripts/README.md.
"""

import json
import subprocess
import sys
import time
import urllib.error
import urllib.request
from decimal import Decimal

REDE = "modular-bff_default"
POSTGRES = "jdbc:postgresql://postgres-db:5432/postgres"

# variante -> (imagem, porta no hospedeiro)
VARIANTES = {
    "bff-default":  ("bff-default:latest", 9101),
    "bff-internal": ("bff-internal:latest", 9102),
    "bff-external": ("bff-external:latest", 9103),
    "linha-base":   ("bff:latest", 9104),
}

# edu.uerj.domain.enums.OrderStatus: (id, nome da constante, descrição persistida)
ORDER_STATUS = [
    (0, "NEW_ORDER", "Novo Pedido"),
    (1, "WAITING_PAYMENT", "Aguardando Pagamento"),
    (2, "UNDER_FRAUD_ANALYSIS", "Em análise de fraude"),
    (3, "PAID", "Pagamento efetuado"),
    (4, "INVOICE_BEGINNING", "Faturamento iniciado"),
    (5, "INVOICED", "Faturamento realizado"),
    (6, "SEPARATION_BEGINNING", "Separação iniciada"),
    (7, "SEPARATION_FINISHED", "Separação realizada"),
    (8, "DELIVERED", "Produto Entregue"),
]
POR_DESCRICAO = {d.lower(): (i, n) for i, n, d in ORDER_STATUS}
NOME_POR_ID = {i: n for i, n, _ in ORDER_STATUS}

LISTA = "/v1/order/list?limitPerPage=20&page=0"
CRIACAO = "/v1/order/create"
TEMPLATE_STATUS = "/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}"

# Qual modo cada variante deve expor, segundo a tese.
ESPERADO = {
    "bff-default":  {"geral"},
    "bff-internal": {"geral", "interno"},
    "bff-external": {"geral", "externo"},
    "linha-base":   {"geral", "externo", "interno"},
}

# Status que indicam que a requisição NÃO alcançou manipulador algum.
#
# 405 é tão indicativo de ausência quanto 404: `/v1/order/create` casa com o
# template `/v1/order/{orderId}`, atendido só por GET; logo, um POST àquela URI,
# na ausência do controller externo, encontra o recurso mas não o método.
SEM_MANIPULADOR = (404, 405)


def docker(*args, **kw):
    return subprocess.run(["docker", *args], capture_output=True, text=True, **kw)


def imagem_existe(tag):
    return bool(docker("images", "-q", tag).stdout.strip())


def requisitar(porta, metodo, caminho, corpo=None, timeout=10):
    """Devolve (status, corpo). Status None se a conexão falhar."""
    req = urllib.request.Request(f"http://localhost:{porta}{caminho}", method=metodo)
    req.add_header("Accept", "application/json")
    if corpo is not None:
        req.add_header("Content-Type", "application/json")
        req.data = corpo.encode()
    try:
        with urllib.request.urlopen(req, timeout=timeout) as r:
            return r.status, r.read()
    except urllib.error.HTTPError as e:
        return e.code, e.read()
    except (urllib.error.URLError, TimeoutError, ConnectionResetError):
        return None, b""


def esperar_de_pe(porta, tentativas=60):
    for _ in range(tentativas):
        if requisitar(porta, "GET", "/health", timeout=2)[0] is not None:
            return True
        time.sleep(1)
    return False


def openapi_paths(porta):
    status, corpo = requisitar(porta, "GET", "/openapi")
    return sorted(json.loads(corpo).get("paths", {})) if status == 200 else []


def listar_pedidos(porta):
    """Pedidos da variante. `parse_float=Decimal` preserva a escala dos preços.

    A validação de `createOrder` compara `unitPrice == product.price` com a
    igualdade de `BigDecimal`, que é sensível à escala: `2500.0` reprova onde
    `2500.00` aprova. Ler os preços como `float` destruiria a escala.
    """
    status, corpo = requisitar(porta, "GET", LISTA)
    if status != 200:
        sys.exit(f"erro: GET {LISTA} devolveu {status}. A listagem é o controle "
                 "do experimento e precisa responder.")
    itens = json.loads(corpo, parse_float=Decimal).get("items") or []
    if not itens:
        sys.exit("erro: a listagem devolveu zero pedidos.\n"
                 "Sem um pedido real, o PATCH devolveria 404 por ausência de "
                 "recurso, e não de rota — status ambíguo.\n"
                 "Rode: make deps/up/populated")
    return itens


def montar_pedido(pedidos):
    """Monta um NewOrderDTO válido a partir de dados obtidos nos endpoints.

    Todo identificador vem da listagem: o cliente, um endereço e um contato seus,
    um SKU e o preço exato daquele produto. Enviar um corpo vazio produziria 400 —
    prova de que o manipulador executou, mas não de que a operação funciona.
    """
    for pedido in pedidos:
        cliente = pedido.get("customer") or {}
        enderecos = cliente.get("addresses") or []
        contatos = cliente.get("contacts") or []
        if not (cliente.get("id") and enderecos and contatos):
            continue
        for entrega in pedido.get("deliverys") or []:
            for item in entrega.get("items") or []:
                produto = item.get("product") or {}
                if not produto.get("sku"):
                    continue
                metodo = ((pedido.get("payments") or [{}])[0].get("method")) or "CREDIT_CARD"
                preco = produto["price"]          # Decimal, escala preservada
                # JSON montado à mão: json.dumps converteria Decimal em string.
                corpo = (
                    '{'
                    f'"customerId": {cliente["id"]}, '
                    f'"addressId": {enderecos[0]["id"]}, '
                    f'"contactId": {contatos[0]["id"]}, '
                    f'"items": [{{"productSku": "{produto["sku"]}", '
                    f'"quantity": 1, "unitPrice": {preco}}}], '
                    f'"paymentMethods": [{{"method": "{metodo}", "installments": 1, '
                    f'"installmentValue": {preco}, "discount": 0}}]'
                    '}'
                )
                legivel = (f"cliente {cliente['id']}, endereço {enderecos[0]['id']}, "
                           f"contato {contatos[0]['id']}, SKU {produto['sku']} a {preco}")
                return corpo, legivel

    sys.exit("erro: nenhum pedido da listagem traz cliente com endereço, contato e "
             "item com produto.\nRode: make deps/restart/populated")


def swagger_projeta_openapi(porta):
    """A UI é servida e busca o documento OpenAPI?

    Não se verifica o que a UI renderiza: sendo projeção do documento, não pode
    exibir rota que ele não contenha. Ver AVALIACAO.md, 7.2.4.
    """
    req = urllib.request.Request(f"http://localhost:{porta}/docs")
    try:
        with urllib.request.urlopen(req, timeout=10) as r:
            return r.status, ("/openapi" in r.read().decode("utf-8", "replace"))
    except urllib.error.HTTPError as e:
        return e.code, False


def escolher_alvo(pedidos):
    """Alvo real para o PATCH, escolhido entre os pedidos listados.

    Devolve (uri, descricao_legivel) ou aborta. Percorre os pedidos em busca de
    uma entrega cujo status admita avanço — `updateStatus` só aceita status de
    `id` estritamente maior que o atual.
    """
    for pedido in pedidos:
        for entrega in pedido.get("deliverys") or []:
            atual = POR_DESCRICAO.get((entrega.get("status") or "").lower())
            if atual and atual[0] < 8:
                proximo = NOME_POR_ID[atual[0] + 1]
                uri = f"/v1/order/{pedido['id']}/delivery/{entrega['id']}/{proximo}"
                legivel = (f"pedido {pedido['id']}, entrega {entrega['id']}: "
                           f"{atual[1]} → {proximo}")
                return uri, legivel

    sys.exit("erro: nenhuma entrega admite avanço de status (todas em DELIVERED).\n"
             "Rode: make deps/restart/populated")


def coletar(variante, imagem, porta):
    nome = f"evid-{variante}"
    docker("rm", "-f", nome)
    r = docker("run", "-d", "--name", nome, "--network", REDE,
               "-e", "API_PORT=9000", "-e", f"POSTGRES_URL={POSTGRES}",
               "-p", f"{porta}:9000", imagem)
    if r.returncode:
        sys.exit(f"erro ao subir {variante}: {r.stderr.strip()}")

    try:
        if not esperar_de_pe(porta):
            sys.exit(f"erro: {variante} não respondeu em /health")

        paths = openapi_paths(porta)

        # Ambos os alvos são derivados da própria variante, imediatamente antes
        # das requisições: a listagem confirma que os recursos existem.
        pedidos = listar_pedidos(porta)
        uri_status, alvo_patch = escolher_alvo(pedidos)
        corpo_pedido, alvo_post = montar_pedido(pedidos)

        status = {
            "geral":   requisitar(porta, "GET", LISTA)[0],
            "externo": requisitar(porta, "POST", CRIACAO, corpo=corpo_pedido)[0],
            "interno": requisitar(porta, "PATCH", uri_status)[0],
        }
        return paths, status, swagger_projeta_openapi(porta), (alvo_patch, alvo_post)
    finally:
        docker("rm", "-f", nome)


def main():
    faltando = [t for t, _ in VARIANTES.values() if not imagem_existe(t)]
    if faltando:
        sys.exit("erro: imagens ausentes: " + ", ".join(faltando)
                 + "\nrode: make container/build/variants && make container/build")

    resultados = {}
    for variante, (imagem, porta) in VARIANTES.items():
        print(f"▶ {variante} ({imagem}) …", flush=True)
        resultados[variante] = coletar(variante, imagem, porta)

    print("\n── Alvos derivados da listagem de cada variante ──────────────")
    for variante, (_, _, _, (alvo_patch, alvo_post)) in resultados.items():
        print(f"{variante}")
        print(f"    PATCH  {alvo_patch}")
        print(f"    POST   {alvo_post}")

    print("\n── Rotas de negócio no documento OpenAPI ─────────────────────")
    for variante, (paths, _, _, _) in resultados.items():
        print(f"\n{variante}:")
        for p in [p for p in paths if p.startswith("/v1/order")]:
            print(f"    {p}")

    print("\n── Status HTTP por modo ──────────────────────────────────────")
    print(f"{'variante':<14}{'geral (GET)':>14}{'externo (POST)':>17}{'interno (PATCH)':>18}")
    for variante, (_, status, _, _) in resultados.items():
        print(f"{variante:<14}{status['geral']!s:>14}"
              f"{status['externo']!s:>17}{status['interno']!s:>18}")

    print("\n── Conferência com a tese ────────────────────────────────────")
    print("critério primário:      a rota consta do OpenAPI da variante?")
    print("critério corroborativo: a requisição alcança um manipulador?\n")
    print(f"{'variante':<14}{'modo':<10}{'esperado':<12}{'OpenAPI':<10}{'HTTP':<7}veredito")

    templates = {"geral": "/v1/order/list", "externo": CRIACAO, "interno": TEMPLATE_STATUS}
    ok = True
    for variante, (paths, status, _, _) in resultados.items():
        for modo, code in status.items():
            exposta = modo in ESPERADO[variante]
            no_openapi = templates[modo] in paths
            alcancou = code not in SEM_MANIPULADOR
            conforme = (no_openapi == exposta) and (alcancou == exposta)
            ok &= conforme
            print(f"{variante:<14}{modo:<10}"
                  f"{'exposta' if exposta else 'suprimida':<12}"
                  f"{'presente' if no_openapi else 'ausente':<10}"
                  f"{code!s:<7}{'ok' if conforme else 'DIVERGE'}")

    print("\nNas variantes que expõem a operação, o 201 (POST) e o 202 (PATCH) provam\n"
          "que o manipulador executou e concluiu sobre recursos reais. O 405 no POST\n"
          "das demais decorre de /v1/order/create casar com o template\n"
          "/v1/order/{orderId}, atendido apenas por GET.")

    print("\n── Swagger UI como projeção do documento ─────────────────────")
    print(f"{'variante':<14}{'GET /docs':<12}referencia /openapi")
    for variante, (_, _, (status, refere), _) in resultados.items():
        print(f"{variante:<14}{status!s:<12}{'sim' if refere else 'não'}")

    print("\n" + ("✓ todas as variantes conformam com a tese"
                  if ok else "✗ há divergências — investigar antes de publicar"))


if __name__ == "__main__":
    main()
