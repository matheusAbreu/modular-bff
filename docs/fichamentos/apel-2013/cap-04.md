# Cap. 4 — Mecanismos clássicos de variabilidade baseados em linguagem (p. 65–97)

> Fichamento de APEL et al. (2013). Resumo com palavras próprias; só as definições formais estão no original, com tradução livre. As páginas são as impressas.

## Visão geral (p. 65)

- Há muitas formas de tornar o código variável, e várias são anteriores às SPL. Até um simples `if` já é uma escolha entre caminhos de execução.
- Para evitar código poluído de condicionais, melhorar a rastreabilidade, permitir extensões sem alterar o original e obter variabilidade em compilação ou carga, desenvolveram-se padrões de programação.
- O capítulo trata de **quatro técnicas** que funcionam em linguagens comuns, sem ferramenta externa:
  1. parâmetros (4.1);
  2. padrões de projeto (4.2);
  3. *frameworks* (4.3);
  4. componentes e serviços (4.4).
- As técnicas baseadas em ferramentas (controle de versão, sistemas de *build*, pré-processadores) ficam para o capítulo 5.
- Os autores registram que a maioria das SPL industriais é implementada com um ou mais desses mecanismos clássicos. Cada um é avaliado pelos critérios do capítulo 3.

O exemplo que atravessa o capítulo é uma **biblioteca de grafos** com *features* opcionais, como *Weighted* (arestas com peso) e *Colored* (nós e arestas coloridos).

---

## 4.1 Parâmetros (p. 66–69)

**A técnica.**
- Condicionais (`if`, `switch`) alteram o fluxo do programa em execução, conforme parâmetros de configuração.
- **Origem dos parâmetros:** linha de comando, arquivos de configuração lidos na inicialização, diálogos de preferências (com efeito imediato ou exigindo reinício) ou valores fixados no código, caso em que mudar exige recompilar.
- **Onde ficam:** em variáveis globais ou passados como argumentos de método. As globais são cômodas, mas desestimulam interfaces explícitas. Os argumentos podem acabar propagados por todo o código.
- **Numa abordagem orientada a *features*:** espera-se **um parâmetro booleano por *feature***. Com uma convenção de nomes, a relação *feature* → parâmetro fica rastreável.

**Exemplo 4.1 (p. 66).** Uma classe de configuração guarda dois parâmetros globais, *Weighted* e *Colored*. As classes do grafo testam esses parâmetros em `if` para ativar o comportamento de cada *feature*.

> 🖼️ **[Figura 4.1, p. 67 — não reproduzível]** É o código Java da biblioteca de grafos com variabilidade por parâmetros: uma classe de configuração com dois campos estáticos booleanos e as classes `Graph`, `Node` e `Edge` com condicionais que consultam esses campos.

### 4.1.1 Discussão (p. 66–69)

A técnica é simples e, por isso, muito usada. A decisão acontece em execução, quando os condicionais rodam, de modo que ela herda os prós e contras da ligação tardia (seção 3.1.1):

- **Tudo vai para todos os produtos (p. 66–67).** Mesmo que se saiba de antemão que uma *feature* nunca será usada, o código dela é entregue. Os autores apontam quatro consequências:
  1. a funcionalidade desativada continua presente no produto;
  2. os testes em execução custam processamento e memória (no exemplo, a classe `Color` segue no produto, cada `print` avalia um `if` a mais e cada nó e aresta carrega um campo extra);
  3. não há como impedir que alguém instancie ou invoque o código desativado, a não ser lançando erros em execução;
  4. código não usado é **alvo potencial desnecessário de ataques** (o exemplo dado é *buffer overflow*).
- **Mudar a seleção sem parar o programa é possível, mas delicado (p. 67).** O código de uma *feature* pode depender de inicializações ou invariantes. No exemplo, ativar *Weighted* em execução poderia gerar *null pointer* em arestas criadas antes. Muitas vezes é mais simples exigir reinício.
- **Vantagem própria dos parâmetros passados como argumento (p. 67–68).** Partes diferentes do programa podem usar configurações diferentes, como grafos coloridos e não coloridos convivendo.
- **Especialização estática (p. 68).**
  - Se o parâmetro for constante conhecida na compilação, o compilador pode eliminar o código morto do `if`.
  - Remover métodos ou classes inteiros, porém, é raro nos compiladores, e a análise falha quando o parâmetro passa por várias chamadas ou é reatribuído.
  - Técnicas como avaliação parcial existem, mas estão longe do uso corrente.
  - Conclusão dos autores: **parâmetros não servem bem para ligação em compilação**. A forma adequada de `if` em compilação aparece com os pré-processadores (5.3).
