# Métrica de Reuso (PP1) — método de cálculo

> 🟡 **Rascunho de método.** Define **como** a pergunta de pesquisa **PP1 — Reuso** será respondida no capítulo de Avaliação: qual métrica se adota, de qual literatura ela vem, como os artefatos do protótipo são classificados e qual o procedimento de coleta. **Não contém resultados** — os números são produzidos na execução e registrados na Avaliação.
>
> ✅ **Implementado no TCC** — absorvido pelo capítulo *Metodologia* do arquivo `Proposta TCC …md`, na seção **5.5.2 Métricas de reuso**. **A fonte da verdade passa a ser o TCC**; este documento é mantido apenas como registro de trabalho, e as notas de atribuição de fontes (seção 5) seguem valendo para quem for redigir a Avaliação.
>
> **Convenções:** citações no sistema autor-data (ABNT NBR 10520). As fontes foram obtidas e verificadas segundo o *modus operandi* de `docs/FONTES-ACADEMICAS.md`; o registro completo está em `REFERENCIAS_CIENTIFICAS.md` (seção 8) e as citações diretas em `CITACOES_ABNT.md` (Eixo 6).
>
> ⚠️ **Método revisado em 10 jul. 2026, após a primeira coleta.** O *reuse level* foi **abandonado** e substituído por uma medição em **granularidade de método**, por alcançabilidade a partir dos pontos de entrada. As seções 1 a 4 abaixo descrevem o método **anterior** e são mantidas como registro; **a seção 6 é a que vale**. A seção 5.5.2 do TCC ainda descreve o método anterior e precisa ser reescrita (ver `ROADMAP-CAPITULOS-TCC.md`).

---

## 1. O que precisa ser medido

A PP1 pergunta se é possível derivar múltiplos artefatos BFF especializados **sem duplicar** regras de negócio, serviços e repositórios. Responder a isso exige quantificar a proporção entre o que é **comum a todas as variantes** e o que é **específico de cada uma**.

Duas decisões precedem qualquer fórmula:

**a) Sobre qual objeto se mede.** Não se mede sobre o bytecode entregue. Como estabelecido na pendência crítica do `ROADMAP-CAPITULOS-TCC.md` (seção 1.1), todas as classes do projeto estão presentes no `app/*.jar` de qualquer variante — o que a `@IfBuildProperty` remove é o *ponto de entrada* (bean e rota), não a classe. Medir reuso sobre o bytecode entregue devolveria trivialmente 100% para todas as variantes e não diria nada. **A medição de reuso é feita sobre o código-fonte**, que é onde a comunalidade e a variabilidade da linha de produto de fato residem. A ausência no artefato é assunto da PP2, não da PP1 — e essa separação é deliberada.

**b) Qual é a granularidade.** Adotam-se **duas**: linhas de código (LOC) e classes. A primeira dimensiona o esforço; a segunda dimensiona a estrutura. Frakes e Terry (1996) observam que a medição de reuso pressupõe definir o nível de abstração dos itens contados, e que um sistema pode ser expresso em termos de módulos, funções ou linhas de código — a escolha do nível é parte do método, não um detalhe.

---

## 2. Métricas adotadas

### 2.1 Reuse Percent (métrica principal)

Frakes e Terry (1996) catalogam, entre as *amount of reuse metrics*, o **reuse percent** proposto por Poulin et al. (1993) e usado pela IBM, que "reflete o quanto do produto pode ser atribuído a reuso":

```
Product Reuse Percent = RSI / (RSI + SSI) × 100
```

onde `RSI` (*reused source instructions*) são as instruções reaproveitadas e `SSI` (*shipped source instructions*) as instruções novas entregues com o produto (FRAKES; TERRY, 1996). O `RSI` é grandeza de uso corrente: Irshad et al. (2016) o empregam, em revisão sistemática com avaliação industrial, para estimar o custo evitado pelo reuso.

