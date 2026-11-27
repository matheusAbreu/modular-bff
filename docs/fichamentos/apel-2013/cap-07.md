# Cap. 7 — Mecanismos avançados de variabilidade baseados em ferramentas (p. 175–188)

> Fichamento de APEL et al. (2013). Resumo com palavras próprias; só as definições formais estão no original, com tradução livre. As páginas são as impressas.

## Visão geral (p. 175)

- O capítulo parte da crítica aos pré-processadores (seção 5.3.6) e pergunta se **ferramentas**, em vez de novas linguagens, podem resolver os problemas da anotação.
- A proposta é manter o modelo simples de "anotar e remover" e acrescentar:
  - rastreamento de *features* gerenciado por ferramenta (7.1);
  - visões do código (7.2);
  - derivação de produtos integrada ao modelo de *features* (7.3).
- O conjunto recebe o nome de **separação virtual de interesses** (7.4).

---

## 7.1 Explorando o rastreamento de *features* (p. 175–180)

> **Definição 7.1 (p. 175).** "A tracing link connects a feature with its implementation artifacts (or parts thereof)."
>
> *Tradução livre:* "Um *link* de rastreamento conecta uma *feature* aos seus artefatos de implementação (ou a partes deles)."

- **Onde ficam os *links*.**
  - Podem ser **extraídos** do código, como as diretivas de um pré-processador, ou **mantidos por uma ferramenta** num modelo de mapeamento à parte, sem alterar o código.
  - Ferramentas citadas: ConcernMapper, CIDE e, para modelos gráficos, fmp2rsm e FeatureMapper.
  - Como os *links* se baseiam na estrutura dos artefatos, equivalem a anotações **disciplinadas**.
  - A ferramenta pode atualizar o mapeamento quando o código muda, por exemplo quando um trecho é movido de arquivo.

> 🖼️ **[Figuras 7.1 e 7.2, p. 176–177 — não reproduzíveis]**
> - **7.1:** *features* ligadas a trechos dos artefatos por *links* gerenciados por ferramenta;
> - **7.2:** tela do ConcernMapper, com os interesses e as estruturas de código associadas.

### 7.1.1 Verificação de consistência (p. 177–178)

Com os *links* acessíveis, é possível verificar a consistência do próprio mapeamento.
- **Exemplo:** Tartler et al. (2011) encontraram no Linux diretivas que se referem a *features* inexistentes. São trechos que nunca entram em produto algum.
- **Outra verificação:** se uma *feature* influencia de fato a derivação. Se não influencia, não há por que oferecer a decisão ao usuário.
- **Base para o cap. 10:** o raciocínio sobre esse mapeamento sustenta muitas das análises de linhas de produto, incluindo as que tratam de erros em combinações específicas (crítica da seção 5.3.6).

> 🖼️ **[Figura 7.3, p. 178 — não reproduzível]** O grafo no CIDE, com o código de cada *feature* exibido em **cor de fundo** diferente.

### 7.1.2 Visualização (p. 178–180)

- **Navegação.** Com os *links*, o usuário navega entre os trechos de uma *feature* mesmo que o mapeamento não esteja visível no código.
- **Cores.** O CIDE e o FeatureMapper usam **cores de fundo** (uma por *feature*) no lugar de diretivas. No exemplo, *Weighted* aparece em vermelho e *Colored* em azul.
- **Ofuscação.** Mesmo quando as diretivas estão visíveis, ferramentas como o FeatureCommander realçam o código das *features* selecionadas e marcam arquivos e margens, o que ajuda contra a ofuscação criticada na seção 5.3.

> 🖼️ **[Figura 7.4, p. 179 — não reproduzível]** O grafo no FeatureCommander, com o código envolvido por `#ifdef` realçado em cores por *feature*.

---

## 7.2 Visões do código (p. 180–182)

> **Definição 7.2 (p. 180).** "In product-line development, a view on source code hides implementation details that are not relevant to a given feature selection."
>
> *Tradução livre:* "No desenvolvimento de linhas de produto, uma visão sobre o código-fonte oculta detalhes de implementação que não são relevantes para uma dada seleção de *features*."