- **Dependências entre *features* (p. 68).** Precisam ser verificadas na inicialização ou a cada mudança. Na experiência dos autores, raramente o são de forma sistemática, e a técnica não garante estaticamente que combinações incompatíveis não sejam ativadas.
- **Classificação (p. 68).**
  - É uma forma de **anotação**, de granularidade **fina** (instrução, até expressão).
  - Linguagens raramente oferecem condicionais no nível de método ou classe, porque isso só faria sentido em compilação.
  - As extensões são **invasivas**, mas pedem pouco pré-planejamento.
  - Funciona em quase toda linguagem, porém **não** se aplica a artefatos que não são código (documentos, gramáticas).
- **Qualidade do código (p. 68–69).**
  - Parâmetros globais violam a separação de interesses e podem furar interfaces.
  - Propagar argumentos gera código repetitivo e métodos com muitos parâmetros. A recomendação usual é um objeto de configuração.
  - O código da variabilidade é **transversal**: fica espalhado (variáveis, argumentos, `if`) e misturado com o restante.
  - Encapsular a *feature* atrás de uma interface é difícil, e **rastrear** todos os trechos de uma *feature* exige seguir o fluxo de controle e as atribuições.
  - O resultado tende a ser implementações *ad hoc*, difíceis de analisar, manter e depurar.

**Quadro de avaliação dos autores (p. 69), em síntese:**
- **A favor:**
  - simples e conhecido;
  - flexível e de granularidade fina;
  - suporte nativo da linguagem;
  - várias configurações no mesmo programa.
- **Contra:**
  - todo o código é entregue, sem configuração em compilação;
  - uso frequentemente indisciplinado;
  - código repetitivo ou pouco modular;
  - conhecimento de configuração espalhado;
  - separação e ocultação dependem da disciplina de quem programa;
  - extensões invasivas;
  - nada para artefatos que não são código;
  - rastreamento difícil e, por isso, análise estática difícil.

> 🔗 **Relação com o TCC.** Esta é a seção mais diretamente ligada ao trabalho. As *feature flags* em execução são a técnica de parâmetros.
> - As consequências listadas na p. 66–67 (código entregue mesmo desativado, invocável e alvo de ataque) sustentam o contraste da seção 7.6 e a segurança por ausência (2.5 e 4.5). Já estão citadas no Eixo 4 de `CITACOES_ABNT.md`.
> - A observação sobre a remoção de código morto (p. 68) ajuda a explicar por que uma *flag* em execução não equivale a uma decisão em compilação, mesmo com otimização do compilador.
> - A dificuldade de rastrear *features* por parâmetros é um argumento ainda não usado no TCC.

---

## 4.2 Padrões de projeto (p. 69–79)

**Ponto de partida.** O problema dos parâmetros é a variabilidade espalhada e fixada no código. Vários padrões do catálogo de Gamma et al. ajudam a separá-la e desacoplá-la.

> **Definição 4.1 (p. 70).** "Design patterns are descriptions of collaborating objects and classes that are customized to solve a general design problem in a particular context." (Gamma et al., 1995)
>
> *Tradução livre:* "Padrões de projeto são descrições de objetos e classes que colaboram, ajustadas para resolver um problema geral de projeto em um contexto particular."

Os quatro padrões apresentados, cada um aplicado ao exemplo do grafo:

| Padrão | Ideia aplicada à variabilidade | Figuras |
|---|---|---|
| ***Observer*** (4.2.1, p. 70–71) | O objeto observado notifica observadores registrados. Cada *feature* entra como um observador, sem alterar o observado. | 🖼️ 4.2 (estrutura do padrão); 4.3 (impressão do grafo estendida por observador) |
| ***Template method*** (4.2.2, p. 71–73) | Uma classe base define o esqueleto do algoritmo e deixa "ganchos" para subclasses preencherem. A variante é uma subclasse. | 🖼️ 4.4 (grafo com e sem peso por subclasses) |
| ***Strategy*** (4.2.3, p. 73–75) | O comportamento variável é extraído para um objeto intercambiável, escolhido por configuração. | 🖼️ 4.5 (estrutura); 4.6 (grafo com e sem peso por estratégias) |
| ***Decorator*** (4.2.4, p. 75–77) | Objetos "embrulham" outros e acrescentam comportamento. Vários decoradores podem ser combinados. | 🖼️ 4.7 (estrutura); 4.8 (decoradores para *Weighted* e *Colored*) |

