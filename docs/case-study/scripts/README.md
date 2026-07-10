# Scripts de coleta de evidência

Este diretório reúne **todo script executado para produzir evidência do estudo**. Nenhuma medição entra no capítulo de Avaliação sem um script versionado aqui.

## Convenção

Todo script deste diretório é documentado, neste `README.md`, com quatro informações:

1. **Propósito** — qual pergunta de pesquisa ele alimenta e o que exatamente calcula.
2. **Requisitos** — ferramentas e versões, e como obtê-las sem alterar a máquina, quando possível.
3. **Invocação exata** — o comando, a partir da raiz do repositório.
4. **Leitura da saída** — o que cada número significa e como ele entra no texto.

A razão é o requisito de **reprodutibilidade** declarado na Metodologia (seção 5.5.4 do TCC): "cada medição registra os comandos exatos e as *flags* utilizadas, de modo que um terceiro possa repetir o experimento e obter os mesmos artefatos".

Consequências práticas:

- **Uma única linguagem: Python.** Toda análise deste diretório é escrita em Python. Ferramentas externas (`cloc`, `javap`) podem ser invocadas de dentro do script, mas a lógica da medição — leitura, classificação, contagem e cálculo — vive em Python, e não em Bash, `awk` ou `sed`. A razão é a **auditabilidade**: os scripts vão inteiros para o TCC (ver a seção seguinte), e um leitor que precise conferir uma fórmula não deveria ter de decifrar aritmética de shell. A escolha da linguagem é também uma decisão de método.
- Comandos *ad hoc* de terminal **não** são evidência. Se um comando produziu um número que vai para o texto, ele vira script aqui.
- Erros encontrados durante a execução ficam **registrados**, não apagados — ver *Histórico de correções*, ao final.
- Toda ferramenta tem sua **versão impressa pelo próprio script**, e não anotada à parte.

---

## ⚠️ Transposição para o TCC final

> **Regra.** Ao implementar o capítulo de Avaliação no TCC, **cada script deste diretório deve ser transposto por inteiro para o documento, como bloco de código.** Não basta remeter ao arquivo, ao repositório ou ao caminho: o TCC é um documento autocontido, e o leitor precisa poder reconstruir a medição sem acesso a este diretório.

O que isso implica, na prática:

- **Integralidade.** O script vai completo — não um recorte, não um pseudocódigo, não "as linhas relevantes". Se ele é longo demais para o corpo do texto, vai para um **apêndice**, com remissão do capítulo ao apêndice.
- **Fidelidade.** O bloco de código reproduz o arquivo **tal como executado** para produzir os números publicados. Se o script mudar depois da coleta, ou os números são refeitos, ou o TCC passa a exibir um script que não gerou o que ele afirma.
- **Contexto.** Cada bloco de código é precedido do que este `README.md` já registra — propósito, requisitos com versões, invocação exata e leitura da saída — reescrito em prosa acadêmica, não copiado em forma de lista de instruções.
- **Limites conhecidos.** Os defeitos da tabela de *Histórico de correções* não vão para o TCC como confissão de processo, mas os **limites de método** que eles revelaram vão para as ameaças à validade, onde já estão registrados.

A razão é a mesma que motiva este diretório: uma medição que o leitor não pode repetir não é evidência. A diferença é o destinatário — aqui, quem trabalha no repositório; lá, quem lê o trabalho sem ter acesso a ele.

### ⚠️ Regra: editar um script já transposto obriga a revisar o TCC

Depois que um script destes é publicado no TCC, o bloco de código do apêndice **é** o código que produziu os números do capítulo. Editá-lo rompe esse vínculo em silêncio: o documento passa a exibir um programa que não gerou a tabela ao lado dele, e quem o executar obterá outra coisa.

**Ao editar um script já transposto, na mesma passagem:**

1. **reexecute** e colha os números de novo;
2. **confronte** com os publicados — se divergirem, o errado é o capítulo, não o script;
3. **atualize** o bloco de código no apêndice, as tabelas e afirmações que dele dependem, a *Leitura da saída* desta página e, se a edição corrigiu um defeito, o *Histórico de correções*;
4. **reavalie as ameaças à validade**, se a edição mudou o **método** e não apenas a implementação.

**Congelamento.** Iniciada a transposição de um capítulo, os scripts que ele publica só se editam por defeito que invalide um número. Melhoria de estilo ou conveniência espera a entrega.

Regra registrada também no *modus operandi* de `docs/ROADMAP-CAPITULOS-TCC.md`.

---

## `metrica-reuso.py` — reuse percent em linhas de código (PP1)

