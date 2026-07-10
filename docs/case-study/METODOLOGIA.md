# Metodologia

> ✅ **Implementado no TCC** — transposto para o arquivo `Proposta TCC …md` como capítulo *Metodologia* (seções 5.1–5.7), entre a *Proposta* e as *Referências*. **A fonte da verdade passa a ser o TCC**; este documento é mantido apenas como registro de trabalho.
>
> Diferenças aplicadas na transposição: o método de reuso de `METRICA-REUSO.md` foi **absorvido** na seção 5.5.2; as remissões por caminho de arquivo viraram referências de capítulo/seção; as seções foram renumeradas para 5.x; as seis referências novas entraram na lista do TCC; e a *Organização do Trabalho* passou a descrever **oito partes**.

Este capítulo caracteriza a natureza da pesquisa, justifica a adoção da *Design Science Research* (DSR) como método condutor, formula as perguntas de pesquisa, detalha as etapas percorridas e define os procedimentos de avaliação do artefato proposto. O objetivo é tornar o trabalho **reprodutível** e deixar explícito o encadeamento entre o problema, o artefato construído (a Arquitetura Metamórfica e seu protótipo) e as evidências que sustentam ou refutam suas propriedades.

---

## 1. Classificação da Pesquisa

Quanto à sua **natureza**, esta é uma pesquisa **aplicada**, pois se volta à produção de um artefato — uma arquitetura de software e um protótipo funcional — destinado a resolver um problema concreto de engenharia: o reuso de código na construção de múltiplos BFF que atendem a um mesmo contexto de negócio.

Quanto aos **objetivos**, classifica-se como **exploratória e descritiva**: explora uma combinação de conceitos ainda pouco sistematizada na literatura *peer-reviewed* específica sobre BFF e descreve, em detalhe, a arquitetura resultante e seu comportamento.

Quanto à **abordagem**, é predominantemente **qualitativa**, com apoio de **medições quantitativas** na etapa de avaliação (contagem de pontos de entrada, proporção de código reutilizado, presença ou ausência de classes no artefato).

Quanto ao **procedimento técnico**, adota-se a **Design Science Research (DSR)** — a pesquisa orientada ao projeto e à avaliação de um artefato —, detalhada na seção seguinte. Embora o paradigma tenha origem no campo de Sistemas de Informação, sua adequação à Engenharia de Software é sustentada por Engström et al. (2020), que observam que boa parte da pesquisa da área visa projetar soluções para problemas práticos em contexto real, ainda que a lente da *design science* seja pouco empregada para enquadrá-la.

---

## 2. Design Science Research como Método Condutor

A *Design Science Research* é um paradigma de pesquisa cujo produto central é um **artefato** projetado para resolver uma classe de problemas relevantes, avaliado quanto à sua utilidade e ao seu desempenho. Diferentemente de paradigmas puramente descritivos — que buscam explicar *como o mundo é* —, a DSR é prescritiva: preocupa-se com *como o mundo deveria ser* para que um objetivo seja atingido, por meio da construção e da avaliação de artefatos. Peffers et al. (2007) consolidam esse entendimento ao propor um modelo de processo e de avaliação para a pesquisa de *design science* em Sistemas de Informação, cujo produto é um artefato projetado e demonstrado.

A escolha da DSR justifica-se porque a contribuição deste trabalho **é, ela própria, um artefato**: a Arquitetura Metamórfica (um método/design) e sua instanciação no protótipo `modular-bff`. Não se trata de observar um fenômeno preexistente, mas de **projetar** uma solução e **demonstrar** que ela entrega as propriedades reivindicadas — reuso sem duplicação e redução da superfície de ataque por construção. Baskerville et al. (2018) discutem precisamente o que constitui contribuição aceitável em DSR — o artefato de projeto, a teoria de projeto, ou ambos —, e é sob esse enquadramento que este trabalho posiciona sua contribuição no **artefato**, sem reivindicar formulação de teoria de projeto.

Segundo a taxonomia usual da DSR, os artefatos produzidos aqui são de dois tipos complementares:

| Tipo de artefato | Instância neste trabalho |
|---|---|
| **Método** (design/arquitetura) | A **Arquitetura Metamórfica**: variabilidade resolvida em tempo de compilação para derivar artefatos BFF especializados de um único código-fonte. |
| **Instanciação** (protótipo) | O **`modular-bff`**: implementação em Kotlin/Quarkus que demonstra o método sobre o domínio de pedidos de e-commerce. |

---

## 3. Perguntas de Pesquisa

A avaliação do artefato é guiada pelas seguintes perguntas, derivadas do objetivo geral e dos objetivos específicos da Proposta:

- **PP1 — Reuso:** é possível derivar múltiplos artefatos BFF especializados (interno e externo) a partir de um único código-fonte, **sem duplicar** as regras de negócio, os serviços e os repositórios?
- **PP2 — Ausência verificável:** a resolução da variabilidade em tempo de compilação garante que o **ponto de entrada** das operações de um modo — o *bean* CDI, o invocador REST e a rota registrada — **não exista no artefato** do outro modo, de forma verificável e não reversível em tempo de execução?
- **PP3 — Superfície de ataque:** a abordagem metamórfica **reduz o número de pontos de entrada** expostos por artefato, quando comparada a um *build* que contém todos os *controllers*?
- **PP4 — Custo operacional:** que custos a abordagem impõe, dada a necessidade de um *build*, um *pipeline* e um artefato distintos por variante?

> **Delimitação do PP4.** A pergunta não abrange tamanho de artefato, tempo de inicialização nem consumo de memória. Tais grandezas responderiam a "o artefato ficou mais leve?", que **não** é uma propriedade reivindicada por este trabalho: o objetivo geral trata de segurança, resiliência e manutenibilidade. Tampouco haveria o que medir: como o bytecode das classes é idêntico entre as variantes, a equivalência de peso é **dedutível** do resultado da PP2, e não achado a coletar. A PP4 é respondida de forma **qualitativa**. ⚠️ **Revisto em 10 jul. 2026:** uma versão anterior desta delimitação mantinha o tamanho do artefato "com finalidade defensiva"; a defesa é feita pela própria PP2, sem medição, e a medição foi descartada.

> **PP2** é a pergunta central: é ela que converte a tese de *segurança por ausência* de afirmação teórica em evidência observável no artefato.
>
> **Unidade de análise: o ponto de entrada, não a classe.** A pergunta é formulada sobre o ponto de entrada por coerência com a métrica que fundamenta a tese: Manadhata e Wing (2011) definem a superfície de ataque pelo conjunto de pontos de entrada por onde os dados alcançam o sistema a partir do ambiente — não pelo conjunto de classes compiladas. Em arquiteturas de microsserviços, é ao grande número de pontos de entrada que Hannousse e Yahiouche (2021) atribuem o agravamento do desafio de segurança. Uma formulação anterior desta pergunta indagava se as operações "estariam presentes no binário"; a inspeção dos artefatos mostrou que, no empacotamento em JVM, o bytecode da classe **permanece**, ainda que sem *bean*, sem invocador e sem rota. Medir a classe, portanto, seria medir uma grandeza que a métrica adotada não contabiliza. A distinção e sua evidência são desenvolvidas no capítulo de Avaliação.

---

## 4. Etapas da Pesquisa

O trabalho segue as **seis atividades** do modelo de processo DSRM proposto por Peffers et al. (2007) — identificação do problema e motivação, definição dos objetivos de uma solução, projeto e desenvolvimento, demonstração, avaliação e comunicação. Trata-se de estrutura ainda corrente: Baskerville et al. (2018) registram que Peffers e colegas "propõem um método e um esboço de processo para conduzir DSR", ao revisarem a literatura de processo da área. As seis atividades são aqui mapeadas aos capítulos do TCC:

1. **Identificação do problema e motivação.** Caracterização do dilema entre especialização e reuso quando múltiplos BFF atendem a um mesmo contexto. *(Introdução e Justificativa.)*
2. **Definição dos objetivos da solução.** Estabelecimento das propriedades desejadas: gerar múltiplas aplicações de um mesmo código-fonte, mais seguras e de melhor manutenibilidade ao ignorar o que não lhes compete. *(Objetivos.)*
3. **Projeto e desenvolvimento do artefato.** Concepção da Arquitetura Metamórfica e sua fundamentação em Linhas de Produto de Software, variabilidade por anotação e superfície de ataque. *(Proposta e Desenvolvimento.)*
4. **Demonstração.** Implementação do protótipo `modular-bff` e geração dos artefatos interno e externo a partir do mesmo código-fonte. *(Desenvolvimento.)*
5. **Avaliação.** Coleta de evidências que respondem às perguntas de pesquisa — inspeção do binário, contagem de pontos de entrada e medição de reuso. *(Avaliação, Resultados e Discussão.)*
6. **Comunicação.** Registro dos resultados neste TCC e nos documentos de apoio do repositório. *(Trabalho como um todo.)*

---

## 5. Procedimentos de Avaliação

A avaliação é conduzida pela **inspeção do artefato** e por **medições sobre o código-fonte e os artefatos gerados**. Cada procedimento responde a uma ou mais perguntas de pesquisa.

**Qual artefato se mede.** O objeto de medição é, exclusivamente, o **artefato de produção**: o gerado no estágio de *builder* do `Dockerfile`, a partir do código-fonte e das *flags* de `--build-arg`, isolado do estado da máquina do desenvolvedor. As afirmações do trabalho referem-se ao que é **entregue**; medi-las sobre um artefato de desenvolvimento — dependente do diretório `build/` local, do cache do Gradle e das variáveis da sessão — não as sustentaria. O *build* local não integra o desenho experimental.

### 5.1 Procedimento experimental

1. **Geração das variantes.** Do mesmo código-fonte, sem qualquer alteração entre os *builds*, produzem-se três artefatos de produção, alternando as *flags* (`BFF_API_INTERNAL_ACTIVE`, `BFF_API_EXTERNAL_ACTIVE`) que controlam a anotação `@IfBuildProperty` dos *controllers*: `default` (ambas `false`), `internal` e `external`. Uma quarta variante, com **ambos os modos ativos**, serve de **linha de base** para as comparações. Os *builds* são feitos com `--no-cache`, pelo alvo `make container/build/variants`.
2. **Inspeção do ponto de entrada (PP2).** Para cada variante verifica-se, em duas camadas:
   - **primária** — a rota do outro modo não consta do documento OpenAPI da variante e uma requisição a ela não é atendida;
   - **corroborativa** — o *bean* CDI e o invocador REST da operação do outro modo não existem no artefato gerado na fase de *augmentation* (`quarkus/generated-bytecode.jar`), enquanto o bytecode da classe permanece no JAR da aplicação (`app/*.jar`).

   A camada primária é a que sustenta a conclusão, por ser independente de convenções internas do *framework*; a corroborativa explica **por que** a rota não existe. O alvo `make artifact/analysis` executa a inspeção da camada corroborativa nas três variantes.
3. **Contagem de pontos de entrada (PP3).** Enumeram-se as rotas de negócio expostas por variante e comparam-se com a variante de linha de base, que contém todos os *controllers*, enquadrando o resultado pela métrica de superfície de ataque (MANADHATA; WING, 2011) — arrolada por Hannousse e Yahiouche (2021) entre as metodologias correntes de verificação e validação de segurança em arquiteturas de microsserviços. Excluem-se da contagem os endpoints de infraestrutura (`/health`, `/openapi`, `/docs`), presentes em toda variante e alheios à variabilidade. A contagem é **não ponderada**, ao passo que a métrica de referência pondera os pontos de entrada por dano potencial e custo de acesso — aproximação assumida e discutida nas ameaças à validade.
4. **Medição de reuso (PP1).** Quantifica-se a proporção entre código **compartilhado** (domínio, serviços, repositórios) e código **especializado** (controllers, DTOs), por número de classes e/ou linhas de código. O método completo — métricas adotadas (*reuse percent* e *reuse level*), fontes, classificação dos ativos e procedimento de coleta — está em `METRICA-REUSO.md` e **deve ser incorporado a esta seção** quando o capítulo for implementado no TCC.
5. **Custo operacional (PP4).** Enumeram-se os custos que decorrem da própria arquitetura, verificáveis por inspeção e **sem medição**: multiplicação de *pipelines*, artefatos e implantações; perda da reconfiguração em tempo de execução (o preço exato da PP2); crescimento combinatório das variantes; custo de verificação de cada artefato derivado; rastreabilidade da variante em produção. ⚠️ **Revisto em 10 jul. 2026:** o **tamanho da imagem** foi **descartado** como grandeza a coletar — o trabalho não reivindica leveza, e a equivalência de peso entre variantes é dedutível do resultado da PP2 (o bytecode é idêntico).
6. **Contraste com feature flag em runtime.** ⚠️ **Revisto em 10 jul. 2026: deixa de ser experimento.** O contraste é estabelecido **por argumento**: na alternativa em runtime, o *bean* é registrado, a rota consta do documento OpenAPI e a requisição alcança um manipulador que a recusa por condicional. É propriedade **definidora** daquela abordagem, não hipótese a testar — reproduzi-la nada acrescentaria. Registrado como escolha de escopo no capítulo de Avaliação (seção 7.6).

