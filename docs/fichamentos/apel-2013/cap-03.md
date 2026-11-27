# Cap. 3 — Conceitos básicos, classificação e critérios de qualidade (p. 47–63)

> Fichamento de APEL et al. (2013). Resumo com palavras próprias; só as definições formais estão no original, com tradução livre. As páginas são as impressas.

## Visão geral (p. 47–48)

- O capítulo abre a Parte II do livro, dedicada à **implementação** de linhas de produto.
- Os autores observam que a área concentrou esforço no processo e na análise (o espaço do problema: identificar e modelar *features*, caminhos de adoção) e deu menos atenção a como **implementar** a linha de produto de forma sistemática. Implementar é o espaço da solução: implementação do domínio e derivação do produto.
- Muitos mecanismos de reuso e variabilidade usados hoje em SPL não nasceram para SPL e foram adotados "silenciosamente". Os capítulos 4 e 5 tratam desses mecanismos clássicos; os capítulos 6 e 7, de abordagens mais recentes, pensadas para SPL.
- O capítulo oferece duas ferramentas para o resto do livro:
  - **três dimensões** para classificar técnicas de implementação (seção 3.1);
  - **seis critérios de qualidade** para compará-las (seção 3.2).

---

## 3.1 Dimensões da implementação de variabilidade (p. 48–52)

Para que uma linha de produto seja implementada com eficiência, o código precisa ser variável. Como "variabilidade" é um termo amplo na literatura, o livro adota uma definição restrita a SPL:

> **Definição 3.1 (p. 48).** "Variability is the ability to derive different products from a common set of artifacts."
>
> *Tradução livre:* "Variabilidade é a capacidade de derivar produtos diferentes a partir de um conjunto comum de artefatos."

A motivação é que os interessados numa linha de produto têm requisitos diversos, expressos nas *features* que querem. Em vez de desenvolver cada produto do zero, o software variável é ajustado a cada caso.

### 3.1.1 Tempo de ligação — *binding time* (p. 48–49)

**Ideia.** Derivar um produto é tomar decisões: quais *features* entram e quais ficam de fora. "Ligar" (*bind*) uma decisão é fixá-la. Cada técnica permite fixar as decisões em um momento diferente.

**Os três momentos considerados pelo livro.** Uma nota de rodapé registra que há autores que distinguem mais momentos (pré-processamento, *link*, *weaving*), mas os três abaixo bastam para o livro.

| Momento | Também chamado | Quando se decide | Exemplos citados no livro |
|---|---|---|---|
| Compilação | ligação antecipada, estática; variabilidade estática | Na compilação ou antes dela. O código das *features* não selecionadas **nem chega a ser compilado** no produto. | Pré-processadores (5.3); programação orientada a *features* (6.1) |
| Carga | — | Depois da compilação, quando o programa inicia (por exemplo, por parâmetros de linha de comando ou arquivos de configuração). Todas as variações estão no binário. | Variabilidade por parâmetros (4.1) |
| Execução | ligação tardia, dinâmica; variabilidade dinâmica | Durante a execução, e a decisão pode mudar com o programa rodando (reconfiguração dinâmica). | Parâmetros (4.1); programação orientada a contexto (6.6.3) |

Alguns mecanismos suportam mais de um momento.

> **Definição 3.2 (p. 49).** "Compile-time variability is decided before or at compile time. Load-time variability is decided after compilation when the program is started. With run-time variability, decisions can be made and changed during program execution."
>
> *Tradução livre:* "A variabilidade em tempo de compilação é decidida antes ou durante a compilação. A variabilidade em tempo de carga é decidida após a compilação, quando o programa é iniciado. Com a variabilidade em tempo de execução, as decisões podem ser tomadas e alteradas durante a execução do programa."

**Vantagens e desvantagens de cada momento (p. 49):**

- **Compilação.**
  - Vantagens: abre espaço para otimizações e permite retirar do produto o código desnecessário, com ganho de tamanho do binário, memória e tempo de execução.
  - Desvantagem: depois de gerado e implantado, o produto **deixa de ser variável**.