**Propósito.** Calcula o *reuse percent* (FRAKES; TERRY, 1996) em granularidade de **linhas de código**, para cada variante, mais a análise de sensibilidade que reclassifica os DTOs de uso exclusivo do modo externo. Alimenta a seção 7.4 da Avaliação.

Reporta também o *reuse level*, hoje **abandonado** como métrica do trabalho — ver `../METRICA-REUSO.md`, seção 6.1. A saída é mantida apenas como registro do que motivou o abandono.

**Requisitos.** `python3` (testado com **3.14.6**) e `cloc` (testado com **2.08**), este último invocado pelo script e cuja versão ele imprime.
No Fedora: `sudo dnf install -y cloc`.

**Invocação.**

```bash
./docs/case-study/scripts/metrica-reuso.py
```

**Leitura da saída.**

- `base comum (RSI)` — LOC de tudo que não carrega anotação de variabilidade.
- `SSI` — LOC do *controller* anotado da variante.
- `reuse %` — `RSI / (RSI + SSI) × 100`. A variante `default` dá 100% por construção: é caso-limite, não resultado favorável.
- Bloco de sensibilidade — atribui `NewOrderDTO`, `ItemInNewOrderDTO` e `PaymentInNewOrderDTO` (25 LOC) à variante externa. Mede o efeito da classificação conservadora dos DTOs.

**Escopo.** `src/main/kotlin/**`. Exclui testes, código gerado, migrações e configuração. LOC = linhas físicas, descontadas linhas em branco e comentários.

---

## `grafo-metodos.py` — Reuse per Method, instrumento da PP3

**Propósito.** Extrai o grafo de chamadas entre métodos declarados em `edu.uerj`, calcula o fecho transitivo a partir dos endpoints de cada *controller* e deriva, por variante, os conjuntos **comum** e **específico** de métodos. Produz o **Reuse per Method** e o número de métodos próprios que cada artefato torna alcançáveis a partir da rede.

Método descrito em `../METRICA-REUSO.md`, seções 6 e 7. **O Reuse per Method alimenta a PP3, não a PP1** — ver 7.1.

**Requisitos.** `python3` (testado com **3.14.6**) e um JDK com `javap` (testado com **javap 21.0.11**). O script usa o `javap` de dentro de um contêiner (`amazoncorretto:21-alpine-jdk`, a mesma imagem base do `config/docker/Dockerfile`), de modo que **não é necessário instalar um JDK na máquina** — apenas Docker. Se um `javap` local existir no `PATH`, ele é usado preferencialmente.

⚠️ O script não usa recurso algum específico de Python 3.14; qualquer 3.8+ deve servir. A versão é registrada por exigência de reprodutibilidade, não como piso de compatibilidade.

Exige o bytecode compilado em `build/classes/kotlin/main`. O script **verifica se está atualizado** em relação ao fonte e aborta se não estiver; nesse caso, rode `make debug/build`.

**Invocação.**

```bash
./docs/case-study/scripts/grafo-metodos.py             # relatório
./docs/case-study/scripts/grafo-metodos.py --detalhe   # + nomeia os conjuntos
```

A opção `--detalhe` imprime, nominalmente, o núcleo `R(geral)` alcançável em toda variante e os métodos alcançáveis pelos modos interno **e** externo, mas não pelo de leitura. Serve à inspeção dos agregados e ao texto da Avaliação, que precisa citar os métodos.

**Leitura da saída.** Por variante: `|R(v)|` é o total de métodos próprios alcançáveis a partir de seus pontos de entrada; `específico` é o subconjunto que **só** aquela variante alcança; `comum` é o restante; `Reuse per Method` é `comum / R(v) × 100`.

⚠️ **O Reuse per Method não mede duplicação.** Um método alcançável só pelo modo externo não está duplicado — ele é **exclusivo daquele fluxo**. A métrica mede **sobreposição de fluxos**. Ver `../METRICA-REUSO.md`, seção 7.1, antes de levar este número ao texto.

**Bloco PP3.** `suprimidos frente à linha-base` conta os métodos próprios que a variante **não** torna alcançáveis a partir da rede. É o número que quantifica a redução da superfície de ataque em granularidade de método.

Duas contagens são reportadas: **declarados** (apenas métodos escritos no projeto — a contagem que vale) e **com sintetizados** (incluindo acessores, `equals`, `hashCode`, `toString`, `copy`, `componentN`, `$default` e `access$`), esta última como análise de sensibilidade.

---

## `evidencia-rotas.py` — evidência primária da PP2