> 🖼️ **[Figuras 4.2 a 4.8 — não reproduzíveis]** Diagramas de classes UML dos quatro padrões e trechos de código Java da biblioteca de grafos implementando *Weighted* e *Colored* com cada um.

### 4.2.5 Discussão (p. 77–79)

- **Limite da herança.** Combinar várias *features* opcionais por subclasses leva a explosão de classes ou a extensões rígidas, porque cada combinação pede uma subclasse própria. Os padrões, sobretudo o *decorator*, contornam parte disso.

> 🖼️ **[Figura 4.9, p. 78 — não reproduzível]** Mostra quatro tentativas de combinar as extensões de `Edge` (com peso, colorida, não dirigida) por herança. Evidencia a falta de flexibilidade composicional: a combinação "com peso e colorida" exige uma classe a mais, e assim por diante.

- **Em relação aos parâmetros, os padrões:**
  - oferecem diretrizes conhecidas e facilitam a comunicação entre desenvolvedores;
  - **separam** o código da *feature* do código base, possivelmente com interfaces claras;
  - permitem **rastrear** cada *feature* até seu observador, subclasse, estratégia ou decorador;
  - permitem estender **sem alterar** o código base, desde que os pontos de extensão tenham sido **planejados**.
- **O que não muda:**
  - são independentes de linguagem, mas não valem para artefatos que não são código;
  - adicionam código estrutural, com possível impacto em tamanho e desempenho;
  - todos os padrões apresentados ligam a variabilidade **em execução**. Algumas linguagens permitem codificá-los para especialização em compilação (por exemplo, com programação genérica em C++).

**Quadro dos autores (p. 79), em síntese.** Os padrões herdam o perfil dos parâmetros. Ganham em disciplina, separação, rastreabilidade e extensão não invasiva. Perdem em código estrutural extra e na necessidade de planejar os pontos de extensão.

> 🔗 **Relação com o TCC.** O protótipo não usa padrões para a variabilidade dos modos. A arquitetura hexagonal, com portas e adaptadores, é um arranjo que **facilita** a variabilidade, porque concentra o que varia nos adaptadores. A decisão, no entanto, é tomada pela anotação de *build*, e não por um padrão em execução.

---

## 4.3 *Frameworks* (p. 79–89)

> **Definição 4.2 (p. 80).** "A framework is a set of classes that embodies an abstract design for solutions to a family of related problems, and supports reuse at a larger granularity than classes. A framework is open for extension at explicit hot spots." (Johnson e Foote, 1988)
>
> *Tradução livre:* "Um *framework* é um conjunto de classes que incorpora um projeto abstrato para soluções de uma família de problemas relacionados e que permite reuso em granularidade maior que a de classes. Um *framework* é aberto a extensões em pontos explícitos (*hot spots*)."

- **Caixa-branca (4.3.1, p. 80–81).** A extensão se faz por herança, com o *template method* sobrescrevendo ganchos. Quem estende precisa conhecer o interior do *framework*.
- **Caixa-preta (4.3.2, p. 81–82).** A extensão se faz por interfaces e composição de objetos, com *strategy* e *observer*. Quem estende só conhece a interface.

> **Definição 4.3 (p. 81).** "A plug-in extends hot spots of a black-box framework with custom behavior. A plug-in can be separately compiled and deployed."
>
> *Tradução livre:* "Um *plug-in* estende os *hot spots* de um *framework* caixa-preta com comportamento próprio. Um *plug-in* pode ser compilado e implantado separadamente."

- **Exemplo de implementação (4.3.3, p. 82–85).** Três aplicações de um botão só (calculadora, *ping*, carregador de arquivo) compartilham a estrutura. O *framework* extrai o que é comum. Primeiro, cada aplicação é feita como extensão caixa-branca; depois, como *plug-in* de um *framework* caixa-preta; por fim, com vários *plug-ins* de tipos diferentes.

> 🖼️ **[Figuras 4.10 a 4.14, p. 82–85 — não reproduzíveis]**
> - **4.10:** telas das três aplicações;
> - **4.11:** código da calculadora, com o que é específico destacado;
> - **4.12:** *framework* caixa-branca com duas extensões;
> - **4.13:** *framework* caixa-preta com um *plug-in*;
> - **4.14:** *framework* com vários *plug-ins* de tipos diferentes.