- **Carga.**
  - Vantagem: é mais flexível. O usuário altera parâmetros e reinicia o mesmo binário, sem recompilar.
- **Execução.**
  - Vantagem: o produto reage a estímulos e adapta o comportamento. Em termos de SPL, deriva "produtos virtuais".
- **Carga e execução, em conjunto.**
  - Custo: memória e desempenho, porque todas as variações estão no mesmo binário e as condições de consistência são verificadas em execução.
  - Risco: incluir funcionalidade desnecessária no produto entregue pode ser visto como **potencial ameaça à segurança**. O livro aprofunda o ponto na seção 4.1.1, p. 66.

> 🔗 **Relação com o TCC:**
> - O `@IfBuildProperty` faz ligação em tempo de compilação.
> - O custo descrito ("o produto deixa de ser variável") é o da PP4.
> - O risco de segurança da ligação tardia sustenta o contraste com as *feature flags* (seções 2.5, 4.5 e 7.6 do TCC).

### 3.1.2 Tecnologia: baseada em linguagem × baseada em ferramenta (p. 49–50)

**A distinção.** É entre técnicas que usam mecanismos da própria linguagem de programação e técnicas que usam ferramentas externas, que operam sobre os artefatos para derivar o produto.
- Exemplo clássico baseado em linguagem: parâmetros em execução (4.1).
- Exemplo clássico baseado em ferramenta: pré-processador (5.3).

**A fronteira não é nítida.** Há abordagens de componentes de um tipo e do outro, mas a divisão grosseira basta para organizar a discussão.

> **Definição 3.3 (p. 50).** "A language-based approach uses the mechanisms provided by a host programming language to implement features and to derive products. A tool-based approach uses one or more external tools to implement or represent features in code and to control the product-derivation process."
>
> *Tradução livre:* "Uma abordagem baseada em linguagem usa os mecanismos oferecidos por uma linguagem de programação hospedeira para implementar *features* e derivar produtos. Uma abordagem baseada em ferramenta usa uma ou mais ferramentas externas para implementar ou representar *features* no código e para controlar o processo de derivação do produto."

**Prós e contras:**
- **Baseada em linguagem.**
  - A favor: implementação e gestão da variabilidade ficam juntas no código-fonte, o que facilita o raciocínio sobre o todo.
  - Contra: conforme a técnica, as fronteiras das *features* se dissolvem no código. É o caso dos parâmetros em execução.
- **Baseada em ferramenta.**
  - A favor: separa a implementação das *features* da gestão da variabilidade e da derivação, o que pode simplificar o código.
  - Contra: obriga a localizar e relacionar vários artefatos em lugares diferentes.

> 🔗 **Relação com o TCC.** A anotação `@IfBuildProperty` está no código Kotlin, mas quem resolve a variabilidade é o *build* do Quarkus (a fase de *augmentation*), a partir de propriedades passadas de fora (`--build-arg`). É um caso misto. Se o TCC citar esta dimensão, convém dizer isso explicitamente, e não forçar um dos dois rótulos.

### 3.1.3 Representação: anotação × composição (p. 50–52)

**Contexto.** Um objetivo central da engenharia de SPL orientada a *features* é derivar o produto **automaticamente** a partir do código variável e da seleção de *features*. O livro foca em duas famílias amplamente usadas na prática, que diferem em como representam a variabilidade e em como geram o produto.

**Anotação.**
- O código de todas as *features* fica numa **única base de código**, e anotações marcam a que *feature* (ou combinação de *features*) cada trecho pertence. Na prática, uma anotação funciona como um mapeamento do elemento de programa para a sua *feature*.
- Exemplo padrão: o pré-processador C, em que diretivas incluem ou excluem trechos conforme constantes definidas.
- Os autores também tratam como anotação o uso de parâmetros em execução: o `if` que testa a seleção de *features* faz o papel da anotação.