**Transposição para este trabalho.** No modelo original, `RSI` e `SSI` distinguem código herdado de um repositório corporativo e código escrito para o produto. Aqui a distinção é **entre a base comum da linha de produto e o adaptador específico da variante**:

| Símbolo | No modelo original | Neste trabalho |
|---|---|---|
| `RSI` | instruções reaproveitadas de outro produto | LOC dos ativos **comuns**, presentes e ativos em toda variante |
| `SSI` | instruções novas escritas para o produto | LOC dos ativos **específicos** da variante em questão |

Essa transposição é do mesmo tipo da já praticada no Referencial Teórico com a distinção *método externo × interno* de Manadhata e Wing (2011), levada do nível dos métodos ao nível do negócio (seção 2.5 do TCC). **Ela precisa ser declarada explicitamente no texto da Avaliação**, com a ressalva de que o valor obtido não é comparável aos números de reuso industrial reportados por Poulin et al. (1993), cuja unidade de reuso é o produto, e não a variante.

### 2.2 Reuse Level (métrica estrutural, complementar)

Frakes e Terry (1996) definem, para um item de nível superior composto de itens de nível inferior:

- `L` — número total de itens de nível inferior no item superior;
- `E` — número de itens vindos de um **repositório externo**;
- `I` — número de itens **não** vindos de repositório externo;
- `M` — número de itens não externos **usados mais de uma vez**.

Contam-se **itens únicos (tipos), não referências (tokens)**. Daí:

```
External Reuse Level (ERL) = E / L
Internal Reuse Level (IRL) = M / L
Total Reuse Level (TRL)    = ERL + IRL
```

Os três assumem valores entre 0 e 1; quanto mais próximo de 1, maior o reuso, e 0 indica ausência de reuso (FRAKES; TERRY, 1996). São métricas de uso corrente: a revisão sistemática de Chen, Usman e Badampudi (2024), sobre 30 casos industriais, mostra que o `External Reuse Level` e a `External Reuse Frequency` permanecem os instrumentos com que estudos empíricos medem o reuso e o relacionam a qualidade, produtividade e esforço. Os resultados que a revisão reúne são, contudo, **mistos**: ao lado das relações positivas entre reuso e qualidade, um dos casos reporta relação marginalmente negativa entre o `ERL` e a razão esforço/linhas de código. O capítulo de Avaliação deve reportar esse contraditório, e não apenas o favorável.

**Aplicação.** O *item de nível superior* é **cada variante**; os *itens de nível inferior* são as **classes Kotlin** ativas nela. Um item é considerado **externo** quando foi criado para uso geral da linha de produto, e não para aquela variante — critério que decorre da própria definição de Frakes e Terry, segundo a qual um componente externo "é usado pelo item de nível superior, mas foi criado para um item diferente ou para uso geral". O núcleo compartilhado (domínio, serviços, repositórios, DTOs) satisfaz esse critério; os *controllers* anotados, não.

O `IRL` captura algo que o `Reuse Percent` não vê: o `OrderService` é uma classe **única** consumida pelos **três** controllers. É exatamente a evidência de "reuso sem duplicação" que a tese reivindica, expressa numa métrica da literatura.

### 2.3 Por que duas métricas, e não uma

O `Reuse Percent` responde "quanto do produto é reaproveitado" em volume de código. O `Reuse Level` responde "quão reaproveitada é a estrutura", contando itens únicos. Uma variante poderia ter alto `Reuse Percent` apenas por conter poucas linhas específicas, sem que houvesse compartilhamento estrutural real. As duas juntas fecham essa brecha. Frakes e Kang (2005) registram que a medição de reuso é justamente a área em que "muito mais trabalho é necessário", o que recomenda triangulação em vez de um único número.

---

## 3. Classificação dos ativos do protótipo

A classificação abaixo é a **decisão de método** e deve ser publicada junto dos resultados, pois qualquer leitor precisa poder contestá-la.

