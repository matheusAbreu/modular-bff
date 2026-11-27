# Cap. 6 — Mecanismos avançados de variabilidade baseados em linguagem (p. 129–173)

> Fichamento de APEL et al. (2013). Resumo com palavras próprias; só as definições formais centrais estão no original, com tradução livre. As páginas são as impressas.

## Visão geral (p. 129)

- O capítulo apresenta abordagens **baseadas em linguagem** e **de composição**, mais recentes e sobretudo acadêmicas, criadas para superar os limites das técnicas clássicas na **separação de interesses transversais**.
- As duas principais são:
  - a **programação orientada a *features*** (FOP, 6.1);
  - a **programação orientada a aspectos** (AOP, 6.2).
- Seguem-se uma comparação e a combinação das duas (6.3), ferramentas (6.4), relevância prática (6.5) e outras abordagens (6.6).

---

## 6.1 Programação orientada a *features* (p. 130–141)

**A ideia.**
- Decompor o projeto e o código do sistema **nas *features* que ele oferece**, de modo que a estrutura do sistema se alinhe às *features*, idealmente com **um módulo por *feature***.
- Para isso, a linguagem ganha construtos que dizem que parte do código contribui para qual *feature* e que encapsulam esse código em unidades combináveis.
- Selecionadas as *features*, os módulos correspondentes são **compostos automaticamente**.

> 🖼️ **[Figura 6.1, p. 130 — não reproduzível]** Mapeamento limpo, um para um, entre as *features* do modelo e os artefatos que as implementam, que é o ideal da FOP.

### 6.1.1 Projeto baseado em colaborações (p. 130–132)

Uma *feature* raramente se limita a uma classe: ela afeta várias classes ao mesmo tempo.

> **Definição 6.1 (p. 131).** "A collaboration is a set of classes (or parts thereof) that cooperate to implement the functionality of a feature."
>
> *Tradução livre:* "Uma colaboração é um conjunto de classes (ou partes delas) que cooperam para implementar a funcionalidade de uma *feature*."

> **Definição 6.2 (p. 131).** "A role defines the responsibilities a class takes in a collaboration."
>
> *Tradução livre:* "Um papel define as responsabilidades que uma classe assume em uma colaboração."

O projeto é visto como uma grade: as **colunas** são as classes e as **camadas** são as colaborações (*features*). Cada célula é o papel de uma classe numa *feature*.

> 🖼️ **[Figura 6.2, p. 132 — não reproduzível]** Grade de colaborações da biblioteca de grafos. As classes (`Graph`, `Node`, `Edge`, `Weight`, `Color`) aparecem em colunas e as *features* (*BasicGraph*, *Weighted*, *Colored*) em camadas, com os papéis em cada interseção.

### 6.1.2 Módulos de *feature* (p. 132–133)

- Cada camada vira um **módulo de *feature***: um contêiner com todos os papéis da *feature*.
- Compondo conjuntos diferentes de módulos, obtêm-se produtos diferentes. No exemplo, são quatro grafos possíveis.

### 6.1.3 A linguagem Jak (p. 133–135)

- Jak é uma extensão de Java para FOP.
- O conceito central é o **refinamento de classe**: um módulo pode acrescentar campos e métodos a uma classe existente e estender métodos, chamando o original. Faz isso sem criar subclasse e sem alterar o código da classe.

> 🖼️ **[Figuras 6.3 a 6.5, p. 133–136 — não reproduzíveis]**
> - **6.3:** o módulo *BasicGraph* em Jak;
> - **6.4:** o módulo *Weighted*, que refina `Edge` com peso;
> - **6.5:** o resultado da composição dos dois módulos.

### 6.1.4 Modelos da FOP (p. 135–138)

