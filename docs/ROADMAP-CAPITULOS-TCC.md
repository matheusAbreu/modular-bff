# Roadmap dos Capítulos do TCC — Próximos Passos

> **Objetivo deste documento:** mapear os capítulos ainda ausentes na proposta (`docs/case-study/Proposta TCC …md`) e organizar os próximos passos para concebê-los, aproveitando o protótipo `modular-bff` já existente, o estudo de caso (`docs/case-study/CASE_STUDY.md`) e as referências científicas já reunidas (`docs/case-study/REFERENCIAS_CIENTIFICAS.md`, `docs/case-study/CITACOES_ABNT.md`, PDFs em `docs/refs/`).
>
> **Insumos de apoio criados durante a redação:** o capítulo de **Metodologia** já rascunhado (`docs/case-study/METODOLOGIA.md`) e o **guia de fontes acadêmicas** com o *modus operandi* de pesquisa (`docs/FONTES-ACADEMICAS.md`).

---

## Modus Operandi — Implementação de rascunhos no TCC

> Procedimento padrão para promover cada capítulo-rascunho (`*.md` em `docs/case-study/`) ao documento final do TCC. Seguir sempre nesta ordem.
>
> **Arquivo final do TCC:** `docs/case-study/Proposta TCC - Reutilização de código para criar múltiplos projetos do tipo BFF .md`

### Convenções de redação (valem para todos os capítulos)

- **3ª pessoa acadêmica.** Vedadas construções em 1ª pessoa ("proponho", "analisamos", "vemos", "temos"); usar "propõe-se", "observa-se", "este trabalho propõe".
- **Não reforçar a qualidade do estudo.** O trabalho fala por si. Evitar frases que anunciem o próprio mérito ou antecipem a conclusão do leitor — "essa tese não repousa sobre intuição", "cada ponto encontra correspondência direta", "o argumento é robusto", "o resultado é maior segurança". Prefira o registro neutro que apenas **declara o que o texto faz**: "retomam-se, a seguir, os pontos estabelecidos na seção 2.5". A evidência convence; o adjetivo, não.
- **Remissões explícitas.** Ao retomar conteúdo de outro capítulo, nomear a seção ("conforme a seção 2.5") em vez de reexpor o conceito.
- **Fonte única do BFF:** apenas o artigo verificado NEWMAN (2015). O livro NEWMAN (2021), não verificado, não é citado nem listado.

### Procedimento

1. **Revisar o rascunho.**
   - Conferir **todas as citações** (autor-data; fontes verificadas em `docs/refs/` / `CITACOES_ABNT.md`; reatribuir ou remover as não verificadas).
   - Aplicar **correções ortográficas e gramaticais** e garantir a consistência de termos.
2. **Implementar no TCC.** Transpor o texto para o **arquivo final** (acima), na posição definida pelo sumário.
   - Ajustar os **níveis de cabeçalho** e **remover a meta de rascunho** (cabeçalho 🟡, notas de pendência).
   - **Converter remissões de rascunho em referências internas:** apontamentos a arquivos (`ver TRABALHOS-RELACIONADOS.md`, caminhos `docs/refs/…`) tornam-se referências de capítulo/seção ("ver capítulo Trabalhos Relacionados"), **nunca** caminho de arquivo.
   - **Atualizar a "Organização do Trabalho":** ajustar a contagem de partes e descrever o novo capítulo no sumário narrativo.
   - **Cross-check citações ↔ Referências:** acrescentar à lista toda fonte nova que o capítulo introduza e **remover as que ficaram sem uso** após reatribuições.
3. **Revisar o resultado no arquivo final.**
   - Validar **duplicações** com capítulos vizinhos e a **coesão textual** (transições, numeração, remissões).
   - **Normalizar os níveis de cabeçalho** dos capítulos entre si, padronizando a hierarquia do documento.
   - Identificar os **pontos que pedem imagem/figura** para ilustração — nesta etapa apenas **anotar**; o registro é feito no passo 4.
4. **Atualizar o ROADMAP.**
   - Marcar o capítulo como implementado (seção 1, sumário, subseção do mapa, checklist e nota de progresso).
   - **Registrar as pendências de figura** e demais follow-ups editoriais identificados no passo 3.
   - **Manter rascunho e TCC coerentes:** propagar as correções ao rascunho ou marcá-lo como "implementado — fonte da verdade passa a ser o TCC".

---

## 1. Situação atual

O que a proposta **já** cobre:

- **Introdução** — microsserviços, API Gateway e o surgimento do BFF (fundamentada).
- **Justificativa** — vantagens do BFF e o problema de reuso/manutenção.
- **Objetivos** — geral e específicos.
- **Organização do Trabalho**.
- **Proposta: Arquitetura Metamórfica** — com *Fundamentação teórica* (SPL, variabilidade por anotação) e *Segurança por ausência*.
- **Referências** (ABNT).

O que **falta** para transformar a proposta num TCC completo:

- ~~Referencial Teórico como capítulo próprio~~ → **implementado no TCC** (arquivo original `Proposta TCC …md`, como capítulo *Referencial Teórico* antes da Proposta). Rascunho de trabalho mantido em `REFERENCIAL-TEORICO.md`.
- ~~Trabalhos Relacionados~~ → **implementado no TCC** (arquivo original `Proposta TCC …md`, como capítulo antes da Proposta). Rascunho de trabalho mantido em `TRABALHOS-RELACIONADOS.md`.
- ~~Proposta: Arquitetura Metamórfica (capítulo redigido)~~ → **implementado no TCC** (seções 4.1–4.6), substituindo a redação preliminar. Rascunho de trabalho mantido em `PROPOSTA-ARQUITETURA-METAMORFICA.md`.
- ~~Metodologia~~ → **rascunho concluído** em `METODOLOGIA.md`.
- ~~Desenvolvimento / Implementação do Protótipo~~ → **rascunho concluído** em `DESENVOLVIMENTO.md`.
- ~~Avaliação, Resultados e Discussão~~ → **rascunho iniciado** em `AVALIACAO.md`, com os resultados de PP2 e PP3 já coletados e os demais marcados como `[A PREENCHER]`.
- Conclusão e Trabalhos Futuros.

---

## 1.1 ⛔ Pendência crítica — a tese afirma mais do que o artefato demonstra

> **Estado:** aberta. **Bloqueia:** o capítulo de Avaliação (experimento 1 / PP2) e, por consequência, a redação definitiva da *segurança por ausência*. **Descrição única:** esta seção é a **fonte da verdade** sobre o assunto; os demais pontos do repositório que tocam a tese devem **remeter a ela**, nunca reexpor o problema.

### O que foi observado

Em 9 jul. 2026 executou-se, pela primeira vez, o experimento 1 (inspeção do artefato) sobre um *build* real:

```bash
BFF_API_EXTERNAL_ACTIVE=true BFF_API_INTERNAL_ACTIVE=false ./gradlew quarkusBuild -x test
```

**O artefato inspecionado foi o *build* local de desenvolvimento** — a aplicação como existe durante o ciclo de codificação e depuração, produzida na máquina do desenvolvedor, com o estado do diretório `build/`, o cache do Gradle e as variáveis da sessão. É o modo de depuração, não o programa final que roda em produção. O que se descreve abaixo é o que ocorre nesse artefato.

**A mesma análise foi então repetida sobre o artefato de produção** — o gerado no estágio de *builder* do `Dockerfile`, isolado do estado local e a partir das *flags* de `--build-arg` (ver *Builds de análise (produção)*, abaixo) — e **reproduz o mesmo comportamento**, conforme registrado ao final desta seção.

**Somente o artefato de produção integra a avaliação.** A observação sobre o *build* local fica registrada aqui como o **modo pelo qual o problema foi descoberto**, e não como experimento do trabalho: medir sobre um artefato que depende do diretório `build/` da máquina, do cache do Gradle e das variáveis da sessão não sustentaria afirmação alguma sobre o que é entregue. O capítulo de Avaliação e o desenho experimental da Metodologia consideram exclusivamente as variantes de produção.

O resultado **contraria o procedimento de verificação prescrito** hoje no `CASE_STUDY.md` (seção 10) e no `DESENVOLVIMENTO.md` (seção 6.8). Os dois mandam inspecionar o JAR da aplicação:

```bash
unzip -l build/quarkus-app/app/modular-bff-*.jar | grep "adapters/api/v1"
```

e esperam que `OrderInternalController` esteja ausente. Ele **está presente**:

```
edu/uerj/adapters/api/v1/OrderExternalController.class
edu/uerj/adapters/api/v1/OrderGeneralController.class
edu/uerj/adapters/api/v1/OrderInternalController.class   ← presente, com a flag em false
```

A razão é que `build/quarkus-app/app/*.jar` é a saída **bruta do compilador Kotlin**, anterior à fase de *augmentation* do Quarkus. A anotação `@IfBuildProperty` não atua sobre o compilador: ela atua sobre o **registro de beans CDI** e sobre a **geração das rotas REST**, cujo produto vive em `build/quarkus-app/quarkus/generated-bytecode.jar`. É nesse artefato que a metamorfose é observável:

```bash
unzip -l build/quarkus-app/quarkus/generated-bytecode.jar | grep "adapters/api/v1"
```

```
OrderExternalController_Bean.class                                ✓
OrderExternalController$quarkusrestinvoker$createOrder…           ✓
OrderGeneralController_Bean.class                                 ✓
OrderGeneralController$quarkusrestinvoker$getOrderById…           ✓
OrderGeneralController$quarkusrestinvoker$listOrders…             ✓
# OrderInternalController_Bean.class                              AUSENTE
# OrderInternalController$quarkusrestinvoker$updateStatusOrder…   AUSENTE
```

### Confirmação sobre o artefato de produção

Geradas as três variantes com `make container/build/variants` e inspecionadas com `make artifact/analysis`, o comportamento é idêntico ao observado no *build* de desenvolvimento. Em **todas as três** imagens, `app/*.jar` contém as três classes de *controller*:

```
edu/uerj/adapters/api/v1/OrderExternalController.class
edu/uerj/adapters/api/v1/OrderGeneralController.class
edu/uerj/adapters/api/v1/OrderInternalController.class
```

Já os **pontos de entrada** registrados em `quarkus/generated-bytecode.jar` variam exatamente conforme as *flags*:

| Variante | *Beans* e *invokers* presentes |
|---|---|
| `bff-default` | `OrderGeneralController_Bean` + *invokers* de `getOrderById` e `listOrders` |
| `bff-internal` | os do `default` + `OrderInternalController_Bean` e *invoker* de `updateStatusOrder` |
| `bff-external` | os do `default` + `OrderExternalController_Bean` e *invoker* de `createOrder` |

A separação entre "classe presente" e "ponto de entrada ausente" é, portanto, uma propriedade do artefato **entregue**, e não um efeito do ambiente de desenvolvimento. O empacotamento de produção é o mesmo `fast-jar` do *build* local, o que explica a coincidência.

### O que isso implica para a tese

A tese **sobrevive, mas está enunciada de forma mais forte do que o artefato de desenvolvimento sustenta**. Convém separar as duas afirmações:

| Afirmação | Situação no artefato de desenvolvimento |
|---|---|
| Não há *bean* CDI, não há *invoker* REST, não há rota registrada; nenhuma configuração de runtime reativa o endpoint | ✅ **Verdadeira e verificável** no `generated-bytecode.jar` |
| A classe do controller "não foi compilada", "não é compilada no artefato", "não existe no binário", "sequer é compilada nele" | ❌ **Falsa ao pé da letra** — o bytecode da classe permanece no `app/*.jar`, inerte e inalcançável |

Trechos que hoje sustentam a afirmação falsa. A correção depende do que a análise do artefato de produção revelar — se ela confirmar o mesmo comportamento, todos passam à formulação do item 1 do encaminhamento:

- `Proposta TCC …md` — seção 4.5, primeiro item da lista ("a classe que a declara **não foi compilada** nele") e último parágrafo da 4.5 ("garante-se que tais operações **sequer sejam compiladas** nele").
- `Proposta TCC …md` — seções 2.4 e 3.2 ("ele **deixa de existir** no artefato final"; "ela simplesmente **não é compilada**"); mesmos trechos nos rascunhos `REFERENCIAL-TEORICO.md` (2.4) e `TRABALHOS-RELACIONADOS.md` (3.2, 3.3).
- `PROPOSTA-ARQUITETURA-METAMORFICA.md` — seções 4.2, 4.5.
- `DESENVOLVIMENTO.md` — seção 6.5 ("a classe **não é compilada** no artefato") e o procedimento da 6.8.
- `CASE_STUDY.md` — seções 3 ("o endpoint simplesmente **não existe no binário**"), 10 (procedimento e saída esperada) e 11.
- `REFERENCIAS_CIENTIFICAS.md` (3.3) e `CITACOES_ABNT.md` (glosa do Eixo 3) — glosas que repetem "não existe no binário".

O risco acadêmico é concreto: um leitor que execute o comando publicado no próprio trabalho verá o controller do outro modo listado, e a evidência central da contribuição fica desmentida pelo próprio procedimento.

### Encaminhamento proposto (a decidir)

