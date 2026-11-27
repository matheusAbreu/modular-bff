# Cap. 5 — Mecanismos clássicos de variabilidade baseados em ferramentas (p. 99–127)

> Fichamento de APEL et al. (2013). Resumo com palavras próprias; só as definições formais estão no original, com tradução livre. As páginas são as impressas.

## Visão geral (p. 99)

- O capítulo trata de três ferramentas de **gerência de configuração de software** que, embora não tenham sido criadas para SPL, são usadas para implementar variabilidade:
  1. sistemas de **controle de versão** (5.1);
  2. **sistemas de *build*** (5.2);
  3. **pré-processadores** (5.3).
- Todas operam **sobre** o código-fonte, de fora da linguagem. São, portanto, abordagens baseadas em ferramenta (seção 3.1.2).

---

## 5.1 Sistemas de controle de versão (p. 99–105)

### 5.1.1 Terminologia (p. 100–101)

O controle de versão registra a evolução dos artefatos. O livro distingue a variação **no tempo** da variação **em paralelo**:

> **Definição 5.1 (p. 101).** "A revision describes ordered variations over time; a release is a specifically named revision. A variant describes intentional variations that exist in parallel. Versions encompass both revisions and variants."
>
> *Tradução livre:* "Uma revisão descreve variações ordenadas no tempo; um *release* é uma revisão nomeada especificamente. Uma variante descreve variações intencionais que existem em paralelo. Versões abrangem tanto revisões quanto variantes."

> 🖼️ **[Figura 5.1, p. 101 — não reproduzível]** Eixo de revisões (tempo) cruzado com o eixo de variantes (produtos paralelos) numa linha de produto.

### 5.1.2 Construindo SPL com controle de versão (p. 101–103)

- **O processo típico com ramos (*branches*)**, ilustrado na Fig. 5.2:
  - um ramo principal de desenvolvimento;
  - ramos de *release*, para estabilizar versões;
  - ramos de *feature*, para trabalhos longos ou arriscados;
  - ramos de correção, integrados (*merge*) onde for preciso.
- **Um ramo por cliente (Fig. 5.3, a partir de Staples e Hill, 2004).**
  - O código comum fica no ramo principal. Para cada cliente que pede modificações, abre-se um ramo próprio, que recebe as mudanças do principal, mas não devolve as suas.
  - A desvantagem óbvia: cada cliente com variação exige um ramo, e o resultado são **produtos customizados**, não *features* combináveis.
- **Um ramo por *feature* (Fig. 5.4).**
  - Cada *feature* é desenvolvida em um ramo, e o produto resulta do *merge* dos ramos das *features* selecionadas.
  - Problema: cada ramo carrega também o código base, então rastrear a *feature* até o seu código deixa de ser óbvio.

> 🖼️ **[Figuras 5.2 a 5.4, p. 102–103 — não reproduzíveis]**
> - **5.2:** diagrama de ramos e *merges* durante o desenvolvimento e os *releases*;
> - **5.3:** ramos por cliente, derivados de um ramo central;
> - **5.4:** ramos por *feature* (*Colored*, *Weighted*) integrados num produto.

### 5.1.3 Discussão (p. 103–105)

- **Por que atrai.**
  - Os desenvolvedores já usam a ferramenta.
  - Ela se aplica de modo **uniforme** a código e não código, com granularidade **arbitrária** (de diretórios inteiros a um caractere).
  - Mudanças transversais são simples, porque se edita qualquer coisa invasivamente, sem pré-planejamento.
  - No início de um projeto, abrir um ramo para atender um cliente é rápido.
- **O veredito dos autores.** À medida que a linha de produto evolui, os problemas se acumulam. A menos que sejam poucas variações pequenas para poucos clientes, o controle de versão deve ficar restrito ao **controle de revisões**, e **não** ser usado para a variabilidade de *features*: as desvantagens superam em muito as vantagens. Os motivos:
  - estimula desenvolver **produtos** distintos, e não *features*. O *merge* por *feature* depende de *merges* eficazes e falha quando *features* tocam os mesmos trechos;
  - com muitos ramos, **evoluir é difícil**: cada correção precisa ser propagada a todos os ramos relevantes;
  - não há noção de modularidade nem de rastreabilidade de *features*.

