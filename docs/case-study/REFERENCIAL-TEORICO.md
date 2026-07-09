# Referencial Teórico

> 🟡 **Rascunho** — documento de trabalho, no mesmo estágio da Metodologia (`METODOLOGIA.md`). Estrutura e conteúdo sujeitos a revisão antes da consolidação no TCC final.
>
> **Situação no TCC:** capítulo que promove e expande a *Fundamentação teórica* hoje embutida na seção *Proposta: Arquitetura Metamórfica* da Proposta, consolidando em um capítulo próprio os conceitos que sustentam o trabalho. Não duplica a Proposta: aprofunda cada conceito com base nas referências reunidas em `REFERENCIAS_CIENTIFICAS.md` e nas citações de `CITACOES_ABNT.md`.
>
> **Convenção:** citações no sistema autor-data (ABNT NBR 10520). Para *An Attack Surface Metric* usa-se a **versão publicada** (MANADHATA; WING, 2011); para o mapeamento de segurança em microsserviços, a versão publicada (HANNOUSSE; YAHIOUCHE, 2021).

Este capítulo reúne os fundamentos teóricos que dão lastro à Arquitetura Metamórfica. Parte do contexto que motiva o problema — a decomposição em microsserviços e a necessidade de agregação de dados para o *frontend* (seções 2.1 e 2.2) — e avança para o núcleo conceitual da proposta: as Linhas de Produto de Software e a variabilidade resolvida em tempo de compilação (seções 2.3 e 2.4), a segurança pela redução da superfície de ataque (seção 2.5) e a organização do código que torna o reuso viável, a Arquitetura Hexagonal (seção 2.6).

---

## 2.1 Microsserviços e o controle descentralizado de dados

O estilo arquitetural de microsserviços organiza um sistema como um conjunto de serviços pequenos e autônomos, resultado de uma decomposição funcional frequentemente conduzida em um contexto de *domain-driven design*. Os serviços são, ainda, implantáveis de forma independente, o que permite ciclos de entrega frequentes e desacoplados (PAHL; JAMSHIDI, 2016).

Essa autonomia traz ganhos reconhecidos de manutenibilidade e resiliência: por serem pequenos e independentes, os microsserviços são mais fáceis de manter e mais tolerantes a falhas, já que a falha de um serviço não derruba o sistema inteiro, como ocorreria em um monólito (TAIBI; LENARDUZZI; PAHL, 2018). Entre os princípios do estilo está o *controle descentralizado de dados* (PAHL; JAMSHIDI, 2016) — cada serviço governa seus próprios dados. É justamente essa descentralização que cria a necessidade de uma camada capaz de **agregar** informações de múltiplos serviços para entregá-las ao *frontend*, motivando os padrões discutidos a seguir.

---

## 2.2 API Gateway e Backend for Frontend (BFF)

A composição de aplicações de usuário final a partir de múltiplos microsserviços exige um mecanismo de agregação do lado do servidor; na literatura de padrões arquiteturais, o **API Gateway** desponta como a abordagem mais recomendada (TAIBI; LENARDUZZI; PAHL, 2018). O API Gateway atua como ponto de entrada do sistema: roteia requisições para os microsserviços apropriados, invoca vários deles e agrega resultados, fornecendo uma API sob medida a cada cliente (TAIBI; LENARDUZZI; PAHL, 2018). O padrão dialoga com o *Aggregator Microservice* descrito por Pahl e Jamshidi (2016) — um serviço que invoca outros para recuperar e processar dados.

O **Backend for Frontend (BFF)** é a especialização dessa ideia por tipo de cliente: uma API personalizada para cada *frontend*, de modo que cada um veja apenas os recursos de que precisa, simplificando as aplicações de usuário final sem a complexidade de expor e interpretar informação desnecessária (TAIBI; LENARDUZZI; PAHL, 2018). Trata-se de um padrão originado na prática da indústria, documentado por Sam Newman, que o caracteriza como um *edge service* de propósito único, dedicado a uma experiência/cliente específico, em contraste com backends de propósito geral (NEWMAN, 2015) — levando ao limite a ideia de API sob medida.

Contudo, a especialização por cliente tem um custo reconhecido. Como cada cliente pode ter uma API própria, é preciso rastrear os casos em que diferentes clientes usam a mesma API e alterá-las em conjunto a cada mudança — o reuso de API "deve ser considerado com cuidado" (TAIBI; LENARDUZZI; PAHL, 2018). Em contrapartida, os mesmos autores registram o valor do reuso quando um único componente é compartilhado entre projetos: a manutenção reflete-se em todos os projetos conectados, reduzindo o esforço ao concentrar cada alteração em um só ponto (TAIBI; LENARDUZZI; PAHL, 2018). Esse é o ponto de tensão que o trabalho ataca: obter o **ganho de reuso sem abrir mão da especialização por cliente**.

---

## 2.3 Linhas de Produto de Software e derivação de produtos