- **Motivação.** Um dos motivos da separação de interesses (3.2.3) é encontrar todo o código de uma *feature* num só lugar, sem distração. A implementação clássica por pré-processador não oferece essa localidade.
- **O que as visões fazem.** Visões **editáveis** do código (e de outros artefatos) ocultam o que não é relevante para uma seleção de *features*. Na Fig. 7.5, o código de *Colored* é ocultado e marcadores indicam onde ele estava.
- **O efeito.** Emulam a separação de interesses: o desenvolvedor trabalha como se o código estivesse separado, embora continue numa base única.

> 🖼️ **[Figuras 7.5 e 7.6, p. 181–182 — não reproduzíveis]**
> - **7.5:** o grafo no CIDE com o código de *Colored* oculto e marcadores no lugar;
> - **7.6:** anotações finas no Berkeley DB vistas no CIDE.

---

## 7.3 Derivação de produtos integrada (p. 182–184)

- **O que são essas ferramentas.** Ferramentas modernas integram todas as etapas do processo de SPL. Não trazem um mecanismo novo: combinam os existentes (parâmetros, *frameworks*, sistemas de *build*, controle de versão, FOP, rastreamento) com o **modelo de *features*** como artefato central.
- **Os dois ganhos:**
  1. **validar a seleção de *features*** antes de derivar o produto;
  2. **automatizar a derivação**.
- **O problema que resolvem.** Sem o modelo de *features* integrado, os desenvolvedores ficavam sozinhos para descobrir como configurar parâmetros, montar componentes ou saber quais *plug-ins* eram compatíveis. O modelo orienta a configuração e impede combinações inválidas.
- **Ferramentas citadas:** FeatureIDE e as comerciais pure::variants e Gears.

> 🖼️ **[Figura 7.7, p. 183 — não reproduzível]** Tela do FeatureIDE com o editor de configuração de *features* e a geração do produto correspondente.

> 🔗 **Relação com o TCC.** O protótipo não tem modelo de *features* formal. As restrições entre os modos ficam implícitas: duas *features* booleanas e independentes, quatro combinações válidas, uma delas a linha de base.
> - A validação da seleção é feita, na prática, pelos alvos do `Makefile`, que só oferecem as combinações previstas.
> - O livro dá base para registrar isso como **limite** ou **trabalho futuro**: integrar um modelo de *features* explícito que valide as combinações e automatize a derivação. Útil para a seção 8 (Trabalhos futuros).

---

## 7.4 Discussão: separação virtual de interesses (p. 184–186)

> **Definição 7.3 (p. 184).** "Virtual separation of concerns is a tool-based approach to product-line development. Based on a combination of tracing information, visualization facilities, source-code views, and the integration of feature models, code that belongs to individual features can be displayed, edited, and understood in separation."
>
> *Tradução livre:* "A separação virtual de interesses é uma abordagem baseada em ferramentas para o desenvolvimento de linhas de produto. Com base na combinação de informações de rastreamento, recursos de visualização, visões do código-fonte e integração com modelos de *features*, o código que pertence a *features* individuais pode ser exibido, editado e compreendido separadamente."

**A posição dos autores.**
- Veem a separação virtual como uma **melhoria** sobre as abordagens clássicas por ferramenta (cap. 5) e como **alternativa completa** às abordagens avançadas por linguagem (cap. 6).
- A ideia é obter os benefícios da separação de interesses **sem** depender de mecanismos de linguagem, como refinamentos e *advice*.

**Características.**
- O rastreamento é usado tipicamente para personalizar o código **em compilação**. Ligação em carga ou execução é concebível, injetando mecanismos de variabilidade no código antes da compilação.
- A rastreabilidade explícita e disciplinada facilita escrever ferramentas de refatoração (cap. 8) e de análise (cap. 10).
- Preserva o "jeito" dos pré-processadores: é **anotação**, admite **qualquer granularidade**, exige **pouco pré-planejamento** e muda o código invasivamente. Por isso combina com a **adoção extrativa**.
- Aplica-se uniformemente a código e não código.
- A integração com o modelo de *features* combate o espalhamento do conhecimento de configuração, e as visualizações reduzem a ofuscação criticada nos pré-processadores.

