# Auditoria do repositório — 28 out. 2026

> **Natureza deste documento:** nota de passagem, conforme o `README.md` deste diretório. Registra o que foi lido, o que foi reexecutado e os achados daí decorrentes. Não altera capítulo algum.
>
> **Resultado em uma linha:** as medições publicadas de PP1 e PP3 **reproduzem-se integralmente**; o **ambiente de referência publicado não corresponde ao artefato medido** (achado A1); e há três resíduos de método no TCC que a revisão de coerência não alcançou, por serem visíveis só na comparação com o código e com a saída dos scripts (A2–A4).

---

## 1. Escopo e método

**O que foi lido.** Todo o conteúdo de `docs/` — os dezesseis arquivos `.md` (5.258 linhas, das quais 1.740 do arquivo do TCC), o `README.md` dos scripts e os três programas de coleta —, mais o `README.md` da raiz e o código do protótipo: os 45 arquivos de `src/main/kotlin`, `build.gradle.kts`, `gradle.properties`, `Makefile`, `config/docker/Dockerfile`, `.dockerignore`, `application.properties`, o *workflow* de integração contínua e o `init.sh`.

Os PDFs de `docs/refs/` foram tratados como **acervo bibliográfico**, não como objeto de auditoria: a conferência de cada fonte já está registrada em `REFERENCIAS_CIENTIFICAS.md` (com status de acesso) e em `CITACOES_ABNT.md` (com citação direta, tradução e glosa), e refazê-la não pertence a esta passagem.

**O que foi reexecutado.** Os dois programas que não dependem de contêiner, pelos comandos que o `README.md` dos scripts registra:

| Programa | Ferramenta | Versão nesta execução | Versão no TCC (5.5.4) |
|---|---|---|---|
| `metrica-reuso.py` | `cloc` | 2.10 | 2.08 |
| `grafo-metodos.py` | `javap` | 21.0.11, de `~/.jdks/corretto-21.0.11` | 21.0.11 |

A divergência de `cloc` não alterou valor algum (ver §2). O `javap` empregado é o mesmo que o TCC registra; como não há JDK no `PATH` desta máquina, foi preciso apontá-lo explicitamente:

```bash
./docs/case-study/scripts/metrica-reuso.py
PATH="$HOME/.jdks/corretto-21.0.11/bin:$PATH" ./docs/case-study/scripts/grafo-metodos.py
```

**O que não foi reexecutado.** A coleta da PP2 — ver §6, que registra o motivo e o custo.

---

## 2. Reprodução das medições publicadas

Confronto entre os valores do capítulo 7 e os obtidos nesta data. Nenhum número foi calculado fora dos scripts.

| Medição | Seção | Publicado | Obtido | Veredito |
|---|---|---|---|---|
| LOC total de `src/main/kotlin` | 7.4.1 | 1720 | 1720 | ✅ |
| `OrderExternalController` · `OrderInternalController` | 7.4.1 | 79 · 85 | 79 · 85 | ✅ |
| Base comum (`RSI`) | 7.4.1 | 1556 | 1556 | ✅ |
| *Reuse percent* interno · externo | 7.4.1 | 94,82% · 95,17% | 94,82% · 95,17% | ✅ |
| *Reuse percent* linha de base | 7.4.1 | 90,47% | 90,47% | ✅ |
| Sensibilidade dos DTOs (25 LOC) — externo · interno | 7.4.1 | 93,64% · 94,74% | 93,64% · 94,74% | ✅ |
| `\|R(v)\|` default · internal · external · linha de base | 7.3.2 | 7 · 13 · 13 · 18 | 7 · 13 · 13 · 18 | ✅ |
| *Reuse per Method* | 7.3.2 | 100% · 61,54% · 61,54% | 100% · 61,54% · 61,54% | ✅ |
| Suprimidos frente à linha de base | 7.3.2 | 11 (61,1%) · 5 (27,8%) | 11 (61,1%) · 5 (27,8%) | ✅ |
| Conjuntos específicos nomeados na 7.3.2 | 7.3.2 | 5 métodos por modo de escrita | os mesmos, nome a nome | ✅ |

