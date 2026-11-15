# Roteiro de escrita dos capítulos do TCC

## 0. O que os três TCCs de referência mostram

| | Caroline N. C. Silva (UERJ, 2023) | Luam E. Leite (UERJ-ZO, 2023) | Matheus O. Vidal (UEZO, 2021) |
|---|---|---|---|
| Tipo | Proposta teórica + modelagem (redes de Petri) | Desenvolvimento de app | Desenvolvimento de sistema |
| Extensão | 47 f. | 118 f. | ~67 p. |
| Estrutura | Intro (sem número) → Fundamentação → Problema → Trab. Relacionados → Tecnologia-base → **Contribuição** → Modelagem → Considerações finais | Intro (justificativa, objetivos, organização) → Fundamentação → Trab. Relacionados → Desenvolvimento (metodologia, requisitos, UML) → Resultados → Conclusão + Trab. Futuros | Intro (problemática, objetivos) → Fundamentação → Implementação → Conclusão + Melhorias futuras → Apêndice |
| Banca | Frederico Sauer (orient.), Carlos A. A. Lemos, Rosana | Denis Cople (orient.) | Mauro Gil, Carlos Lemos, Frederico Sauer, Denis Cople |

**O que aproveitar.**
1. **Forma institucional.** O de Caroline é o único no modelo UERJ completo: ficha catalográfica da Rede Sirius, autorização de reprodução, folha de aprovação com a instituição de cada membro, resumo encabeçado pela referência ABNT do próprio trabalho e lista de siglas. Usar como molde dos pré-textuais.
2. **Introdução como capítulo 1, com subseções.** Os dois da ZO fazem assim (1.1 Justificativa, 1.2 Objetivos, 1.3 Organização), e ela fecha com um parágrafo que diz o que cada capítulo faz.
3. **Trabalhos relacionados com tabela de critérios.** Caroline (Tabela 1: S/R/D) e Luam (Tabela 3.1) classificam cada trabalho por critérios explícitos e encerram dizendo onde o próprio trabalho se posiciona. Luam declara os **termos de busca** usados.
4. **Capítulo de contribuição nomeado.** Caroline separa claramente o que é base técnica (cap. 4) do que é dela (cap. 5, "Contribuição").
5. **Trabalhos futuros em lista concreta.** Todos os três fazem isso.

**O que evitar (observado neles).**
- **Fundamentação enciclopédica.** Vidal gasta ~20 p. com Git, nuvem, Python, Bootstrap e Docker sem ligar nada disso ao problema. A banca lê como enchimento.
- **Implementação como tutorial.** Vidal descreve clique a clique no Heroku, com capturas de tela. O que interessa é a decisão e o porquê, não o passo operacional.
- **Conclusão que afirma sem evidência.** Vidal escreve "estável e performático… pronto para produção" sem nenhuma medição mostrada. Caroline delimita: "teoricamente satisfatórios".
- **Marcadores esquecidos.** Luam deixou "Seção X.X (Colocar seção depois)" no texto entregue. O TCC atual tem o equivalente em `![][image1]\[Trocar imagem\]`.
- **Metodologia de um parágrafo.** Luam e Vidal praticamente não têm metodologia. Este trabalho afirma propriedades mensuráveis (PP1–PP4), então precisa de uma; aqui os três não servem de modelo.

## 1. Ordem de escrita (diferente da ordem do documento)

1. **Desenvolvimento (cap. 6)**: é o que você conhece de cor, porque descreve o código. Destrava a escrita.
2. **Proposta (cap. 4)**: com o protótipo descrito, a ideia geral fica fácil de abstrair.
3. **Metodologia (cap. 5)**: define as PPs e as métricas que a Avaliação vai responder.
4. **Avaliação (cap. 7)**: segue a Metodologia, uma PP por vez.
5. **Referencial (cap. 2)** e **Trabalhos Relacionados (cap. 3)**: agora você sabe exatamente quais conceitos e quais alternativas o texto usa. Nada entra que não seja usado.
6. **Conclusão (cap. 8)**.
7. **Introdução (cap. 1)**: por último, porque só agora se sabe o que o trabalho de fato estabeleceu.
8. **Resumo/Abstract**: depois de tudo.

**Motivação:** escrever a introdução primeiro leva a prometer o que a avaliação não entrega. Esse foi o caso real deste projeto: a tese teve de ser reancorada no ponto de entrada (ROADMAP §1.1).

## 2. Pré-textuais (molde: Caroline, UERJ 2023)