**Quadro dos autores (p. 104), em síntese.**
- **A favor:** ferramentas maduras e conhecidas; personalização arbitrária em compilação, independente de granularidade e transversalidade; uniformidade; pouco pré-planejamento.
- **Contra:** voltado a produtos, não a *features*; evolução e propagação de correções caras; nenhuma modularidade; rastreabilidade fraca.

> 🔗 **Relação com o TCC.** Esta é a análise mais próxima da alternativa **"um repositório (ou ramo) por BFF"**. Os autores a desaconselham para variabilidade de *features*: incentiva produtos em vez de *features* e torna a propagação de correções cara. Esses argumentos fortalecem a seção de Trabalhos Relacionados do TCC (alternativa de repositórios separados) e podem ser citados com página.

---

## 5.2 Sistemas de *build* (p. 105–110)

- **O papel.** Um sistema de *build* agenda e executa as tarefas de construção: geradores, compilação, testes, empacotamento. Junto com o controle de versão, integra a gerência de configuração.
  - Vai de um *script shell* a ferramentas como `make`, `ant` e `maven`, que oferecem múltiplos alvos, dependências, *build* incremental e *download* de bibliotecas.
- **Por que serve à variabilidade.** O sistema de *build* já decide **quando, o quê e como** compilar. É, portanto, um candidato natural a gerenciar a **variabilidade em compilação**.

### 5.2.1 Variabilidade em *scripts* de *build* (p. 105–106)

- Um *script* simples compila e empacota tudo. Com variabilidade, ele testa um parâmetro e escolhe, por exemplo, entre duas versões das classes `Node` e `Edge` (com ou sem cor), compilando `Color` só se for o caso.
- É a técnica de **parâmetros aplicada no nível do *build***.
- **Origem das opções de configuração:**
  - linha de comando;
  - **arquivos de configuração**, possivelmente gerados por uma ferramenta de seleção de *features*;
  - detecção automática do contexto (sistema operacional, *hardware*, bibliotecas disponíveis).
- **O que mais o *build* controla:** com quais bibliotecas (e versões) o produto é compilado, e como os arquivos são compilados (otimizações, informação de depuração).

> 🖼️ **[Figura 5.5, p. 106 — não reproduzível]** Dois *scripts* de *build* para o exemplo do grafo, lado a lado: sem variabilidade (à esquerda) e com uma condição que seleciona os arquivos conforme o parâmetro de cor (à direita).

### 5.2.2 *Scripts* de *build* por cliente (p. 106–107)

- **O arranjo (Staples e Hill, 2004):** uma implementação base em um diretório e, para cada cliente, um *script* próprio com as extensões daquele cliente, que pode **substituir** arquivos da base ou **acrescentar** arquivos.
- É parecido com ramos de controle de versão, mas só os arquivos alterados são copiados.

> 🖼️ **[Figura 5.6, p. 107 — não reproduzível]** Estrutura de diretórios com a base e as extensões por cliente, cada uma com seu *script* de *build*.

### 5.2.3 Estudo de caso: o *build* do Linux (p. 107–108)

- O *kernel* usa o Kbuild, uma hierarquia de *scripts* para `make`. Mais de **600** *scripts* decidem quais dos milhares de arquivos C entram em cada configuração.
- **O mecanismo.** Uma linha associa um arquivo a uma opção do modelo de *features*. Conforme a opção valha "compilar", "compilar como módulo" ou "não compilar", o arquivo entra no *kernel*, vira módulo carregável ou fica de fora.
- **O alcance.** Na versão 2.6.33.3, **97%** dos 9.146 arquivos C são incluídos **condicionalmente** em alguma configuração.
- **A divisão de trabalho.** O *build* controla a variabilidade **no nível de arquivo**. A variabilidade mais fina fica com o pré-processador, dentro dos arquivos (5.3).

> 🖼️ **[Figura 5.7, p. 108 — não reproduzível]** Trecho do `Makefile` de drivers de vídeo do Linux, com as linhas que condicionam arquivos a opções de configuração.