- **Carregamento de *plug-ins* (4.3.4, p. 85–86).** No exemplo caixa-preta, o *plug-in* desejado é passado ao *framework* por uma classe de inicialização separada. Na prática, são comuns carregadores de *plug-ins* dedicados, que os localizam e instanciam na inicialização, por exemplo com a API de reflexão do Java (Fig. 4.15). É o que dá a ligação em tempo de **carga**.

> 🖼️ **[Figura 4.15, p. 86 — não reproduzível]** Código de um carregador simples de *plug-ins* em Java, que usa reflexão.

### 4.3.5 Discussão (p. 86–89)

**Pontos fortes.**
- *Frameworks* caixa-preta são uma forma adequada de implementar SPL. É uma técnica de **composição**, com ligação típica em **carga**; em princípio, também em execução.
- Funcionam em quase toda linguagem, mas não em artefatos que não são código.
- Com **um *plug-in* por *feature***:
  - a rastreabilidade é direta;
  - *features* alternativas e opcionais são tratadas com disciplina e podem ser combinadas;
  - a derivação pode ser **automatizada** de ponta a ponta;
  - *plug-ins* não selecionados **nem precisam ser entregues**, o que reduz o tamanho do produto.
- *Frameworks* caixa-branca servem para alternativas ou para uma única opcional, mas sofrem com a combinação de várias opcionais pela limitação da herança.
- **Modularidade.** As interfaces separam o *framework* dos *plug-ins*, que podem mudar independentemente desde que respeitem o contrato. No melhor caso, a *feature* inteira se entende olhando só o seu *plug-in*. Isso viabiliza **ecossistemas** de *plug-ins* de terceiros (Eclipse, navegadores).

**Dificuldades.**
- **Projetar o *framework* é caro.**
  - É preciso **prever** onde haverá *hot spots* e desenhar as interfaces: investimento inicial alto, em geral feito por desenvolvedores experientes.
  - Se o projetista não expuser algo de que uma extensão precisa, ela fica difícil ou impossível.
- **Evoluir é difícil.** Acrescentar *hot spots* é possível, mas mudar a interface de *plug-in* quebra todos os *plug-ins* existentes, inclusive os de terceiros. Por isso, *frameworks* combinam mais com adoção **proativa** do que com adoção reativa ou extrativa.
- ***Plug-ins* não migram** entre *frameworks*: cada *framework* tem convenções próprias.
- **Sobrecarga** de desenvolvimento e de execução. Há código extra de desacoplamento mesmo em *hot spots* não usados, o que gera mais código e binário maior.

> 🔗 **Relação com o TCC.** O Quarkus é um *framework*, e os *controllers* do protótipo são extensões dele. Mas a variabilidade dos modos **não** usa o mecanismo de *plug-in* do Quarkus: usa uma anotação resolvida no *build*. A comparação com *plug-ins* pode entrar em Trabalhos Relacionados como alternativa de composição. A observação de que *plug-ins* não selecionados não precisam ser entregues é o equivalente, na composição, do efeito que a metamorfose obtém por anotação.

---

## 4.4 Componentes e serviços (p. 89–95)

**Contexto.** Componentes são comuns em SPL, mas não têm o potencial de automação da orientação a *features*. O livro os apresenta de forma breve.

> **Definição 4.4 (p. 89).** "A software component is a unit of composition with contractually specified interfaces and explicit context dependencies only. A software component can be deployed independently and is subject to composition by third parties." (Szyperski, 1997)
>
> *Tradução livre:* "Um componente de software é uma unidade de composição com interfaces especificadas por contrato e apenas dependências de contexto explícitas. Um componente pode ser implantado de forma independente e está sujeito à composição por terceiros."

- **A ideia** é uma unidade modular e reutilizável que expõe funcionalidade por interface e esconde a implementação. Os autores assumem componentes caixa-preta.
- Uma classe é um componente pequeno; uma biblioteca de algoritmos de grafo é um componente maior.
- **Origem e motivação.**
  - Parnas (1979) já propunha implementar "famílias de programas" encapsulando as partes que mudam, para trocá-las ou removê-las facilmente.
  - Outro argumento a favor dos componentes é a formação de **mercados**: comprar, em vez de reimplementar.

