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

---

## Eixo 5 — Isolamento de regras de negócio e frameworks como detalhe (Arquitetura Hexagonal)

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

---

## Mapa síntese: argumento do TCC × citações

| Argumento do TCC | Citações de apoio |
|---|---|
| Microsserviços descentralizados exigem agregação | PAHL; JAMSHIDI (2016, p. 137–140); TAIBI; LENARDUZZI; PAHL (2018, p. 221) |
| API Gateway/BFF: API sob medida por frontend | TAIBI; LENARDUZZI; PAHL (2018, p. 223–224); PAHL; JAMSHIDI (2016, p. 140) |
| Duplicação/custo com múltiplos BFFs; valor do reuso | TAIBI; LENARDUZZI; PAHL (2018, p. 224, 227) |
| Metamorfose = variabilidade/derivação de produtos (SPL) | HORCAS; PINTO; FUENTES (2023, p. 377–381) |
| `@IfBuildProperty` = variabilidade por anotação em build-time | HORCAS; PINTO; FUENTES (2023, p. 381); HUNSEN et al. (2016, p. 1, 6); BAUM et al. (2019, p. 1) |
| Segurança por ausência = reduzir superfície de ataque | MANADHATA; WING (2005, p. 1–5); HANNOUSSE; YAHIOUCHE (2020, p. 1–2) |
| Separação interno/externo (método/ameaça) | MANADHATA; WING (2005, p. 5); HANNOUSSE; YAHIOUCHE (2020, p. 2) |
| Hexagonal: isolar negócio; framework é detalhe | MARTIN (2017, cap. 31–33) |

---

## Referências (ABNT NBR 6023)

BAUM, David; SIXTUS, Christina; VOGELSBERG, Lisa; EISENECKER, Ulrich. Understanding conditional compilation through integrated representation of variability and source code. In: INTERNATIONAL SYSTEMS AND SOFTWARE PRODUCT LINE CONFERENCE, 23., 2019, Paris. **Proceedings** [...]. New York: ACM, 2019. p. 1-4. DOI: 10.1145/3307630.3342387.

HANNOUSSE, Abdelhakim; YAHIOUCHE, Salima. Securing microservices and microservice architectures: a systematic mapping study. **Computer Science Review**, [*s. l.*], v. 41, art. 100415, 2021. DOI: 10.1016/j.cosrev.2021.100415. Preprint disponível em: arXiv:2003.07262, 2020.

HORCAS, José Miguel; PINTO, Mónica; FUENTES, Lidia. Empirical analysis of the tool support for software product lines. **Software and Systems Modeling**, [*s. l.*], v. 22, p. 377-414, 2023. DOI: 10.1007/s10270-022-01011-2.

HUNSEN, Claus; ZHANG, Bo; SIEGMUND, Janet; KÄSTNER, Christian; LESSENICH, Olaf; BECKER, Martin; APEL, Sven. Preprocessor-based variability in open-source and industrial software systems: an empirical study. **Empirical Software Engineering**, [*s. l.*], v. 21, n. 2, p. 449-482, 2016. DOI: 10.1007/s10664-015-9360-1.

MANADHATA, Pratyusa K.; WING, Jeannette M. **An attack surface metric**. Pittsburgh: School of Computer Science, Carnegie Mellon University, 2005. (Technical Report CMU-CS-05-155). Versão publicada: MANADHATA, Pratyusa K.; WING, Jeannette M. An attack surface metric. **IEEE Transactions on Software Engineering**, [*s. l.*], v. 37, n. 3, p. 371-386, 2011. DOI: 10.1109/TSE.2010.60.

MARTIN, Robert C. **Clean architecture**: a craftsman's guide to software structure and design. Boston: Prentice Hall, 2017.

PAHL, Claus; JAMSHIDI, Pooyan. Microservices: a systematic mapping study. In: INTERNATIONAL CONFERENCE ON CLOUD COMPUTING AND SERVICES SCIENCE, 6., 2016. **Proceedings** [...]. Setúbal: SciTePress, 2016. v. 1, p. 137-146. DOI: 10.5220/0005785501370146.

TAIBI, Davide; LENARDUZZI, Valentina; PAHL, Claus. Architectural patterns for microservices: a systematic mapping study. In: INTERNATIONAL CONFERENCE ON CLOUD COMPUTING AND SERVICES SCIENCE, 8., 2018. **Proceedings** [...]. Setúbal: SciTePress, 2018. p. 221-232. DOI: 10.5220/0006798302210232.