Capa → folha de rosto (com "Monografia apresentada, como requisito parcial…", orientador) → ficha catalográfica (pedir à Biblioteca CTC/D, Rede Sirius) → autorização de reprodução → folha de aprovação (banca com instituição) → agradecimentos (opcional) → **Resumo** e **Abstract** → listas de ilustrações, tabelas/quadros e **siglas** → sumário.

- **Resumo (150–250 palavras), nesta ordem:** problema (reuso × especialização em múltiplos BFFs) → proposta (variantes resolvidas em build) → método (DSR, protótipo, PP1–PP4) → resultados com números (os do cap. 7, como estão) → limite principal. De 3 a 5 palavras-chave.
- **Siglas prováveis:** API, BFF, CDI, DSR, DTO, HTTP, JAR, JVM, LOC, ORM, PP, REST, SPL.
- **Motivação:** o TCC atual não tem nenhum pré-textual. Eles são exigência formal e o primeiro contato da banca com o trabalho.

## 3. Capítulo a capítulo

Cada capítulo traz o que escrever (passos), onde pôr o foco, a motivação e o material de consulta.

### Cap. 1 — Introdução
**Estrutura:** 1.1 Contextualização · 1.2 Problema e justificativa · 1.3 Objetivos (geral e específicos) · 1.4 Organização do trabalho.

**Passos**
1. **Contexto em funil:** microsserviços → dados descentralizados → API Gateway → generalidade do gateway → BFF (NEWMAN, 2015; TAIBI; LENARDUZZI; PAHL, 2018). Um parágrafo por degrau.
2. **Problema:** cada frontend com seu BFF duplica estrutura e manutenção. Formular em uma frase, porque ela volta na conclusão.
3. **Justificativa:** por que vale resolver. Precisa de citação; hoje a lista de vantagens do BFF não tem nenhuma.
4. **Objetivo geral:** uma frase, sem as ressalvas que hoje ocupam o parágrafo inteiro. As ressalvas vão para 8.3.
5. **Objetivos específicos, espelhando as PPs:** propor a arquitetura; implementar o protótipo; verificar a ausência do ponto de entrada (PP2); medir a redução de exposição (PP3); medir o reuso (PP1); caracterizar o custo (PP4). Cada um precisa ter resposta na conclusão.
6. **Organização:** um período por capítulo, numerado ("O capítulo 2 apresenta…").

**Foco:** o problema e os objetivos. O resto é moldura.

**Motivação:** hoje os objetivos específicos são só dois ("apresentar a arquitetura", "apresentar um protótipo"). Eles não sustentam os quatro capítulos de avaliação e não deixam a conclusão "retomar os objetivos" de verdade. Além disso, Introdução, Justificativa, Objetivos e Organização estão hoje como capítulos separados ("onze partes"), contra o padrão dos três modelos.

**Corrigir na reescrita:**
- A frase "A solução acaba sendo encarar…" é coloquial e tem o erro "natura".
- Há dois marcadores de imagem a decidir: figura própria (Fonte: o autor) ou remover.

**Consulta:** TCC §Introdução até §Organização; `CITACOES_ABNT.md`.

### Cap. 2 — Referencial Teórico
**Estrutura atual (manter):** microsserviços · API Gateway e BFF · Linhas de Produto de Software (composição × anotação) · compilação condicional/variabilidade em build · superfície de ataque · arquitetura hexagonal · síntese.

**Passos**
1. Abra o capítulo dizendo quais conceitos ele apresenta e para qual capítulo cada um serve.
2. Para cada conceito: definição com fonte → o aspecto que este trabalho usa → uma frase de ponte ("esse conceito é retomado na seção 4.x").
3. Feche com uma síntese: um quadro conceito → onde é usado.

**Foco:** SPL e variabilidade por anotação. São elas que dão nome acadêmico ao que o `@IfBuildProperty` faz.

**Motivação:** é o contraexemplo de Vidal. Conceito que não volta em nenhum capítulo posterior é enchimento. Quarkus, Kotlin, Docker e PostgreSQL **não** entram aqui; entram no cap. 6, como escolhas de implementação.

**Regra:** a fonte única do BFF é NEWMAN (2015). O livro de 2021 não é citado.

**Consulta:** `REFERENCIAL-TEORICO.md`, `CITACOES_ABNT.md`, `REFERENCIAS_CIENTIFICAS.md`, PDFs em `docs/refs/`.

### Cap. 3 — Trabalhos Relacionados
**Passos**
1. **Declare como buscou:** bases, termos em português e inglês, período. Registre a escassez de literatura revisada por pares sobre BFF (hoje em 3.6), como Luam faz com o próprio tema.
2. **Para cada alternativa**, diga o que ela resolve e o que deixa em aberto: repositórios separados; *feature flags* em runtime; microsserviços por cliente; SPL por composição.
3. **Tabela de critérios** (molde Caroline/Luam). Linhas: as alternativas + a proposta. Colunas, por exemplo:
   - reuso de código;
   - especialização por cliente;
   - momento da decisão (build/runtime);
   - rota alheia presente no artefato;
   - custo de manutenção.