**Fidelidade do Apêndice A.** Os três blocos de código do apêndice foram confrontados, linha a linha, com os arquivos de `case-study/scripts/`: `metrica-reuso.py` (139 linhas), `grafo-metodos.py` (196) e `evidencia-rotas.py` (316) são **idênticos** ao publicado. O vínculo que a regra de congelamento protege está intacto.

**Nota sobre os nomes.** A 7.3.2 nomeia dois métodos como `OrderStatus.getById` e `OrderStatus.getByDescription`; o script os reporta como `OrderStatus$Companion.getById` e `...getByDescription`, por serem declarados em `companion object`. Trata-se da mesma entidade, e a forma abreviada do capítulo é a adequada ao texto.

---

## 3. Divergências entre o publicado e o repositório

### A1 — ⛔ O ambiente de referência publicado não é o do artefato medido

**Estabelecido por inspeção da própria imagem medida.**

O TCC publica, na tabela de ambiente de referência (5.5.4) e na abertura do Desenvolvimento (6.1), **Quarkus 3.37.0**, qualificando-o como a versão do "mecanismo `@IfBuildProperty` do Quarkus Arc". O repositório diz outra coisa, em três lugares:

| Onde | O que declara |
|---|---|
| `build.gradle.kts:25` | `id("io.quarkus") version "3.37.1"` — versão do **plugin Gradle** |
| `gradle.properties` | `quarkusPlatformVersion=3.32.4` — versão do **BOM**, que resolve as dependências |
| imagem `bff-external:latest` | `io.quarkus.quarkus-arc-3.32.4.jar`, `io.quarkus.arc.arc-3.32.4.jar`, `io.quarkus.quarkus-core-3.32.4.jar` |

A última linha é a decisiva: **o artefato de produção sobre o qual PP2 e PP3 foram medidas carrega o Quarkus Arc 3.32.4**. O número 3.37.x nunca descreveu a plataforma — descreve o plugin que executa a *augmentation*, cujas dependências de *deployment* vêm do BOM. Como o mecanismo sob estudo é precisamente o `@IfBuildProperty`, fornecido pelo Arc, a versão que o trabalho precisa publicar é a do Arc.

Três consequências, distintas entre si:

1. **Fato publicado incorreto.** Nenhum artefato do trabalho conteve Quarkus 3.37.0. Um terceiro que fixe essa versão para reproduzir o experimento não obtém o ambiente medido.
2. **Defasagem plugin × BOM no projeto.** Plugin em 3.37.1 e plataforma em 3.32.4 é configuração que o próprio Quarkus não pressupõe; é dívida de manutenção do protótipo, independente do TCC.
3. **Deriva posterior à coleta.** O commit `47177ea` (10 jul. 2026, dependabot) subiu o plugin de 3.37.0 para 3.37.1 — **no mesmo dia** em que os capítulos 7 e o Apêndice A foram fechados. Ainda que o número publicado se referisse ao plugin, ele já não corresponde ao `HEAD`.

**Encaminhamento sugerido.** Publicar a versão do Arc efetivamente embarcada (3.32.4), obtida por inspeção do artefato, e registrar separadamente a do plugin, se interessar. A correção é de **fato**, não de método: nenhum resultado depende dela, porque o comportamento observado é o documentado pelo próprio Quarkus para a anotação (QUARKUS, 2026a) e foi verificado empiricamente sobre o artefato. Convém, contudo, não descrever a versão como "do framework" quando se mede o artefato — e, se a tabela quiser ser verificável, o caminho é derivá-la do artefato, como se fez aqui.

### A2 — 🔴 A epígrafe da 7.2 reenuncia a PP2 na formulação abandonada

**Estabelecido por leitura do TCC contra si mesmo.**

A 5.3 define a PP2 sobre o **ponto de entrada**: "o *bean* CDI, o invocador REST e a rota registrada". A epígrafe que abre a 7.2 pergunta outra coisa:

> "**PP2:** a resolução da variabilidade em tempo de compilação garante que as operações de um modo não estejam presentes **no binário** do outro modo?"

É a formulação que o trabalho refutou, reposta no alto do capítulo que a refuta: dezesseis linhas adiante, a 7.2.1 estabelece que o bytecode **permanece** em todas as variantes. O leitor que leia a epígrafe e a subseção em sequência encontra a pergunta respondida em "não".

É o mesmo tipo de resíduo que `REVISAO-COERENCIA-TCC.md` corrigiu em C1 (4.3) e C2 (4.5) — e escapou porque aquela revisão procurou afirmações, não epígrafes. Vale conferir, na mesma passagem, a epígrafe da 7.3 (que está correta) e a da 7.5.

### A3 — 🔴 A sensibilidade do *Reuse per Method* é prometida e não publicada

**Estabelecido pela saída do script, confrontada com o capítulo.**

A 5.5.3 encerra o escopo da contagem com um compromisso: "A contagem que inclui os métodos sintetizados é reportada em paralelo, como análise de sensibilidade." A 7.3.2 não a reporta. Em todo o arquivo do TCC, "sintetizados" ocorre duas vezes: na 5.5.3, que promete, e dentro do código do Apêndice A. O capítulo 7 não a menciona.

O `grafo-metodos.py` produz os valores, e eles não são decorativos:

| Variante | `\|R(v)\|` declarados | `\|R(v)\|` com sintetizados | *RpM* declarados | *RpM* com sintetizados |
|---|---|---|---|---|
| `bff-default` | 7 | 13 | 100% | 100% |
| `bff-internal` | 13 | 29 | 61,54% | 62,07% |
| `bff-external` | 13 | 61 | 61,54% | **29,51%** |
| linha de base | 18 | 72 | — | — |

A assimetria é o ponto. Sob a contagem declarada, as duas variantes de escrita são **simétricas** — 13 métodos, 5 exclusivos cada —, e a 7.3.2 lê essa simetria como propriedade do protótipo. Incluídos os acessores, a simetria desaparece: a variante externa passa a 61 métodos alcançáveis contra 29 da interna, e seu *Reuse per Method* cai para menos da metade do valor publicado. A razão é visível na saída nominal — os DTOs do fluxo de criação (`NewOrderDTO`, `ItemInNewOrderDTO`, `PaymentInNewOrderDTO`) e as entidades que ele percorre contribuem com dezenas de acessores exclusivos, enquanto o fluxo interno percorre poucos.

Isso **não invalida** a escolha de método: a exclusão dos sintetizados está justificada na 5.5.3, e pela razão certa — contá-los desloca a medida de lógica reusada para estrutura de dados percorrida, e a assimetria acima é justamente o efeito que a justificativa prevê. O que falta é publicar o número que a prevê. Convém notar o contraste: a sensibilidade da PP1 **é** publicada (7.4.1), com variação de 1,53 ponto percentual; a da PP3, de magnitude muito maior, não.

**Encaminhamento sugerido.** Acrescentar à 7.3.2 um parágrafo com a tabela acima e a leitura de por que os números divergem — é o cumprimento de um compromisso já assumido pela Metodologia, e reforça a justificativa do escopo em vez de a fragilizar.

### A4 — 🟡 O Apêndice A.2 publica um docstring que a Metodologia contradiz

**Estabelecido por leitura do código publicado.**

A primeira linha do `grafo-metodos.py`, reproduzida verbatim no Apêndice A.2, diz:

> `"""Reuso por alcançabilidade a partir do ponto de entrada (PP1, PP3).`

A 5.5.3 dedica um parágrafo inteiro — *Por que o Reuse per Method não responde à PP1* — a negar exatamente essa atribuição, e `METRICA-REUSO.md` (7.1) a trata como erro de construto. O apêndice, portanto, publica como cabeçalho do programa uma atribuição que o corpo do trabalho refuta. O mesmo docstring remete a `METRICA-REUSO.md, seção 6` — arquivo que não existe para quem lê o TCC.