A estratégia de derivar múltiplas aplicações de uma base de código comum insere-se no campo das **Linhas de Produto de Software** (*Software Product Lines* — SPL). Um número crescente de domínios adota abordagens de SPL para lidar com a alta variabilidade que apresentam (HORCAS; PINTO; FUENTES, 2023). Nesse campo, uma *feature* é a unidade de funcionalidade que estrutura a variabilidade de um sistema, e é a seleção de *features* — expressa em uma configuração do modelo de *features* — que distingue um produto de outro (HORCAS; PINTO; FUENTES, 2023).

O momento em que essa variabilidade é resolvida é a **fase de derivação do produto** (*product derivation*): gera-se o produto final resolvendo a variabilidade de cada ponto de variação nos artefatos da SPL, segundo a configuração selecionada no modelo de *features* (HORCAS; PINTO; FUENTES, 2023). É exatamente esse o enquadramento da Arquitetura Metamórfica: cada artefato gerado — um voltado ao cliente interno, outro ao externo — corresponde a um **produto derivado** do mesmo código-fonte pela seleção de *features* no *build*.

### Variabilidade baseada em composição × baseada em anotação

A literatura distingue duas grandes famílias de implementação de variabilidade: as abordagens **baseadas em composição** (composição de componentes e serviços, padrões de projeto, orientação a *features*, orientação a aspectos etc.) e as **baseadas em anotação** (parâmetros de configuração, pré-processadores, separação virtual de interesses), além de combinações das duas (HORCAS; PINTO; FUENTES, 2023). A proposta deste trabalho pertence à **segunda família**: a anotação `@IfBuildProperty`, do Quarkus Arc, é um parâmetro de configuração resolvido no *build* — categoria *configuration parameters* na taxonomia de Horcas, Pinto e Fuentes (2023). Essa classificação é o que permite tratar a "metamorfose" com rigor conceitual, e não como mera decisão *ad hoc* de engenharia.

---

## 2.4 Compilação condicional e variabilidade em tempo de build

A variabilidade baseada em anotação tem, na **compilação condicional**, seu mecanismo mais consolidado. Trata-se de um mecanismo simples e amplamente usado em projetos de código aberto e na indústria: por diretivas de pré-processador (por exemplo, `#ifdef`), o programador inclui ou exclui partes da base de código conforme a configuração fornecida — sendo o kernel do Linux, com suas cerca de 12.000 opções escolhidas em tempo de *build*, o exemplo mais proeminente (HUNSEN et al., 2016). Definir diferentes conjuntos de configuração resulta em **programas compilados diferentes**, isto é, em variantes distintas do mesmo sistema (HUNSEN et al., 2016). O estudo empírico de Hunsen et al. (2016), sobre 27 sistemas reais, mostra que esse não é um recurso de nicho, mas um mecanismo mensurável e disseminado — inclusive com fração de código variável ainda maior nos sistemas industriais.

Cabe a distinção essencial que caracteriza a metamorfose. A compilação condicional introduz variabilidade **imediatamente antes do tempo de compilação**, incluindo ou excluindo componentes de código que **alteram a estrutura e o comportamento** do programa resultante — e frequentemente *feature flags* booleanas são usadas para projetar SPLs completas (BAUM et al., 2019). É essa alteração estrutural — e não uma ramificação condicional avaliada em tempo de execução — que sustenta a tese do trabalho: o trecho não selecionado não é apenas ignorado; ele **deixa de existir** no artefato final. Embora o `@IfBuildProperty` opere sobre *beans* CDI da JVM, e não sobre o pré-processador C, pertence à mesma categoria conceitual — variabilidade por anotação resolvida no *build* — e produz o mesmo efeito: variantes distintas geradas de um código-fonte único.

---

## 2.5 Superfície de ataque e segurança por redução de exposição

A propriedade de segurança da Arquitetura Metamórfica apoia-se no conceito de **superfície de ataque**. A segurança de um sistema pode ser aferida pelo conjunto de recursos e pontos de entrada por meio dos quais um atacante pode influenciá-lo: quanto maior e mais exposta a superfície de ataque, mais formas de ataque existem e mais inseguro o sistema tende a ser (MANADHATA; WING, 2011). Os **pontos de entrada** — as vias por onde os dados entram no sistema a partir do ambiente — funcionam como os pontos de entrada de ataque (MANADHATA; WING, 2011). Decisivamente para este trabalho, a métrica atua no **nível de projeto** — acima do código, abaixo do sistema como um todo —, podendo ser empregada deliberadamente por projetistas para melhorar a segurança (MANADHATA; WING, 2011).

