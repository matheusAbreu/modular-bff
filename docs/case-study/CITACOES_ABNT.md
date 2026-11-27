# Citações de Apoio ao TCC (padrão ABNT)

> **Tema:** *Reutilização de código para criar múltiplos projetos do tipo BFF* — a **Arquitetura Metamórfica**, em que um único código-fonte gera múltiplos artefatos por configuração em **tempo de compilação**, reduzindo duplicação e superfície de ataque.

Este documento reúne **citações diretas** extraídas dos PDFs em `docs/refs/`, organizadas pelos eixos de argumentação do TCC. Cada trecho vem no idioma original (para não descaracterizar a citação), seguido de uma **tradução livre em pt-br** e de uma glosa explicando **como corrobora o trabalho**, e a chamada no **sistema autor-data (ABNT NBR 10520)**. As referências completas (ABNT NBR 6023) estão ao final.

---

## Eixo 1 — Microsserviços descentralizados e a necessidade de agregação (Introdução)

**PAHL; JAMSHIDI (2016):**

> "microservices are about functional decomposition often in a domain-driven design context. They are characterised by well-defined and explicitly published interfaces. Each service is fully autonomous and full-stack. Consequently, changing a service implementation has no impact to other services as communication takes place using interfaces only." (PAHL; JAMSHIDI, 2016, p. 138).
>
> *Tradução livre:* "os microsserviços dizem respeito à decomposição funcional, frequentemente num contexto de design orientado ao domínio. Caracterizam-se por interfaces bem definidas e explicitamente publicadas. Cada serviço é totalmente autônomo e full-stack. Consequentemente, alterar a implementação de um serviço não tem impacto sobre os demais, pois a comunicação ocorre apenas por meio de interfaces."

> "Microservices are independently deployable, usually supported by a deployment and orchestration framework […] enabling them to deploy often and independently at arbitrary schedules." (PAHL; JAMSHIDI, 2016, p. 137).
>
> *Tradução livre:* "Os microsserviços são implantáveis de forma independente, geralmente apoiados por um framework de implantação e orquestração […] que lhes permite implantar com frequência e de forma independente, em cronogramas arbitrários."

Entre os princípios do estilo, os autores listam o "*decentralized control of data*" (PAHL; JAMSHIDI, 2016, p. 140) — o *controle descentralizado de dados* que, no TCC, justifica a existência de uma camada de agregação para o frontend.

> *Glosa:* fundamentam o cenário da Introdução — serviços autônomos, com dados descentralizados, comunicando-se só por interfaces —, exatamente o contexto que motiva um ponto central de agregação (API Gateway/BFF).

**TAIBI; LENARDUZZI; PAHL (2018):**

> "Microservices are small autonomous services deployed independently, with a single and clearly defined purpose. […] because of their size, they are easier to maintain and more fault-tolerant since the failure of one service will not break the whole system, which could happen in a monolithic system." (TAIBI; LENARDUZZI; PAHL, 2018, p. 221).
>
> *Tradução livre:* "Os microsserviços são pequenos serviços autônomos, implantados de forma independente, com um propósito único e claramente definido. […] por causa de seu tamanho, são mais fáceis de manter e mais tolerantes a falhas, já que a falha de um serviço não derruba o sistema inteiro, o que poderia acontecer num sistema monolítico."

> *Glosa:* corrobora as vantagens de manutenibilidade e resiliência dos microsserviços que o TCC contrasta com o monólito.

---

## Eixo 2 — API Gateway, BFF e o problema da duplicação (Justificativa)

**TAIBI; LENARDUZZI; PAHL (2018)** — sobre o padrão API-Gateway como agregador:

> "the creation of end-user applications based on the composition of different microservices requires a server-side aggregation mechanism. In the selected works, the API-Gateway emerged as a commonly recommended approach." (TAIBI; LENARDUZZI; PAHL, 2018, p. 223).
>
> *Tradução livre:* "a criação de aplicações para o usuário final a partir da composição de diferentes microsserviços exige um mecanismo de agregação do lado do servidor. Nos trabalhos selecionados, o API-Gateway despontou como a abordagem mais recomendada."

> "API Gateway is the entry point of the system that routes the requests to the appropriate microservices, also invoking multiple microservices and aggregating results. It provides a tailored API to each client […]." (TAIBI; LENARDUZZI; PAHL, 2018, p. 223).
>
> *Tradução livre:* "O API Gateway é o ponto de entrada do sistema que roteia as requisições para os microsserviços apropriados, invocando também múltiplos microsserviços e agregando resultados. Ele fornece uma API sob medida para cada cliente […]."

O trecho a seguir descreve, na prática, a ideia de **BFF** (API sob medida por frontend):

> "the API-Gateway is responsible for communicating with the different front-ends, creating a custom API for each client for them to see only the features they need, which simplifies the creation of end-user applications without adding the complexity of exposing and parsing useless information." (TAIBI; LENARDUZZI; PAHL, 2018, p. 224).
>
> *Tradução livre:* "o API-Gateway é responsável por se comunicar com os diferentes front-ends, criando uma API personalizada para cada cliente, de modo que ele veja apenas os recursos de que precisa, o que simplifica a criação de aplicações para o usuário final sem acrescentar a complexidade de expor e interpretar informação inútil."

E o **problema central do TCC** (duplicação/manutenção quando há vários clientes) aparece como desvantagem reconhecida do padrão:

> "API reuse must be considered carefully. Since each client can have a custom API, we must keep track of cases where different types of clients use the same API and modify both of them accordingly for changes to the API." (TAIBI; LENARDUZZI; PAHL, 2018, p. 224).
>
> *Tradução livre:* "O reuso de API deve ser considerado com cuidado. Como cada cliente pode ter uma API personalizada, é preciso rastrear os casos em que diferentes tipos de clientes usam a mesma API e modificá-las em conjunto a cada alteração da API."

A contrapartida — o ganho de **reuso** ao compartilhar um mesmo componente entre projetos — também é registrada:

> "Reuse. The maintenance of a single microservice will reflect on any connected project, reducing the effort overhead by applying the same changes to the same component used in different projects." (TAIBI; LENARDUZZI; PAHL, 2018, p. 227).
>
> *Tradução livre:* "Reuso. A manutenção de um único microsserviço se refletirá em qualquer projeto conectado, reduzindo o esforço adicional ao aplicar as mesmas mudanças ao mesmo componente usado em diferentes projetos."

> *Glosa:* este é o núcleo da Justificativa. A literatura reconhece tanto o valor da "custom API por cliente" (BFF) quanto o **custo de manter APIs duplicadas** — precisamente a dor que a Arquitetura Metamórfica se propõe a resolver reusando um único código-fonte.

Complementarmente, o padrão de agregação também aparece em **PAHL; JAMSHIDI (2016):**

> "Aggregator Microservice Design Pattern – e.g., a service invoking others to retrieve / process data." (PAHL; JAMSHIDI, 2016, p. 140).
>
> *Tradução livre:* "Padrão de Projeto de Microsserviço Agregador – p. ex., um serviço que invoca outros para recuperar / processar dados."

**NEWMAN (2015)** — a caracterização primária do BFF (fonte pública do autor; **paráfrase**, não citação direta, por ser artigo web protegido):

Newman caracteriza o BFF como um serviço de borda (*edge service*) de propósito único, acoplado a uma experiência de usuário específica e mantido pela mesma equipe que constrói aquela interface — em contraste com um backend/gateway de propósito geral que serve a múltiplos clientes (NEWMAN, 2015). Situa a origem do padrão em observações na REA e no SoundCloud, e atribui a diretriz "*one experience, one BFF*" a Stewart Gleadow (NEWMAN, 2015).