Daí decorre uma tensão entre duas regras do *modus operandi*, e nenhuma cede sem decisão explícita:

| Regra | O que exige |
|---|---|
| Fidelidade (README dos scripts) | O bloco reproduz o arquivo **tal como executado**; editá-lo rompe o vínculo com os números. |
| Remissões (ROADMAP, passo 2) | No TCC, apontamentos a arquivos tornam-se referências de capítulo, **nunca** caminho de arquivo. |

Três saídas possíveis, em ordem de custo: (a) deixar como está e **declarar** a tensão numa nota do apêndice, ressalvando que os comentários dos programas remetem ao repositório e que o cabeçalho de A.2 antecede a realocação da métrica para a PP3; (b) corrigir os docstrings nos arquivos e **reexecutar** os três programas, pelo procedimento de edição de script congelado — a edição é de comentário e não muda número algum, o que torna a reexecução uma confirmação, não uma recoleta; (c) manter o código e anotar a divergência em nota de rodapé por script. A opção (b) é a única que deixa código e texto coerentes entre si, e seu custo é baixo exatamente porque nenhum valor depende de comentário.

### A5 — 🟡 O inventário de figuras mudou, e os documentos de apoio não acompanharam

**Estabelecido por `git diff` e contagem no arquivo.**

A árvore de trabalho tem uma alteração **não commitada** no arquivo do TCC: a remoção de `![][image6]` e `![][image7]` da seção 4.2. Entretanto, o ROADMAP (3.2b) e o `PROPOSTA-ARQUITETURA-METAMORFICA.md` continuam afirmando que "as **cinco** imagens continuam sendo os *placeholders* originais" e que `image3`–`image7` foram preservadas.

Inventário real, hoje:

| Tipo | Quantidade | Onde |
|---|---|---|
| `🖼️ [FIGURA A INSERIR]` | 9 | 5 no Desenvolvimento, 4 na Avaliação |
| `[Trocar imagem]` | 2 | Introdução (`image1`, `image2`) |
| Imagens inseridas sem legenda nem numeração | 3 | Proposta 4.1 (`image3`, `image4`, `image5`) |

Os itens 7d e 9 do checklist continuam, portanto, válidos, mas com **contagem diferente** da que registram. Convém decidir se a remoção de `image6`/`image7` é definitiva antes de commitá-la — e, em sendo, atualizar as duas afirmações acima, sob pena de o ROADMAP descrever um estado que já não existe.

### A6 — 🟡 Glosas vencidas nos documentos de apoio, além das já catalogadas

**Estabelecido por leitura dos arquivos de apoio contra `METRICA-REUSO.md`.**

O item 10 do checklist prevê corrigir as glosas que ainda repetem "não existe no binário". Duas observações:

- **A localização está trocada.** O item aponta `REFERENCIAS_CIENTIFICAS.md` (5.1); a ocorrência está em **3.3** (Baum et al.), `REFERENCIAS_CIENTIFICAS.md:88`. A outra está onde o item diz: `CITACOES_ABNT.md:129`, Eixo 3.
- **Há glosas vencidas que o item não cobre**, decorrentes do abandono do *reuse level* e não da reformulação da tese:
  - `CITACOES_ABNT.md`, Eixo 6: "o `IRL` é o que traduz numericamente 'reuso sem duplicação': `M` conta os itens internos usados mais de uma vez, categoria à qual pertence o `OrderService`". `METRICA-REUSO.md` 6.1(a) declara essa proposição **falsa sob a própria classificação declarada** — o `OrderService` está em `E`, e o `IRL` não olha para `E`. A reexecução confirma: `IRL = 0` em toda variante.
  - `CITACOES_ABNT.md`, *Mapa síntese*: a linha "Medição de reuso (PP1): *reuse percent* e *reuse level*" e a entrada de FRAKES; KANG descrevem o método anterior. A fonte foi removida das Referências do TCC por ter ficado órfã.