> ⚠️ **Estado da coleta.** Os procedimentos 1 a 3 foram executados e seus resultados constam do capítulo de Avaliação. Os procedimentos 4 a 6 permanecem pendentes; até que sejam executados, o capítulo de Avaliação mantém marcadores `[A PREENCHER]` nos pontos correspondentes.

### 5.2 Ambiente e ferramentas

Para garantir a **reprodutibilidade**, registram-se as versões e ferramentas do ambiente de referência:

| Item | Especificação |
|---|---|
| Linguagem / plataforma | Kotlin 2.4.0 · JVM 21 |
| Framework | Quarkus 3.37.0 (mecanismo `@IfBuildProperty` do Quarkus Arc) |
| Build local (não integra o experimento) | Gradle (Kotlin DSL) |
| Geração dos artefatos de produção | Docker · `make container/build/variants` (com `--no-cache`) |
| Inspeção de artefato | `make artifact/analysis` (executa `jar tf` no interior de cada imagem) |
| Contagem de linhas de código (PP1) | Contador que distinga comentários — `cloc`, `scc` ou `tokei` — com a versão registrada |
| Persistência | PostgreSQL + Hibernate ORM · migrações com Goose |

Não se registram sistema operacional nem especificação de *hardware*: nenhuma das medições é sensível a eles. As grandezas aferidas — presença de pontos de entrada, contagem de rotas, proporção de código e tamanho de imagem — são propriedades dos artefatos, e não do ambiente que os executa.

Cada medição deve registrar os **comandos exatos** e as *flags* utilizadas, de modo que um terceiro possa repetir o experimento e obter os mesmos artefatos.

---

## 6. Quadro de Amarração (Objetivos × Método × Evidência)

O quadro a seguir liga cada objetivo específico e cada pergunta de pesquisa ao método de verificação e ao capítulo que apresentará a evidência.

| Objetivo / Pergunta | Método de verificação | Evidência esperada | Capítulo |
|---|---|---|---|
| Apresentar conceitualmente a Arquitetura Metamórfica | Fundamentação teórica e posicionamento na literatura (SPL, variabilidade, superfície de ataque) | Arquitetura descrita e ancorada em referências consolidadas | Referencial Teórico · Proposta |
| Apresentar um protótipo conceito | Implementação e demonstração do `modular-bff` | Protótipo funcional com as variantes reproduzíveis | Desenvolvimento |
| **PP1 — Reuso** | *Reuse percent* e *reuse level* sobre o código-fonte (seção 5.1, passo 4) | Proporção de reuso quantificada | Avaliação |
| **PP2 — Ausência do ponto de entrada** | Ausência da rota no documento OpenAPI e no atendimento HTTP; ausência de *bean* e invocador no artefato de *augmentation* | Ponto de entrada do outro modo inexistente na variante | Avaliação |
| **PP3 — Superfície de ataque** | Contagem não ponderada de rotas de negócio por variante, contra a linha de base | Redução mensurável de pontos de entrada | Avaliação |
| **PP4 — Custo operacional** | Enumeração qualitativa dos custos decorrentes da arquitetura, por inspeção | Custo estrutural explicitado, incluindo a rigidez em runtime como contrapartida da PP2 | Avaliação · Ameaças à Validade |