0. **Repetir a análise sobre o artefato de produção.** Gerar as três variantes pelo `Dockerfile` (alvos `make container/build/*`) e inspecionar cada imagem com `make artifact/entrypoints/*`. É o passo que converte a observação acima — feita no artefato de depuração — em evidência publicável, e é pré-requisito dos itens seguintes. O empacotamento de produção pode diferir do local (modo `fast-jar` versus *uber-jar*, ausência de resíduos do `build/` da máquina), e a redação final deve descrever o que o artefato entregue de fato contém.
1. **Reancorar a tese no *ponto de entrada*, não na classe.** Substituir "o endpoint não existe no binário" por formulação do tipo "o artefato não contém o ponto de entrada: não há *bean*, *invoker* nem rota registrada". Isso preserva integralmente o argumento de superfície de ataque de MANADHATA; WING (2011), cuja unidade de medida é o **ponto de entrada**, e não a classe — o que, a rigor, torna a formulação corrigida **mais aderente** à métrica citada do que a original.
2. **Trocar o procedimento de verificação** em `CASE_STUDY.md` (10), `DESENVOLVIMENTO.md` (6.8) e `METODOLOGIA.md` (5.1/5.2): inspecionar `quarkus/generated-bytecode.jar` filtrando por `_Bean` e `quarkusrestinvoker`, e não `app/*.jar`.
3. **Investigar o *build* nativo** (`-Dquarkus.native.enabled=true`): a *dead code elimination* do GraalVM possivelmente elimina também o bytecode da classe, o que restauraria a versão forte da tese — desta vez de forma literalmente verdadeira. **Hipótese não testada**; se confirmada, vira uma seção da Avaliação e um argumento adicional. Se refutada, a versão do item 1 é a definitiva.
4. **Registrar o achado como resultado**, não como erratum: a distinção entre "classe presente e inerte" e "ponto de entrada ausente" é exatamente o tipo de precisão que o capítulo de Avaliação existe para produzir, e o contraste com a *feature flag* em runtime permanece intacto (lá o ponto de entrada **existe e responde**).

> **Nota de escopo:** os artefatos analisados devem ser os de **produção** (imagem Docker), não os do *build* de desenvolvimento — ver a seção *Builds de análise* logo abaixo.

---

## 1.2 Builds de análise (produção) — insumo do capítulo de Avaliação

> **Distinção que o TCC precisa deixar explícita.** O `make debug/build` compila o projeto **na máquina do desenvolvedor** e serve ao ciclo de desenvolvimento e depuração; seu resultado depende do estado local (`build/`, cache do Gradle, variáveis de ambiente da sessão). Ele **não** é o artefato a ser medido.
>
> O artefato de análise é o produzido **dentro do `Dockerfile`**, em estágio de *builder* isolado, a partir do código-fonte e das *flags* passadas por `--build-arg`. É esse o artefato que se aproxima do que iria a produção e é ele, e somente ele, que deve alimentar as medições de PP2 (ausência do ponto de entrada), PP3 (superfície de ataque) e PP4 (custo operacional).
>
> As variantes de análise são três, geradas pelos alvos `make container/build/default`, `.../internal` e `.../external`, e inspecionadas por `make artifact/entrypoints/*`. A execução e os resultados obtidos ficam registrados na seção correspondente do capítulo de Avaliação.

---

## 2. Estrutura-alvo do TCC

Sugestão de sumário completo (✅ existe · 🟡 existe parcialmente · ⬜ a criar):

1. ✅ Introdução
2. ✅ Referencial Teórico — **implementado no TCC** (`Proposta TCC …md`); rascunho de trabalho em `REFERENCIAL-TEORICO.md`
3. ✅ Trabalhos Relacionados — **implementado no TCC** (`Proposta TCC …md`); rascunho de trabalho em `TRABALHOS-RELACIONADOS.md`
4. ✅ Proposta: Arquitetura Metamórfica — **implementado no TCC** (seções 4.1–4.6); rascunho de trabalho em `PROPOSTA-ARQUITETURA-METAMORFICA.md`
5. 🟡 Metodologia — rascunho em `METODOLOGIA.md`
6. 🟡 Desenvolvimento / Implementação do Protótipo — rascunho em `DESENVOLVIMENTO.md`
7. 🟡 Avaliação, Resultados e Discussão — rascunho em `AVALIACAO.md`
8. ⬜ Conclusão e Trabalhos Futuros
9. ✅ Referências

> Observação: *Justificativa*, *Objetivos* e *Organização do Trabalho* permanecem como seções da Introdução (ou como capítulo introdutório), conforme o modelo da instituição.

---

## 3. Mapa dos capítulos a desenvolver

### 3.1 Referencial Teórico — ✅ implementado no TCC