> *Glosa:* fonte **primária, pública e verificável** do conceito central do trabalho; sustenta a caracterização do BFF como backend dedicado a um único tipo de cliente. Optou-se por paráfrase (e não citação direta longa) por se tratar de artigo web protegido por direito autoral.

---

## Eixo 3 — Linhas de produto, variabilidade e derivação de produtos (fundamento da "Metamorfose")

**HORCAS; PINTO; FUENTES (2023)** — SPL como derivação de múltiplos produtos de uma base comum:

> "An increasing number of software application domains are adopting Software Product Line (SPL) approaches to cope with the high variability they present." (HORCAS; PINTO; FUENTES, 2023, p. 377).
>
> *Tradução livre:* "Um número crescente de domínios de aplicação de software vem adotando abordagens de Linha de Produto de Software (SPL) para lidar com a alta variabilidade que apresentam."

A distinção formal entre as duas famílias de implementação de variabilidade — a base teórica para classificar o `@IfBuildProperty`:

> "There are different approaches to implement the variability of the reusable artifacts of an SPL. Mainly, they can be divided in composition-based approaches and annotation-based approaches or a combination of both approaches. Composition-based approaches include component and service composition, design patterns, feature-orientation, aspect-orientation, etc., while annotation-based approaches include configuration parameters, preprocessors, and virtual separation of concerns, among others." (HORCAS; PINTO; FUENTES, 2023, p. 381).
>
> *Tradução livre:* "Existem diferentes abordagens para implementar a variabilidade dos artefatos reutilizáveis de uma SPL. Basicamente, elas podem ser divididas em abordagens baseadas em composição e abordagens baseadas em anotação, ou uma combinação das duas. As abordagens baseadas em composição incluem composição de componentes e serviços, padrões de projeto, orientação a features, orientação a aspectos etc., enquanto as abordagens baseadas em anotação incluem parâmetros de configuração, pré-processadores e separação virtual de interesses, entre outras."

E a definição da fase de **derivação do produto**:

> "The product derivation phase is in charge of generating or deriving the final product by resolving the variability specified in the product configuration. […] This includes the derivation of the product, by resolving the variability of each variation point in the artifacts of the SPL according to the selected configuration of the feature model." (HORCAS; PINTO; FUENTES, 2023, p. 381).
>
> *Tradução livre:* "A fase de derivação do produto é responsável por gerar ou derivar o produto final resolvendo a variabilidade especificada na configuração do produto. […] Isso inclui a derivação do produto, resolvendo a variabilidade de cada ponto de variação nos artefatos da SPL de acordo com a configuração selecionada do modelo de features."

> *Glosa:* posiciona a Arquitetura Metamórfica no campo de SPL: cada artefato (interno/externo) é um **produto derivado** do mesmo código pela seleção de features; o `@IfBuildProperty` é uma técnica **baseada em anotação** (categoria "configuration parameters").

**HUNSEN et al. (2016)** — evidência empírica de que a variabilidade por anotação/compilação condicional é mainstream:

> "Configuration and variability mechanisms form the technical basis for software-product-line engineering, as they facilitate to create families of software systems that share common assets." (HUNSEN et al., 2016, p. 1).
>
> *Tradução livre:* "Os mecanismos de configuração e variabilidade formam a base técnica da engenharia de linhas de produto de software, pois facilitam a criação de famílias de sistemas de software que compartilham ativos comuns."

> "Conditional compilation is a simple variability-implementation mechanism that is widely used in open-source projects and industry. […] CPP supports conditional compilation through preprocessor directives (e.g., #ifdef), which enable the programmer to include or exclude parts of the code base by providing a corresponding configuration. […] One of the most prominent examples is the Linux kernel, which uses the preprocessor to allow the developer to choose among 12 000 distinct options or features at build time." (HUNSEN et al., 2016, p. 1).
>
> *Tradução livre:* "A compilação condicional é um mecanismo simples de implementação de variabilidade, amplamente usado em projetos de código aberto e na indústria. […] O CPP suporta compilação condicional por meio de diretivas de pré-processador (p. ex., #ifdef), que permitem ao programador incluir ou excluir partes da base de código fornecendo a configuração correspondente. […] Um dos exemplos mais proeminentes é o kernel do Linux, que usa o pré-processador para permitir ao desenvolvedor escolher entre 12.000 opções ou features distintas em tempo de compilação (build)."

> "Defining different sets of configuration constants (macros) will result in different compiled programs — the variants of the software system." (HUNSEN et al., 2016, p. 6).
>
> *Tradução livre:* "Definir diferentes conjuntos de constantes de configuração (macros) resultará em programas compilados diferentes — as variantes do sistema de software."

> *Glosa:* dá lastro empírico ao "coração" do TCC — a variabilidade resolvida **em tempo de build** gera artefatos distintos ("variants") a partir de uma base única, mecanismo consolidado em sistemas reais.

**BAUM; SIXTUS; VOGELSBERG; EISENECKER (2019)** — compilação condicional como técnica de SPL:

> "The C preprocessor (CPP) is a standard tool for introducing variability into source programs and is often applied either implicitly or explicitly for implementing a Software Product Line (SPL)." (BAUM et al., 2019, p. 1).
>
> *Tradução livre:* "O pré-processador C (CPP) é uma ferramenta padrão para introduzir variabilidade em programas-fonte e é frequentemente aplicado, implícita ou explicitamente, para implementar uma Linha de Produto de Software (SPL)."

> "Conditional compilation is a way of introducing variability […] immediately before compile time. The CPP can be used to include or exclude source code components, which change the structure and behavior of the resulting program. Often Boolean feature flags are used to design complete SPLs." (BAUM et al., 2019, p. 1).
>
> *Tradução livre:* "A compilação condicional é uma forma de introduzir variabilidade […] imediatamente antes do tempo de compilação. O CPP pode ser usado para incluir ou excluir componentes de código-fonte, que alteram a estrutura e o comportamento do programa resultante. Frequentemente, feature flags booleanas são usadas para projetar SPLs completas."

> *Glosa:* reforça que **feature flags booleanas em tempo de compilação** incluem/excluem componentes e "mudam a estrutura do programa resultante" — a essência do *Design Metamórfico* (o endpoint não existe no binário quando a flag está desligada).

---

## Eixo 4 — Segurança por redução de superfície de ataque (Design Metamórfico como segurança)

**MANADHATA; WING (2005)** — a tese de que menos superfície = mais segurança:

> "the larger the attack surface, the more likely the system will be attacked, and hence the more insecure it is." (MANADHATA; WING, 2005, p. 1).
>
> *Tradução livre:* "quanto maior a superfície de ataque, mais provável é que o sistema seja atacado e, portanto, mais inseguro ele é."

> "An attacker uses a system's resources to attack a system, hence a system's resources contribute to the system's attack surface. […] The more exposed the attack surface, the more ways the system can be attacked, and hence the more insecure it is." (MANADHATA; WING, 2005, p. 1).
>
> *Tradução livre:* "Um atacante usa os recursos de um sistema para atacá-lo; portanto, os recursos de um sistema contribuem para a sua superfície de ataque. […] Quanto mais exposta a superfície de ataque, mais formas há de atacar o sistema e, portanto, mais inseguro ele é."

A métrica atua no **nível de projeto** — útil a quem desenha a arquitetura:

> "Our attack surface metric strikes at the design level of a system: above the level of code, but below the level of the entire system. […] Our approach focuses on a system's design, and hence can be used by system designers and developers to improve the security of a system." (MANADHATA; WING, 2005, p. 3).
>
> *Tradução livre:* "Nossa métrica de superfície de ataque atua no nível de projeto de um sistema: acima do nível do código, mas abaixo do nível do sistema como um todo. […] Nossa abordagem foca no projeto do sistema e, portanto, pode ser usada por projetistas e desenvolvedores para melhorar a segurança de um sistema."

Os **pontos de entrada** como pontos de ataque:

> "entry points of a system are the ways through which data 'enters' the system from its environment […]. Hence the entry points and the exit points of a system act as the entry points of attack on the system." (MANADHATA; WING, 2005, p. 4).
>
> *Tradução livre:* "os pontos de entrada de um sistema são as vias pelas quais os dados 'entram' no sistema a partir de seu ambiente […]. Portanto, os pontos de entrada e de saída de um sistema funcionam como os pontos de entrada de ataque ao sistema."

E — diretamente aplicável à separação interno/externo do projeto — a definição formal de **método externo vs. interno**:

> "A method m of s is an external method if the user U or the methods of any system s1 ∈ T can invoke m. A method m is an internal method if only methods of s can invoke m." (MANADHATA; WING, 2005, p. 5).
>
> *Tradução livre:* "Um método m de s é um método externo se o usuário U ou os métodos de qualquer sistema s1 ∈ T puderem invocar m. Um método m é um método interno se apenas métodos de s puderem invocá-lo."

> *Glosa:* fundamenta cientificamente a "segurança por ausência". Remover endpoints no build **reduz a superfície de ataque** (menos pontos de entrada = menos ataques possíveis), e a distinção formal método externo/interno espelha os controllers `External`/`Internal` do protótipo.

**HANNOUSSE; YAHIOUCHE (2020)** — a superfície de ataque ampliada é a dor de segurança dos microsserviços:

> "security is a longstanding problem in networking systems, but with microservices, security becomes more challenging. This is due to the large number of entry points and overload on communication traffic emerged by decomposing systems into smaller, independent and distributed software units." (HANNOUSSE; YAHIOUCHE, 2020, p. 2).
>
> *Tradução livre:* "a segurança é um problema antigo em sistemas de rede, mas com os microsserviços ela se torna mais desafiadora. Isso se deve ao grande número de pontos de entrada e à sobrecarga do tráfego de comunicação que surgem ao decompor sistemas em unidades de software menores, independentes e distribuídas."

O estudo classifica ameaças por origem **interna ou externa** ("*Source of Threats: internal or external*", item D7 do formulário de extração) e conclui:

> "The results of the mapping revealed an unbalanced research focus in favor of external attacks […]. We advocate more researches addressing internal attacks, proposing mitigation techniques and concerning more layers of MSA including communication and deployment." (HANNOUSSE; YAHIOUCHE, 2020, p. 1).
>
> *Tradução livre:* "Os resultados do mapeamento revelaram um foco de pesquisa desequilibrado em favor dos ataques externos […]. Defendemos mais pesquisas que tratem de ataques internos, propondo técnicas de mitigação e abrangendo mais camadas da MSA, incluindo comunicação e implantação."

> *Glosa:* liga "*large number of entry points*" à insegurança de MSA, sustentando que **eliminar pontos de entrada** (o que o Design Metamórfico faz por construção) ataca a raiz do problema; e a distinção ameaça interna/externa dá respaldo à separação de modos do TCC.

E o registro de que a superfície de ataque permanece método corrente de verificação de segurança:

> "Methodology based analysis: use predefined and well-known methodologies such as OWASP risk rating, attack surface or security risk comparison." (HANNOUSSE; YAHIOUCHE, 2020, p. 15).
>
> *Tradução livre:* "Análise baseada em metodologia: usar metodologias predefinidas e bem conhecidas, tais como a classificação de risco da OWASP, a superfície de ataque ou a comparação de riscos de segurança."

*Glosa:* cumpre a função de **redundância de fonte** para MANADHATA; WING (2011), anterior a 2014. Um mapeamento sistemático posterior arrola a superfície de ataque entre as metodologias "bem conhecidas" de verificação e validação de segurança em arquiteturas de microsserviços — o que atesta a vigência da métrica adotada, sem substituir a fonte que a formaliza.

---

## Eixo 5 — Isolamento de regras de negócio e frameworks como detalhe (Arquitetura Hexagonal) e integração contínua

**MARTIN (2017)** — colocar detalhes atrás de fronteiras, longe do núcleo:

> "The GUI is a detail. The web is a GUI. So the web is a detail. And, as an architect, you want to put details like that behind boundaries that keep them separate from your core business logic." (MARTIN, 2017, cap. 31).
>
> *Tradução livre:* "A GUI é um detalhe. A web é uma GUI. Logo, a web é um detalhe. E, como arquiteto, você quer colocar detalhes assim atrás de fronteiras que os mantenham separados da lógica de negócio central."

> "The business logic can be thought of as a suite of use cases, each of which performs some function on behalf of a user. Each use case can be described based on the input data, the processing performed, and the output data." (MARTIN, 2017, cap. 31).
>
> *Tradução livre:* "A lógica de negócio pode ser pensada como um conjunto de casos de uso, cada um dos quais executa alguma função em nome de um usuário. Cada caso de uso pode ser descrito com base nos dados de entrada, no processamento realizado e nos dados de saída."

O ponto mais relevante para o protótipo (que usa Quarkus/`@IfBuildProperty`) — **não acoplar o framework ao núcleo**:

> "Treat the framework as a detail that belongs in one of the outer circles of the architecture. Don't let it into the inner circles. […] you should not sprinkle @autowired annotations all throughout your business objects. Your business objects should not know about Spring." (MARTIN, 2017, cap. 32).
>
> *Tradução livre:* "Trate o framework como um detalhe que pertence a um dos círculos externos da arquitetura. Não o deixe entrar nos círculos internos. […] você não deve espalhar anotações @autowired por todos os seus objetos de negócio. Seus objetos de negócio não devem saber da existência do Spring."

E o respaldo à **separação do build em entregáveis independentes** (ressoa com o build metamórfico):

> "I would certainly break the compile and build environment up this way, so that I could build independent deliverables like that. […] I could then independently deploy the components that are most likely to change independently of each other. Keeping these options open will allow us to adapt the way we deploy the system based on how the system changes over time." (MARTIN, 2017, cap. 33).
>
> *Tradução livre:* "Eu certamente separaria o ambiente de compilação e build dessa forma, para poder gerar entregáveis independentes assim. […] Eu poderia então implantar de forma independente os componentes que têm mais probabilidade de mudar independentemente uns dos outros. Manter essas opções em aberto nos permitirá adaptar a forma como implantamos o sistema conforme ele evolui ao longo do tempo."

> "we want to partition the system such that a change to one actor does not affect any of the other actors." (MARTIN, 2017, cap. 33).
>
> *Tradução livre:* "queremos particionar o sistema de modo que uma mudança em um ator não afete nenhum dos outros atores."

> *Glosa:* legitima a arquitetura hexagonal do projeto — regras de negócio isoladas de framework/IO — e, crucialmente, **valida colocar as anotações do Quarkus nos controllers (adaptadores), não no domínio**. A ideia de quebrar o build em entregáveis independentes por ator dialoga com os artefatos interno/externo gerados do mesmo código.

### SHAHIN; ALI BABAR; ZHU (2017) — definições de CI, entrega e implantação contínuas (pipeline de build por artefato)