### 6.1 Propriedades reivindicadas sem método de verificação

O objetivo geral reivindica aplicações mais **seguras**, **resilientes** e de melhor **manutenibilidade**. O quadro acima verifica a primeira, por meio da PP2 e da PP3. As outras duas exigem declaração explícita, sob pena de o trabalho afirmar o que não verificou:

| Propriedade | Situação |
|---|---|
| Segurança | **Verificada**, no recorte da superfície de ataque (PP2, PP3). Não abrange autenticação, autorização, limitação de abuso nem mascaramento de dados. |
| Manutenibilidade | **Inferida, não medida.** O trabalho afere o **reuso** (PP1), que é seu antecedente: concentrar o código comum em um único ponto reduz o esforço de manutenção (TAIBI; LENARDUZZI; PAHL, 2018), relação que Chen, Usman e Badampudi (2024) observam em casos industriais — com a ressalva de que a evidência ali reunida é mista. Não se mede esforço de alteração nem densidade de defeitos. |
| Resiliência | **Não aferida.** Nenhum procedimento deste capítulo a verifica. O objetivo geral a enuncia como **decorrência esperada** da menor exposição, e é assim que deve ser lida — nunca como resultado. |

A distinção entre propriedade verificada, inferida e não aferida é parte do método, e não uma concessão feita no capítulo de resultados.

---

## 7. Ameaças à Validade (encaminhamento)

Por se tratar de pesquisa baseada em um único artefato, a discussão das **ameaças à validade** — protótipo e domínio únicos (validade externa), escopo delimitado da segurança por ausência e escolha das métricas — é desenvolvida no capítulo de Avaliação, Resultados e Discussão, conforme praxe em TCCs orientados a evidência.

---

> ### 📌 Registro de fechamento
> - **Absorver o método de reuso:** ✅ feito — `METRICA-REUSO.md` foi incorporado à seção 5.5.2 do TCC.
> - **Fundamentação metodológica:** ✅ resolvida. As seis atividades estão ancoradas em PEFFERS et al. (2007), com corroboração de BASKERVILLE et al. (2018) quanto ao processo e de ENGSTRÖM et al. (2020) quanto à adequação da DSR à Engenharia de Software. As referências 7.1, 7.4 e 7.5 de `REFERENCIAS_CIENTIFICAS.md` permanecem **não verificadas** e não são citadas.
> - **Resiliência no objetivo geral:** ✅ decidida — mantida como **decorrência esperada** da menor exposição, explicitamente não aferida (seção 5.6.1 do TCC).
>
> **Follow-ups editoriais (no TCC):** as seções 5.3, 5.5 e 5.7 remetem aos capítulos *Desenvolvimento* e *Avaliação*, ainda não implementados; as remissões passam a valer quando esses capítulos entrarem.

---

## 8. Auditoria: divergências entre a Metodologia publicada e a Avaliação executada

> **Data:** 10 jul. 2026, após a implementação do capítulo de Avaliação no TCC (seções 7.1–7.8) e do Apêndice A.
>
> **Conclusão da auditoria:** ⚠️ **a Metodologia do TCC (capítulo 5) NÃO descrevia mais o que a Avaliação de fato fez.** Havia uma contradição **interna ao capítulo 5** e seis lacunas de procedimento.
>
> ✅ **Corrigido no TCC em 10 jul. 2026.** As sete divergências abaixo foram sanadas no capítulo 5, que é a fonte da verdade. Esta seção é mantida como registro da auditoria e do que foi alterado.

### 8.1 ⛔ Contradição interna — o objeto de medição

**Onde:** TCC, seção 5.5, parágrafo de abertura.

O texto afirma:

> "O objeto de medição é, **exclusivamente**, o **artefato de produção** […] O *build* local não integra o desenho experimental."