> **Status:** inserido no arquivo original `Proposta TCC …md` como capítulo *Referencial Teórico* (seções 2.1–2.7), antes da *Proposta*. A *Fundamentação teórica* embutida foi desduplicada (virou remissão) e a *Organização do Trabalho* foi atualizada (hoje em **"sete partes"**, após a entrada de *Trabalhos Relacionados*). Integridade preservada: Apel & Kästner (2009) — não verificado — foi reatribuído a Horcas, Pinto e Fuentes (2023); Newman citado como (2015). **Follow-ups editoriais:** (a) sobreposição entre a *Introdução* e as seções 2.1/2.2 (microsserviços/BFF) — **ainda aberto**; (b) ~~sobreposição entre a *Segurança por ausência* da Proposta e a seção 2.5~~ → **resolvido:** a 2.5 permanece intocada como base teórica e a 4.5 passou a **retomá-la ponto a ponto**, em lista, sem reexpor os conceitos; (c) ~~níveis de cabeçalho (o capítulo está em `##` e a *Proposta* em `###`)~~ → **resolvido:** *Introdução*, *Proposta* e *Referências* normalizadas para `##`; (d) ~~decidir se o livro Newman (2021), agora não citado no corpo, sai da lista~~ → **resolvido:** o livro foi **removido** das Referências do TCC; adota-se sempre o artigo verificado NEWMAN (2015).

- **Objetivo:** consolidar os conceitos que sustentam o trabalho, hoje dispersos na *Fundamentação teórica* da Proposta.
- **Conteúdo sugerido:**
  - Microsserviços e API Gateway (contexto).
  - Backend for Frontend (BFF).
  - Linhas de Produto de Software (SPL): domínio × aplicação, derivação de produto, variabilidade **baseada em anotação × baseada em composição**.
  - Compilação condicional / variabilidade em tempo de *build*.
  - Superfície de ataque e segurança por redução de exposição.
  - Arquitetura Hexagonal (Ports & Adapters) / Clean Architecture.
- **Próximos passos:** mover os parágrafos de fundamentação já escritos para este capítulo e expandi-los; garantir uma subseção por conceito.
- **Apoio já disponível:** todas as citações em `CITACOES_ABNT.md` (Eixos 1, 3 e 5); refs 1.1, 1.2, 3.1a, 3.1b, 3.2, 3.3, 5.1, 5.2, 6.2.
- **Pronto quando:** cada conceito usado na Proposta tiver base citável neste capítulo.

### 3.2 Trabalhos Relacionados — ✅ implementado no TCC

> **Status:** capítulo rascunhado em `docs/case-study/TRABALHOS-RELACIONADOS.md`, contrastando quatro abordagens — repositórios separados, *feature flags* em runtime, microsserviços distintos e SPL por composição — com a metamorfose, mais a síntese comparativa e a discussão da lacuna sobre BFF. Todas as afirmações estão ancoradas em referências do acervo (`docs/refs/`); o capítulo trata *feature toggles* de forma equilibrada, reconhecendo seu valor na indústria. **Pendência opcional:** obter o PDF de Rahman et al. (2016) para citar formalmente a adoção das toggles.

- **Objetivo:** posicionar a Arquitetura Metamórfica frente a abordagens existentes e evidenciar a lacuna que ela preenche.
- **Conteúdo sugerido — comparar com:**
  - Repositórios separados (um BFF por frontend).
  - *Feature flags* em tempo de execução.
  - Microsserviços distintos por cliente.
  - Linhas de produto por composição (FOP/AOP).
  - A tabela comparativa (reuso × segurança × complexidade operacional) já esboçada no `CASE_STUDY.md` (seção 11) é o ponto de partida.
- **Próximos passos:** transformar a tabela do estudo de caso em texto analítico, citando a literatura; destacar que a literatura *peer-reviewed* específica sobre BFF é escassa (reforça o ineditismo).
- **Apoio:** refs 4.1–4.3 (feature toggles), 3.1a/3.1b/3.3 (SPL/variabilidade), 1.2 (padrões de microsserviços).
- **Pronto quando:** houver um quadro comparativo justificado e a lacuna estiver explícita.

### 3.2b Proposta: Arquitetura Metamórfica — ✅ implementado no TCC