> **Definição 3.4 (p. 51).** "Annotation-based approaches annotate a common code base, such that code that belongs to a certain feature is marked accordingly. During product derivation, all code that belongs to deselected features or invalid feature combinations is removed (at compile time) or ignored (at run time) to form the final product."
>
> *Tradução livre:* "As abordagens baseadas em anotação anotam uma base de código comum, de modo que o código pertencente a uma determinada *feature* seja marcado como tal. Durante a derivação do produto, todo o código que pertence a *features* não selecionadas ou a combinações inválidas de *features* é removido (em tempo de compilação) ou ignorado (em tempo de execução) para formar o produto final."

- **Avaliação das abordagens por anotação:**
  - São muito usadas por serem simples e já suportadas nativamente por muitos ambientes.
  - Pré-processadores e parâmetros são criticados por complexidade, falta de modularidade e pior legibilidade (discutido em 4.1 e 5.3).
  - Parte desses problemas (código espalhado, propensão a erro) se resolve com ferramentas relativamente simples (cap. 7).

**Composição.**
- O código de cada *feature* (ou combinação) fica em um arquivo, contêiner ou módulo próprio.
- Exemplo clássico: um *framework* estendido por *plug-ins*, idealmente um por *feature*, em que produtos diferentes resultam de conjuntos diferentes de *plug-ins* (4.3.3).
- Há ainda abordagens mais avançadas, como a programação orientada a *features* e a aspectos (cap. 6).

> **Definição 3.5 (p. 51).** "Composition-based approaches implement features in the form of composable units, ideally one unit per feature. During product derivation, all units of all selected features and valid feature combinations are composed to form the final product."
>
> *Tradução livre:* "As abordagens baseadas em composição implementam *features* na forma de unidades combináveis, idealmente uma unidade por *feature*. Durante a derivação do produto, todas as unidades de todas as *features* selecionadas e das combinações válidas são compostas para formar o produto final."

- **O desafio da composição** é manter o mapeamento entre *features* (espaço do problema) e unidades de composição (espaço da solução) simples, de preferência um para um. Assim, o gerador só precisa incluir a unidade correspondente a cada *feature* selecionada.
- **As duas famílias podem ser combinadas.** Por exemplo, componentes cujo código interno também é anotado. Na derivação, escolhem-se os componentes e, dentro deles, remove-se o código anotado das *features* não selecionadas.
- **Outras leituras da diferença (p. 51–52):**
  - A anotação dá **variabilidade negativa**: o código é removido sob demanda. A composição dá **variabilidade positiva**: unidades são acrescentadas sob demanda.
  - A anotação separa interesses **virtualmente**; a composição, **fisicamente** (tema do cap. 7).
- As duas são casos particulares de um conjunto maior de técnicas de geração e transformação de programas.

> 🖼️ **[Figura 3.1, p. 52 — não reproduzível na tradução]** Dois diagramas lado a lado:
> - (a) **anotação**: uma base de código única com trechos marcados por *feature*, da qual se derivam produtos removendo os trechos não selecionados;
> - (b) **composição**: unidades separadas por *feature*, combinadas para formar cada produto.
>
> Em ambos, a configuração de *features* (espaço do problema) leva ao produto (espaço da solução).

> 🔗 **Relação com o TCC:**
> - O protótipo é **anotação**: os *controllers* marcados pertencem à base comum.
> - É também **variabilidade negativa**: a variante "remove" o modo não selecionado.
> - **Ressalva importante.** A Def. 3.4 fala em código *removido*. No Quarkus, porém, a classe compilada permanece no `app/*.jar`; o que deixa de ser gerado é o *bean*, o invocador e a rota (seção 7.2 do TCC). Ao citar a definição, deixar isso explícito.

---

## 3.2 Critérios de qualidade (p. 52–60)

As técnicas de implementação têm forças e fraquezas diferentes. Para compará-las, o livro propõe **seis critérios**:
1. baixo esforço de pré-planejamento;
2. rastreabilidade de *features*;
3. separação de interesses;
4. ocultação de informação;
5. granularidade;
6. uniformidade.

Alguns critérios conflitam entre si, de modo que nenhuma técnica atende a todos. Cada uma faz as suas escolhas.