### 5.2.4 Discussão (p. 108–110)

- **Classificação.**
  - É natural usar o *build* para variabilidade em compilação. Tipicamente, ele aplica a técnica de **parâmetros**, mas **em tempo de compilação**.
  - Os autores o consideram um mecanismo de **anotação no nível de arquivo**: o *build* decide quais arquivos compilar sob quais condições.
- **Pontos fortes.**
  - No nível de arquivo, é independente de linguagem e se aplica **uniformemente** a código e não código.
  - Mudanças arbitrárias são possíveis em compilação, sem grande pré-planejamento.
  - Enquanto as *features* se mapearem a arquivos, a rastreabilidade é boa.
- **Problema: a granularidade de arquivo.**
  - Para mudanças pequenas, é preciso **replicar arquivos inteiros**. No exemplo, copiou-se a classe toda para acrescentar um campo e uma linha de impressão.
  - **Mitigação:** usar padrões de projeto em arquivos separados (*observer*, subclasses, *strategy*, *decorator*) ou um *factory method* central que decide quais classes instanciar.
- **Papel de orquestração.** Além da variabilidade por arquivo, o *build* costuma **orquestrar outros mecanismos**: passa opções ao pré-processador, escolhe *plug-ins*, dispara geradores.
- **Fraquezas.** *Scripts* complexos e indisciplinados ficam difíceis de manter e analisar, e não há noção de modularidade.

**Quadro dos autores (p. 110), em síntese.**
- **A favor:** consolidado; adequado à compilação condicional por arquivo; orquestra outros mecanismos; personalização arbitrária em compilação; uniforme; pouco pré-planejamento.
- **Contra:**
  - limitado ao nível de arquivo, o que, somado à falta de planejamento sistemático, leva a **replicação de código**;
  - inadequado para granularidade fina;
  - sem modularidade;
  - rastreabilidade só por arquivo;
  - *scripts* complexos difíceis de manter e analisar.

> 🔗 **Relação com o TCC.** Esta seção descreve exatamente a camada de **orquestração** do protótipo:
> - o `Makefile` e o `Dockerfile` recebem `BFF_API_*_ACTIVE` por `--build-arg` e os repassam ao Gradle/Quarkus;
> - quem decide o que entra na variante é o *build* do Quarkus, a partir da anotação nos *controllers*.
>
> Pelos termos do livro, o *build* do protótipo **orquestra** um mecanismo de anotação (o `@IfBuildProperty`), com ligação em compilação.
>
> Isso dá à seção 6.6 do TCC (*Build* por artefato) uma fonte para descrever o papel do *build*: APEL et al. (2013, p. 105 e 108–109). Ressalva: o livro trata de seleção de arquivos, e o protótipo não seleciona arquivos. Todas as classes são compiladas, e a anotação decide quais *beans* e rotas são gerados.

---

## 5.3 Pré-processadores (p. 110–124)

### 5.3.1 O pré-processador C, `cpp` (p. 110–111)

- O `cpp` transforma o texto antes da compilação. Oferece:
  - inclusão de arquivos;
  - macros, que funcionam como variáveis e funções de compilação;
  - **compilação condicional** (`#if`, `#ifdef`, `#else`, `#endif`).
- Conforme as macros definidas (inclusive por parâmetro de linha de comando), os trechos são **incluídos ou removidos antes da compilação**. O trecho entre diretivas é dito **anotado**.

### 5.3.2 Implementando variabilidade com pré-processadores (p. 111–113)

- Cada *feature* vira uma macro, e o código da *feature* fica entre diretivas condicionais.
- O exemplo real mostrado é o Berkeley DB, da Oracle, em que um bloco anotado ultrapassa cem linhas. O exemplo do grafo também é reescrito com compilação condicional.
- Aparecem ainda outros padrões de uso, como macros alternativas definidas conforme a *feature*.

> 🖼️ **[Figuras 5.8 a 5.10, p. 112–113 — não reproduzíveis]**
> - **5.8:** trecho do Berkeley DB com compilação condicional;
> - **5.9:** o grafo com `#ifdef` para *Weighted* e *Colored*;
> - **5.10:** padrão de macros alternativas.