> **Status:** o rascunho `PROPOSTA-ARQUITETURA-METAMORFICA.md` foi implementado no TCC como capítulo *Proposta* (seções 4.1–4.6), substituindo a redação preliminar. As **cinco figuras** da versão preliminar (`image3`–`image7`) foram **preservadas** e reancoradas: a comparação API Gateway × BFF e o cenário de BFFs duplicados na 4.1; o esquema da configuração em tempo de compilação na 4.2. As remissões por caminho de arquivo viraram referências de capítulo. Cabeçalhos normalizados: *Introdução*, *Proposta* e *Referências* passaram a `##`, alinhando-se a *Referencial Teórico* e *Trabalhos Relacionados* — **resolve o follow-up (c)** da seção 3.1. Nenhuma citação nova foi introduzida e nenhuma ficou órfã. Desduplicação aplicada: o parágrafo de 4.1 que repetia o fechamento da *Justificativa* (Taibi et al. sobre o custo do reuso) virou remissão.
>
> **Follow-ups editoriais:**
> - **Figuras definitivas:** as cinco imagens continuam sendo os *placeholders* originais; produzir os diagramas finais (ver checklist, item 9). Restam também dois `[Trocar imagem]` na Introdução (`image1`, `image2`).
> - ~~**Sobreposição 4.5 × 2.5:** a *segurança por ausência* reintroduz a superfície de ataque, já desenvolvida na seção 2.5.~~ → **resolvido** (follow-up (b) da seção 3.1). A 2.5 é a **base teórica** e permaneceu **intocada**; a 4.5 é a **solução** e passou a **retomar a 2.5 ponto a ponto**, em lista de quatro itens — superfície como conjunto de pontos de entrada; métrica em nível de projeto; distinção método externo × interno transposta ao negócio; microsserviços e ameaça interna —, mostrando a correspondência de cada um no artefato, sem redefinir os conceitos. Aplicadas também as *Convenções de redação* (registro neutro): removidas as frases que reforçavam a qualidade do estudo, na 4.5 e na 4.1.
> - **Remissões antecipadas:** as seções 4.3 e 4.6 remetem aos capítulos *Desenvolvimento*, *Metodologia* e *Avaliação*, ainda não implementados no TCC. As remissões passam a valer quando esses capítulos entrarem.
> - **Cabeçalhos vazios:** sobraram dois `###` sem título entre a *Justificativa* e os *Objetivos* (resquício do documento original) — remover na revisão final.

### 3.3 Metodologia — 🟡 rascunho concluído

> **Status:** capítulo rascunhado em `docs/case-study/METODOLOGIA.md` (classificação da pesquisa, DSR como método, 4 perguntas de pesquisa, 6 etapas DSR mapeadas aos capítulos, procedimentos de avaliação, ambiente/ferramentas e quadro de amarração objetivo→método→evidência). **Pendência:** integrar as citações de DSR (refs 7.1–7.5 de `REFERENCIAS_CIENTIFICAS.md`, ainda em *análise pendente*) e reancorar as "seis atividades" em Peffers et al. (2007).

- **Objetivo:** descrever como o trabalho foi conduzido e como a proposta será avaliada.
- **Abordagem recomendada:** **Design Science Research (DSR)** — pesquisa que produz e avalia um *artefato* (a arquitetura + o protótipo) para resolver um problema real. Alternativa: estudo de caso / prova de conceito.
- **Conteúdo sugerido:**
  - Classificação da pesquisa (aplicada, exploratória, DSR).
  - Etapas: identificação do problema → definição de objetivos → projeto do artefato (arquitetura) → demonstração (protótipo) → avaliação (métricas) → comunicação.
  - Definição das **métricas de avaliação** (ver 3.5) e do procedimento experimental (builds com diferentes *flags*).
  - Ferramentas e ambiente (Kotlin/Quarkus, Docker, inspeção de artefato).
- **Próximos passos:** ler as referências de DSR reunidas na seção 7 de `REFERENCIAS_CIENTIFICAS.md` e inserir as citações no texto; as perguntas de pesquisa PP1–PP4 já estão definidas.
- **Pronto quando:** cada objetivo específico tiver um método de verificação associado. ✅

### 3.4 Desenvolvimento / Implementação do Protótipo — 🟡 rascunho concluído

> **Status:** capítulo rascunhado em `docs/case-study/DESENVOLVIMENTO.md`, com as divergências doc × código já reconciliadas em `README.md` e `CASE_STUDY.md`. **Pendências:** produzir as figuras e tratar as melhorias sinalizadas no rodapé do capítulo — CORS permissivo, `init.sh`, `countOrders`, `@Transactional` em leituras e as métricas de reuso.

- **Objetivo:** descrever o protótipo `modular-bff` como *demonstração* concreta da arquitetura.
- **Conteúdo sugerido (reaproveitar o `CASE_STUDY.md`, sem duplicá-lo):**
  - Domínio escolhido (pedidos de e-commerce): 11 entidades, 9 estados.
  - Camadas hexagonais (`domain`, `services`, `adapters/api`, `adapters/infrastructure`, `config`).
  - O mecanismo metamórfico: `@IfBuildProperty` nos controllers `OrderInternalController` / `OrderExternalController`; `OrderGeneralController` sempre ativo.
  - Configuração via `application.properties` e variáveis `BFF_API_INTERNAL_ACTIVE` / `BFF_API_EXTERNAL_ACTIVE`.
  - Pipeline de *build* por artefato (interno vs externo).
