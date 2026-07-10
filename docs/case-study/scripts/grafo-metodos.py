#!/usr/bin/env python3
"""Reuso por alcançabilidade a partir do ponto de entrada (PP1, PP3).

Extrai o grafo de chamadas entre métodos declarados em `edu.uerj`, a partir do
bytecode compilado, e calcula o fecho transitivo desde os endpoints de cada
controller. Deriva, por variante, os conjuntos comum e específico de métodos.

Método descrito em docs/case-study/METRICA-REUSO.md, seção 6.
Uso e leitura da saída: docs/case-study/scripts/README.md.
"""

import re
import shutil
import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[3]
CLASSES = REPO / "build/classes/kotlin/main"
SRC = REPO / "src/main/kotlin"
PKG = "edu/uerj"
JDK_IMAGE = "amazoncorretto:21-alpine-jdk"

CONTROLLER = f"{PKG}/adapters/api/v1"
RAIZES = {
    "geral": [(f"{CONTROLLER}/OrderGeneralController", "getOrderById"),
              (f"{CONTROLLER}/OrderGeneralController", "listOrders")],
    "externo": [(f"{CONTROLLER}/OrderExternalController", "createOrder")],
    "interno": [(f"{CONTROLLER}/OrderInternalController", "updateStatusOrder")],
}

# Um método é "declarado" quando aparece como `fun` no código-fonte Kotlin da sua
# classe. 
FUN = re.compile(r"^\s*(?:(?:override|open|private|internal|public|final|suspend)\s+)*fun\s+([\w`]+)\s*\(")


def funcoes_declaradas():
    """{fqcn-do-arquivo: {nomes de fun}}, a partir do fonte."""
    decl = {}
    for kt in SRC.rglob("*.kt"):
        fqcn = str(kt.relative_to(SRC).with_suffix("")).replace("\\", "/")
        decl[fqcn] = {m.group(1).strip("`") for m in map(FUN.match, kt.read_text().splitlines()) if m}
    return decl


DECLARADAS = {}


def javap_cmd():
    """javap local, se houver; senão, o do contêiner (mesma imagem do Dockerfile)."""
    if shutil.which("javap"):
        return lambda paths: subprocess.run(
            ["javap", "-c", "-p", *paths], capture_output=True, text=True, check=True).stdout
    if not shutil.which("docker"):
        sys.exit("erro: nem javap nem docker disponíveis")
    def run(paths):
        rel = [f"/cls/{Path(p).relative_to(CLASSES)}" for p in paths]
        return subprocess.run(
            ["docker", "run", "--rm", "-v", f"{CLASSES}:/cls:ro",
             "--entrypoint", "javap", JDK_IMAGE, "-c", "-p", *rel],
            capture_output=True, text=True, check=True).stdout
    return run


def verificar_bytecode():
    if not CLASSES.is_dir():
        sys.exit(f"erro: {CLASSES} não existe — rode `make debug/build`")
    src_mt = max(f.stat().st_mtime for f in SRC.rglob("*.kt"))
    cls_mt = max(f.stat().st_mtime for f in CLASSES.rglob("*.class"))
    if cls_mt < src_mt:
        sys.exit("erro: bytecode desatualizado em relação ao fonte — rode `make debug/build`")


# `  public final void foo(int, java.lang.String);`  → nome do método
DECL = re.compile(r"^  \S.*?\s([\w<>$]+)\(.*\);\s*$")
# `  12: invokevirtual #34  // Method edu/uerj/services/OrderService.createOrder:(...)`
CALL = re.compile(r"//\s*(?:Method|InterfaceMethod)\s+([\w/$]+)\.\"?([\w<>$]+)\"?:")
CLASSE = re.compile(r"^(?:public |final |abstract |static )*(?:class|interface|enum) ([\w.$]+)")


def construir_grafo(saida):
    """Devolve {metodo: {metodos chamados}}, restrito a proprietários em edu.uerj."""
    grafo, classe_atual, metodo_atual = {}, None, None
    for linha in saida.splitlines():
        if m := CLASSE.match(linha.strip()):
            classe_atual = m.group(1).replace(".", "/")
            metodo_atual = None
            continue
        if classe_atual and (m := DECL.match(linha)):
            metodo_atual = f"{classe_atual}.{m.group(1)}"
            grafo.setdefault(metodo_atual, set())
            continue
        if metodo_atual and (m := CALL.search(linha)):
            dono, nome = m.group(1), m.group(2)
            if dono.startswith(PKG):
                grafo[metodo_atual].add(f"{dono}.{nome}")
    return grafo


