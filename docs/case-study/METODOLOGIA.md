# Metodologia

> **Situação no TCC:** capítulo a ser inserido após a *Proposta: Arquitetura Metamórfica* (Cap. 4) e antes do *Desenvolvimento* (Cap. 6), conforme o sumário-alvo do `ROADMAP-CAPITULOS-TCC.md`. Descreve **como** o trabalho foi conduzido e **como** a proposta será avaliada, amarrando cada objetivo específico a um método de verificação.

Este capítulo caracteriza a natureza da pesquisa, justifica a adoção da *Design Science Research* (DSR) como método condutor, formula as perguntas de pesquisa, detalha as etapas percorridas e define os procedimentos de avaliação do artefato proposto. O objetivo é tornar o trabalho **reprodutível** e deixar explícito o encadeamento entre o problema, o artefato construído (a Arquitetura Metamórfica e seu protótipo) e as evidências que sustentam ou refutam suas propriedades.

---

## 1. Classificação da Pesquisa

Quanto à sua **natureza**, esta é uma pesquisa **aplicada**, pois se volta à produção de um artefato — uma arquitetura de software e um protótipo funcional — destinado a resolver um problema concreto de engenharia: o reuso de código na construção de múltiplos BFF que atendem a um mesmo contexto de negócio.

Quanto aos **objetivos**, classifica-se como **exploratória e descritiva**: explora uma combinação de conceitos ainda pouco sistematizada na literatura *peer-reviewed* específica sobre BFF e descreve, em detalhe, a arquitetura resultante e seu comportamento.

Quanto à **abordagem**, é predominantemente **qualitativa**, com apoio de **medições quantitativas** na etapa de avaliação (contagem de pontos de entrada, proporção de código reutilizado, presença ou ausência de classes no artefato).

Quanto ao **procedimento técnico**, adota-se a **Design Science Research (DSR)** — a pesquisa orientada ao projeto e à avaliação de um artefato —, detalhada na seção seguinte.

---

## 2. Design Science Research como Método Condutor

A *Design Science Research* é um paradigma de pesquisa cujo produto central é um **artefato** projetado para resolver uma classe de problemas relevantes, avaliado quanto à sua utilidade e ao seu desempenho. Diferentemente de paradigmas puramente descritivos — que buscam explicar *como o mundo é* —, a DSR é prescritiva: preocupa-se com *como o mundo deveria ser* para que um objetivo seja atingido, por meio da construção e da avaliação de artefatos.

A escolha da DSR justifica-se porque a contribuição deste trabalho **é, ela própria, um artefato**: a Arquitetura Metamórfica (um método/design) e sua instanciação no protótipo `modular-bff`. Não se trata de observar um fenômeno preexistente, mas de **projetar** uma solução e **demonstrar** que ela entrega as propriedades reivindicadas — reuso sem duplicação e redução da superfície de ataque por construção.

Segundo a taxonomia usual da DSR, os artefatos produzidos aqui são de dois tipos complementares:

| Tipo de artefato | Instância neste trabalho |
|---|---|
| **Método** (design/arquitetura) | A **Arquitetura Metamórfica**: variabilidade resolvida em tempo de compilação para derivar artefatos BFF especializados de um único código-fonte. |
| **Instanciação** (protótipo) | O **`modular-bff`**: implementação em Kotlin/Quarkus que demonstra o método sobre o domínio de pedidos de e-commerce. |

---

## 3. Perguntas de Pesquisa

A avaliação do artefato é guiada pelas seguintes perguntas, derivadas do objetivo geral e dos objetivos específicos da Proposta:

- **PP1 — Reuso:** é possível derivar múltiplos artefatos BFF especializados (interno e externo) a partir de um único código-fonte, **sem duplicar** as regras de negócio, os serviços e os repositórios?
- **PP2 — Ausência verificável:** a resolução da variabilidade em tempo de compilação garante que as operações de um modo **não estejam presentes no binário** do outro modo?
- **PP3 — Superfície de ataque:** a abordagem metamórfica **reduz o número de pontos de entrada** expostos por artefato, quando comparada a um *build* que contém todos os *controllers*?
- **PP4 — Custo operacional:** que custos a abordagem impõe (pipelines/artefatos distintos e, opcionalmente, tamanho, tempo de inicialização e memória por variante)?

> **PP2** é a pergunta central: é ela que converte a tese de *segurança por ausência* de afirmação teórica em evidência observável no artefato.

---

## 4. Etapas da Pesquisa

O trabalho segue as seis atividades comumente associadas ao método DSR, mapeadas aos capítulos do TCC:

1. **Identificação do problema e motivação.** Caracterização do dilema entre especialização e reuso quando múltiplos BFF atendem a um mesmo contexto. *(Introdução e Justificativa.)*
2. **Definição dos objetivos da solução.** Estabelecimento das propriedades desejadas: gerar múltiplas aplicações de um mesmo código-fonte, mais seguras e leves ao ignorar o que não lhes compete. *(Objetivos.)*
3. **Projeto e desenvolvimento do artefato.** Concepção da Arquitetura Metamórfica e sua fundamentação em Linhas de Produto de Software, variabilidade por anotação e superfície de ataque. *(Proposta e Desenvolvimento.)*
4. **Demonstração.** Implementação do protótipo `modular-bff` e geração dos artefatos interno e externo a partir do mesmo código-fonte. *(Desenvolvimento.)*
5. **Avaliação.** Coleta de evidências que respondem às perguntas de pesquisa — inspeção do binário, contagem de pontos de entrada e medição de reuso. *(Avaliação, Resultados e Discussão.)*
6. **Comunicação.** Registro dos resultados neste TCC e nos documentos de apoio do repositório. *(Trabalho como um todo.)*