- **Próximos passos:**
  - Incluir trechos de código-chave (o controller anotado, o `application.properties`).
  - Registrar os comandos de *build* de cada variante.
  - Preparar diagramas (o `CASE_STUDY.md` tem os textos; faltam figuras).
- **Apoio:** `CASE_STUDY.md` (seções 2–10), código-fonte, ref 6.2 (Clean Architecture) para justificar o posicionamento das anotações nos adaptadores.
- **Pronto quando:** um leitor conseguir reproduzir os dois artefatos a partir do capítulo.

### 3.5 Avaliação, Resultados e Discussão

> **Capítulo central da contribuição empírica.** É aqui que a "segurança por ausência" deixa de ser tese e vira evidência.

- **Objetivo:** demonstrar, com dados, que a arquitetura entrega o que promete.
- **Experimentos/medições sugeridos:**
  1. **Ausência verificável de endpoints** — construir o artefato externo e o interno e inspecionar o JAR (`jar tf` / `unzip -l | grep adapters/api/v1`), mostrando que o controller do outro modo **não existe** no binário. É a prova direta da tese (procedimento já descrito no `CASE_STUDY.md`, seção 10).
  2. **Redução da superfície de ataque** — quantificar os pontos de entrada expostos por artefato (nº de rotas/endpoints) e comparar com um *build* que contém todos os controllers; enquadrar com a métrica de superfície de ataque (MANADHATA; WING, 2011).
  3. **Reuso de código** — medir a proporção de código compartilhado (domínio, serviços, repositórios) vs. especializado (controllers, DTOs) — LOC ou nº de classes —, sustentando o ganho de reuso.
  4. **Custo operacional** — registrar que a abordagem exige *pipelines*/artefatos distintos; opcionalmente medir tamanho do artefato, tempo de *startup* e memória por variante.
  5. **(Opcional) Comparação com *feature flag* em runtime** — evidenciar que, na alternativa em runtime, o endpoint permanece no binário (reproduzir e inspecionar).
- **Discussão:** relacionar os números aos objetivos; retomar o contraste segurança por ausência × por proteção.
- **Apoio:** refs 5.1, 5.2 (segurança/superfície de ataque), 3.1b (variabilidade em sistemas reais), 4.1–4.3 (feature toggles).
- **Pronto quando:** cada afirmação da Proposta (reuso, segurança, ausência no binário) tiver um resultado que a confirme ou refute.

### 3.6 Ameaças à Validade *(subseção de 3.5 ou seção própria)*

- **Objetivo:** discutir os limites do estudo (padrão em TCCs orientados a evidência).
- **Conteúdo sugerido:** protótipo único e domínio único (validade externa); escopo do "Design Metamórfico" que **não** cobre autenticação/autorização/rate limiting (já delimitado na seção *Segurança por ausência*); métricas escolhidas.

### 3.7 Conclusão e Trabalhos Futuros

- **Objetivo:** retomar objetivos × resultados e apontar continuidade.
- **Conteúdo sugerido:**
  - Síntese: a arquitetura reusou código sem duplicação e reduziu a superfície de ataque por construção.
  - **Trabalhos futuros:** metamorfose que atinge camadas mais profundas (domínio/repositórios) — a ideia de granularidade mais grossa/composição descrita em `CITACOES_ABNT.md` (Eixo 3); automação da geração de *pipelines* por variante; aplicação a outro domínio; medição de segurança em produção; integração das camadas complementares (JWT/OAuth2, rate limiting).
- **Pronto quando:** cada objetivo específico for explicitamente respondido.

---

## 4. Checklist priorizado de próximos passos

1. [x] **Promover o Referencial Teórico a capítulo** — **implementado no TCC** (`Proposta TCC …md`); *Fundamentação teórica* da Proposta já desduplicada (passou a remeter ao novo capítulo).
2. [x] **Escrever Trabalhos Relacionados** a partir da tabela comparativa do `CASE_STUDY.md` — **implementado no TCC** (`Proposta TCC …md`).
2b. [x] **Implementar a Proposta** — rascunho `PROPOSTA-ARQUITETURA-METAMORFICA.md` transposto para o TCC (seções 4.1–4.6), com as figuras preservadas e os cabeçalhos normalizados.
3. [x] **Definir a Metodologia (DSR)** e amarrar cada objetivo específico a um método de verificação — rascunho em `METODOLOGIA.md`.
4. [x] **Redigir o Desenvolvimento** com trechos de código, `application.properties` e comandos de *build* — rascunho em `DESENVOLVIMENTO.md`.
5. [x] **Executar a Avaliação** — experimento 1 (inspeção do artefato) executado nas três variantes de produção; resultados em `AVALIACAO.md` (seções 7.2 e 7.3). O achado obrigou a reformular a tese: ver a pendência crítica da seção 1.1.
6. [ ] **Coletar métricas** de reuso e de superfície de ataque (experimentos 2 e 3) — o método de cálculo do reuso está definido em `METRICA-REUSO.md`; falta executá-lo.
7. [ ] **Escrever Resultados/Discussão e Ameaças à Validade**.
8. [ ] **Concluir** com trabalhos futuros.
9. [ ] **Produzir as figuras** (o `CASE_STUDY.md` traz os textos; faltam os diagramas — inclusive substituir os placeholders `[Trocar imagem]` da proposta).