| Pacote / arquivo | Classe | Justificativa |
|---|---|---|
| `domain/**` | Comum | Sem anotação de variabilidade; idêntico em toda variante |
| `services/**` | Comum | `OrderService` é injetado nos três controllers |
| `adapters/infrastructure/repository/**` | Comum | Porta de saída, indiferente ao modo |
| `adapters/api/dto/**` | Comum | Compartilhado; ver ressalva abaixo |
| `config/**`, `adapters/api/liveness/**` | Comum | Infraestrutura da aplicação |
| `adapters/api/v1/OrderGeneralController.kt` | Comum | **Sem** `@IfBuildProperty`: ativo em toda variante |
| `adapters/api/v1/OrderExternalController.kt` | Específico (externo) | `@IfBuildProperty(...external.enable)` |
| `adapters/api/v1/OrderInternalController.kt` | Específico (interno) | `@IfBuildProperty(...internal.enable)` |

**Ressalva sobre os DTOs.** Alguns DTOs servem a um só modo — `NewOrderDTO` e `ItemInNewOrderDTO` só são usados pelo controller externo. Ainda assim são classificados como **comuns**, porque não carregam anotação de variabilidade e permanecem ativos em toda variante. Classificá-los como específicos exigiria anotá-los, o que é uma **mudança de projeto**, não de medição. Registrar essa escolha como **ameaça à validade de construto**: ela é conservadora, pois **infla** o denominador comum e, portanto, **favorece** a tese de reuso. Uma medição alternativa, que atribua os DTOs de uso exclusivo à variante correspondente, deve ser reportada em paralelo como análise de sensibilidade.

**Escopo da contagem.** Apenas `src/main/kotlin/**`. Excluem-se testes (`src/test`), código gerado (`build/`), migrações SQL e configuração. LOC = linhas físicas descontadas linhas em branco e comentários.

---

## 4. Procedimento de coleta

1. Instalar um contador de linhas que distinga comentários (`cloc`, `scc` ou `tokei`) e registrar a versão exata no capítulo de Avaliação.
2. Contar LOC por pacote, segundo a classificação da seção 3, produzindo `RSI` (comum) e `SSI` (específico) **por variante**:
   - variante `default`: `SSI = 0` (nenhum controller anotado ativo);
   - variante `internal`: `SSI = LOC(OrderInternalController)`;
   - variante `external`: `SSI = LOC(OrderExternalController)`.
3. Calcular `Reuse Percent` de cada variante.
4. Contar as classes ativas por variante (`L`), quantas são comuns (`E`) e quantas das não-comuns são usadas mais de uma vez (`M`); calcular `ERL`, `IRL` e `TRL`.
5. Repetir o passo 2 sob a classificação alternativa dos DTOs (análise de sensibilidade).
6. Registrar os comandos exatos, para que um terceiro reproduza os mesmos números.

> **Observação sobre a variante `default`.** Com `SSI = 0`, o `Reuse Percent` é 100% por construção. O número é matematicamente correto e **substantivamente vazio**: ele apenas confirma que a variante default não tem código específico. Deve ser reportado como caso-limite, e não como resultado favorável.

---

## 5. Fontes e status de verificação