### 5.3.3 Outros pré-processadores (p. 113–116)

- **Java (p. 113–114).**
  - Nativamente, o Java tem compilação condicional limitada: se a condição de um `if` for estaticamente falsa, o compilador **pode** omitir o corpo. A especificação abre essa exceção à regra de código inalcançável.
  - Os autores fazem três ressalvas:
    1. nem todo compilador faz isso;
    2. com o mesmo construto, a variabilidade em compilação e em execução ficam difíceis de distinguir;
    3. não há forma nativa de remover métodos ou classes inteiros, o que exige ferramentas externas.
- **Munge e Antenna (p. 113–114).** São pré-processadores para Java, com diretivas em comentários. O Antenna, em vez de remover, comenta e descomenta trechos conforme a seleção, o que permite trocar de configuração dentro do editor.
- **Pré-processadores configuráveis.** Exemplos: GPP, M4 e os das ferramentas comerciais pure::variants e Gears.
- **Pré-processadores sintáticos.** Respeitam a estrutura da linguagem e anotam só unidades sintáticas inteiras (função, instrução). São menos sujeitos a erros de sintaxe.
- **Anotações em modelos.** Há ferramentas que anotam diagramas e outros artefatos não textuais (FeatureMapper, fmp2rsm).

> 🖼️ **[Figuras 5.11 a 5.13, p. 114–116 — não reproduzíveis]**
> - **5.11:** compilação condicional "nativa" em Java, com uma constante e um `if`;
> - **5.12:** pré-processador sintático na abordagem *tag-and-prune*;
> - **5.13:** diagrama entidade-relacionamento com anotações de *feature* no FeatureMapper.

> 🔗 **Relação com o TCC.** Esta é a passagem mais útil para situar o `@IfBuildProperty` no ecossistema JVM.
> - O Java puro **não** remove classes nem métodos por configuração, e a compilação condicional por `if` constante é limitada e indistinguível da variabilidade em execução.
> - O Quarkus resolve parte disso de outro modo: a classe é compilada, mas o *bean* e a rota **não são gerados** na fase de *augmentation*.
> - Essa diferença de mecanismo já está registrada na seção 2.4 do TCC. Esta passagem do livro (p. 113–114) pode ser a fonte para a afirmação de que a JVM não tem compilação condicional nativa de classes.

### 5.3.4 Anotações disciplinadas (p. 116–118)

- Um pré-processador permite manipular o código quase arbitrariamente.
- **Anotação disciplinada:** a que se alinha à estrutura sintática da linguagem (envolve uma função inteira, uma declaração, uma instrução). As indisciplinadas cortam a sintaxe (meio de uma expressão, parte de uma instrução).
- As disciplinadas são mais fáceis de ler, manter e processar por ferramentas. Até analisar sintaticamente código com anotações indisciplinadas é difícil, e por isso ferramentas de refatoração raramente o suportam.
- **Na prática:** um estudo mostra que **84%** das anotações `#ifdef` já são disciplinadas, e a maioria das restantes pode ser reescrita.

> 🖼️ **[Figuras 5.14 e 5.15, p. 117 — não reproduzíveis]** Trechos do editor vim com anotações disciplinadas (em torno de declarações e instruções inteiras) e indisciplinadas (dentro de instruções e expressões).

> 🔗 **Relação com o TCC.** O `@IfBuildProperty` é uma anotação **disciplinada** por construção: só pode envolver uma unidade sintática inteira, a classe. Isso afasta do protótipo as críticas mais pesadas aos pré-processadores.

### 5.3.5 Pré-processadores na prática (p. 118–120)

- São muito usados em C e C++, e bem menos em Java.
- **Resultados de Liebig et al. (2010, 2011)**, em 40 projetos C de código aberto:
  - todos usam compilação condicional, com entre 13 e mais de 16 mil *features*, cobrindo de 2,5% a 69% do código;
  - em média, cada *feature* tem 7 blocos `#ifdef` e cerca de 60 linhas;
  - cerca de 10% das *features* injetam o **mesmo** código em vários pontos; as demais injetam código diferente em cada ponto;
  - a maioria dos blocos é de granularidade **grossa**: 52% envolvem instruções inteiras, 46% funções ou declarações de topo e só 1,8% mexem dentro de uma instrução;
  - o número de *features* cresce aproximadamente de forma linear com o tamanho do projeto, mas o espalhamento varia muito e independe do tamanho.