- **GenVoca** é um modelo **algébrico** da FOP (Batory e O'Malley, 1992). Cada linha de produto é uma álgebra. A *feature* base é uma constante (o programa base), e as demais *features* são funções que recebem um programa e devolvem o programa modificado. Um produto é a composição dessas funções.
- Há também a ideia de **hierarquias de contenção** (pacotes, classes, membros): compor *features* é sobrepor essas hierarquias (Fig. 6.8).
- A mesma ideia se estende a artefatos que não são código, como a documentação HTML do exemplo. É a **uniformidade** do capítulo 3.

> 🖼️ **[Figuras 6.6 a 6.8, p. 137–138 — não reproduzíveis]**
> - **6.6:** hierarquia de contenção e modelo de *features* do grafo;
> - **6.7:** grade de colaborações incluindo a documentação HTML;
> - **6.8:** composição de hierarquias de contenção.

### 6.1.5 Discussão (p. 138–141)

- **Classificação.** É baseada em linguagem e de composição. Os módulos podem ser compostos **em compilação** e, com pesquisa recente, em execução. Há mecanismos que incorporam os refinamentos ao código **sem custo em execução**.
- **Separação de interesses.** Todo o código de uma *feature*, inclusive o transversal ao longo de várias classes, fica num módulo nomeado pela *feature*.
- **Rastreabilidade.** É o ponto mais forte: o mapeamento *feature* → módulo é direto. Nas técnicas clássicas (parâmetros, pré-processadores), o código fica espalhado. Nos *frameworks* e componentes, a falta de modularidade transversal dificulta a separação.
- **Pré-planejamento.** O refinamento de classe permite estender sem prever pontos de extensão e sem quebrar clientes. Diferentemente da herança, todas as referências à classe passam a apontar para a versão refinada.

> 🖼️ **[Figura 6.9, p. 140 — não reproduzível]** Contraste entre herança (a extensão exige nova subclasse e mudança nos clientes) e refinamento de classe (a classe é estendida no lugar).

**Quadro dos autores (p. 141), em síntese.**
- **A favor:** mecanismo de linguagem simples, com poucas extensões; personalização **em compilação sem custo em execução**; separação de código transversal em módulos; rastreabilidade direta; uniformidade conceitual com ferramentas para várias linguagens; pouco pré-planejamento.
- **Contra:**
  - exige adotar uma extensão de linguagem ou ferramentas de composição pouco familiares;
  - granularidade no nível de método (ou de outras entidades nomeadas);
  - composição guiada pela sintaxe, sem interfaces obrigatórias entre módulos de *feature*;
  - ferramentas precisam ser construídas para cada linguagem, embora possam ser geradas;
  - só ferramentas acadêmicas até então, com pouca experiência prática.

> 🔗 **Relação com o TCC.** A FOP é a alternativa **por composição** mais elaborada ao que o protótipo faz por anotação: cada modo (interno, externo) seria um módulo de *feature* refinando um núcleo comum. Ela cabe em Trabalhos Relacionados como o "outro lado" da classificação anotação × composição. O próprio livro registra (6.5) que a FOP ainda é sobretudo acadêmica, o que justifica não adotá-la num protótipo sobre Kotlin/Quarkus.

---

## 6.2 Programação orientada a aspectos (p. 141–152)

**Motivação.** Algumas *features* espalham **o mesmo** código por muitos pontos. O exemplo é *Colored*, que repete a mesma lógica em várias classes.

> 🖼️ **[Figura 6.10, p. 142 — não reproduzível]** Código do grafo com o trecho da *feature Colored* destacado, espalhado e replicado em várias classes.

### 6.2.1 Aspectos: separando interesses transversais (p. 142–145)

> **Definição 6.3 (p. 143).** "An aspect encapsulates the implementation of a crosscutting concern."
>
> *Tradução livre:* "Um aspecto encapsula a implementação de um interesse transversal."

Os demais conceitos são definidos formalmente no livro (Def. 6.4 a 6.9, p. 143–145). Em resumo:
- ***weaver*:** o combinador de aspectos, que incorpora os aspectos ao programa nos pontos escolhidos;
- ***join point*:** ponto de junção, um evento da execução em que um aspecto pode agir (por exemplo, a chamada ou a execução de um método);
- ***pointcut*:** a especificação declarativa de **quais** pontos de junção um aspecto afeta;
- ***advice*:** o código executado nesses pontos, antes, depois ou em torno deles;
- **declaração inter-tipos:** um aspecto injeta campos, métodos ou interfaces em classes existentes;
- **quantificação:** selecionar muitos pontos de junção com uma única especificação.

> 🖼️ **[Figura 6.11, p. 144 — não reproduzível]** O *weaver* combina os aspectos com o programa base para produzir o programa final.

### 6.2.2 A linguagem AspectJ (p. 145–147)

AspectJ é a extensão de Java mais conhecida para AOP. O livro mostra a *feature Colored* implementada como aspecto, exemplos de *advice* (antes, em torno, depois) e padrões típicos de *pointcut*.

> 🖼️ **[Figuras 6.12 a 6.15, p. 145–147 — não reproduzíveis]** Código AspectJ da *feature Colored*, exemplos de *advice* e uma tabela de padrões de *pointcut* com o significado de cada um.

### 6.2.3 Aspectos para linhas de produto (p. 147–149)

- Cada *feature* opcional pode ser um aspecto, e o produto é o programa base combinado com os aspectos das *features* selecionadas.
- Aspectos típicos (perfilador, rastreamento, cache, sincronização) mostram o tipo de *feature* em que a AOP se sai bem.
- O livro também mostra o grafo inteiro em AspectJ.

> 🖼️ **[Figuras 6.16 e 6.17, p. 148–149 — não reproduzíveis]** Quatro aspectos típicos, segundo Laddad (2003), e um excerto do grafo com *Weighted* e *Colored* como aspectos.

### 6.2.4 Discussão (p. 149–152)

- **Classificação.** É baseada em linguagem e de composição. A ligação é em compilação (AspectJ) ou em carga, conforme a tecnologia de combinação.
- **Granularidade.** É definida pelo modelo de pontos de junção. A AspectJ é a mais fina entre as abordagens de composição do livro, mas não alcança a granularidade de certas abordagens por anotação: variáveis locais e estruturas de controle ficam fora.
- **Rastreabilidade.** Com um aspecto por *feature*, todo o código dela fica num único lugar endereçável.
- **Pré-planejamento.** Mais que a FOP, a AOP estende o programa **sem planejamento prévio**: intercepta o fluxo em pontos arbitrários (acesso a campos, chamadas, exceções).
**Quadro dos autores (p. 151–152), em síntese.**
- **A favor:**
  - variabilidade em compilação sem custo em execução, com ligação em carga também possível;
  - separação de código transversal em aspectos;
  - rastreabilidade direta *feature* → aspecto;
  - granularidade fina, definida pelo modelo de pontos de junção;
  - pouco ou nenhum pré-planejamento.
- **Contra:**
  - exige adotar uma extensão de linguagem sofisticada, com um paradigma novo;
  - extensões diferentes para cada linguagem de código ou não código, com modelos uniformes só experimentais;
  - composição guiada pela sintaxe, sem interfaces obrigatórias;
  - ferramentas por linguagem;
  - só ferramentas acadêmicas, com pouca aplicação prática.
- Em 6.3.5, os autores acrescentam que mecanismos como *advice* e quantificação podem violar a ocultação de informação e prejudicar compreensão, manutenção e evolução.

> 🔗 **Relação com o TCC.** A AOP aparece na literatura de SPL (Adkins et al., ref. 3.4 do registro) como alternativa à compilação condicional. O livro dá base para classificá-la como **composição**, e não anotação. Útil se Trabalhos Relacionados mencionar a migração de `#ifdef` para aspectos.

---

## 6.3 Aspectos e módulos de *feature* em conjunto (p. 152–166)

Esta seção compara FOP e AOP e propõe combiná-las.

### 6.3.1 Interesses transversais homogêneos e heterogêneos (p. 153–157)

- **Homogêneo:** a **mesma** extensão aplicada em vários pontos. É o forte da AOP, graças à quantificação.
- **Heterogêneo:** **extensões diferentes** em pontos diferentes, cada uma num lugar. É o forte da FOP, com colaborações.
- O livro define as duas categorias formalmente (Def. 6.10, p. 153, a partir de Colyer et al., 2004).

> 🖼️ **[Figuras 6.18 a 6.27, p. 154–157 — não reproduzíveis]** Diagramas contrastando os dois tipos de interesse transversal e implementações lado a lado em Jak e em AspectJ: a *feature Colored*, uma colaboração implementada como aspecto, uma *feature* de grande escala implementada das duas formas, e interesses transversais estáticos em Jak e AspectJ.

### 6.3.2 Interesses transversais estáticos e dinâmicos (p. 157–161)

- **Estático:** altera a **estrutura** do programa (acrescenta membros). Os dois paradigmas o tratam: refinamento de classe e declaração inter-tipos.
- **Dinâmico:** altera o **comportamento** em eventos da execução. O livro distingue:
  - o **básico**, ligado só à execução de métodos;
  - o **avançado**, ligado a outros eventos (exceções, atribuições, fluxo de controle). Só a AOP trata este bem (Def. 6.11, p. 159).

> 🖼️ **[Figuras 6.28 a 6.33, p. 158–161 — não reproduzíveis]** Implementações de interesses dinâmicos em Jak e AspectJ: rastrear mudanças de peso só durante um teste, uma estrutura de grafo recursiva, e *advice* avançado sobre o mecanismo de impressão comparado ao refinamento de classe.

### 6.3.3 Síntese da comparação (p. 161)

- A FOP se concentra em **colaborações** (heterogêneas). A AOP se concentra em interesses **homogêneos** e em abstrair o fluxo dinâmico.
- Como uma *feature* costuma misturar os dois tipos, **combinar** os mecanismos tende a render mais do que qualquer um isolado.

> 🖼️ **[Tabela 6.1, p. 162 — conferir no impresso]** Comparação ponto a ponto entre FOP e AOP: tipo de transversalidade tratada (heterogênea × homogênea; estática × dinâmica), granularidade e outros critérios.

### 6.3.4 a 6.3.6 Combinação e estudo empírico (p. 161–166)

- **Módulos de *feature* aspectuais.** Módulos de *feature* que contêm aspectos, de modo que a decomposição por *features* e por aspectos convivam.
- **Estudo de Apel (2010).** A AOP oferece mecanismos poderosos (*advice*, quantificação) que podem ferir a ocultação de informação. O estudo mede, com métricas de código, com que frequência programas AspectJ reais usam esses mecanismos **avançados** em comparação com os **básicos**, que a FOP também oferece. Os números estão na Tabela 6.2 e na Fig. 6.37 (conferir no impresso).

> 🖼️ **[Figuras 6.34 a 6.37 e Tabela 6.2, p. 162–166 — não reproduzíveis]**
> - **6.34:** decomposição por *features* de um projeto orientado a aspectos;
> - **6.35:** *Colored* como módulo de *feature* aspectual;
> - **6.36:** composição de hierarquias com aspectos;
> - **Tabela 6.2:** os programas AspectJ analisados;
> - **6.37:** frações de mecanismos básicos e avançados no código analisado.

---

## 6.4 Ferramentas (p. 166–167)

O livro lista ferramentas para FOP e AOP, entre elas FeatureIDE, FeatureC++, AspectJ/AJDT, AspectC++/ACDT, JBoss AOP e Spring. A maioria é acadêmica. Conferir no impresso a lista completa.

## 6.5 Relevância prática (p. 167–168)

- **O diagnóstico dos autores.** AOP e FOP continuam **movidas principalmente pela academia**. Há sistemas substanciais feitos com elas (como a suíte AHEAD e uma linha de *middleware* da IBM), mas a adoção industrial ainda está distante.
- **Os caminhos indiretos de entrada na indústria:**
  - mecanismos de extensão não invasivos em linguagens correntes, como *traits* (Scala) e classes parciais (C#), parentes do refinamento de classe;
  - conceitos de aspectos dentro de *frameworks* populares, como o Spring.

> 🔗 **Relação com o TCC.** A constatação de que FOP e AOP ainda são sobretudo acadêmicas (p. 167–168) justifica a escolha de um mecanismo **disponível no *framework*** (o `@IfBuildProperty` do Quarkus) em vez de uma extensão de linguagem. O exemplo do Spring mostra o mesmo movimento: conceitos acadêmicos que chegam à indústria **embutidos em *frameworks***. Pode-se argumentar algo análogo para a variabilidade em *build* do Quarkus, mas como leitura do autor, não do livro.

## 6.6 Outras abordagens (p. 168–171)

- **Programação orientada a deltas (6.6.1).** Um módulo base mais **módulos delta** que o modificam passo a passo. Diferentemente dos módulos de *feature*, um delta também pode **remover** classes e membros.
- **Refatoração por módulos de *feature* (6.6.2).** Um módulo que aplica uma refatoração, como renomear uma classe, para conciliar *features*.
- **Programação orientada a contexto (6.6.3).** A linguagem expressa comportamento dependente do **contexto** (sistema operacional, rede, localização), que pode mudar em execução. Em termos de SPL, os comportamentos são *features*, e o mecanismo de ativação é uma composição **dinâmica**.

> 🖼️ **[Figuras 6.38 a 6.40, p. 169–171 — não reproduzíveis]** Um delta que substitui o método `print` por `display`, um módulo de refatoração que renomeia `Node` para `Vertex`, e a *feature Colored* como camada dependente de contexto em ContextJ.

## 6.7 Leituras complementares e exercícios (p. 171–173)

- **Leituras:**
  - Os autores registram que **não há livro-texto de FOP**. Os artigos seminais são Prehofer (1997) e Batory et al. (2004). Czarnecki e Eisenecker (2000) cobrem parte do tema, e Apel e Kästner (2009) dão uma visão geral atualizada (é a ref. 3.2 do registro do acervo).
  - Para AOP: os livros de Laddad (2003) e outros, os artigos de Kiczales et al. (1997, 2001), a coletânea de Filman et al. (2005) e a visão crítica de Steimann (2006).
- **Exercícios:** reimplementar o *chat* com FOP e AOP e compará-las.