### 3.2.1 Esforço de pré-planejamento (p. 52–54)

- Toda engenharia de SPL exige algum pré-planejamento: quais *features* serão pedidas, quais vão interagir, onde uma *feature* vai estender outra. Exige mais na adoção proativa e menos na reativa, mas sempre exige.
- O objetivo do pré-planejamento é antecipar mudanças e fontes de variabilidade e reuso. As técnicas facilitam ou dificultam isso de formas diferentes.
- **O exemplo das formas geométricas (p. 53–54).** Dois produtos A e B do mesmo domínio são representados por formas. Uma decomposição *ad hoc* de A e B em partes não permite montar um terceiro produto C, natural no domínio, a partir das partes existentes. Já uma decomposição planejada permite reconstruir A, B e C rapidamente com partes reutilizáveis.

> 🖼️ **[Figuras 3.2 a 3.5, p. 53–54 — não reproduzíveis na tradução]** Ilustram o exemplo acima:
> - **3.2:** o desenho de dois produtos (A e B) como formas;
> - **3.3:** uma decomposição *ad hoc* dessas formas;
> - **3.4:** o contorno de um terceiro produto (C);
> - **3.5:** decomposições reutilizáveis que permitem montar A, B e C.

- **O processo de SPL é, em si, pré-planejamento:** analisa-se o domínio e antecipam-se requisitos em forma de *features*. Mas nem tudo pode ser antecipado, sobretudo nas adoções reativa e extrativa (seção 2.4).
- **O ideal** é uma técnica que exija pouco pré-planejamento e aceite *features* não previstas com baixo esforço.
  - *Frameworks* exigem prever onde haverá extensão.
  - Programação orientada a *features* e a aspectos buscam reduzir essa necessidade. Por exemplo, um aspecto pode acrescentar membros a uma classe sem alterá-la.
  - O caso extremo, a "obliviousness", é discutido em 6.2.4.

### 3.2.2 Rastreabilidade de *features* (p. 54–55)

Toda a orientação a *features* depende de manter o mapeamento entre o espaço do problema e o espaço da solução.

> **Definição 3.6 (p. 54).** "Feature traceability is the ability to trace a feature from the problem space (for example, the feature model) to the solution space (that is, its manifestation in design and code artifacts)."
>
> *Tradução livre:* "Rastreabilidade de *features* é a capacidade de rastrear uma *feature* do espaço do problema (por exemplo, o modelo de *features*) até o espaço da solução (isto é, sua manifestação nos artefatos de projeto e de código)."

- **Diretivas de pré-processador** são fáceis de reconhecer, por ferramentas e por pessoas.
- **Parâmetros em execução** são difíceis de rastrear: confundem-se com outras variáveis e podem ser reatribuídos durante a execução.
- **Na composição**, a rastreabilidade é trivial enquanto houver uma unidade por *feature*, e se perde quando uma *feature* se espalha por vários componentes.

> 🔗 **Relação com o TCC.** No protótipo, cada modo corresponde a um *controller* anotado e a uma propriedade de *build*. A rastreabilidade é direta: *feature* → propriedade → classe. Esse é um argumento a favor da anotação no nível de classe em relação ao `if` espalhado de uma *feature flag*.

### 3.2.3 Separação de interesses (p. 55–56)

- Separar interesses é um princípio fundamental de projeto (Parnas; Dijkstra). Em SPL, as *features* são os interesses principais.
- Separar *features* em artefatos próprios torna explícita a relação entre *feature* e código e facilita manutenção e evolução. O código relacionado fica junto, o que é a **coesão**.
- Procedimentos, módulos e classes separam interesses em estruturas hierárquicas ou de blocos. Nos anos 1990, percebeu-se que certos interesses, os **transversais**, resistem a esse tipo de separação.

> **Definição 3.7 (p. 55).** "Crosscutting is a structural relationship between the representations of two concerns. It is an alternative to hierarchical and block structure."
>
> *Tradução livre:* "Transversalidade (*crosscutting*) é uma relação estrutural entre as representações de dois interesses. É uma alternativa à estrutura hierárquica e de blocos."