### A7 — 🟡 `CITACOES_ABNT.md`: chamadas e lista desalinhadas com a convenção do TCC

**Estabelecido por leitura do arquivo.**

Duas pendências internas ao documento de apoio, sem reflexo no TCC (cuja lista de Referências está completa e cujas chamadas seguem a convenção):

- **Ano das chamadas.** O Eixo 4 cita MANADHATA; WING (**2005**) e HANNOUSSE; YAHIOUCHE (**2020**) — o relatório técnico e o *preprint* —, enquanto o TCC adota uniformemente as versões publicadas, (2011) e (2021), como o `REFERENCIAL-TEORICO.md` declara na sua convenção. A lista ao final do arquivo registra corretamente as duas versões de cada obra; são as **chamadas** e o *Mapa síntese* que divergem.
- **Lista incompleta.** As Referências do arquivo não trazem QUARKUS (2026e), DEVANBU et al. (1996) nem PONTA, PLATE e SABETTA (2018), embora os três sejam citados no corpo do próprio arquivo — o primeiro em seção própria do Eixo 8, os dois últimos em seções que seguem o Eixo 6.

---

## 4. Pendências do próprio repositório, confirmadas em aberto

Conferidas nesta data, para que a próxima passagem não as reabra à toa. Nenhuma é achado novo; a descrição de cada uma está na sua fonte.

| Item | Fonte | Situação em 28 out. 2026 |
|---|---|---|
| I1 — figura da 6.6 promete três artefatos, tabela arrola quatro | `REVISAO-COERENCIA-TCC.md` | aberto |
| I2 — "três variantes de produção" onde foram construídas quatro | idem | aberto (7.2.1) |
| I3 — a 4.3 reduz o conjunto a "interno e externo" | idem | aberto |
| F1 — a síntese 4.6 enuncia custo mais estreito que a 7.5.1 | idem | aberto |
| F2 — sobreposição Introdução × 2.1/2.2 | idem · ROADMAP 3.1(a) | aberto |
| F3 — marcadores de figura | idem · checklist 7d, 9 | aberto (contagem em A5) |
| Item 10 — glosas dos documentos de apoio | ROADMAP, checklist | aberto (localização e escopo em A6) |
| Item 11 — *build* nativo | ROADMAP 1.1, item 3 | fora de escopo, por decisão de 10 jul. 2026 |

Registra-se também o que foi conferido e **está consistente**, por ter sido objeto de verificação explícita: a fidelidade dos três blocos do Apêndice A (§2); a correção de C1, aplicada e presente na 4.3; e a afirmação da 6.6 sobre o contexto de *build* (B4, abaixo).

---

## 5. Achados no código, não registrados em documento algum

### B1 — Um pedido nunca alcança o estado `DELIVERED`

`src/main/kotlin/edu/uerj/domain/Order.kt:74`

```kotlin
if (autoCheckStatus < OrderStatus.DELIVERED.id) {
    this.status = OrderStatus.getById(autoCheckStatus).description
}
```

`checkOrderStatus()` calcula o menor estado entre as entregas e só o propaga ao pedido quando esse mínimo é **estritamente menor** que `DELIVERED` (8). Com todas as entregas em `DELIVERED`, `autoCheckStatus` vale 8, a condição é falsa e o pedido **permanece no estado anterior** — tipicamente "Separação realizada". O agregado, portanto, nunca registra a conclusão do fluxo que o próprio enum define como terminal.

É defeito de domínio, e não de medição: a PP3 conta alcançabilidade estática, não comportamento, e nenhum valor do capítulo 7 muda. Mas contraria a descrição da 6.2 — "`checkOrderStatus()` recalcula o status do pedido a partir do menor estado entre suas entregas, mantendo a consistência do agregado" —, que é verdadeira para oito dos nove estados e falsa para o nono.

Cabe a mesma decisão tomada para os demais pormenores de código (`DESENVOLVIMENTO.md`, decisões registradas): corrigir ou declarar fora de escopo. Se a opção for não corrigir, convém que a 6.2 não afirme a generalidade.