**Propósito.** Para cada variante de produção, sobe o contêiner, extrai o documento OpenAPI e requisita as rotas de negócio dos três modos, registrando o status HTTP. Alimenta a seção 7.2 da Avaliação.

É a evidência **primária** da PP2: independe das convenções internas do Quarkus (`_Bean`, `$quarkusrestinvoker$`) sobre as quais repousa a evidência corroborativa da inspeção de bytecode.

**Requisitos.** `python3` (biblioteca padrão apenas), `docker`, e:

```bash
make deps/up/populated                                   # Postgres migrado e populado
make container/build/variants && make container/build    # as quatro imagens
```

**Invocação.**

```bash
./docs/case-study/scripts/evidencia-rotas.py
```

**Leitura da saída.** Cinco blocos. O primeiro mostra o **alvo do `PATCH`**, derivado da listagem de cada variante. O segundo lista as rotas de negócio declaradas no OpenAPI. O terceiro tabula o status HTTP por modo. O quarto confronta ambos com a tese. O quinto verifica que o Swagger UI é servido e busca o documento OpenAPI.

**Os dois alvos de escrita são derivados, não fixados.** Antes das requisições, o script executa `GET /v1/order/list` e extrai dela tudo:

- para o `PATCH`, um pedido e uma entrega **existentes**, mais um avanço de status legítimo (`updateStatus` só aceita status de `id` maior que o atual);
- para o `POST`, um `NewOrderDTO` **válido**: cliente real, um endereço e um contato seus, e um SKU cujo `unitPrice` coincide com o preço do catálogo.

Corpo vazio produziria `400`, e alvo inexistente produziria `404` — o primeiro prova pouco, o segundo é ambíguo. Com recursos reais, as variantes que expõem as rotas respondem **`201 Created`** e **`202 Accepted`**: evidência positiva de que a operação se completa.

⚠️ **Escala decimal.** A validação compara `unitPrice == product.price` com a igualdade de `BigDecimal`, sensível à escala: `2500.0` reprova onde `2500.00` aprova. Por isso a resposta é lida com `parse_float=Decimal` e o corpo é montado à mão — `json.dumps` converteria `Decimal` em string.

⚠️ **A coleta muda o estado do banco.** O `202` avança a entrega; o `201` cria um pedido. A cada execução, o alvo do `PATCH` avança dois passos (uma vez em `bff-internal`, outra na linha de base) e dois pedidos novos surgem. Esgotados os nove estados, o script aborta com instrução para repopular. Isso é propriedade do experimento, não defeito: a evidência de que a rota **funciona** exige que ela produza efeito.

⚠️ **O status HTTP não é critério decisivo, por duas razões independentes.**

*Primeira: a ausência se manifesta como `404` **ou** `405`.* A URI `/v1/order/create` casa com o template `/v1/order/{orderId}`, atendido apenas por `GET`. Quando o *controller* externo está ausente, um `POST` àquela URI encontra o **recurso**, mas não o **método**, e recebe `405 Method Not Allowed`. Já `/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}` não casa com template algum e recebe `404`. Os dois atestam a mesma coisa; a diferença é acidente do roteamento.

*Segunda: o `404` é ambíguo.* Ele tanto pode significar "não há rota" quanto "há rota, mas o recurso não existe". A derivação do alvo pela listagem elimina o segundo sentido, e o script aborta se a listagem devolver zero pedidos ou se nenhuma entrega admitir avanço.

Por ambas, o critério **primário** de conferência é a presença da rota no documento OpenAPI; o HTTP é corroborativo.

**Sobre o Swagger UI.** O script verifica que `/docs` responde e que a página busca `/openapi` — isto é, que a interface é **projeção** do documento. Não se verifica o que ela renderiza: sendo projeção, não pode exibir rota que o documento não contenha, e observá-la seria reobservar a mesma evidência por um renderizador. Ver a seção 7.2.4 da Avaliação.

---

## Histórico de correções

Registrado por integridade: os números publicados dependem da correção destes scripts.