Manadhata e Wing (2011) formalizam, ainda, a distinção entre **método externo e método interno**: um método é externo se puder ser invocado pelo usuário ou por outro sistema, e interno se apenas métodos do próprio sistema puderem invocá-lo. Neste trabalho, essa distinção — originalmente definida no nível dos métodos — é **transposta para o nível de negócio**, e dela decorre uma **abordagem flexível** entre as aplicações derivadas de um mesmo código-fonte. Nessa abordagem, separam-se as responsabilidades dos **clientes internos** (consumidores de dentro da própria empresa) e dos **clientes externos** (que usufruem dos serviços prestados pela empresa): cada modo do protótipo atende a um desses públicos, espelhando os *controllers* interno e externo. O resultado é **maior segurança** e uma **divisão clara do fluxo de uso** entre as aplicações — cada artefato expõe apenas as operações pertinentes ao seu público. Ao remover no *build* as rotas que não competem a determinado cliente, a arquitetura reduz por construção o número de pontos de entrada do artefato: onde a rota não existe, não há *bean* instanciado nem configuração capaz de reativá-la por engano.

A pertinência dessa propriedade acentua-se em arquiteturas distribuídas. Com microsserviços, a segurança torna-se mais desafiadora justamente pelo grande número de pontos de entrada gerados pela decomposição do sistema, e a literatura aponta um foco de pesquisa desequilibrado em favor dos ataques externos, recomendando maior atenção às ameaças de origem interna (HANNOUSSE; YAHIOUCHE, 2021). Eliminar pontos de entrada — o que o design metamórfico faz por construção — ataca a raiz desse problema, e a distinção entre ameaça interna e externa dá respaldo à separação de modos adotada no trabalho.

---

## 2.6 Arquitetura Hexagonal e Clean Architecture

A viabilidade de uma metamorfose que **preserva as camadas inferiores** — domínio, serviços e repositórios compartilhados entre todas as variantes — apoia-se em um princípio de arquitetura limpa: isolar as regras de negócio dos detalhes de entrega e de *framework*. A Arquitetura Hexagonal (*Ports & Adapters*), proposta por Cockburn, organiza a aplicação em torno de um núcleo de regras de negócio que se comunica com o mundo externo apenas por portas (interfaces) e adaptadores, permitindo testar o domínio em isolamento e trocar tecnologias externas sem afetar o núcleo. Clean Architecture sistematiza os mesmos princípios pela **Regra de Dependência**, em que as dependências apontam sempre para dentro, do detalhe para a política (MARTIN, 2017).

Dois pontos de Martin (2017) são especialmente relevantes ao protótipo. Primeiro, o *framework* deve ser tratado como um **detalhe** pertencente aos círculos externos da arquitetura, evitando espalhar suas anotações pelos objetos de negócio — os objetos de negócio "não devem saber da existência" do *framework* (MARTIN, 2017). Essa orientação sustenta posicionar a anotação de variabilidade (`@IfBuildProperty`) nos *controllers* — os adaptadores de entrada —, mantendo domínio e repositórios livres dela e, portanto, compartilháveis entre as variantes. Segundo, Martin (2017) descreve a prática de **separar o ambiente de build** de modo a produzir entregáveis independentes, implantáveis conforme o sistema evolui — que é, em essência, a estratégia de gerar artefatos interno e externo distintos a partir de um único código-fonte.

---

## 2.7 Síntese

O quadro abaixo relaciona cada fundamento a seu papel na Arquitetura Metamórfica:

| Fundamento | Referência-chave | Papel no trabalho |
|---|---|---|
| Microsserviços e dados descentralizados | PAHL; JAMSHIDI (2016); TAIBI; LENARDUZZI; PAHL (2018) | Motiva a agregação de dados para o *frontend* |
| API Gateway / BFF e o custo do reuso | TAIBI; LENARDUZZI; PAHL (2018); NEWMAN (2015) | Define o problema: especialização vs. reuso |
| SPL, *feature* e derivação de produto | HORCAS; PINTO; FUENTES (2023) | Enquadra a metamorfose como derivação de produtos |
| Variabilidade por anotação; compilação condicional | HORCAS; PINTO; FUENTES (2023); HUNSEN et al. (2016); BAUM et al. (2019) | Classifica e dá lastro empírico ao `@IfBuildProperty` |
| Superfície de ataque; método interno/externo | MANADHATA; WING (2011); HANNOUSSE; YAHIOUCHE (2021) | Fundamenta a "segurança por ausência" |
| Hexagonal / Clean Architecture | MARTIN (2017); Cockburn | Torna o reuso viável; justifica a anotação no adaptador |

Esses fundamentos convergem para a proposta central do trabalho: um único código-fonte, organizado de modo que o domínio e os serviços sejam compartilhados, do qual se derivam — por variabilidade em tempo de compilação — artefatos especializados que reduzem, por construção, a superfície de ataque de cada cliente.

---

> **Pendências de redação:**
> - Cockburn (2005/2024) é *grey literature*; ao citar formalmente, complementar com Martin (2017), como já indicado em `REFERENCIAS_CIENTIFICAS.md` (6.1).
> - As referências completas (ABNT) já constam em `CITACOES_ABNT.md` e `REFERENCIAS_CIENTIFICAS.md`; consolidar tudo no capítulo *Referências* ao montar o documento final.
> - Ao promover este capítulo, **remover a duplicação** com a *Fundamentação teórica* da Proposta (a Proposta passa a remeter a este capítulo).