### B2 — A listagem anuncia o limite pedido, não o aplicado

`src/main/kotlin/edu/uerj/services/OrderService.kt:52`

`listAll` sanea o limite para a consulta — fora da faixa de 1 a 500, adota o *fallback* 50 —, mas devolve `PaginatedResult(limit = limit)` com o **parâmetro cru**. Uma requisição com `limitPerPage=9999` recebe 50 itens e um campo `limit` informando 9999. O `CASE_STUDY.md` (5) descreve o saneamento e as faixas, sem mencionar essa divergência entre o anunciado e o aplicado.

Sem efeito sobre as perguntas de pesquisa. Registrado porque a listagem é o `GET` de controle da coleta da PP2 — o `evidencia-rotas.py` requisita `limitPerPage=20`, valor dentro da faixa, de modo que a coleta não é afetada.

### B3 — `EXPOSE 9000` contra `API_PORT` com padrão 8081

`config/docker/Dockerfile` declara `EXPOSE 9000`; `application.properties:18` define `quarkus.http.port=${API_PORT:8081}`. A imagem só escuta em 9000 porque os alvos `container/run/*` do `Makefile` injetam `API_PORT=9000`. Quem execute a imagem sem essa variável encontra a aplicação em 8081, enquanto o `EXPOSE` anuncia 9000.

Nota de reprodutibilidade: o `evidencia-rotas.py` passa a variável, e a coleta não depende do padrão.

### B4 — ✅ Verificado: o contexto de *build* não herda estado da máquina

A afirmação da 6.6 e da 7.8 — "o contexto de *build* exclui o diretório de saída local, o cache do sistema de *build* e os metadados do repositório" — **confere**. O `.dockerignore` exclui `build/`, `.gradle/`, `.kotlin/`, `.quarkus/`, `bin/`, `.git/`, `.idea/`, `.vscode/` e `docs/`. Registrado para que a próxima passagem não a reverifique.

### B5 — `init.sh` segue como registrado

`init.sh` não tem *shebang* (o `ENTRYPOINT ["sh", "init.sh"]` supre) e a invocação de modo `dev` monta `-Dquarkus-args= "$RUN_MODULE"`, com espaço após o `=`, o que não passa o valor como se pretende. Já consta como decisão deliberada de não alterar (`DESENVOLVIMENTO.md`, decisões registradas); a nota apenas confirma que permanece assim e que nenhuma medição o atravessa.

---

## 6. O que esta passagem não verificou

**A coleta da PP2** — rotas no documento OpenAPI e atendimento HTTP por variante, mais a verificação do Swagger UI. Não foi reexecutada, por três razões que convém ter à vista antes de decidir repeti-la:

1. **A coleta muta o estado do banco.** Cada execução avança a entrega-alvo dois estados — uma vez em `bff-internal`, outra na linha de base — e cria dois pedidos. Esgotados os nove estados, o script aborta pedindo repopulação. Reexecutar exige `make deps/up/populated`, isto é, derrubar e repovoar a infraestrutura.
2. **As quatro imagens existem, mas são de 10 jul. 2026.** `bff-default`, `bff-internal`, `bff-external` e `bff` estão na máquina. Usá-las como estão mede o artefato de julho; reconstruí-las com `make container/build/variants && make container/build` produz artefatos com o plugin 3.37.1 — ambiente diferente do publicado, pelo que se expôs em A1.
3. **Nada do que se afirma aqui depende disso.** A evidência da PP2 é a mais bem registrada do trabalho: tem procedimento descrito na 5.5.1, três camadas de estatuto declarado, programa congelado e histórico de seis correções documentadas.

**Encaminhamento sugerido**, se a reexecução vier a interessar: decidir primeiro A1 — qual ambiente o trabalho publica —, porque é essa decisão que determina se a reconstrução das imagens confirma ou substitui os números de 7.2.3. Reconstruir antes de decidir produz uma terceira versão do ambiente, e nenhuma resposta.