> Trechos conferidos no PDF `docs/refs/6.3`. A paginação do arXiv não coincide com a do periódico; a localização é dada por seção.

> "Continuous Integration (CI) is a widely established development practice in software development industry, in which members of a team integrate and merge development work (e.g., code) frequently, for example multiple times per day. […] This practice includes automated software building and testing." (SHAHIN; ALI BABAR; ZHU, 2017, seção II-A).
>
> *Tradução livre:* "A Integração Contínua (CI) é uma prática de desenvolvimento amplamente estabelecida na indústria de software, na qual os membros de uma equipe integram e mesclam o trabalho de desenvolvimento (por exemplo, código) com frequência, por exemplo várias vezes por dia. […] Essa prática inclui *build* e testes automatizados do software."

> "Continuous DElivery (CDE) is aimed at ensuring an application is always at production-ready state after successfully passing automated tests and quality checks." (SHAHIN; ALI BABAR; ZHU, 2017, seção II-A).
>
> *Tradução livre:* "A Entrega Contínua (CDE) visa garantir que uma aplicação esteja sempre em estado pronto para produção após passar com sucesso por testes automatizados e verificações de qualidade."

> "Whilst the final deployment in CDE is a manual step, there should be no manual steps in CD, in which as soon as developers commit a change, the change is deployed to production through a deployment pipeline." (SHAHIN; ALI BABAR; ZHU, 2017, seção II-A).
>
> *Tradução livre:* "Enquanto a implantação final na CDE é um passo manual, não deve haver passos manuais na CD, na qual, assim que os desenvolvedores fazem *commit* de uma mudança, ela é implantada em produção por meio de um *pipeline* de implantação."

> "We have also determined a list of critical factors such as 'testing (effort and time)' […] that should be carefully considered when introducing continuous practices in a given organization." (SHAHIN; ALI BABAR; ZHU, 2017, resumo).
>
> *Tradução livre:* "Determinamos também uma lista de fatores críticos, como 'teste (esforço e tempo)' […], que devem ser cuidadosamente considerados ao introduzir práticas contínuas em uma organização."

*Glosa:* as três primeiras passagens dão a definição revisada por pares do vocabulário usado nas seções 6.6 e 6.7 — o *job* de integração contínua e o *pipeline* que gera cada artefato. A quarta sustenta a leitura de custo da PP4 (seção 7.5): se teste e *build* já são fatores críticos com **um** artefato, a abordagem metamórfica, que produz **um artefato por variante** e deveria verificar cada um (seção 7.5.1), multiplica esse custo.

⚠️ **Limite de atribuição.** O artigo não trata de variabilidade em tempo de compilação nem de múltiplos artefatos por código-fonte, e não formula o princípio *build once, deploy many* (HUMBLE; FARLEY, 2010, fora do acervo). A inferência de que o custo se multiplica por variante é do autor, e não da fonte.

---

## Eixo 6 — Medição de reuso (fundamento do cálculo de PP1)

> Trechos conferidos no relatório técnico de autor (`docs/refs/8.1`), versão aberta de FRAKES; TERRY (1996); as páginas indicadas são as do relatório. O método derivado destes trechos está em `METRICA-REUSO.md`.

**FRAKES; TERRY (1996)** — a forma geral da métrica de *quantidade de reuso*:

> "Amount of reuse metrics are used to assess and monitor a reuse improvement effort by tracking percentages of reuse of life cycle objects over time." (FRAKES; TERRY, 1996, p. 15).
>
> *Tradução livre:* "As métricas de quantidade de reuso são usadas para avaliar e monitorar um esforço de melhoria de reuso, acompanhando ao longo do tempo os percentuais de reuso dos objetos do ciclo de vida."

O **reuse percent**, atribuído a Poulin et al. — fórmula adotada como métrica principal da PP1:

> "Reuse percent - reflects how much of the product can be attributed to reuse. […] Product Reuse percent = RSI / (RSI+SSI) x 100 percent" (FRAKES; TERRY, 1996, p. 8).
>
> *Tradução livre:* "Reuse percent — reflete o quanto do produto pode ser atribuído a reuso. […] Reuse percent do produto = RSI / (RSI+SSI) × 100 por cento."

A definição de **componente externo**, critério que classifica o núcleo compartilhado da linha de produto:

> "An external lower level component is used by the higher level component, but was created for a different item or for general use." (FRAKES; TERRY, 1996, p. 16).
>
> *Tradução livre:* "Um componente de nível inferior externo é usado pelo componente de nível superior, mas foi criado para um item diferente ou para uso geral."

E as **reuse level metrics**, com sua escala de interpretação:

> "External Reuse Level: E / L […] Internal Reuse Level: M / L […] Total Reuse Level: External Reuse Level + Internal Reuse Level" e "Internal, external, and total reuse levels will assume values between 0 and 1. More reuse occurs as the reuse level value approaches 1. A reuse level of 0 indicates no reuse." (FRAKES; TERRY, 1996, p. 16).
>
> *Tradução livre:* "Nível de Reuso Externo: E / L […] Nível de Reuso Interno: M / L […] Nível de Reuso Total: Nível de Reuso Externo + Nível de Reuso Interno" e "Os níveis de reuso interno, externo e total assumem valores entre 0 e 1. Há mais reuso à medida que o valor se aproxima de 1. Um nível de reuso 0 indica ausência de reuso."

*Glosa:* fornecem as duas fórmulas empregadas na Avaliação. O `IRL` é o que traduz numericamente a afirmação de "reuso sem duplicação": `M` conta os itens internos **usados mais de uma vez**, categoria à qual pertence o `OrderService`, consumido pelos três *controllers*.

**FRAKES; KANG (2005)** — enquadramento do reuso e da medição:

> "Software reuse is the use of existing software or software knowledge to construct new software." (FRAKES; KANG, 2005, p. 529).
>
> *Tradução livre:* "Reuso de software é o uso de software ou de conhecimento de software existentes para construir novo software."

> "Measurement and experimentation of reuse and domain engineering is one area where much more work is needed." (FRAKES; KANG, 2005, p. 531).
>
> *Tradução livre:* "A medição e a experimentação em reuso e engenharia de domínio constituem uma área em que muito mais trabalho é necessário."

*Glosa:* o segundo trecho sustenta a decisão metodológica de **triangular duas métricas** em vez de reportar um índice único. O artigo também registra que a engenharia de domínio parte da premissa de que sistemas novos são, em geral, **variantes** de sistemas já construídos — leitura que coincide com a derivação de variantes proposta neste trabalho.

**IRSHAD et al. (2016)** — vigência das métricas de reuso e do `RSI` em avaliação industrial:

> "A study by Frakes [8] described different types of reuse metrics such as costs related to software reuse, levels of software reuse, measuring reusability, etc. These measurements provide a foundation for building, maintaining and improving the reuse processes and reusable products." (IRSHAD et al., 2016, p. 1).
>
> *Tradução livre:* "Um estudo de Frakes descreveu diferentes tipos de métricas de reuso, tais como custos relacionados ao reuso de software, níveis de reuso de software, medição de reusabilidade etc. Essas medições fornecem uma fundação para construir, manter e melhorar os processos de reuso e os produtos reusáveis."

E o emprego do `RSI` no modelo de custo evitado de Poulin et al.:

> "According to this model, the direct cost avoided (DCA) can be estimated by the following equation: DCA = RSI × (1 − RCR) × NCC, where RSI includes completely unmodified reused software lines of code, RCR is the relative cost of reuse." (IRSHAD et al., 2016, p. 5).
>
> *Tradução livre:* "Segundo esse modelo, o custo direto evitado (DCA) pode ser estimado pela equação: DCA = RSI × (1 − RCR) × NCC, em que RSI compreende as linhas de código reusadas sem qualquer modificação e RCR é o custo relativo do reuso."

*Glosa:* sustenta que o `RSI` — a grandeza sobre a qual se calcula o *reuse percent* adotado neste trabalho — permanece em uso em revisão sistemática com avaliação industrial, vinte anos depois de sua formulação. Note-se o **limite do apoio**: o artigo aplica o `RSI` a um modelo de **custo evitado**, não à razão `RSI/(RSI+SSI)`. Corrobora-se a vigência da grandeza, não a da fórmula.

**CHEN; USMAN; BADAMPUDI (2024)** — corroboração recente de que o *reuse level* segue em uso na literatura empírica:

> "QN7 reported that the higher the External Reuse Frequency or External Reuse Level, the higher the rating in quality." (CHEN; USMAN; BADAMPUDI, 2024, p. 12).
>
> *Tradução livre:* "O estudo QN7 relatou que, quanto maior a Frequência de Reuso Externo ou o Nível de Reuso Externo, maior a avaliação de qualidade."

E o vínculo entre reuso e **manutenibilidade**, que sustenta a formulação do objetivo geral:

> "The same goes for maintainability, that the overall effort used to fix or isolate the errors is reduced in reusable assets […] but the proportion of errors requiring a longer fix is more in reusable assets." (CHEN; USMAN; BADAMPUDI, 2024, p. 14).
>
> *Tradução livre:* "O mesmo vale para a manutenibilidade: o esforço total empregado para corrigir ou isolar os erros é reduzido nos ativos reusáveis […] embora a proporção de erros que exigem correção mais demorada seja maior neles."

A evidência, contudo, não é unânime:

> "Another case showed a marginally negative relationship between the External Reuse Level (ERL) and NSCL/Effort." (CHEN; USMAN; BADAMPUDI, 2024, p. 16).
>
> *Tradução livre:* "Outro caso mostrou uma relação marginalmente negativa entre o Nível de Reuso Externo (ERL) e a razão linhas de código não comentadas por esforço."

*Glosa:* cumpre duas funções. Mostra que as métricas de FRAKES; TERRY (1996) permanecem instrumento corrente em revisão sistemática de 2024 — o que responde à objeção de que o método de avaliação se apoiaria em literatura de três décadas. E vincula reuso a **manutenibilidade** em casos industriais, dando lastro ao objetivo geral do trabalho. O terceiro trecho é citado deliberadamente: os achados da revisão são **mistos**, e reportá-los apenas na direção favorável seria seleção enviesada de evidência.

---

### DEVANBU; KARSTU; MELO; THOMAS (1996) — granularidade de função e reuso medido por chamadas

> "c_j = A software component (module, class, **function**, subsystem). With a superscript `e`, `c^e` refers to an external component, which existed independently of the system in which it is being used." (DEVANBU et al., 1996, p. 3).
>
> *Tradução livre:* "c_j = Um componente de software (módulo, classe, **função**, subsistema). Com o sobrescrito `e`, `c^e` refere-se a um componente externo, que existia independentemente do sistema em que é usado."

> "we are concerned with the development of an indirect internal measurement of R_b, the reuse benefit of a system S, from the product, by searching the source code of S for instances of language-based reuse such as **subroutine calls**." (DEVANBU et al., 1996, p. 3).
>
> *Tradução livre:* "ocupamo-nos do desenvolvimento de uma medida interna indireta de R_b, o benefício de reuso de um sistema S, a partir do produto, buscando no código-fonte de S instâncias de reuso baseado em linguagem, tais como **chamadas de sub-rotina**."

> "Cu(S_1, c_1) = The number of times component c_1 is used in a system S_1." (DEVANBU et al., 1996, p. 3).
>
> *Tradução livre:* "Cu(S_1, c_1) = O número de vezes que o componente c_1 é usado em um sistema S_1."

*Glosa:* autoriza duas decisões do **Reuse per Method** (ver `METRICA-REUSO.md`, seção 7): contar em granularidade de **função/método**, e ler reuso a partir das **chamadas** presentes no código. Nenhuma das duas é invenção deste trabalho.

> "It is certainly possible to write the same program in ways that can artificially inflate internal measures of reuse. Thus, one can artificially insert function invocations into the different branches of a conditional […] In most cases, it is infeasible to detect occurrences of spurious inflation." (DEVANBU et al., 1996, p. 6).
>
> *Tradução livre:* "É certamente possível escrever o mesmo programa de maneiras que inflem artificialmente as medidas internas de reuso. Assim, pode-se inserir artificialmente invocações de função nos diferentes ramos de uma condicional […] Na maioria dos casos, é inviável detectar ocorrências de inflação espúria."

*Glosa:* citada **contra** o próprio instrumento, e não a favor. A contagem de métodos alcançáveis é sensível à granularidade com que o código foi decomposto em funções; a advertência entra nas **ameaças à validade** do capítulo de Avaliação. Reportar a ressalva da fonte é condição para usá-la.

---

### PONTA; PLATE; SABETTA (2018) — alcançabilidade de métodos a partir dos pontos de entrada

> "We define a *program construct* (or simply *construct*) as a structural element of the source code characterized by a *type* (e.g., package, class, constructor, **method**), a *language* […] and a *unique identifier*." (PONTA; PLATE; SABETTA, 2018, seção II-A).
>
> *Tradução livre:* "Definimos um *construto de programa* (ou simplesmente *construto*) como um elemento estrutural do código-fonte caracterizado por um *tipo* (por exemplo, pacote, classe, construtor, **método**), uma *linguagem* […] e um *identificador único*."

> "R_ai represents the set of all constructs, either part of application a or its bundled library i, that are found reachable starting from the application a and thus can be potentially executed. Static analysis is performed by using a static analyzer to compute a **graph of all library constructs reachable from the application constructs**." (PONTA; PLATE; SABETTA, 2018, seção II-D).
>
> *Tradução livre:* "R_ai representa o conjunto de todos os construtos, sejam da aplicação a ou de sua biblioteca embarcada i, que são encontrados alcançáveis a partir da aplicação a e que, portanto, podem ser potencialmente executados. A análise estática é realizada usando um analisador estático para computar um **grafo de todos os construtos de biblioteca alcançáveis a partir dos construtos da aplicação**."

> "The reachable body stability […] considers the reachable share of a library, i.e., the set of dynamically and/or statically reachable library constructs. Given the total number of constructs of S_i reachable from the application, it measures the **ratio** of those that are contained as-is […] also in S_j." (PONTA; PLATE; SABETTA, 2018, seção III).
>
> *Tradução livre:* "A estabilidade do corpo alcançável […] considera a parcela alcançável de uma biblioteca, isto é, o conjunto de construtos de biblioteca alcançáveis dinâmica e/ou estaticamente. Dado o número total de construtos de S_i alcançáveis a partir da aplicação, ela mede a **razão** daqueles que estão contidos tal como estão […] também em S_j."

*Glosa:* é o precedente revisado por pares da **técnica** do Reuse per Method — contagem em granularidade de método, alcançabilidade computada por **grafo de chamadas a partir dos pontos de entrada**, e **razões definidas sobre o conjunto alcançável**. O artigo enquadra o problema como consequência do **reuso** de bibliotecas e liga alcançabilidade a **explorabilidade**, que é o vínculo entre PP1 e PP3 neste trabalho.