---

## 5. Procedimentos de Avaliação

A avaliação é conduzida pela **inspeção do artefato** e por **medições sobre o código-fonte e os binários gerados**, sem depender de ambiente de produção. Cada procedimento responde a uma ou mais perguntas de pesquisa.

### 5.1 Procedimento experimental

1. **Geração das variantes.** A partir do mesmo código-fonte, produzem-se dois artefatos, alternando as *flags* de build (`BFF_API_INTERNAL_ACTIVE` e `BFF_API_EXTERNAL_ACTIVE`) que controlam a anotação `@IfBuildProperty` dos *controllers*.
2. **Inspeção do binário (PP2).** Lista-se o conteúdo do JAR de cada variante e verifica-se a presença ou ausência das classes de *controller* de cada modo, evidenciando que a operação do outro modo **não existe** no artefato.
3. **Contagem de pontos de entrada (PP3).** Enumeram-se as rotas/endpoints expostos por variante e comparam-se com um *build* hipotético que contivesse todos os *controllers*, enquadrando o resultado pela métrica de superfície de ataque (MANADHATA; WING, 2011).
4. **Medição de reuso (PP1).** Quantifica-se a proporção entre código **compartilhado** (domínio, serviços, repositórios) e código **especializado** (controllers, DTOs), por número de classes e/ou linhas de código.
5. **Custo operacional (PP4).** Registra-se a necessidade de pipelines/artefatos distintos e, opcionalmente, tamanho do artefato, tempo de inicialização e consumo de memória por variante.
6. **(Opcional) Contraste com feature flag em runtime.** Reproduz-se a alternativa em tempo de execução e verifica-se que o endpoint **permanece** no binário, contrastando com a abordagem metamórfica.

> ⚠️ **Coleta de dados pendente (execução).** Os procedimentos 2–6 produzem os números que sustentam o capítulo de Avaliação. Sua execução (builds e inspeção) é etapa de *demonstração/medição*, a ser realizada em sessão dedicada; até lá, o capítulo de Avaliação mantém marcadores `[RESULTADO A PREENCHER]`.

### 5.2 Ambiente e ferramentas

Para garantir a **reprodutibilidade**, registram-se as versões e ferramentas do ambiente de referência:

| Item | Especificação |
|---|---|
| Linguagem / plataforma | Kotlin 2.4.0 · JVM 21 |
| Framework | Quarkus 3.37.0 (mecanismo `@IfBuildProperty` do Quarkus Arc) |
| Build | Gradle (Kotlin DSL) |
| Persistência | PostgreSQL + Hibernate ORM · migrações com Goose |
| Inspeção de artefato | `jar tf` (JDK) ou `unzip -l` |
| Containerização | Docker Compose |

Cada medição deve registrar os **comandos exatos** e as *flags* utilizadas, de modo que um terceiro possa repetir o experimento e obter os mesmos artefatos.

---

## 6. Quadro de Amarração (Objetivos × Método × Evidência)

O quadro a seguir liga cada objetivo específico e cada pergunta de pesquisa ao método de verificação e ao capítulo que apresentará a evidência — garantindo que **nenhuma afirmação da proposta fique sem verificação**.

| Objetivo / Pergunta | Método de verificação | Evidência esperada | Capítulo |
|---|---|---|---|
| Apresentar conceitualmente a Arquitetura Metamórfica | Fundamentação teórica e posicionamento na literatura (SPL, variabilidade, superfície de ataque) | Arquitetura descrita e ancorada em referências consolidadas | Referencial Teórico · Proposta |
| Apresentar um protótipo conceito | Implementação e demonstração do `modular-bff` | Protótipo funcional com as variantes reproduzíveis | Desenvolvimento |
| **PP1 — Reuso** | Medição de código compartilhado × especializado | Proporção de reuso quantificada | Avaliação |
| **PP2 — Ausência verificável** | Inspeção do JAR de cada variante | Classe do outro modo ausente no binário | Avaliação |
| **PP3 — Superfície de ataque** | Contagem de pontos de entrada por variante | Redução mensurável de endpoints | Avaliação |
| **PP4 — Custo operacional** | Registro de pipelines e métricas de artefato | Custos explicitados e dimensionados | Avaliação · Ameaças à Validade |

---

## 7. Ameaças à Validade (encaminhamento)

Por se tratar de pesquisa baseada em um único artefato, a discussão das **ameaças à validade** — protótipo e domínio únicos (validade externa), escopo delimitado da segurança por ausência e escolha das métricas — é desenvolvida no capítulo de Avaliação, Resultados e Discussão, conforme praxe em TCCs orientados a evidência.

---

> ### 📌 Pendência: fundamentação metodológica sem citação
> Este capítulo descreve a DSR e suas seis atividades **sem referência bibliográfica**, pois ainda não há fonte metodológica nas nossas notas (`REFERENCIAS_CIENTIFICAS.md` / `CITACOES_ABNT.md`). Para dar rigor acadêmico ao capítulo, será preciso **selecionar e verificar** uma ou duas referências de DSR, adicioná-las às notas e então reinserir as citações nos pontos correspondentes (definição da DSR na seção 2 e as seis atividades na seção 4).