> 🖼️ **[Figura 4.16, p. 90 — não reproduzível]** Código de um pequeno componente de cores da biblioteca de grafos, com uma fachada e um *singleton* como interface. O Exemplo 4.2 descreve esse componente.

- **Dimensionar componentes (4.4.1, p. 91–92).**
  - Há um conflito conhecido entre **reuso e uso**. Um componente grande é fácil de usar, mas serve a poucos contextos. Um componente pequeno se combina com flexibilidade, mas transfere ao usuário o trabalho de conectá-lo.
  - Szyperski resume isso numa máxima: maximizar o reuso minimiza o uso.
  - O Exemplo 4.3 (p. 91) ilustra a escolha entre um código pequeno e incompleto e outro enorme e entrelaçado: a tendência é escolher o pequeno e escrever o resto, isto é, reusar menos.
- **Compor componentes (4.4.2, p. 92–93).**
  - Foi a estratégia comum no início da engenharia de SPL. Na engenharia de domínio, decide-se o que reusar e desenham-se componentes. Na engenharia de aplicação, eles são compostos.
  - Diferentemente dos *plug-ins*, componentes **não são feitos para composição automática**: para cada produto, alguém escreve **código de cola** à mão.
  - Mesmo manual, isso é muito melhor do que construir cada produto do zero. Aumenta o custo por produto, mas não elimina o benefício econômico da SPL.
  - É adequado quando quem seleciona *features* é o desenvolvedor (não o cliente) e há **poucos produtos**. O exemplo citado são os eletrônicos de consumo da Philips.
  - O objetivo da SPL orientada a *features* é **automatizar inteiramente a derivação**. Os autores defendem que essa automação é essencial em larga escala; Krueger chega a afirmar que a engenharia de aplicação manual é "nociva".
- **Componentes × *plug-ins* (4.4.3, p. 93–94).**
  - **Em comum:** modularidade (idealmente um módulo por *feature*), interfaces, rastreabilidade e pré-planejamento semelhantes.
  - **A diferença:** está na **automação** e no **reuso além da linha de produto**. O *plug-in* é feito para um *framework* específico. O componente é mais geral e reutilizável em outros contextos, mas exige cola manual.

**Quadro dos autores (p. 94), em síntese.**
- **A favor:**
  - técnica consolidada;
  - adaptação estática, entregando só o que foi selecionado;
  - separação, ocultação e rastreabilidade;
  - reuso dentro e fora da linha, inclusive de terceiros e em ambientes distribuídos (serviços);
  - aplicável a muitas linguagens.
- **Contra:**
  - sem derivação automática, porque exige cola;
  - evolução difícil;
  - sobrecarga de desenvolvimento, execução e tamanho;
  - pré-planejamento para dimensionar;
  - inadequado para *features* finas e transversais.

> 🔗 **Relação com o TCC.**
> - A ideia de "cola manual por produto" descreve bem a alternativa de **BFFs em repositórios separados que compartilham bibliotecas**: o núcleo vira componente, e cada BFF é cola escrita à mão.
> - A metamorfose dispensa essa cola, porque a derivação é automática pelo *build*. Esse contraste pode reforçar Trabalhos Relacionados (alternativa "repositórios separados").
> - O critério dos autores ("poucos produtos, seleção feita por desenvolvedores") também ajuda a delimitar quando a alternativa por componentes ainda é razoável.

---

## 4.5 Leituras complementares e exercícios (p. 95–97)

- **Observação dos autores.** As técnicas clássicas são muito usadas em SPL, mas raramente discutidas explicitamente como tal na literatura, sobretudo os parâmetros.
- **Leituras indicadas (entre outras):**
  - Meyer (1997), sobre modularidade, encapsulamento e acoplamento;
  - Fowler (1999), sobre métodos com parâmetros demais e objetos de parâmetro;
  - Reisner et al. (2010), que encontraram opções de configuração majoritariamente ortogonais, que raramente interagem;
  - Rabkin e Katz (2011), que encontraram opções de configuração documentadas de forma inconsistente (o que os autores leem como incentivo a planejar a variabilidade com técnicas de SPL);
  - Gamma et al. (1995), ainda a melhor referência sobre padrões de projeto.
- **Exercícios.** Implementar um sistema de *chat* com as técnicas do capítulo e compará-las pelos critérios do capítulo 3 (Exercício 4.11, retomado nos capítulos seguintes).