4. **Feche com a lacuna** em um parágrafo: o que nenhuma alternativa cobre e que a proposta cobre.

**Foco:** a tabela e o parágrafo da lacuna. A banca procura os dois.

**Motivação:** este capítulo justifica a existência da proposta. Sem os critérios explícitos, a comparação parece escolhida a dedo.

**Consulta:** `TRABALHOS-RELACIONADOS.md`, TCC §3.5 (síntese comparativa).

### Cap. 4 — Proposta: Arquitetura Metamórfica
**Passos**
1. **O problema em termos técnicos:** especialização × reuso.
2. **A ideia:** a configuração resolvida em build faz uma base comum gerar variantes. Defina o termo "metamórfica" em uma frase.
3. **Concretização no protótipo, só o essencial:** `@IfBuildProperty` nos controllers externo e interno; o controller geral sempre presente.
4. **Situe nas SPL:** é variabilidade por anotação. Remeta ao cap. 2, sem reexpor.
5. **Segurança por ausência:** a operação alheia não tem ponto de entrada no artefato. Diga com precisão o que **não** se afirma: a classe compilada permanece (ROADMAP §1.1).
6. **Deixe explícito o que é contribuição** deste trabalho e o que é mecanismo pronto do Quarkus (lição de Caroline).

**Foco:**
- uma **figura conceitual**: base comum → build com flags → três variantes, cada uma com suas rotas;
- a formulação exata da tese, já reancorada no ponto de entrada.

**Motivação:** é o capítulo que a banca vai citar ao arguir. Se a tese estiver ambígua aqui, toda a avaliação parece medir outra coisa.

**Pendências:** decidir as figuras `image3`–`image5` (§4.1–4.2) e o que substitui as `image6`/`image7` já removidas.

**Consulta:** `PROPOSTA-ARQUITETURA-METAMORFICA.md`; ROADMAP §1.1 (a tese reancorada).

### Cap. 5 — Metodologia
**Passos**
1. **Classifique a pesquisa:** natureza, abordagem, objetivos, procedimento.
2. **Justifique a Design Science Research:** artefato + avaliação. Diga qual etapa da DSR cada capítulo cumpre.
3. **Perguntas de pesquisa PP1–PP4:** uma frase cada, na formulação final (PP2 como ausência do *ponto de entrada*).
4. **Etapas da pesquisa** em ordem.
5. **Procedimentos por PP:** o que se mede, sobre qual artefato (só o de produção, o build Docker), com qual ferramenta e qual script.
6. **Métricas:** *Reuse percent* (LOC) e *Reuse per Method*. Fórmula, origem e o que cada uma não captura.
7. **Ambiente:** versões **obtidas do artefato**. O Arc é 3.32.4, não 3.37.0 (auditoria A1).
8. **Quadro de amarração:** objetivo → PP → método → seção de resultado.
9. **Ameaças à validade** (ou remissão a 7.8; escolha um lugar só).

**Foco:** o quadro de amarração. É o que conecta objetivos, PPs e resultados numa leitura só.

**Motivação:** nenhum dos três modelos tem metodologia à altura de um trabalho que mede. Aqui se decide se os números do cap. 7 são verificáveis. Também é onde se registra que o build local foi só o meio de descoberta, não um experimento.

**Consulta:** `METODOLOGIA.md`, `METRICA-REUSO.md`, `scripts/README.md`.

### Cap. 6 — Desenvolvimento
**Passos**
1. **Visão geral:** camadas hexagonais (domain, services, adapters/api, adapters/infrastructure, config). Uma figura de camadas.
2. **Domínio e serviço compartilhado:** `OrderService` como o ponto onde o reuso acontece.
3. **Persistência:** PostgreSQL, migrações Goose. Breve.
4. **O mecanismo:** trecho curto dos três controllers, com a anotação e a propriedade correspondente em `application.properties`.
5. **Build por artefato:** variáveis `BFF_API_*_ACTIVE` / `--build-arg`, estágio *builder* isolado. Uma tabela variante → flags → rotas presentes.
6. **Testes e CI:** o que cobrem.
7. **Reprodução:** os comandos mínimos para gerar cada variante.

**Foco:** os itens 4 e 5. São a ponte entre a ideia (cap. 4) e a medição (cap. 7).