Isso é **falso** para duas das quatro perguntas, e o próprio capítulo 5 o desmente duas subseções adiante:

| Onde | O que diz | Conflito |
|---|---|---|
| 5.5.2 | "A medição de reuso é feita sobre o **código-fonte**" | O código-fonte não é o artefato de produção |
| 5.5.3 | "grafo de chamadas […] por análise estática do **bytecode compilado**" | O bytecode compilado localmente não é o artefato de produção |

A Avaliação já corrigiu o ponto: a seção 7.1 declara o objeto **por pergunta**, sob a regra "mede-se onde a propriedade existe" — PP2 e a contagem de rotas da PP3 recaem sobre o artefato de produção em execução; o *Reuse per Method* recai sobre o bytecode compilado (porque a variante é definida pelo conjunto de raízes, e o bytecode é idêntico entre variantes); a PP1 recai sobre o código-fonte; a PP4 sobre a arquitetura, por inspeção.

**Correção — ✅ aplicada.** O parágrafo de 5.5 foi substituído por um quadro que declara o objeto **por pergunta**, sob a regra "mede-se onde a propriedade existe", com a consequência para a validade remetida ao capítulo de Avaliação.

### 8.2 O estatuto das duas camadas de evidência da PP2 mudou

**Onde:** TCC, seção 5.5.1, passo 2.

O texto coloca **OpenAPI e HTTP juntos** na camada "primária". A Avaliação hierarquizou: **o critério primário é a ausência da rota no documento OpenAPI**; o status HTTP é **corroborativo** (seção 7.2.3). A mudança não é cosmética — decorre de dois achados da coleta, ambos ausentes da Metodologia:

1. **A rota suprimida devolve `404` *ou* `405`.** A URI `/v1/order/create` casa com o template `/v1/order/{orderId}`, atendido apenas por `GET`; na ausência do *controller* externo, o `POST` encontra o recurso e não o método. Verificar esperando `404` produziria **falso negativo**.
2. **O `404` é ambíguo.** Significa "não há rota" **ou** "há rota, e o recurso não existe". Sobre recurso inexistente, produziria **falso positivo** numa rota exposta.

O passo 2 da Metodologia diz apenas "uma requisição a ela não é atendida", formulação que **não distingue** nenhum desses casos.

**Correção — ✅ aplicada.** O passo 2 passou a ter **três camadas de estatuto desigual** — primária (contrato publicado), corroborativa (atendimento HTTP) e explicativa (bytecode gerado) — e um parágrafo *Leitura do status HTTP* que registra os dois status e as duas razões da ambiguidade.

### 8.3 O procedimento de derivação dos alvos não está descrito

**Onde:** TCC, seção 5.5.1, passo 2 — ausente.

A Avaliação (7.2.3) estabelece que **nenhum alvo de escrita é fixado de antemão**: antes das requisições, lista-se os pedidos da própria variante e deriva-se dela um pedido e uma entrega existentes, com avanço de status legítimo, e um pedido novo válido, com cliente, endereço, contato e SKU cujo preço coincide com o do catálogo. É o que permite obter `201` e `202` — evidência **positiva** de que a operação se completa — em lugar da mera recusa de requisições malformadas.

Nada disso consta da Metodologia, que assim descreve um experimento mais fraco do que o executado.

**Correção — ✅ aplicada.** O passo 2 ganhou o parágrafo *Derivação dos alvos*, que descreve a extração do pedido, da entrega, do avanço de status e do pedido novo a partir da listagem da própria variante, e declara a base populada como pré-condição.

### 8.4 A verificação do contrato publicado não estava prevista

**Onde:** TCC, seção 5.5.1 — ausente; seção 5.6, linha da PP2 — ausente.

A Avaliação inclui a seção **7.2.4**, que verifica, em cada variante, que a interface do Swagger é servida e busca o documento OpenAPI, estabelecendo a relação de **projeção** entre interface e contrato, e daí a propriedade de a documentação metamorfosear junto com o artefato. A verificação foi executada e citada (QUARKUS, 2026e), mas **não é prevista por procedimento algum** do capítulo 5.