| Fonte | Papel | Acesso |
|---|---|---|
| FRAKES; TERRY (1996) — *Software Reuse: Metrics and Models*, ACM Computing Surveys, v. 28, n. 2, p. 415-435 | Fonte das fórmulas: *amount of reuse*, *reuse percent* (atribuído a Poulin et al.) e *reuse level* | 🟢 **Verificado.** Versão de registro (ACM) paga; **relatório técnico de autor** TR-95-07 (Virginia Tech) aberto e conferido — `docs/refs/8.1` |
| FRAKES; KANG (2005) — *Software Reuse Research: Status and Future*, IEEE TSE, v. 31, n. 7, p. 529-536 | Enquadramento: classificação das métricas de reuso e reconhecimento de que a medição de reuso segue subdesenvolvida | 🟢 **Verificado.** PDF aberto — `docs/refs/8.2` |
| IRSHAD; PETERSEN; TORKAR; AFZAL (2016) — *Capturing Cost Avoidance through Reuse*, EASE, p. 1-12 | Vigência do `RSI` em revisão sistemática com avaliação industrial | 🟢 **Verificado.** Versão de registro (ACM) paga; versão de autor aberta — `docs/refs/8.4` |
| CHEN; USMAN; BADAMPUDI (2024) — *Understanding and evaluating software reuse costs and benefits from industrial cases*, Information and Software Technology, v. 171, art. 107451 | Corroboração recente: o *reuse level* segue em uso na literatura empírica; vínculo entre reuso e manutenibilidade | 🟢 **Verificado.** Artigo *open access* (CC BY) — `docs/refs/8.3` |
| POULIN et al. (1993) — métricas de reuso da IBM (`RSI`, `SSI`, *reuse percent*) | Origem do *reuse percent* | 🔴 **Não verificado na fonte primária.** Citado **via** Frakes e Terry (1996) — no TCC, usar citação de segunda mão explícita ("apud") ou atribuir a fórmula a FRAKES; TERRY (1996), que a documenta |

> **Limites de atribuição.** Irshad et al. (2016) aplicam o `RSI` a um modelo de **custo evitado**, não à razão `RSI/(RSI+SSI)`; Chen, Usman e Badampudi (2024) tratam do `reuse level`, não do `reuse percent`. A fórmula do `reuse percent` permanece ancorada em Frakes e Terry (1996). Não atribuir a nenhuma das duas fontes recentes mais do que elas afirmam.
>
> **Pendência.** Obter o texto de Poulin et al. (1993), *The business case for software reuse*, IBM Systems Journal, v. 32, n. 4, p. 567-594, para citar a fórmula na fonte primária. Enquanto isso, **não atribuir a Poulin et al. nada além do que Frakes e Terry (1996) registram**.

---

## 6. Revisão do método (10 jul. 2026) — do *reuse level* à contagem de métodos

> Esta seção **substitui** as seções 2.2, 2.3 e 3 no que toca ao *reuse level*. O **reuse percent** (seção 2.1) permanece válido e continua sendo a métrica de volume, agora em duas granularidades.

### 6.1 Por que o *reuse level* foi abandonado

A primeira coleta produziu os números do *reuse level* e, com eles, três problemas que só se tornam visíveis depois de calculados. Nenhum é erro de cálculo: são consequências da métrica aplicada corretamente a este objeto.

**a) O `IRL` é nulo por construção, e a justificativa publicada é falsa.** Frakes e Terry (1996) definem `M` como o número de itens **não externos** usados mais de uma vez. Sob a classificação declarada — item externo é o "criado para uso geral da linha de produto" —, o `OrderService` e todo o núcleo compartilhado são **externos**. O único item não externo de cada variante é o seu *controller* anotado, que é ponto de entrada e não é consumido por ninguém. Logo `M = 0`, `IRL = 0` e `TRL = ERL`, em toda variante. A frase da seção 2.2 (e da seção 5.5.2 do TCC) segundo a qual "o `IRL` captura que o `OrderService` é consumido pelos três *controllers*" é **falsa sob a própria classificação que o texto declara**: o `OrderService` está em `E`, e o `IRL` não olha para `E`.

**b) O `ERL` é redundante com o *reuse percent*.** Um afirma que 95,17% das **linhas** da variante externa são comuns; o outro, que 97,7% de suas **classes** são comuns. É a mesma proposição medida com régua diferente, e ambas são dominadas pelo mesmo fato: apenas dois arquivos carregam anotação de variabilidade. A triangulação invocada em 2.3 pressupõe que as métricas possam **discordar**; aqui elas não podem discordar de modo informativo.