⚠️ **Limite de atribuição.** Os autores medem estabilidade de bibliotecas entre versões, **não** reuso entre variantes de um mesmo código-fonte; e o texto verificado fala em *exploitability*, **não** em "superfície de ataque" — termo que permanece ancorado em MANADHATA; WING (2011). Não atribuir à fonte mais do que ela afirma.

---

## Eixo 7 — Design Science Research (fundamento do método)

> Trechos conferidos nos PDFs `docs/refs/7.2`, `7.3` e `7.6`. As páginas são as do periódico.

**PEFFERS et al. (2007)** — as seis atividades adotadas na seção *Etapas da Pesquisa*:

> "The DS process includes six steps: problem identification and motivation, definition of the objectives for a solution, design and development, demonstration, evaluation, and communication." (PEFFERS et al., 2007, p. 46).
>
> *Tradução livre:* "O processo de *design science* compreende seis etapas: identificação do problema e motivação, definição dos objetivos de uma solução, projeto e desenvolvimento, demonstração, avaliação e comunicação."

> "The result of our synthesis is a process model consisting of six activities in a nominal sequence." (PEFFERS et al., 2007, p. 52).
>
> *Tradução livre:* "O resultado de nossa síntese é um modelo de processo composto de seis atividades em uma sequência nominal."

**BASKERVILLE et al. (2018)** — enquadramento da contribuição e corroboração do processo:

> "it is crucial to engage in the ongoing debate on what constitutes an acceptable contribution for publishing DSR — the design artifact, the design theory, or both." (BASKERVILLE et al., 2018, p. 358).
>
> *Tradução livre:* "é crucial participar do debate em curso sobre o que constitui uma contribuição aceitável para publicar DSR — o artefato de projeto, a teoria de projeto, ou ambos."

> "Peffers, Tuunanen, Rothenberger, & Chatterjee (2008) propose a method and a process outline for performing DSR." (BASKERVILLE et al., 2018, p. 364).
>
> *Tradução livre:* "Peffers, Tuunanen, Rothenberger e Chatterjee propõem um método e um esboço de processo para conduzir DSR."

*Glosa:* o primeiro trecho ampara posicionar a contribuição deste trabalho no **artefato**, sem reivindicar teoria de projeto. O segundo cumpre a **redundância de fontes**: um texto de 2018 confirma que o modelo de processo de Peffers et al. segue sendo referência corrente. Observe-se que Baskerville et al. datam a obra como **2008**, enquanto este trabalho adota **2007** — o volume do JMIS é de inverno de 2007–08.

**ENGSTRÖM et al. (2020)** — adequação da DSR ao campo da Engenharia de Software:

> "Similar to other design sciences, much software engineering research aims to design solutions to practical problems in a real-world context." (ENGSTRÖM et al., 2020, p. 2630).
>
> *Tradução livre:* "À semelhança de outras ciências do projeto, boa parte da pesquisa em engenharia de software visa projetar soluções para problemas práticos em contexto real."

> "Design science, although suggested for some time as a useful research paradigm for software engineering research, is not commonly used as a way to frame software engineering research." (ENGSTRÖM et al., 2020, p. 2652).
>
> *Tradução livre:* "A *design science*, embora há tempos sugerida como paradigma de pesquisa útil para a engenharia de software, não é comumente usada para enquadrar a pesquisa da área."

*Glosa:* a DSR nasce em Sistemas de Informação, e este trabalho é de Engenharia de Software. Engström et al. (2020) sustentam a transposição do paradigma para o campo — o primeiro trecho pela natureza da pesquisa em ES, o segundo ao registrar que o enquadramento ainda é pouco praticado, o que situa a escolha metodológica deste trabalho como deliberada, e não corrente. **Limite:** a fonte não descreve as seis atividades; essa corroboração cabe a BASKERVILLE et al. (2018).

---

## Eixo 8 — Documentação oficial das tecnologias (comportamento do mecanismo)

> Conferido nas páginas oficiais do Quarkus em **9 jul. 2026**, conforme a **regra obrigatória** de `FONTES-ACADEMICAS.md`, que exige registrar o **link completo**, a **seção consultada**, a **versão** e a **data de acesso** de toda documentação citada. Documentação é fonte viva: muda sem aviso e sem trocar de endereço.
>
> **Links das fontes deste eixo** (todos referentes à linha 3.x, a mesma da versão 3.37.0 empregada no protótipo):
>
> | Chamada | Guia | Seção conferida | Link |
> |---|---|---|---|
> | QUARKUS (2026a) | *Quarkus ArC: CDI reference guide* | 5.8, *Enabling Beans for Quarkus Build Properties* | https://quarkus.io/guides/cdi-reference |
> | QUARKUS (2026b) | *Writing your own extension* | 2.6, *Generating Bytecode* | https://quarkus.io/guides/writing-extensions |
> | QUARKUS (2026c) | *Using Kotlin* | *Important Maven configuration points* | https://quarkus.io/guides/kotlin |
> | QUARKUS (2026d) | *Building applications with Gradle* | *Using fast-jar* | https://quarkus.io/guides/gradle-tooling |
>
> ⚠️ **Nuance de 2026c.** O trecho sobre classes `final` e o plugin `all-open` está sob uma seção de configuração **Maven** do guia de Kotlin. Isso não invalida a citação: o `all-open` é plugin do **compilador Kotlin** e independe do sistema de *build*. O protótipo o configura no `build.gradle.kts`, com as mesmas anotações que a documentação indica. Registrar a seção tal como ela é, e não como conviria que fosse.

**QUARKUS (2026a)** — o que a `@IfBuildProperty` faz:

> "conditionally enable a bean when a Quarkus build time property has or does not have a specific value, via the `@io.quarkus.arc.properties.IfBuildProperty` and `@io.quarkus.arc.properties.UnlessBuildProperty` annotations." (QUARKUS, 2026a).
>
> *Tradução livre:* "habilitar condicionalmente um *bean* quando uma propriedade de tempo de compilação do Quarkus tem, ou não tem, um valor específico, por meio das anotações `@IfBuildProperty` e `@UnlessBuildProperty`."

E a afirmação decisiva para a tese — a irreversibilidade em tempo de execução:

> "Properties set at runtime have absolutely no effect on the bean resolution using `@IfBuildProperty` / `@UnlessBuildProperty`." (QUARKUS, 2026a).
>
> *Tradução livre:* "Propriedades definidas em tempo de execução não têm absolutamente nenhum efeito sobre a resolução do *bean* quando se usa `@IfBuildProperty` / `@UnlessBuildProperty`."

*Glosa:* é a fonte primária do comportamento do mecanismo. O segundo trecho sustenta, na documentação do próprio *framework*, a afirmação de que nenhuma configuração de *deploy* reativa um ponto de entrada não registrado no *build* — o que distingue a metamorfose da *feature flag* em runtime.

**QUARKUS (2026b)** — a fase de *augmentation*, onde a variabilidade é resolvida:

> "The output of these build steps is some recorded bytecode, using a bytecode generation library called Gizmo, that is used to actually bootstrap the application at runtime." (QUARKUS, 2026b).
>
> *Tradução livre:* "A saída dessas etapas de *build* é um *bytecode* gravado, por meio de uma biblioteca de geração de *bytecode* chamada Gizmo, que é usado para efetivamente inicializar a aplicação em tempo de execução."

*Glosa:* explica **por que** o efeito da anotação é observável num artefato distinto do JAR da aplicação: o *bean* e o invocador são *bytecode gerado* na *augmentation*, e não saída do compilador Kotlin.