**Quadro dos autores (p. 185), em síntese.**
- **A favor:**
  - modelo simples de anotar e remover;
  - rastreamento estruturado;
  - personalização em compilação sem código repetitivo;
  - granularidade arbitrária;
  - pouco pré-planejamento;
  - separação virtual apesar do espalhamento físico;
  - adequada à adoção extrativa;
  - uniforme;
  - fácil de usar e de analisar.
- **Contra:**
  - **sem ocultação de informação**, pois não há interfaces entre *features*;
  - ferramentas ainda sobretudo acadêmicas, com pouca experiência prática;
  - muitas anotações finas e entrelaçadas ainda atrapalham a compreensão.

> 🔗 **Relação com o TCC.** A separação virtual e o protótipo estão do mesmo lado da classificação: **anotação, em compilação, disciplinada**. A diferença é que o protótipo não precisa de visões nem de cores, porque a anotação é de **classe inteira** e cada modo está fisicamente num *controller* próprio. Na prática, ele obtém **separação física por convenção de projeto** (arquitetura hexagonal: um adaptador por modo) com um mecanismo de anotação. Esse ponto pode ser explorado na discussão (seção 7.7 do TCC), apresentado como leitura do autor.

---

## 7.5 Ferramentas e 7.6 Leituras complementares (p. 186–187)

- **Ferramentas:** FEAT, ConcernMapper, CIDE, FeatureMapper, fmp2rsm, FeatureCommander, FeatureIDE e as comerciais Gears e pure::variants.
- **Leituras:**
  - Os autores registram que **não há literatura padrão** sobre abordagens por ferramenta para SPL orientadas a *features*.
  - As ferramentas de SPL aparecem nos livros clássicos de SPL: Clements e Northrop (2001) e Pohl et al. (2005).
  - Greenfield e Short apresentam o conceito afim de *software factory*.
  - Kästner (2010) aprofunda a separação virtual de interesses.

## Exercícios (p. 187–188)

Os exercícios pedem para:
- reimplementar o *chat* no CIDE ou ferramenta semelhante;
- discutir prós e contras da separação virtual;
- imaginar usos da informação de rastreamento;
- desenhar o processo de SPL indicando onde cada ferramenta atua;
- **completar a comparação de todas as técnicas** do Exercício 4.11, que fecha a Parte II do livro.

---

## Síntese da Parte II (cap. 3–7) para o TCC

| Técnica (livro) | Ligação | Representação | Onde aparece no TCC |
|---|---|---|---|
| Parâmetros (4.1) | Carga / execução | Anotação | É a *feature flag*. Contraste nas seções 2.5, 4.5 e 7.6 |
| Padrões de projeto (4.2) | Execução | Composição | — (a hexagonal facilita a variabilidade, mas não é o mecanismo) |
| *Frameworks* / *plug-ins* (4.3) | Carga | Composição | Possível alternativa em Trabalhos Relacionados |
| Componentes (4.4) | Compilação / carga | Composição | Alternativa "núcleo compartilhado + cola por BFF" |
| Controle de versão (5.1) | Compilação | — (produtos por ramo) | Alternativa "um repositório por BFF", desaconselhada pelos autores |
| Sistemas de *build* (5.2) | Compilação | Anotação por arquivo | O `Makefile` e o `Dockerfile` **orquestram** a variante (seção 6.6) |
| Pré-processadores (5.3) | Compilação | Anotação | É a categoria mais próxima do `@IfBuildProperty`. O protótipo é disciplinado e de granularidade de classe |
| FOP / AOP (6.1–6.3) | Compilação (principalmente) | Composição | O "outro lado" da classificação; ainda acadêmicas (6.5) |
| Separação virtual (7.4) | Compilação | Anotação | Mesma família do protótipo; modelo de *features* integrado como trabalho futuro |

> ⚠️ As classificações desta tabela vêm do **texto** dos capítulos 4 a 7. As marcações da Tabela 3.1 do livro não sobreviveram à extração do PDF. Conferir no impresso antes de reproduzir a tabela no TCC.