**c) O `ERL` é quase insensível ao fenômeno que deveria vigiar.** A PP1 pergunta se as variantes são derivadas **sem duplicar** regras de negócio. Se cada variante tivesse seu próprio `OrderService`, `L` passaria de 44 a 45 e o `ERL` cairia de 0,977 para 0,956 — dois pontos percentuais para um colapso arquitetural. Uma métrica que mal reage à falha que investiga não sustenta a conclusão.

**d) Homonímia com o vocabulário do trabalho.** Em Frakes e Terry, *external* e *internal* qualificam a **origem de um componente** (veio, ou não, de um repositório de reuso fora do produto). Neste trabalho, *externo* e *interno* qualificam o **tipo de cliente** atendido — o consumidor do serviço e o funcionário da empresa. São homônimos sem relação. Um leitor que encontre "External Reuse Level" num capítulo sobre "cliente externo" será induzido a supor que uma coisa mede a outra.

**Consequência para o texto.** Cai a citação de Frakes e Kang (2005), hoje usada apenas para justificar a triangulação. A citação de Chen, Usman e Badampudi (2024) **sai** do argumento de vigência do *reuse level* e **permanece** onde ancora a relação entre reuso e manutenibilidade (seção 5.6.1 do TCC). Frakes e Terry (1996) permanece, sustentando o *reuse percent*.

### 6.2 O que entra no lugar: reuso por alcançabilidade a partir do ponto de entrada

Mede-se o reuso em **granularidade de método**, tomando como raiz cada **ponto de entrada** do artefato. A escolha tem três fundamentos.

**É granularidade prevista pela fonte.** Frakes e Terry (1996) observam que a medição de reuso pressupõe definir o nível de abstração dos itens contados, e que um sistema pode ser expresso em termos de módulos, **funções** ou linhas de código. Método é nível legítimo, não invenção deste trabalho.

**É a unidade que a tese já usa.** Manadhata e Wing (2011) definem a superfície de ataque pelo conjunto de **pontos de entrada**. Partir do *controller* e perguntar o que ele torna alcançável mede reuso (PP1) sobre o mesmo objeto conceitual com que se mede exposição (PP3).

**É sensível à falha que investiga.** Se as variantes duplicassem a regra de negócio, o conjunto de métodos **exclusivos** de cada uma deixaria de conter apenas o método do *controller* e passaria a conter todo o fluxo de negócio. A métrica desabaria — como deve.

### 6.3 Desenho da medição

**Objeto.** O **grafo de chamadas** entre métodos declarados em `edu.uerj`, extraído do bytecode compilado (`build/classes/kotlin/main`) por análise estática das instruções de invocação.

**Por que sobre o bytecode, sem contradizer a seção 1(a).** Aquela decisão vetava medir reuso pela **presença** de classes no artefato entregue — que é trivialmente total, em qualquer variante, como o experimento da PP2 estabeleceu. Aqui não se mede presença, e sim **alcançabilidade a partir do ponto de entrada**, que varia. E há uma consequência do achado da PP2 que simplifica a coleta: como o bytecode é **idêntico** nas três variantes, não é necessário construir três artefatos. O grafo é construído uma vez, e **a variante é definida por seu conjunto de raízes**.

**Raízes.** Os métodos de endpoint de cada *controller*:

| *Controller* | Raízes | Presente em |
|---|---|---|
| `OrderGeneralController` | `getOrderById`, `listOrders` | toda variante |
| `OrderExternalController` | `createOrder` | `external`, linha de base |
| `OrderInternalController` | `updateStatusOrder` | `internal`, linha de base |

**Conjuntos.** Seja `R(c)` o fecho transitivo dos métodos alcançáveis a partir das raízes do *controller* `c`. Então:

```
R(default)  = R(geral)
R(internal) = R(geral) ∪ R(interno)
R(external) = R(geral) ∪ R(externo)
```