**QUARKUS (2026c)** — classes `final` e o plugin `all-open`:

> "Having `final` classes however does not work well with various frameworks that need to create Dynamic Proxies. Thus, the `all-open` Kotlin compiler plugin allows us to configure the compiler to **not** mark as `final` classes that have certain annotations." (QUARKUS, 2026c).
>
> *Tradução livre:* "Ter classes `final`, contudo, não funciona bem com diversos *frameworks* que precisam criar *dynamic proxies*. Assim, o plugin de compilação Kotlin `all-open` permite configurar o compilador para **não** marcar como `final` as classes que tenham certas anotações."

*Glosa:* fundamenta a seção 6.1. As anotações configuradas no protótipo (`@Path`, `@ApplicationScoped`, `@Entity`) são exatamente as indicadas pela documentação.

**QUARKUS (2026d)** — o empacotamento `fast-jar` e seu diretório de saída:

> "`fast-jar` is now the default quarkus package type. The result of `./gradlew build` command is a new directory under `build` named `quarkus-app`." e "In order to successfully run the produced jar, you need to have the entire contents of the `quarkus-app` directory. If any of the files are missing, the application will not start or might not function correctly." (QUARKUS, 2026d).
>
> *Tradução livre:* "`fast-jar` é agora o tipo de empacotamento padrão do Quarkus. O resultado do comando `./gradlew build` é um novo diretório sob `build`, chamado `quarkus-app`." e "Para executar com sucesso o *jar* produzido, é necessário ter todo o conteúdo do diretório `quarkus-app`. Se qualquer um dos arquivos estiver faltando, a aplicação não iniciará ou poderá não funcionar corretamente."

*Glosa:* sustenta a caracterização do artefato na seção 6.8. Adota-se o guia de **Gradle**, e não o de Maven, por ser o sistema de *build* do protótipo — exigência do passo 8 do procedimento de `FONTES-ACADEMICAS.md`.