def declarado(metodo):
    # Verdadeiro se `metodo` foi escrito como `fun` no fonte da sua classe.
    dono, nome = metodo.rsplit(".", 1)
    if nome in ("<init>", "<clinit>"):
        return False
    arquivo = dono.split("$")[0]                      # Foo$Companion → Foo
    return nome in DECLARADAS.get(arquivo, ())


def alcancaveis(grafo, raizes, so_declarados):
    """Fecho transitivo. Métodos sintetizados são atravessados, mas não contados."""
    vistos, pilha = set(), [f"{c}.{m}" for c, m in raizes]
    while pilha:
        n = pilha.pop()
        if n in vistos:
            continue
        vistos.add(n)
        pilha.extend(grafo.get(n, ()))
    return {m for m in vistos if not so_declarados or declarado(m)}


def relatorio(grafo, so_declarados):
    R = {c: alcancaveis(grafo, r, so_declarados) for c, r in RAIZES.items()}
    variantes = {
        "default":  R["geral"],
        "internal": R["geral"] | R["interno"],
        "external": R["geral"] | R["externo"],
    }
    outros = {
        "default":  set(),
        "internal": R["externo"],
        "external": R["interno"],
    }
    print(f"{'variante':<12}{'|R(v)|':>8}{'comum':>8}{'específico':>13}{'Reuse per Method':>20}")
    for v, Rv in variantes.items():
        # específico = alcançável só por esta variante, via seu próprio controller
        proprio = Rv - R["geral"]
        espec = proprio - outros[v]
        comum = len(Rv) - len(espec)
        pct = comum / len(Rv) * 100 if Rv else 0.0
        print(f"{v:<12}{len(Rv):>8}{comum:>8}{len(espec):>13}{pct:>19.2f}%")
        if espec:
            for m in sorted(espec):
                print(f"{'':<12}  └─ específico: {m}")
    base = R["geral"] | R["interno"] | R["externo"]
    print(f"{'linha-base':<12}{len(base):>8}")
    return R


def detalhar(R):
    """Nomeia os conjuntos que o relatório agrega, para inspeção e para o texto."""
    curto = lambda s: sorted(m.replace(f"{PKG}/", "") for m in s)
    print("\n── Detalhe dos conjuntos ──")
    print(f"\nR(geral) — alcançável em toda variante ({len(R['geral'])} métodos):")
    for m in curto(R["geral"]):
        print(f"    {m}")
    partilhado = (R["interno"] & R["externo"]) - R["geral"]
    print(f"\nAlcançável por interno E externo, mas não pelo geral ({len(partilhado)}):")
    for m in curto(partilhado):
        print(f"    {m}")


def main():
    verificar_bytecode()
    global DECLARADAS
    DECLARADAS = funcoes_declaradas()
    run = javap_cmd()
    print(f"javap: {'local' if shutil.which('javap') else 'contêiner ' + JDK_IMAGE}")
    classes = sorted(str(p) for p in CLASSES.rglob("*.class"))
    print(f"classes analisadas: {len(classes)}")
    print(f"funções declaradas no fonte: {sum(len(v) for v in DECLARADAS.values())}\n")

    grafo = construir_grafo(run(classes))
    proprios = [m for m in grafo if declarado(m)]
    print(f"métodos no grafo: {len(grafo)} (declarados: {len(proprios)})\n")

    print("── Reuse per Method (PP3) — apenas métodos declarados ──────────────")
    R = relatorio(grafo, so_declarados=True)
    print("\n── Sensibilidade: incluindo métodos sintetizados ──")
    relatorio(grafo, so_declarados=False)

    print("\n── PP3: métodos próprios alcançáveis a partir da rede ──")
    base = R["geral"] | R["interno"] | R["externo"]
    for v, Rv in (("default", R["geral"]),
                  ("internal", R["geral"] | R["interno"]),
                  ("external", R["geral"] | R["externo"]),
                  ("linha-base", base)):
        sup = len(base) - len(Rv)
        print(f"{v:<12}{len(Rv):>6}   suprimidos frente à linha-base: {sup:>2} "
              f"({sup / len(base) * 100:.1f}%)")

    if "--detalhe" in sys.argv:
        detalhar(R)


if __name__ == "__main__":
    main()