> 🖼️ **[Tabela 5.1, p. 119 — não reproduzível]** Uso de pré-processador em 40 sistemas C de código aberto: por projeto, tamanho, número de *features*, proporção de código anotado e métricas de espalhamento (excerto de Liebig et al., 2010).

### 5.3.6 Discussão (p. 120–124)

**O debate.** Os pré-processadores são **controversos**: muito usados na prática, muito criticados na academia. Os autores lembram o artigo "#ifdef considered harmful" e a expressão "#ifdef hell".

**Benefícios (p. 120).**
- São leves, fáceis de usar e já disponíveis para variabilidade **em compilação**.
- O modelo é simples: **anotar e remover condicionalmente**.
- Aplicam-se uniformemente a qualquer artefato textual.
- Permitem granularidade arbitrária. Isso dá **controle preciso sobre o que é efetivamente entregue**, até o nível da instrução.
- A rastreabilidade é fácil de extrair, porque as anotações citam as *features* diretamente.
- Por serem leves, são adequados à **adoção extrativa**: basta acrescentar diretivas ao código existente, com pouco pré-planejamento.

**Críticas (p. 121–124).**
- **Negligenciam a separação de interesses.** O código da *feature* fica espalhado e misturado, sem encapsulamento. O próprio conhecimento de configuração se espalha, porque macros podem ser redefinidas no código.
- **Ofuscação.** Anotações aninhadas e numerosas tornam o código difícil de ler (exemplo do Femto OS).
- **Propensão a erros.** Erros de sintaxe ou de tipo podem aparecer só em certas combinações de *features*. O exemplo do Berkeley DB tem um erro que só surge em produtos sem determinada *feature*.

> 🖼️ **[Figuras 5.16 a 5.18, p. 122–124 — não reproduzíveis]**
> - **5.16:** código Java com anotações;
> - **5.17:** código do Femto OS com as linhas de diretiva destacadas, mostrando a densidade de anotações;
> - **5.18:** trecho adaptado do Berkeley DB com um erro de sintaxe que só aparece em certos produtos.

**Quadro dos autores (p. 122), em síntese.**
- **A favor:** simples e conhecido; anotar e remover; personalização **em compilação** sem código repetitivo; granularidade arbitrária; pouco pré-planejamento; *features* rastreáveis; leve para adoção extrativa.
- **Contra:** sem separação de interesses nem ocultação; conhecimento de configuração espalhado; risco de código ofuscado; erros que surgem só em certas combinações.

> 🔗 **Relação com o TCC.**
> - O protótipo herda os **benefícios** atribuídos aos pré-processadores: anotar e remover, em compilação, com controle do que é entregue, e adequado à adoção extrativa.
> - Mas evita as críticas centrais: a anotação é disciplinada, de granularidade de classe, e não se espalha por dentro do código.
> - **O risco de erro "só em certas combinações" é real para o protótipo.** É a lacuna já registrada na seção 7.5.1 do TCC: a integração contínua testa só a linha de base. O livro dá fonte para essa ameaça à validade (p. 121–124).

---

## 5.4 Leituras complementares e exercícios (p. 124–127)

- **Leituras:** o capítulo remete à literatura de gerência de configuração e controle de versão e aos estudos citados ao longo do texto: o *build* do Linux (Berger et al., 2010; Dietrich et al., 2012), o uso de pré-processadores (Ernst et al., 2002; Liebig et al., 2010, 2011) e as críticas ao `#ifdef` (Spencer e Collyer, 1992). Conferir no impresso a lista completa da seção 5.4.
- **Exercícios:**
  - reimplementar o *chat* com ramos, *scripts* de *build* e pré-processador;
  - investigar projetos reais;
  - classificar erros em trechos com anotações;
  - estender a comparação do Exercício 4.11.