- **A "tirania da decomposição dominante".** Com estruturas hierárquicas, o programa só pode ser decomposto em uma dimensão por vez. Os interesses que não se alinham a ela acabam espalhados e entrelaçados. Isso não indica necessariamente um projeto ruim: é inevitável.

> **Definição 3.8 (p. 56).** "Code scattering refers to a concern representation that is scattered across representations of multiple other concerns."
>
> *Tradução livre:* "Espalhamento de código refere-se à representação de um interesse que se encontra espalhada pelas representações de vários outros interesses."

> **Definição 3.9 (p. 56).** "Code tangling refers to the intermingled representation of several concerns within a module."
>
> *Tradução livre:* "Entrelaçamento de código refere-se à representação misturada de vários interesses dentro de um módulo."

> 🖼️ **[Figura 3.6, p. 57 — não reproduzível na tradução]** É um "instantâneo" do código do subsistema de sessões do servidor Apache Tomcat, com várias classes lado a lado em letra minúscula. O código do interesse *expiração de sessão* aparece destacado em vermelho, espalhado por todas as classes e misturado com código de outros interesses. A figura serve de ilustração visual de espalhamento e entrelaçamento.

- Como *features* frequentemente (não sempre) são interesses transversais, separar inclusive o código transversal em implementações coesas é um critério de qualidade importante.

### 3.2.4 Ocultação de informação (p. 56–58)

**O conceito.**
- Separar interesses pode não bastar, se entender o todo exigir conhecer os detalhes de todas as partes.
- A **ocultação de informação** divide cada módulo em uma parte interna e uma parte externa:
  - a interna é o "segredo" do módulo, encapsulado, e costuma ser a maior parte do código;
  - a externa é a **interface**, um contrato com o restante do sistema.
- Idealmente, cada módulo se entende sozinho, olhando seu segredo e as interfaces que importa. Uma nota de rodapé observa que há vários tipos de sistema de módulos, com formas diferentes de importação.

**As consequências (p. 58):**
- dividir o trabalho entre equipes independentes;
- entender um módulo sem ler a base inteira;
- alterar a implementação interna livremente, desde que o comportamento da interface seja preservado;
- compor módulos raciocinando só sobre as interfaces;
- muitas vezes, compilar e implantar módulos separadamente.

O desafio é ter interfaces pequenas e claras e, ao mesmo tempo, esconder o máximo possível.

**Em SPL.**
- O ideal é também esconder o interior de cada *feature* e explicitar a comunicação entre *features* em interfaces.
- *Frameworks* e componentes (4.3 e 4.4) têm longa tradição nisso.
- Abordagens mais recentes, como a programação orientada a *features* e a separação virtual de interesses (6.1 e 7.4), separam interesses, mas nem sempre oferecem mecanismos de interface adequados.

**As duas coisas juntas.**
- Separação sem ocultação ajuda na navegação, mas não dá garantias.
- Ocultação sem uma boa escolha do que esconder pode não separar o que interessa.
- A "verdadeira modularidade" exige as duas e, em geral, bastante pré-planejamento.

**Nota terminológica.** O livro **evita o termo "modularidade"**, por ser sobrecarregado. Usa "separação de interesses" ou "coesão" para a separação física e "ocultação de informação" para interfaces. "Módulo" aparece no sentido amplo de arquivo ou contêiner.

> 🔗 **Relação com o TCC.** A arquitetura hexagonal do protótipo é a ocultação de informação aplicada às camadas: domínio e serviços atrás de portas, com os *controllers* (adaptadores) como única parte variável. Isso permite que a variabilidade fique só nos adaptadores, sem tocar o núcleo (seção 2.6 do TCC, MARTIN, 2017).

### 3.2.5 Granularidade (p. 59)

