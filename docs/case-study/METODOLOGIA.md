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