> **Sugestão de ordem de execução:** 1 → 5 (o experimento de inspeção do JAR dá o resultado mais forte com o menor esforço e ajuda a calibrar todo o resto) → 3/4 → 2 → 6/7 → 8 → 9.
>
> **Progresso:** **Referencial Teórico**, **Trabalhos Relacionados** e **Proposta** já **implementados no TCC**; **Metodologia** e **Desenvolvimento** seguem em rascunho, prontos para transposição. **Próximo alvo:** implementar Metodologia e Desenvolvimento (a Metodologia depende de resolver a pendência das citações de DSR — ver 3.3) e, em paralelo, executar o experimento 1 (inspeção do JAR), que prova a tese central com o menor esforço.

---

## 5. Riscos e cuidados

- **Consistência de citações:** manter a convenção autor-data alinhada entre a Proposta e os documentos de apoio (atenção às versões de registro vs. preprint de 5.1 e 5.2, já anotadas em `REFERENCIAS_CIENTIFICAS.md`).
- **Convenções de redação:** consolidadas no *modus operandi*, no início deste documento. Já aplicadas ao arquivo do TCC.
- **Natureza dos arquivos `*.md` em `docs/case-study/`:** cada um é o **rascunho de um capítulo** do TCC — **não** uma versão concorrente dele nem um documento a "coexistir" com ele. Ao ser implementado pelo *modus operandi*, o rascunho **substitui** a redação correspondente no TCC, e a **fonte da verdade passa a ser o TCC**. Vale para `PROPOSTA-ARQUITETURA-METAMORFICA.md` (já implementado), `REFERENCIAL-TEORICO.md` e `TRABALHOS-RELACIONADOS.md` (já implementados), e para `METODOLOGIA.md` e `DESENVOLVIMENTO.md` (pendentes).
- **Não duplicar o estudo de caso:** o `CASE_STUDY.md` é insumo; o capítulo de Desenvolvimento deve resumi-lo e referenciá-lo, não copiá-lo.
- **Escopo da segurança:** manter sempre explícito que a segurança por ausência é a *primeira linha de defesa*, não a única.
- **Reprodutibilidade:** registrar versões e comandos exatos para que a avaliação seja repetível.

---

## 6. Insumos já disponíveis (mapa rápido)

| Necessidade | Onde buscar |
|---|---|
| Conceitos e citações (ABNT) | `docs/case-study/CITACOES_ABNT.md`, `docs/case-study/REFERENCIAS_CIENTIFICAS.md` |
| PDFs das referências | `docs/refs/` |
| Detalhes do protótipo (arquitetura, entidades, endpoints, build) | `docs/case-study/CASE_STUDY.md` |
| Tese de segurança por ausência e tabela comparativa | `docs/case-study/CASE_STUDY.md` (seção 11) e a seção *Segurança por ausência* da Proposta |
| Proposta (capítulo) redigida | `docs/case-study/PROPOSTA-ARQUITETURA-METAMORFICA.md` |
| Referencial Teórico redigido | `docs/case-study/REFERENCIAL-TEORICO.md` |
| Trabalhos Relacionados redigido | `docs/case-study/TRABALHOS-RELACIONADOS.md` |
| Metodologia (DSR) redigida | `docs/case-study/METODOLOGIA.md` |
| Método de cálculo da métrica de reuso (PP1) | `docs/case-study/METRICA-REUSO.md` |
| Resultados coletados e evidências pendentes | `docs/case-study/AVALIACAO.md` |
| Desenvolvimento redigido | `docs/case-study/DESENVOLVIMENTO.md` |
| Onde buscar fontes + *modus operandi* de pesquisa | `docs/FONTES-ACADEMICAS.md` |
| Código-fonte | raiz do repositório (`src/main/kotlin/edu/uerj/...`) |