O **específico** de uma variante é o conjunto de métodos que **só ela** alcança; o **comum** é o restante de `R(variante)`. Daí, em granularidade de método:

```
Reuse Percent(v) = |comum(v)| / |R(v)| × 100
```

**Escopo da contagem — decisão de método.** Contam-se **apenas os métodos declarados no projeto**. Ficam de fora:

- os métodos sintetizados pelo compilador Kotlin — acessores de propriedade, `equals`, `hashCode`, `toString`, `copy`, `componentN` das *data classes*, pontes de argumento padrão (`$default`) e acessores sintéticos (`access$`);
- todo método cujo proprietário esteja fora de `edu.uerj` (Jakarta, Hibernate, biblioteca-padrão). Tais chamadas são **folhas** do grafo: registram-se, mas não se percorrem.

A razão é substantiva, e não de conveniência: os acessores das entidades e dos DTOs são numerosos e dominariam a contagem, deslocando a medida de **lógica reusada** para **estrutura de dados percorrida**. O reuso que a tese reivindica é o das regras de negócio.

### 6.4 Ameaças à validade próprias desta medição

Registram-se no capítulo de Avaliação, junto às demais:

- **Análise estática.** O grafo não observa **reflexão**, e resolve o **despacho dinâmico** de forma conservadora: uma chamada por `invokeinterface` ou `invokevirtual` sobre um supertipo é atribuída ao proprietário declarado na instrução, não ao alvo efetivo em execução.
- **Injeção e *proxying*.** As chamadas que CDI e JPA realizam por *proxy* não aparecem no bytecode do chamador. Nos fluxos medidos isso não altera o resultado — os *controllers* invocam o serviço diretamente, e o serviço invoca os repositórios diretamente —, mas o limite deve ser declarado.
- **Fronteira do grafo.** Métodos de *framework* não são percorridos; portanto a medida é de reuso **dos ativos próprios**, e não do sistema em execução.
- **Sensibilidade à decisão de escopo.** A exclusão dos métodos sintetizados é uma escolha; reportar, em paralelo, a contagem que os inclui, como análise de sensibilidade.

### 6.5 Procedimento de coleta

Executado pelos scripts versionados em `docs/case-study/scripts/` (ver o `README.md` daquele diretório, que registra propósito, requisitos, invocação exata e leitura da saída de cada um).

1. Compilar o projeto e confirmar que o bytecode está atualizado em relação ao fonte.
2. Extrair o grafo de chamadas com `javap -c -p` sobre `build/classes/kotlin/main`, restringindo os arestas a proprietários em `edu.uerj`.
3. Calcular o fecho transitivo a partir das raízes de cada *controller*.
4. Derivar `comum` e `específico` por variante e calcular o *reuse percent* em granularidade de método.
5. Repetir incluindo os métodos sintetizados (análise de sensibilidade).
6. Registrar a versão exata das ferramentas (`cloc`, JDK) e os comandos.

O *reuse percent* em **linhas de código** (seção 2.1) permanece como a métrica da **PP1**, coletado por `metrica-reuso.py`.

---

## 7. Reuse per Method — instrumento da PP3

> **Nomenclatura.** A medição em granularidade de método chama-se **Reuse per Method**. A designação provisória "*reuse percent* por método" foi abandonada: ela sugeria uma variante do *reuse percent* de Frakes e Terry (1996), quando se trata de grandeza distinta, com objeto e finalidade próprios.
>
> **Realocação.** O Reuse per Method **não** responde à PP1 e foi movido para a **PP3**. A justificativa está em 7.1.

### 7.1 Por que o Reuse per Method não serve à PP1

Ressalva imprescindível, sob pena de o número ser lido ao contrário.