- *Features* raramente funcionam isoladas. Para interagir, uma *feature* induz mudanças no programa: intercepta um evento, registra um *callback*, injeta instruções numa função.
- **Granularidade** é o nível da estrutura hierárquica do artefato em que a mudança acontece. Mudanças no topo da hierarquia são **grossas**; nos níveis inferiores, **finas**.
- **Exemplo 3.1 (p. 59), em resumo.** Acrescentar um arquivo ou uma classe Java é uma mudança grossa. Acrescentar um membro a uma classe é média. Acrescentar uma instrução a um método é fina.
- Na prática, as *features* aparecem em **todos** os níveis de granularidade.
  - Expressar mudanças finas com técnicas que só oferecem extensão grossa é trabalhoso. Por exemplo, substituir arquivos inteiros por variantes locais gera replicação de código (caso discutido em sistemas de *build*, 5.2).
  - Se os pontos finos forem conhecidos de antemão, pode-se refatorá-los para estruturas mais grossas, como extrair as instruções para um método.
- **Regra prática:** a anotação suporta mudanças mais finas do que a composição. As anotações podem ser aplicadas quase em qualquer lugar da base comum, enquanto a composição depende de interfaces, tipicamente de classes e métodos.

> 🔗 **Relação com o TCC.** O protótipo usa a granularidade **mais grossa**: a anotação está na classe inteira do *controller*. Isso combina com a nota da p. 116 do livro sobre anotações "disciplinadas", alinhadas à estrutura sintática, e explica por que o efeito no artefato é tudo ou nada por *controller*.

### 3.2.6 Uniformidade (p. 59–60)

- Muitos conceitos do livro independem da linguagem. Java é usado por razão didática.
- A ideia de anotar uma base de código comum, ou de compor unidades, vale para C, C#, Java e qualquer linguagem textual. Herança, composição de componentes e *weaving* de aspectos também não são exclusivos de uma linguagem.
- A pesquisa em SPL passou a buscar ferramentas e teorias independentes de linguagem. Batory formulou o **princípio da uniformidade**:

> **Definição 3.11 (p. 60).** "Principle of Uniformity: Features are implemented by a diverse selection of software artifacts and any kind of software artifact can be subject of subsequent changes and extensions. Conceptually, all artifacts (annotated or composed) should be encoded and synthesized in a similar manner."
>
> *Tradução livre:* "Princípio da Uniformidade: as *features* são implementadas por uma seleção diversa de artefatos de software, e qualquer tipo de artefato pode ser objeto de mudanças e extensões posteriores. Conceitualmente, todos os artefatos (anotados ou compostos) deveriam ser codificados e sintetizados de maneira semelhante."

- **A motivação** não é que um mecanismo exista em várias linguagens. É que os sistemas reais têm muitos tipos de artefato: código em várias linguagens, requisitos, documentação, arquitetura, modelos, **arquivos de configuração**. Uma abordagem geral, aplicável a todos eles, escala melhor do que soluções *ad hoc* por linguagem.
- **Conclusão do critério:** idealmente, uma técnica deve se aplicar a uma ampla variedade de artefatos, de código e de não código.

> 🔗 **Relação com o TCC.** No protótipo, a mesma decisão de *build* alcança mais de um tipo de artefato: o código (*bean* e rota), o contrato OpenAPI publicado e a interface Swagger, que são derivados da mesma variabilidade (seção 7.2.4 do TCC). Isso é uniformidade, ainda que parcial.

> ⚠️ A extração do PDF não mostrou uma "Definição 3.10". A numeração salta de 3.9 (p. 56) para 3.11 (p. 60). Conferir no impresso se a 3.10 existe, provavelmente na seção 3.2.4, ou se há um salto na própria numeração do livro.

---

## 3.3 Estrutura dos capítulos seguintes (p. 60–61)

- Os capítulos 4 a 7 apresentam e comparam estratégias de implementação, classificadas na Tabela 3.1 segundo as três dimensões da seção 3.1. Alguns mecanismos aparecem em mais de uma categoria (por exemplo, componentes estáticos e dinâmicos; de linguagem e de ferramenta).
- O livro também separa abordagens **clássicas**, já difundidas na prática, de **avançadas**, ainda sobretudo acadêmicas. Os capítulos se organizam por essa distinção e pela divisão linguagem × ferramenta.