> ⚠️ **Limite da documentação (passo 7 do procedimento).** A **estrutura interna** do diretório `quarkus-app` — os subdiretórios `app/` e `quarkus/` e os arquivos `generated-bytecode.jar` e `transformed-bytecode.jar` — **não é descrita** em nenhuma das páginas oficiais consultadas: `gradle-tooling` e `maven-tooling` apenas registram que o resultado do *build* é o diretório `quarkus-app`, e `class-loading-reference` (https://quarkus.io/guides/class-loading-reference, acesso em 9 jul. 2026) trata dos carregadores de classe, não do layout físico. As afirmações do trabalho sobre esses arquivos são, portanto, **evidência empírica por inspeção**, e o TCC as apresenta como tais na seção 6.8, com os comandos que as reproduzem.

---

### QUARKUS (2026e) — geração do contrato OpenAPI e do Swagger UI

> "Quarkus provides the SmallRye OpenAPI extension compliant with the MicroProfile OpenAPI specification in order to generate your API OpenAPI v3 specification." (QUARKUS, 2026e, seção *Expose OpenAPI Specifications*).
>
> *Tradução livre:* "O Quarkus fornece a extensão SmallRye OpenAPI, em conformidade com a especificação MicroProfile OpenAPI, a fim de gerar a especificação OpenAPI v3 da sua API."

> "Swagger UI is a great tool permitting to visualize and interact with your APIs. The UI is automatically generated from your OpenAPI specification." (QUARKUS, 2026e, seção *Use Swagger UI for development*).
>
> *Tradução livre:* "O Swagger UI é uma excelente ferramenta que permite visualizar suas APIs e interagir com elas. A interface é gerada automaticamente a partir da sua especificação OpenAPI."

> "If you want to make it available in production too, you can include the following configuration in your `application.properties`: `quarkus.swagger-ui.always-include=true`. This is a build time property, it cannot be changed at runtime." (QUARKUS, 2026e, seção *Use Swagger UI for development*).
>
> *Tradução livre:* "Se você quiser torná-lo disponível também em produção, pode incluir a seguinte configuração no seu `application.properties`: `quarkus.swagger-ui.always-include=true`. Esta é uma propriedade de tempo de compilação; não pode ser alterada em tempo de execução."

*Glosa:* sustenta a seção 7.2.4 da Avaliação. As duas primeiras passagens estabelecem o encadeamento — a especificação é gerada a partir da API, e a interface é gerada a partir da especificação —, do qual decorre que documento e interface são **artefatos derivados da mesma variabilidade** que produz o executável, e não documentação mantida à parte. A terceira sustenta a nota de escopo de segurança da seção 7.8: o protótipo publica o contrato em produção, por escolha deliberada de projeto-referência, e essa exposição é ela própria decidida em tempo de compilação.

⚠️ **Limite de atribuição.** A documentação **não** afirma que a especificação é gerada a partir dos *endpoints efetivamente registrados* — o que a distinguiria de uma geração a partir do código-fonte. Essa é **evidência empírica** deste trabalho, obtida por inspeção do documento em cada variante (seção 7.2.3), e como tal é apresentada. A documentação tampouco emite advertência de segurança sobre expor o Swagger UI em produção; a observação da seção 7.8 é do autor, e não da fonte.

---

## Mapa síntese: argumento do TCC × citações

| Argumento do TCC | Citações de apoio |
|---|---|
| Microsserviços descentralizados exigem agregação | PAHL; JAMSHIDI (2016, p. 137–140); TAIBI; LENARDUZZI; PAHL (2018, p. 221) |
| API Gateway/BFF: API sob medida por frontend | TAIBI; LENARDUZZI; PAHL (2018, p. 223–224); PAHL; JAMSHIDI (2016, p. 140); NEWMAN (2015) |
| Duplicação/custo com múltiplos BFFs; valor do reuso | TAIBI; LENARDUZZI; PAHL (2018, p. 224, 227) |
| Metamorfose = variabilidade/derivação de produtos (SPL) | HORCAS; PINTO; FUENTES (2023, p. 377–381) |
| `@IfBuildProperty` = variabilidade por anotação em build-time | HORCAS; PINTO; FUENTES (2023, p. 381); HUNSEN et al. (2016, p. 1, 6); BAUM et al. (2019, p. 1) |
| Segurança por ausência = reduzir superfície de ataque | MANADHATA; WING (2005, p. 1–5); HANNOUSSE; YAHIOUCHE (2020, p. 1–2) |
| Separação interno/externo (método/ameaça) | MANADHATA; WING (2005, p. 5); HANNOUSSE; YAHIOUCHE (2020, p. 2) |
| Hexagonal: isolar negócio; framework é detalhe | MARTIN (2017, cap. 31–33) |
| Definições de CI, entrega e implantação contínuas; custo de *build*/teste (PP4) | SHAHIN; ALI BABAR; ZHU (2017, seção II-A, resumo) |
| Medição de reuso (PP1): *reuse percent* e *reuse level* | FRAKES; TERRY (1996, p. 8, 15–16); FRAKES; KANG (2005, p. 529, 531) |
| Comportamento do `@IfBuildProperty`; irreversibilidade em runtime | QUARKUS (2026a) |
| Fase de *augmentation*; bytecode gerado no *build* | QUARKUS (2026b) |
| Classes `final` em Kotlin e o plugin `all-open` | QUARKUS (2026c) |
| DSR: seis atividades do processo de pesquisa | PEFFERS et al. (2007, p. 46, 52); BASKERVILLE et al. (2018, p. 364) |
| DSR: contribuição no artefato; adequação à Engenharia de Software | BASKERVILLE et al. (2018, p. 358); ENGSTRÖM et al. (2020, p. 2630, 2652) |
| Vigência do `RSI` e das métricas de reuso em avaliação industrial | IRSHAD et al. (2016, p. 1, 5) |
| Atualidade das métricas de reuso e vínculo reuso ↔ manutenibilidade | CHEN; USMAN; BADAMPUDI (2024, p. 12, 14, 16) |

---

## Referências (ABNT NBR 6023)

BASKERVILLE, Richard; BAIYERE, Abayomi; GREGOR, Shirley; HEVNER, Alan; ROSSI, Matti. Design science research contributions: finding a balance between artifact and theory. **Journal of the Association for Information Systems**, [*s. l.*], v. 19, n. 5, p. 358-376, 2018. DOI: 10.17705/1jais.00495.

BAUM, David; SIXTUS, Christina; VOGELSBERG, Lisa; EISENECKER, Ulrich. Understanding conditional compilation through integrated representation of variability and source code. In: INTERNATIONAL SYSTEMS AND SOFTWARE PRODUCT LINE CONFERENCE, 23., 2019, Paris. **Proceedings** [...]. New York: ACM, 2019. p. 1-4. DOI: 10.1145/3307630.3342387.

CHEN, Xingru; USMAN, Muhammad; BADAMPUDI, Deepika. Understanding and evaluating software reuse costs and benefits from industrial cases: a systematic literature review. **Information and Software Technology**, [*s. l.*], v. 171, art. 107451, 2024. DOI: 10.1016/j.infsof.2024.107451.

ENGSTRÖM, Emelie; STOREY, Margaret-Anne; RUNESON, Per; HÖST, Martin; BALDASSARRE, Maria Teresa. How software engineering research aligns with design science: a review. **Empirical Software Engineering**, [*s. l.*], v. 25, p. 2630-2660, 2020. DOI: 10.1007/s10664-020-09818-7.

FRAKES, William B.; KANG, Kyo. Software reuse research: status and future. **IEEE Transactions on Software Engineering**, [*s. l.*], v. 31, n. 7, p. 529-536, 2005. DOI: 10.1109/TSE.2005.85.

FRAKES, William; TERRY, Carol. Software reuse: metrics and models. **ACM Computing Surveys**, [*s. l.*], v. 28, n. 2, p. 415-435, 1996. DOI: 10.1145/234528.234531. Versão de autor: **Software reuse and reusability metrics and models**. Blacksburg: Department of Computer Science, Virginia Tech, 1995. (Technical Report TR-95-07).

HANNOUSSE, Abdelhakim; YAHIOUCHE, Salima. Securing microservices and microservice architectures: a systematic mapping study. **Computer Science Review**, [*s. l.*], v. 41, art. 100415, 2021. DOI: 10.1016/j.cosrev.2021.100415. Preprint disponível em: arXiv:2003.07262, 2020.

HORCAS, José Miguel; PINTO, Mónica; FUENTES, Lidia. Empirical analysis of the tool support for software product lines. **Software and Systems Modeling**, [*s. l.*], v. 22, p. 377-414, 2023. DOI: 10.1007/s10270-022-01011-2.

HUNSEN, Claus; ZHANG, Bo; SIEGMUND, Janet; KÄSTNER, Christian; LESSENICH, Olaf; BECKER, Martin; APEL, Sven. Preprocessor-based variability in open-source and industrial software systems: an empirical study. **Empirical Software Engineering**, [*s. l.*], v. 21, n. 2, p. 449-482, 2016. DOI: 10.1007/s10664-015-9360-1.

IRSHAD, Mohsin; PETERSEN, Kai; TORKAR, Richard; AFZAL, Wasif. Capturing cost avoidance through reuse: systematic literature review and industrial evaluation. In: INTERNATIONAL CONFERENCE ON EVALUATION AND ASSESSMENT IN SOFTWARE ENGINEERING, 20., 2016, Limerick. **Proceedings** [...]. New York: ACM, 2016. p. 1-12. DOI: 10.1145/2915970.2915989.

MANADHATA, Pratyusa K.; WING, Jeannette M. **An attack surface metric**. Pittsburgh: School of Computer Science, Carnegie Mellon University, 2005. (Technical Report CMU-CS-05-155). Versão publicada: MANADHATA, Pratyusa K.; WING, Jeannette M. An attack surface metric. **IEEE Transactions on Software Engineering**, [*s. l.*], v. 37, n. 3, p. 371-386, 2011. DOI: 10.1109/TSE.2010.60.

MARTIN, Robert C. **Clean architecture**: a craftsman's guide to software structure and design. Boston: Prentice Hall, 2017.

NEWMAN, Sam. **Backends For Frontends**. [*S. l.*], 18 nov. 2015. Disponível em: https://samnewman.io/patterns/architectural/bff/. Acesso em: 9 jul. 2026.

PAHL, Claus; JAMSHIDI, Pooyan. Microservices: a systematic mapping study. In: INTERNATIONAL CONFERENCE ON CLOUD COMPUTING AND SERVICES SCIENCE, 6., 2016. **Proceedings** [...]. Setúbal: SciTePress, 2016. v. 1, p. 137-146. DOI: 10.5220/0005785501370146.

PEFFERS, Ken; TUUNANEN, Tuure; ROTHENBERGER, Marcus A.; CHATTERJEE, Samir. A design science research methodology for information systems research. **Journal of Management Information Systems**, [*s. l.*], v. 24, n. 3, p. 45-77, 2007. DOI: 10.2753/MIS0742-1222240302.

QUARKUS. **Quarkus ArC: CDI reference guide**. Seção 5.8, *Enabling Beans for Quarkus Build Properties*. [*S. l.*]: Red Hat, 2026a. Documentação oficial, versão 3.x. Disponível em: https://quarkus.io/guides/cdi-reference. Acesso em: 9 jul. 2026.

QUARKUS. **Writing your own extension**. Seção 2.6, *Generating Bytecode*. [*S. l.*]: Red Hat, 2026b. Documentação oficial, versão 3.x. Disponível em: https://quarkus.io/guides/writing-extensions. Acesso em: 9 jul. 2026.

QUARKUS. **Using Kotlin**. Seção *Important Maven configuration points*. [*S. l.*]: Red Hat, 2026c. Documentação oficial, versão 3.x. Disponível em: https://quarkus.io/guides/kotlin. Acesso em: 9 jul. 2026.

QUARKUS. **Building applications with Gradle**. Seção *Using fast-jar*. [*S. l.*]: Red Hat, 2026d. Documentação oficial, versão 3.x. Guia de Gradle — o sistema de *build* do protótipo —, e não o de Maven. Disponível em: https://quarkus.io/guides/gradle-tooling. Acesso em: 9 jul. 2026.

SHAHIN, Mojtaba; ALI BABAR, Muhammad; ZHU, Liming. Continuous integration, delivery and deployment: a systematic review on approaches, tools, challenges and practices. **IEEE Access**, [*s. l.*], v. 5, p. 3909-3943, 2017. DOI: 10.1109/ACCESS.2017.2685629. Versão de autor disponível em: arXiv:1703.07019.

TAIBI, Davide; LENARDUZZI, Valentina; PAHL, Claus. Architectural patterns for microservices: a systematic mapping study. In: INTERNATIONAL CONFERENCE ON CLOUD COMPUTING AND SERVICES SCIENCE, 8., 2018. **Proceedings** [...]. Setúbal: SciTePress, 2018. p. 221-232. DOI: 10.5220/0006798302210232.