**Ele não mede duplicação.** Um método alcançável apenas pelo modo externo — `OrderService.createOrder`, por exemplo — **não está duplicado**: é declarado uma única vez e pertence exclusivamente àquele fluxo. Que a variante externa exiba Reuse per Method de 61,54% **não** significa que 38,46% do seu código esteja replicado alhures. Significa que pouco mais de um terço dos métodos que ela torna alcançáveis são privativos do seu fluxo de negócio — o que é **especialização**, exatamente o que um BFF deve fazer.

**O que ele mede é a sobreposição dos fluxos**: que fração dos métodos alcançáveis a partir dos pontos de entrada de uma variante também é alcançável a partir dos de outra. Responde a "quanto os modos compartilham em execução", e não a "quanto de código foi reaproveitado em vez de reescrito".

**Limite lógico.** A alcançabilidade **não pode**, em princípio, provar ausência de duplicação: duas cópias de uma mesma regra, em classes diferentes, seriam dois nós distintos no grafo, e a métrica não as reconheceria como cópias. Prova de não duplicação exige detecção de clones ou inspeção estrutural — não análise de alcance.

**A PP1, portanto, é respondida por dois instrumentos, e nenhum é o Reuse per Method:**

1. o **reuse percent em LOC** (seção 2.1) — quanto do código de uma variante não foi escrito para ela: 94,82% na interna, 95,17% na externa;
2. o **fato estrutural**, verificável por inspeção: existe **um** `OrderService`, declarado uma vez, cujos métodos `createOrder` e `updateStatus` são alcançados por *controllers* distintos; existe **um** conjunto de repositórios e **um** domínio, sem anotação de variabilidade. Nenhuma validação, cálculo ou fluxo é reescrito por variante.

### 7.2 Por que o Reuse per Method serve à PP3

A PP3 pergunta se a abordagem **reduz o número de pontos de entrada** expostos por artefato. A contagem de rotas responde, mas responde pouco: dizer que a variante externa expõe três rotas em vez de quatro não dimensiona o que cada rota carrega atrás de si.

O Reuse per Method converte a contagem de rotas em contagem de **lógica exposta**. Suprimir o *controller* interno do artefato externo não retira apenas uma rota: torna **inalcançáveis a partir da rede** os cinco métodos que estavam atrás dela — `OrderInternalController.updateStatusOrder`, `OrderService.updateStatus`, `Order.checkOrderStatus`, `OrderStatus.getById` e `OrderStatus.getByDescription`.

| Variante | Métodos alcançáveis | Suprimidos frente à linha de base | Reuse per Method |
|---|---|---|---|
| `bff-default` | 7 | 11 (61,1%) | 100% |
| `bff-internal` | 13 | 5 (27,8%) | 61,54% |
| `bff-external` | 13 | 5 (27,8%) | 61,54% |
| linha de base | 18 | — | — |

**Aderência à métrica de referência.** Manadhata e Wing (2011) definem a superfície de ataque pelos **pontos de entrada** e ponderam-nos por dano potencial. A contagem de rotas é não ponderada, e a ponderação integral exigiria estimar dano e custo por rota — o que não se faz. O Reuse per Method oferece uma aproximação intermediária e defensável: **o número de métodos próprios que cada ponto de entrada torna alcançáveis** é um indicador do que está ao alcance de quem atinge aquela rota. Não é a ponderação de Manadhata e Wing, e não deve ser apresentado como tal; é uma grandeza adicional, na mesma unidade conceitual — o alcance a partir do ambiente.

**O que isso acrescenta à tese.** A *segurança por ausência* reivindica que a operação alheia ao cliente não exista no artefato dele. O Reuse per Method mostra que a supressão **não para no ponto de entrada**: a lógica de negócio atrás dele perde caminho a partir do ambiente. Não há rota, não há *bean*, não há invocador — e, por consequência, não há caminho até `OrderService.updateStatus`.