**Motivação:** é a lição contrária a Vidal. O capítulo explica decisões, não ensina a instalar; o passo a passo de ambiente fica no README. Trechos de código só onde mostram o mecanismo.

**Consulta:** `DESENVOLVIMENTO.md`, `src/main/kotlin/edu/uerj/adapters/api/v1/*`, `config/docker/Dockerfile`, `Makefile`.

### Cap. 7 — Avaliação, Resultados e Discussão
**Padrão para cada PP:** pergunta → procedimento (remissão ao cap. 5) → **resultado em tabela** → leitura do resultado → limite.

**Passos**
1. **7.1 Objeto da avaliação:** quais artefatos (as 3 variantes de produção + a linha de base).
2. **PP2:** a classe permanece; o *bean* e o invocador somem; o OpenAPI e o HTTP (404/405) confirmam.
3. **PP3:** rotas expostas; métodos alcançáveis (*Reuse per Method*).
4. **PP1:** *Reuse percent*, linha de base e sensibilidade dos DTOs; o fato estrutural da não duplicação.
5. **PP4:** custos da abordagem (perde a reconfiguração em runtime, múltiplos builds) e o que não se afere.
6. **Contraste** com a *feature flag* em runtime.
7. **Discussão:** o que os resultados significam em conjunto.
8. **Ameaças à validade:** interna, externa, de construção e de conclusão.

**Foco:** as tabelas e a "leitura do resultado". Diga o que o número significa e pare aí, sem adjetivos.

**Motivação:** nenhum dos modelos mostra resultado quantitativo (Vidal cita "resultados dos testes" que não aparecem). É aqui que a banca vai conferir se a proposta se sustenta.

**Regra:** todo número vem dos scripts congelados (`metrica-reuso.py`, `grafo-metodos.py`, `evidencia-rotas.py`). Reescrever o texto **não** muda número nenhum.

**Pendências da auditoria:**
- A2: a epígrafe da 7.2 ainda está na formulação antiga da PP2.
- A3: a sensibilidade do *Reuse per Method* é prometida e não publicada. Publicar ou retirar a promessa.

**Consulta:** `AVALIACAO.md`, TCC §7, `docs/notes/2026-10-28-auditoria-repositorio.md`.

### Cap. 8 — Conclusão e Trabalhos Futuros
**Passos**
1. **Retome cada objetivo específico do cap. 1**, um por um: o que foi feito e em qual seção está a evidência.
2. **O que se estabeleceu**, com os números principais.
3. **O que não se estabeleceu:** a forma forte no build nativo, a resiliência, a metamorfose das camadas inferiores.
4. **Trabalhos futuros em lista concreta**, cada item derivado de um limite do item 3.
5. **Considerações finais:** um parágrafo curto, sem autoelogio.

**Foco:** a correspondência um a um com os objetivos.

**Motivação:** a banca lê a introdução e a conclusão lado a lado. Evitar o erro de Vidal (afirmar além da evidência) e seguir o registro de Caroline, que delimita o alcance.

**Consulta:** `CONCLUSAO.md`, TCC §8.

### Referências e Apêndice A
- **Referências:** ABNT (NBR 6023). Toda citação no texto deve estar na lista e toda entrada da lista deve ser citada; refazer esse cruzamento depois da reescrita. Auditoria A7: alinhar `CITACOES_ABNT.md`.
- **Apêndice A:** o código dos scripts **não se reescreve**, porque está congelado. Só a prosa que precede cada bloco (propósito, requisitos, invocação, leitura da saída) entra na reescrita. A4: conferir o docstring de A.2 contra a Metodologia.

## 4. Regras transversais (valem para todos os capítulos)
- 3ª pessoa acadêmica ("propõe-se", "observa-se").
- **Registro neutro:** não anunciar o mérito do próprio estudo; declarar o que o texto faz.
- **Remissão por seção** ("conforme a seção 2.5"), nunca por caminho de arquivo.
- Número só nasce de script versionado em `docs/case-study/scripts/`.
- Figura e tabela sempre com número, título e "Fonte:" (as próprias: "Fonte: o autor"), e citadas no texto antes de aparecer.
- Sigla definida na primeira ocorrência e incluída na lista de siglas.

## 5. Checklist de fechamento por capítulo
- [ ] Nenhum marcador pendente (`[Trocar imagem]`, `image*`, "X.X").
- [ ] Toda figura e tabela numerada, com fonte e citada no texto.
- [ ] Citações conferidas contra `CITACOES_ABNT.md`.
- [ ] Remissões entre capítulos com o número de seção correto.
- [ ] Nenhum número fora do que os scripts produzem.
- [ ] ROADMAP atualizado (passo 4 do *modus operandi*).