**Correção — ✅ aplicada.** Optou-se por **incorporar ao método**: a camada primária do passo 2 inclui agora a verificação de que a interface do Swagger é servida e busca o documento, e o quadro de amarração (5.6) menciona o contrato que não anuncia a rota suprimida.

### 8.5 O ambiente de execução da PP2 não consta

**Onde:** TCC, seção 5.5.4 (tabela).

A tabela cobre a geração dos artefatos, a inspeção dos JAR, `cloc` e `javap`. Não cobre o que a PP2 de fato exigiu:

- execução de **cada variante em contêiner**, contra um **PostgreSQL migrado e populado**;
- a **base de dados populada** como pré-condição do experimento — sem pedidos reais, o `PATCH` devolveria `404` por ausência de recurso, e o status seria ambíguo;
- Python como linguagem de **todos** os programas de coleta, e não apenas do roteiro do grafo.

**Correção — ✅ aplicada.** A tabela de 5.5.4 ganhou as linhas *Execução das variantes*, *Interrogação do contrato e das rotas* e *Programas de coleta*; os comandos exatos passaram a ser remetidos ao **Apêndice A**.

### 8.6 A afirmação de insensibilidade ao ambiente ficou forte demais

**Onde:** TCC, seção 5.5.4, último parágrafo.

O texto afirma que "nenhuma das medições é sensível" ao sistema operacional e ao *hardware*, porque as grandezas são propriedades dos artefatos. A conclusão permanece verdadeira quanto a SO e *hardware*, mas a coleta da PP2 introduziu uma dependência que o texto não registra: **o resultado depende do estado da base de dados**, e a própria coleta **muta esse estado** — cada execução avança a entrega alvo dois estados e cria dois pedidos.

Não é defeito: a mutação é o que prova que as rotas expostas fazem o que prometem. Mas é condição de reprodutibilidade, e cabe declará-la.

**Correção — ✅ aplicada.** A afirmação sobre SO e *hardware* permaneceu; acrescentou-se parágrafo declarando a dependência do estado da base e que **a coleta o altera**, sendo isso o que evidencia que as rotas expostas produzem efeito.

### 8.7 Resíduo do objetivo geral

**Onde:** TCC, seção 5.4, atividade 2.

O texto ainda diz "mais seguras e de melhor manutenibilidade **ao ignorar** o que não lhes compete". O objetivo geral foi corrigido para **"ao suprimirem"** — o artefato não ignora a operação alheia em tempo de execução; ele não a contém. O verbo antigo descreve a *feature flag*, não a metamorfose.

**Correção — ✅ aplicada.** Na 5.4, "ao ignorar" virou "ao **suprimirem**". Corrigiu-se também a 5.1, que ainda falava em medir "presença ou ausência de **classes**".

---

### 8.8 Síntese

| # | Divergência | Gravidade |
|---|---|---|
| 8.1 | 5.5 afirma objeto de medição único; 5.5.2 e 5.5.3 o contradizem | ⛔ **Contradição interna** |
| 8.2 | Hierarquia OpenAPI × HTTP e os status `404`/`405` ausentes | 🔴 Método descreve experimento mais fraco |
| 8.3 | Derivação dos alvos de escrita não descrita | 🔴 Método descreve experimento mais fraco |
| 8.4 | Verificação do Swagger como projeção não prevista | 🟡 Procedimento executado sem previsão |
| 8.5 | Ambiente da PP2 (contêiner, banco populado) ausente da tabela | 🟡 Reprodutibilidade incompleta |
| 8.6 | "Insensível ao ambiente" ignora estado da base e mutação | 🟡 Afirmação forte demais |
| 8.7 | "ao ignorar" em vez de "ao suprimirem" | 🟢 Resíduo de redação |

**Todas corrigidas no TCC em 10 jul. 2026.** Nenhuma invalidava um resultado: todas eram do tipo *o método publicado descreve menos, ou coisa diversa, do que a execução realizou* — e três delas (8.2, 8.3, 8.5) descrevem um experimento **mais fraco** do que o efetivamente conduzido, o que é o pior tipo de erro nesta direção: subestima a própria evidência e impede que um terceiro a reproduza.