> 🖼️ **[Tabela 3.1, p. 61 — conferir no impresso]** "Classification of implementation approaches discussed in this book". As linhas são os mecanismos:
> - parâmetros;
> - padrões de projeto;
> - *frameworks*;
> - componentes;
> - controle de versão;
> - sistemas de *build*;
> - pré-processadores;
> - programação orientada a *features*;
> - programação orientada a aspectos;
> - separação virtual de interesses.
>
> As colunas são as três dimensões: tempo de ligação (compilação; carga e depois), tecnologia (linguagem; ferramenta) e representação (anotação; composição).
>
> **As marcações (✓) não sobreviveram à extração do PDF.** O que o próprio texto do capítulo permite afirmar:
> - **parâmetros:** carga/execução, linguagem, anotação (p. 49–51);
> - **pré-processadores:** compilação, ferramenta, anotação (p. 49–51);
> - ***frameworks* e componentes:** composição (p. 51);
> - **programação orientada a *features* e a aspectos:** composição (p. 51); a orientada a *features*, em compilação (p. 49).
>
> **Notas de rodapé da tabela:**
> 1. padrões de projeto e *frameworks* suportam compilação e execução, mas mesmo em compilação têm custo em execução;
> 2. programação orientada a *features* e a aspectos admitem ligação dinâmica, com ferramentas ainda experimentais;
> 3. em sistemas de *build*, pode-se anotar um arquivo inteiro para atribuí-lo a uma *feature*.

## 3.4 Leituras complementares (p. 61–62)

Os autores indicam:
- **Conceitos e classificação:** Czarnecki e Eisenecker (2000) como base de muitos dos conceitos do capítulo.
- **Transversalidade:** a literatura de programação orientada a aspectos.
- **Anotação × composição:** a distinção foi proposta primeiro por Kästner e Apel (2008, 2009), que também discutem o papel da granularidade.
- **Ocultação de informação:** os trabalhos de Parnas (1972, 1976, 1979).
- **Modularidade:** uma discussão própria dos autores (Kästner et al., 2011).
- **Uniformidade:** o princípio de Batory et al. (2004).
- **Classificações alternativas:** a taxonomia de técnicas de realização de variabilidade de Svahnberg et al. (2005) e outros levantamentos.

## Exercícios (p. 62–63)

O capítulo termina com 14 exercícios. Em resumo, eles pedem:
- comparar técnicas pelas três dimensões;
- escolher o tempo de ligação adequado para cenários concretos (localização de interface, algoritmos de ordenação, suporte a multiprocessador, formatos de arquivo em *firmware*);
- discutir a combinação de anotação e composição e o papel da granularidade;
- classificar como o Linux, o Eclipse e o Apache implementam variabilidade;
- aplicar os critérios a uma aplicação real (o LlamaChat).

---

## Síntese para o TCC

| Conceito do cap. 3 | Onde se aplica no TCC |
|---|---|
| Variabilidade (Def. 3.1) | 2.3: a metamorfose como derivação de produtos |
| Tempo de ligação (Def. 3.2) e seus custos | 2.3 e 2.5; custo na PP4 (7.5); contraste na 7.6 |
| Funcionalidade desnecessária = potencial ameaça (p. 49) | 2.5 e 4.5: segurança por ausência |
| Linguagem × ferramenta (Def. 3.3) | Ainda não usado. O `@IfBuildProperty` é um caso misto (anotação no código, resolução pelo *build*) |
| Anotação × composição (Def. 3.4 e 3.5); variabilidade negativa | 2.3 e 3.4. **Ressalva:** a classe permanece no artefato (7.2) |
| Rastreabilidade (Def. 3.6) | Possível argumento a favor da anotação por classe frente ao `if` da *feature flag* (3.2 / 7.6) |
| Ocultação de informação | 2.6: arquitetura hexagonal; variabilidade só nos adaptadores |
| Granularidade | O protótipo usa a mais grossa (classe inteira) |
| Uniformidade (Def. 3.11) | A mesma decisão alcança código, OpenAPI e Swagger (7.2.4) |