**Ressalva simétrica, que acompanha toda a tese.** A classe permanece no artefato e segue carregável por reflexão, conforme estabelecido na PP2. "Inalcançável" significa aqui **sem caminho a partir de qualquer ponto de entrada registrado** — a definição de superfície de ataque adotada —, e não remoção física do bytecode.

### 7.3 Achados incidentais da coleta

Dois fatos que o grafo revelou e que merecem registro na Avaliação:

- **`OrderRepository.save` é o único método alcançável pelos dois modos de escrita e por nenhum modo de leitura.** É reuso estrutural aparecendo no dado, sem ter sido procurado: a porta de saída de escrita é uma só, partilhada por `createOrder` e `updateStatus`.
- **A simetria das variantes.** Interna e externa alcançam 13 métodos cada, com 5 exclusivos cada. A simetria não foi imposta pelo método; decorre do protótipo.

### 7.4 Fundamentação — ✅ resolvida (10 jul. 2026)

A busca prevista foi executada segundo o *modus operandi* de `docs/FONTES-ACADEMICAS.md` e localizou **duas fontes revisadas por pares, ambas verificadas no texto integral**, que sustentam o Reuse per Method. O instrumento continua sendo **derivado**, e não métrica consagrada — mas nenhuma de suas duas operações é invenção deste trabalho.

**a) A granularidade de método e a medição de reuso por chamadas — Devanbu et al. (1996).** No artigo do ICSE, a definição de componente inclui explicitamente a função: `c_j` é "*a software component (module, class, **function**, subsystem)*". E o método de medição é declaradamente baseado em invocações: mede-se o benefício de reuso "*searching the source code of S for instances of language-based reuse such as **subroutine calls***", com `Cu(S, c)` designando o **número de vezes** que o componente `c` é usado no sistema `S`. Contar métodos e ler reuso a partir de chamadas é, portanto, prática documentada em literatura revisada por pares.

**b) A alcançabilidade a partir dos pontos de entrada — Ponta, Plate e Sabetta (2018).** No artigo do ICSME, um **construto de programa** é "*a structural element of the source code characterized by a type (e.g., package, class, constructor, **method**)*". A análise estática computa `R_ai`, "*the set of all constructs […] that are found reachable starting from the application a*", por meio de "*a graph of all library constructs reachable from the application constructs*". Os autores definem ainda métricas — como a *Reachable Body Stability* — que são **razões calculadas sobre o conjunto de construtos alcançáveis**, exatamente a forma do Reuse per Method. E o artigo trata o problema como consequência do **reuso** de bibliotecas, ligando alcançabilidade a **explorabilidade**.

**Delimitação das atribuições — o que cada fonte não diz.**

| Fonte | Sustenta | **Não** sustenta |
|---|---|---|
| Devanbu et al. (1996) | granularidade de função; reuso medido por chamadas | alcançabilidade a partir de pontos de entrada |
| Ponta, Plate e Sabetta (2018) | grafo de alcançabilidade desde pontos de entrada; razões sobre o conjunto alcançável; vínculo com explorabilidade | reuso entre variantes de um mesmo código-fonte; o termo "superfície de ataque" (que permanece ancorado em MANADHATA; WING, 2011) |

**Ameaça à validade que a própria fonte impõe.** Devanbu et al. (1996) advertem que medidas internas de reuso podem ser **artificialmente infladas**: "*one can artificially insert function invocations into the different branches of a conditional*", e "*in most cases, it is infeasible to detect occurrences of spurious inflation*". A advertência recai sobre o Reuse per Method: a contagem de métodos alcançáveis é sensível à granularidade com que o código foi decomposto em funções, e um domínio decomposto em métodos menores produziria números diferentes sem qualquer mudança de comportamento. Isso **deve** constar das ameaças à validade do capítulo de Avaliação.

Registro das fontes em `REFERENCIAS_CIENTIFICAS.md` (itens 8.5 e 5.4) e citações diretas em `CITACOES_ABNT.md`.