| Data | Script | Defeito | Efeito no resultado |
|---|---|---|---|
| 9 jul. 2026 | contagem de referências (Bash *ad hoc*) | A exclusão do próprio arquivo comparava `./caminho` com `caminho`, e falhava. Toda classe contava a si mesma como referência externa. | Todas as contagens de referência vinham com `+1`. Corrigido antes de qualquer número ir ao texto. |
| 10 jul. 2026 | `metrica-reuso.sh` (antecessor do `.py`) | O filtro da linha de totais do `cloc` testava o campo 2 (`filename`), mas o `cloc` grava `SUM` no campo 1. A linha de totais entrava na soma. | LOC total saiu como 3440, exatamente o dobro de 1720. Corrigido; conferido por `1556 + 79 + 85 = 1720`. |
| 10 jul. 2026 | `evidencia-rotas.py` | O critério de conformidade exigia `404` na rota suprimida. Mas `POST /v1/order/create` devolve `405`, porque a URI casa com o template `/v1/order/{orderId}`, atendido só por `GET`. | Três falsos negativos ("DIVERGE") em rotas corretamente suprimidas. Critério primário passou a ser a **ausência da rota no OpenAPI**; o HTTP é corroborativo, aceitando `404` **ou** `405`. O achado foi para o texto (Avaliação, 7.2.3). |
| 10 jul. 2026 | `evidencia-rotas.py` | O `POST /v1/order/create` era feito com corpo vazio (`{}`), colhendo `400`. O `PATCH` mirava `/v1/order/1/delivery/1/PAID`, alvo fixo e arbitrário. | A evidência era apenas **negativa** (o manipulador executou e recusou). Passou-se a derivar ambos os alvos da listagem: pedido, entrega e avanço válido para o `PATCH`; cliente, endereço, contato e SKU com preço exato para o `POST`. As rotas expostas passaram a responder `201` e `202`. |
| 10 jul. 2026 | `config/database/test-data/seed.sql` | O *seed* gravava o **nome da constante** do enum (`PAID`), mas o domínio persiste e lê a **descrição** (`getByDescription`, `status.description`). | Todo `PATCH` estourava `IllegalArgumentException` e devolvia `400` — que o texto interpretava como "recusa de validação". O manipulador de fato executava, mas quebrava ao ler status irreconhecível. Corrigido o *seed* para as descrições; o `PATCH` passou a devolver `202`. |
| 10 jul. 2026 | `evidencia-rotas.py` | A coleta corria com o banco **não populado**. O `PATCH` sobre o pedido `1` devolvia `404` por o pedido não existir, e não por a rota faltar — na variante que **expõe** a operação. | Dois falsos negativos. Revelou que o `404` é ambíguo (`sem rota` × `sem recurso`), segunda razão para o OpenAPI ser o critério primário. Acrescentada guarda que aborta se `GET /v1/order/1` não devolver `200`. |
| 10 jul. 2026 | imagem `bff:latest` (linha de base) | A imagem existente fora construída **sem** as *flags*, e seu OpenAPI não expunha `create` nem `status`. | A linha de base aparecia com menos rotas que as variantes especializadas — resultado absurdo, que denunciou a imagem obsoleta. Reconstruída com `make container/build`. |

| 10 jul. 2026 | `grafo-metodos.py` | O filtro de métodos sintetizados era um regex sobre o **nome** (`get[A-Z].*`), e descartava funções de negócio: `OrderGeneralController.getOrderById`, `OrderService.getOrder`, `OrderStatus.getById` e `getByDescription`. Uma das próprias raízes sumia do resultado. | O núcleo comum saía com 7 métodos errados, a variante externa aparecia com 21 métodos alcançáveis contra 13 da interna, e o *reuse percent* externo dava 47,62%. Substituído por critério de **fonte**: é declarado quem aparece como `fun` no `.kt` da classe. Números corretos: núcleo de 7, variantes simétricas em 13. |

## Histórico de mudanças estruturais

**Porte de `metrica-reuso.sh` para `metrica-reuso.py` (10 jul. 2026).** O script de LOC nasceu em Bash e foi reescrito em Python, para que toda a análise do estudo esteja em **uma única linguagem**. O porte foi validado por comparação: as duas versões produzem números idênticos — `1720` LOC totais, base comum de `1556`, *reuse percent* de 94,82% (interna) e 95,17% (externa), sensibilidade de 93,64%, e `IRL` nulo. O arquivo `.sh` foi removido.

Uma saída **não** foi portada: a versão em Bash calculava também uma *leitura alternativa* do *reuse level*, na qual nenhum item seria externo (`E = 0`) e o `IRL` passaria a contar itens usados mais de uma vez. Ela serviu à discussão que levou ao **abandono da métrica** e não sustenta nenhum número publicado; com o abandono, deixou de ter função. Fica o registro de que existiu.

---

**Desvio de processo registrado (10 jul. 2026).** A inspeção dos conjuntos `R(geral)` e "interno ∩ externo" foi feita, na primeira vez, por um Python *ad hoc* de terminal — contrariando a convenção acima. A inspeção foi convertida na opção `--detalhe` de `grafo-metodos.py`, e os números só foram reproduzidos por ela. Registra-se porque a convenção existe justamente para impedir que um número sem script chegue ao texto.
