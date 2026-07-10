# Avaliação, Resultados e Discussão

> ✅ **Implementado no TCC** — transposto para o arquivo `Proposta TCC …md` como capítulo *Avaliação, Resultados e Discussão* (seções 7.1–7.8), entre o *Desenvolvimento* e as *Referências*. **A fonte da verdade passa a ser o TCC**; este documento é mantido apenas como registro de trabalho.
>
> Diferenças aplicadas na transposição: a seção 7.9 (*Evidências pendentes*) **não foi transposta** — era nota de trabalho, e seu conteúdo se distribuiu entre os marcadores de figura e a nota de reprodutibilidade; os três scripts passaram a ser reproduzidos por inteiro no **Apêndice A**, e as remissões a `docs/case-study/scripts/` viraram remissões ao apêndice; os cabeçalhos foram rebaixados um nível; e a *Organização do Trabalho* passou a descrever **dez partes** mais o apêndice.
>
> 🧊 **Scripts congelados.** São o código publicado no Apêndice A, ao lado dos números deste capítulo: **`metrica-reuso.py`** (A.1, seção 7.4.1), **`grafo-metodos.py`** (A.2, seção 7.3.2) e **`evidencia-rotas.py`** (A.3, seções 7.2.3 e 7.2.4). Editar qualquer um obriga a reexecutá-lo, confrontar com os números do capítulo e atualizar o apêndice — ver a regra no *modus operandi* de `docs/ROADMAP-CAPITULOS-TCC.md`. Scripts **novos** só entram nesse regime ao serem transpostos.
>
> **Convenções:** citações no sistema autor-data (ABNT NBR 10520); 3ª pessoa acadêmica. **Todas as medições previstas foram executadas**; não restam marcadores de pendência. Dois itens foram deliberadamente postos fora de escopo e assim declarados no texto: o *build* nativo (seção 7.2.5) e a reprodução da alternativa em runtime (seção 7.6).

Este capítulo apresenta os resultados dos experimentos definidos na Metodologia. A seção 7.1 delimita as variantes e o objeto de medição de cada pergunta. A 7.2 responde à PP2 — a pergunta central —, separando a classe compilada do ponto de entrada registrado e apresentando, ao final, a evidência que independe do *framework*. A 7.3 responde à PP3, a 7.4 à PP1 e a 7.5 à PP4. A 7.6 contrasta os achados com a alternativa de *feature flag* em tempo de execução. A 7.7 discute os resultados frente aos objetivos e a 7.8 registra as ameaças à validade.

---

## 7.1 Objeto da avaliação

Duas distinções precedem a leitura dos números.

**Quais variantes.** Quatro. Três são as variantes de interesse, geradas por `make container/build/variants`; a quarta, com ambos os modos ativos, serve de **linha de base** para as comparações:

| Variante | `BFF_API_EXTERNAL_ACTIVE` | `BFF_API_INTERNAL_ACTIVE` |
|---|---|---|
| `bff-default` | `false` | `false` |
| `bff-internal` | `false` | `true` |
| `bff-external` | `true` | `false` |
| linha de base | `true` | `true` |

Todas derivam do **mesmo código-fonte**, sem qualquer alteração entre os *builds* — condição necessária para que as diferenças observadas sejam atribuíveis exclusivamente à resolução da variabilidade em tempo de compilação.

**Qual objeto se mede.** Não há um objeto único, e supor que houvesse seria erro de método: cada pergunta de pesquisa recai sobre o objeto em que a propriedade que ela investiga de fato reside.

| Pergunta | Objeto medido | Por quê |
|---|---|---|
| **PP2** — ausência do ponto de entrada | **Artefato de produção** (imagem gerada pelo `Dockerfile`) e sua execução | A afirmação é sobre o que é **entregue**. Medi-la sobre um *build* local — dependente do diretório de saída, do cache do sistema de *build* e das variáveis da sessão — não a sustentaria. |
| **PP3** — rotas expostas | **Artefato de produção**, em execução | Idem: a superfície de ataque é a do que se implanta. |
| **PP3** — lógica alcançável (*Reuse per Method*) | **Bytecode compilado** | O bytecode é **idêntico** nas três variantes, como estabelece a seção 7.2.1; o que distingue uma variante é o conjunto de **pontos de entrada registrados**, isto é, o conjunto de raízes do grafo de chamadas. Medir sobre o artefato de produção devolveria, para todas, o mesmo grafo. |
| **PP1** — reuso | **Código-fonte** | A comunalidade e a variabilidade da linha de produto residem no fonte. No bytecode entregue, todas as classes estão presentes em toda variante, e a medição devolveria trivialmente 100%. |
| **PP4** — custo | **A arquitetura**, por inspeção | Os custos decorrem do modo de derivação dos artefatos, e não de grandeza aferível em qualquer um deles. |

A regra que governa a escolha é única: **mede-se onde a propriedade existe.** A ausência do ponto de entrada é propriedade do artefato entregue e só nele pode ser verificada. O reuso é propriedade do código escrito e só nele pode ser quantificado. Confundir os dois objetos produziria, num caso, uma afirmação sobre o que não se entrega; no outro, um resultado trivialmente igual a 100%.

Registre-se a consequência para a validade: as medições de PP1 e do *Reuse per Method* recaem sobre artefatos do ambiente de desenvolvimento — o fonte e o bytecode compilado localmente —, e não sobre a imagem de produção. Isso é discutido em 7.8.

---

## 7.2 PP2 — Ausência verificável do ponto de entrada

> **PP2:** a resolução da variabilidade em tempo de compilação garante que as operações de um modo não estejam presentes no binário do outro modo?

A resposta exige separar dois objetos que a formulação original da tese tratava como um só: a **classe compilada** e o **ponto de entrada registrado**.

### 7.2.1 O bytecode da classe permanece em todas as variantes

A inspeção do JAR da aplicação (`app/*.jar`) devolve, nas **três** variantes de produção, o mesmo conteúdo:

```
edu/uerj/adapters/api/v1/OrderExternalController.class
edu/uerj/adapters/api/v1/OrderGeneralController.class
edu/uerj/adapters/api/v1/OrderInternalController.class
```

A razão é que `app/*.jar` é a saída do **compilador Kotlin**, anterior à fase de *augmentation* do Quarkus: a anotação `@IfBuildProperty` não instrui o compilador a omitir a classe.

### 7.2.2 O ponto de entrada varia conforme a configuração de build

O que a anotação controla é o **registro do bean CDI** e a **geração do invocador REST**, cujo produto reside em `quarkus/generated-bytecode.jar`. Nesse artefato, as variantes divergem exatamente conforme as *flags*:

| Variante | *Beans* e invocadores presentes | Rotas expostas |
|---|---|---|
| `bff-default` | `OrderGeneralController_Bean`; invocadores de `getOrderById` e `listOrders` | `GET /v1/order/{orderId}`, `GET /v1/order/list` |
| `bff-internal` | os anteriores + `OrderInternalController_Bean` e invocador de `updateStatusOrder` | as anteriores + `PATCH /v1/order/{orderId}/delivery/{deliveryId}/{newStatus}` |
| `bff-external` | os anteriores + `OrderExternalController_Bean` e invocador de `createOrder` | as anteriores + `POST /v1/order/create` |

No artefato externo não há bean, invocador nem rota da operação interna; no interno, não há os da operação externa. Nenhuma variável de ambiente, nenhum parâmetro de execução e nenhuma reconfiguração em tempo de *deploy* reintroduzem o que não foi registrado no *build*.

### 7.2.3 A evidência primária: OpenAPI e atendimento HTTP

A evidência de 7.2.2 depende de convenções de nomenclatura internas do Quarkus. A evidência **primária** não depende de nenhuma: cada variante foi executada e interrogada quanto ao seu documento OpenAPI e quanto ao atendimento das rotas de negócio dos três modos.

**Rotas declaradas no documento OpenAPI de cada variante:**

| Variante | `/v1/order/list` e `/v1/order/{orderId}` | `/v1/order/create` | `/v1/order/{orderId}/delivery/{deliveryId}/{newStatus}` |
|---|---|---|---|
| `bff-default` | presentes | **ausente** | **ausente** |
| `bff-internal` | presentes | **ausente** | presente |
| `bff-external` | presentes | presente | **ausente** |
| linha de base | presentes | presente | presente |

O documento OpenAPI é gerado a partir dos *endpoints* efetivamente registrados. A rota do modo alheio **não consta** do contrato publicado pela variante — não está oculta nem desabilitada: não existe para o artefato.

**Atendimento HTTP.** Nenhum dos dois alvos é fixado de antemão. Antes de requisitar as operações de escrita, o procedimento **lista os pedidos da própria variante** (`GET /v1/order/list`) e deriva dela tudo o que precisa. Para o `PATCH`, escolhe um pedido e uma entrega existentes e calcula um avanço de status legítimo — `OrderService.updateStatus` só aceita status de `id` maior que o atual (seção 6.2). Para o `POST`, monta um pedido válido com um cliente real, um endereço e um contato seus, e um SKU cujo preço unitário coincide com o do catálogo, como a validação de `createOrder` exige.

A construção não é preciosismo. Requisições malformadas ou dirigidas a recursos inexistentes seriam recusadas pela própria aplicação, e a recusa nada diria sobre a existência da rota — no caso do recurso inexistente, o status devolvido seria inclusive indistinguível do da rota ausente. Derivando ambos os alvos dos *endpoints*, obtém-se evidência **positiva**: a operação não é apenas alcançada, mas se completa.

| Variante | `GET /v1/order/list` | `POST /v1/order/create` | `PATCH` de avanço de status |
|---|---|---|---|
| `bff-default` | `200` | `405` | `404` |
| `bff-internal` | `200` | `405` | **`202`** |
| `bff-external` | `200` | **`201`** | `404` |
| linha de base | `200` | **`201`** | **`202`** |

Onde a rota **existe**, a requisição alcança o manipulador e a operação se conclui. O `201 Created` da rota de criação devolve um pedido novo, persistido; o `202 Accepted` da rota de status efetiva a transição. Ambas as mutações são observáveis na execução seguinte: a entrega escolhida reaparece já no estado subsequente, e o pedido criado por `bff-external` torna-se, ele próprio, alvo elegível para a linha de base. As rotas expostas não apenas respondem — elas fazem o que prometem.

Onde a rota **não existe**, a requisição não alcança manipulador algum, e o servidor responde `404` **ou** `405`.

A alternância entre `404` e `405` **não é ruído, e é preciso explicá-la**. A URI `/v1/order/create` casa com o template `/v1/order/{orderId}`, do *controller* de leitura, atendido apenas por `GET`. Na ausência do *controller* externo, um `POST` àquela URI encontra o **recurso**, mas não o **método**, e recebe `405 Method Not Allowed`. Já a URI da operação interna não casa com template algum, e recebe `404`. Os dois status atestam a mesma coisa — a requisição não foi atendida por manipulador nenhum —, e a diferença entre eles é acidente do roteamento, não propriedade da arquitetura.

Há ainda uma segunda razão, independente da primeira, para não confiar no status HTTP como critério decisivo: **ele é ambíguo**. Um `404` tanto pode significar "não há rota" quanto "a rota existe e o recurso pedido não existe". É precisamente essa ambiguidade que a derivação do alvo pela listagem elimina: sabendo-se que o pedido e a entrega existem, o `404` das variantes que suprimem a operação só pode significar ausência de rota.

Daí decorre a consequência de método: **o critério primário é a ausência da rota no documento OpenAPI**, e o status HTTP é corroborativo. Um procedimento de verificação que esperasse `404` — como o que este trabalho chegou a prescrever — acusaria falso negativo nas rotas capturadas por outro template e, sobre recurso inexistente, falso positivo nas rotas expostas. Um leitor que o executasse concluiria, nos dois casos, coisa diversa da verdadeira.

O controle está na primeira coluna: `GET /v1/order/list` responde `200` em **toda** variante. A aplicação está de pé, o banco responde, e o que não é atendido não o é por não existir.

### 7.2.4 A documentação acompanha a metamorfose

O documento OpenAPI não é um arquivo mantido à parte, redigido por quem escreve a API e sujeito a envelhecer em relação a ela. A documentação do Quarkus registra que a extensão SmallRye OpenAPI existe "para gerar a especificação OpenAPI v3 da sua API" (QUARKUS, 2026e), e que a interface do Swagger "é gerada automaticamente a partir da sua especificação OpenAPI" (QUARKUS, 2026e).

Encadeando: a especificação é derivada dos *endpoints* registrados; a interface é derivada da especificação. Ambas são, portanto, **artefatos derivados da mesma variabilidade** que produz o artefato executável. A verificação, em cada variante, de que a interface é servida e busca o documento:

| Variante | `GET /docs` | Referencia `/openapi` |
|---|---|---|
| `bff-default` | `200` | sim |
| `bff-internal` | `200` | sim |
| `bff-external` | `200` | sim |
| linha de base | `200` | sim |

Não se verificou o que a interface **renderiza**, e a omissão é deliberada. Sendo projeção do documento, a interface não pode exibir rota que ele não contenha; observá-la seria reobservar, por um renderizador, a evidência já estabelecida em 7.2.3. O que a tabela estabelece é a relação de projeção, e dela a conclusão decorre por dedução, não por nova medição.

**A consequência é uma propriedade que a arquitetura ganha sem ter pedido.** Cada variante publica um contrato que descreve exatamente as operações que ela expõe — nem mais, nem menos. O artefato interno documenta a atualização de status e ignora a criação de pedidos; o externo faz o inverso. Nenhum esforço de manutenção documental é exigido para que assim seja, e nenhuma divergência entre documento e comportamento é possível, porque o documento **é** o comportamento, projetado.

Trata-se de reforço à tese, e não de mero comodismo de ferramenta. A *segurança por ausência* afirma que a operação alheia ao cliente não existe no artefato dele. Um contrato que ainda a anunciasse — porque redigido à mão, ou porque derivado do código-fonte e não dos *endpoints* registrados — a manteria presente ao menos como promessa, indicando ao leitor do contrato uma superfície que ele poderia tentar alcançar. A geração a partir dos pontos de entrada registrados fecha essa fresta: a metamorfose atinge também aquilo que o artefato **diz de si**.

> **Nota de escopo.** A propriedade descrita depende de a documentação ser derivada dos *endpoints* registrados, e não de uma especificação escrita à parte. Ela é, portanto, uma propriedade da combinação entre a arquitetura proposta e a geração automática de contrato — não da arquitetura isoladamente.

### 7.2.5 Leitura do resultado

A PP2 recebe uma resposta **afirmativa quanto ao ponto de entrada** e **negativa quanto à classe**. A distinção não é acessória, e três consequências decorrem dela.

Primeiro, a formulação correta da tese é a que se ancora no **ponto de entrada**, e não na classe. A métrica de superfície de ataque adotada no Referencial Teórico define a superfície pelo conjunto de pontos de entrada por onde os dados entram no sistema a partir do ambiente (MANADHATA; WING, 2011). Onde não há rota registrada, não há via de entrada — e é isso, e apenas isso, que a métrica citada requer. Afirmar que a classe "não existe no binário" seria simultaneamente falso e desnecessário ao argumento.

Segundo, a classe remanescente é **inalcançável pelas vias que a superfície de ataque contabiliza**, mas permanece carregável por reflexão. A segurança por ausência elimina a via de entrada; não promete a remoção física do bytecode no empacotamento em JVM.

Terceiro, a evidência do bytecode gerado (7.2.2) é **corroborativa**, não fundacional: os nomes `..._Bean` e `...$quarkusrestinvoker$...` são convenções internas do Quarkus Arc e do RESTEasy Reactive, sujeitas a mudança entre versões. A conclusão repousa sobre a evidência de 7.2.3 — a ausência da rota no documento OpenAPI e no atendimento HTTP —, que independe do *framework* e é observável por qualquer cliente do artefato. A inspeção do bytecode explica **por que** a rota não existe; o OpenAPI e o HTTP estabelecem **que** ela não existe.

> **Forma forte da tese, não investigada.** A supressão física do bytecode da classe poderia ocorrer em um *build* nativo (GraalVM), cuja *dead code elimination* possivelmente removeria a classe inalcançável — o que tornaria a afirmação "o endpoint não existe no binário" literalmente verdadeira, e não apenas verdadeira quanto ao ponto de entrada. **Este trabalho não testa essa hipótese.** Ela é registrada como **trabalho futuro**, e nada do que aqui se afirma depende dela: a propriedade reivindicada — ausência do ponto de entrada — está estabelecida no empacotamento em JVM, que é o que o protótipo entrega.

---

## 7.3 PP3 — Redução da superfície de ataque

> **PP3:** a abordagem reduz o número de pontos de entrada expostos por artefato, comparada a um *build* que contém todos os controllers?

A resposta é dada em duas medidas. A primeira conta os **pontos de entrada**. A segunda, o **Reuse per Method**, conta a **lógica que cada ponto de entrada torna alcançável** — e é ela que dimensiona o que a supressão de uma rota de fato retira do artefato.

### 7.3.1 Pontos de entrada

Contam-se as rotas REST de negócio expostas por variante, conforme a tabela da seção 7.2.2. Excluem-se da contagem os endpoints de infraestrutura (`/health`, `/openapi`, `/docs`), presentes em toda variante e alheios à variabilidade.

| Variante | Rotas de negócio | Redução frente ao *build* completo |
|---|---|---|
| *Build* completo (ambos os modos) | 4 | — (linha de base) |
| `bff-external` | 3 | 1 rota (25%) |
| `bff-internal` | 3 | 1 rota (25%) |
| `bff-default` | 2 | 2 rotas (50%) |

A redução é modesta em valor absoluto porque o protótipo expõe poucas operações; o que o número dimensiona não é a magnitude do ganho, mas sua **natureza**: a rota suprimida deixa de integrar o conjunto de pontos de entrada do artefato, em vez de nele permanecer sob proteção. Em um BFF de porte industrial, a proporção de operações privativas de um modo tende a ser maior, e com ela a redução — extrapolação que **este trabalho não mede** e que fica registrada em 7.8.

> **Nota de escopo.** A métrica de Manadhata e Wing (2011) pondera os pontos de entrada por dano potencial e custo de acesso. A contagem acima é **não ponderada**, o que a torna uma aproximação. Adotá-la integralmente exigiria estimar dano e custo por rota, o que não se fez.

### 7.3.2 Lógica alcançável: o Reuse per Method

Contar rotas diz pouco sobre o que cada rota carrega atrás de si. Aplicou-se, por isso, o **Reuse per Method**, definido na Metodologia (seção 5.5.3): construído o grafo de chamadas entre os métodos declarados no projeto, computa-se o conjunto `R(v)` dos métodos próprios alcançáveis, por fecho transitivo, a partir das rotas registradas em cada variante.

| Variante | Métodos alcançáveis `\|R(v)\|` | Comuns | Específicos | Suprimidos frente à linha de base | *Reuse per Method* |
|---|---|---|---|---|---|
| `bff-default` | 7 | 7 | 0 | 11 (61,1%) | 100% |
| `bff-internal` | 13 | 8 | 5 | 5 (27,8%) | 61,54% |
| `bff-external` | 13 | 8 | 5 | 5 (27,8%) | 61,54% |
| linha de base (ambos os modos) | 18 | — | — | — | — |

O núcleo alcançável em **toda** variante reúne sete métodos: os dois endpoints de leitura, `OrderService.getOrder` e `OrderService.listAll`, e três consultas de `OrderRepository`. Um método é alcançável pelos dois modos de escrita e por nenhum modo de leitura — `OrderRepository.save` —, o que expõe, no dado, o compartilhamento da porta de saída de escrita entre `createOrder` e `updateStatus`.

A leitura substantiva está nos **suprimidos**. Retirar o *controller* interno do artefato externo não retira uma rota apenas: torna inalcançáveis, a partir da rede, os cinco métodos que estavam atrás dela — `OrderInternalController.updateStatusOrder`, `OrderService.updateStatus`, `Order.checkOrderStatus`, `OrderStatus.getById` e `OrderStatus.getByDescription`. Simetricamente, o artefato interno não oferece caminho algum até `OrderService.createOrder`, `CustomerRepository.findById`, `ProductRepository.findBySku` e `Delivery.addItem`. A variante `default` torna inalcançáveis 11 dos 18 métodos próprios da linha de base.

É o que a *segurança por ausência* reivindica, agora medido: a supressão **não para no ponto de entrada**. Onde não há rota registrada, não há caminho até a lógica de negócio que ela servia. A afirmação é mais forte, e mais próxima do que a métrica de referência mede, do que "três rotas em vez de quatro".

**O que o Reuse per Method não afirma.** A coluna *Reuse per Method* exprime a **sobreposição dos fluxos** — que fração da lógica alcançável por uma variante também o é por outra —, e **não** duplicação. Que a variante externa exiba 61,54% não significa que 38,46% de seu código esteja replicado: os cinco métodos exclusivos são declarados uma única vez e pertencem àquele fluxo, o que é especialização. Por essa razão a métrica responde à PP3 e não à PP1, conforme a seção 5.5.3.

**Ressalva simétrica.** "Inalcançável" significa aqui **sem caminho a partir de qualquer ponto de entrada registrado** — a definição de superfície de ataque adotada (MANADHATA; WING, 2011) —, e não remoção física do bytecode: a classe permanece no artefato e segue carregável por reflexão, conforme estabelecido em 7.2.3.

**Precedente do instrumento.** A alcançabilidade computada por grafo a partir dos pontos de entrada da aplicação, com razões calculadas sobre o conjunto alcançável, é a técnica empregada por Ponta, Plate e Sabetta (2018) para avaliar a explorabilidade de vulnerabilidades em bibliotecas reusadas. A granularidade de método e a leitura de reuso a partir de invocações estão em Devanbu et al. (1996). A composição das duas operações, aplicada a variantes derivadas de um mesmo código-fonte, é contribuição instrumental deste trabalho — e não métrica consagrada.

---

## 7.4 PP1 — Reuso sem duplicação

> **PP1:** é possível derivar múltiplos artefatos BFF especializados a partir de um único código-fonte, sem duplicar regras de negócio, serviços e repositórios?

A resposta combina uma medida de volume — o *reuse percent* (FRAKES; TERRY, 1996) — e um fato estrutural. O método de cálculo, a classificação dos ativos e o procedimento de coleta estão na Metodologia (seção 5.5.2).

### 7.4.1 Reuse percent

A contagem recai sobre `src/main/kotlin/**`, com `cloc` 2.08, descontadas linhas em branco e comentários. O código-fonte tem **1720 LOC**, dos quais 79 no `OrderExternalController` e 85 no `OrderInternalController` — os dois únicos arquivos que carregam anotação de variabilidade. A base comum soma, portanto, **1556 LOC**.

| Variante | `RSI` (LOC comum) | `SSI` (LOC específico) | *Reuse Percent* |
|---|---|---|---|
| `bff-default` | 1556 | 0 | 100% (caso-limite; ver ressalva) |
| `bff-internal` | 1556 | 85 | **94,82%** |
| `bff-external` | 1556 | 79 | **95,17%** |
| linha de base (ambos os modos) | 1556 | 164 | 90,47% |

A especialização de um BFF inteiro custou **menos de noventa linhas**. É esse o resultado que a PP1 pede, e ele não depende de interpretação: as linhas específicas de cada variante são as de um único *controller*.

**Análise de sensibilidade.** Três DTOs servem exclusivamente ao modo externo — `NewOrderDTO`, `ItemInNewOrderDTO` e `PaymentInNewOrderDTO`, somando 25 LOC — e foram classificados como comuns por não carregarem anotação de variabilidade e permanecerem ativos em toda variante. Reclassificando-os como específicos da variante externa, a base comum cai para 1531 LOC e o *reuse percent* externo passa de 95,17% para **93,64%**, uma queda de 1,53 ponto percentual; o interno passa a 94,74%. A escolha conservadora, portanto, favorece a tese de reuso, mas por margem pequena. Registre-se que o modo **interno não possui DTO exclusivo algum**: a sensibilidade é assimétrica por propriedade do protótipo, e não do método.

Há, ademais, razão estrutural para a classificação adotada: `NewOrderDTO` é referenciado pela assinatura de `OrderService.createOrder`, isto é, pela camada compartilhada, presente e ativa em toda variante. Atribuí-lo à variante externa contradiria o fato de o serviço comum depender dele.

> **Ressalva sobre a variante `default`.** Com `SSI = 0`, o *reuse percent* é 100% por construção. O valor é correto e substantivamente vazio: apenas confirma que a variante não possui código específico. Não deve ser reportado como resultado favorável.

### 7.4.2 O fato estrutural: ausência de duplicação

O *reuse percent* mede volume, e volume não decide a questão da duplicação. A PP1 pergunta se as variantes são derivadas **sem duplicar** regras de negócio, e a isso responde a inspeção do código:

- as regras de negócio residem em **uma única** classe de serviço, `OrderService`, declarada uma vez e injetada nos **três** *controllers*;
- as portas de saída residem em repositórios igualmente **únicos**, sobre uma única classe-base;
- o **domínio** — as onze entidades e o enum `OrderStatus` — não contém anotação de variabilidade e é idêntico em toda variante;
- a especialização por modo vive **apenas** nos adaptadores de entrada, e neles apenas um método de endpoint.

Nenhuma validação, cálculo ou fluxo é reescrito por variante. O grafo de chamadas da seção 7.3.2 corrobora o ponto por outro caminho: `OrderService.createOrder` e `OrderService.updateStatus` são nós **únicos**, alcançados de pontos de entrada distintos, e `OrderRepository.save` é um só, partilhado pelos dois fluxos de escrita.

Cabe delimitar o alcance dessa evidência. Ausência de duplicação é aqui estabelecida por **inspeção estrutural**, não por métrica. Nenhum instrumento deste trabalho detectaria duas cópias de uma mesma regra alojadas em classes diferentes — nem o *reuse percent*, que apenas somaria as linhas de ambas, nem a alcançabilidade da seção 7.3.2, que veria dois nós distintos sem reconhecê-los como cópias. Detecção de clones seria o instrumento adequado, e este trabalho não a emprega. O que se afirma, portanto, é que a duplicação **não existe no código examinado**, e não que a arquitetura a torne impossível.

### 7.4.3 Leitura do resultado

A PP1 recebe resposta **afirmativa**. Três artefatos especializados foram derivados de um mesmo código-fonte, sem alteração alguma entre os *builds*, com mais de 94% das linhas de cada variante compartilhadas e com regras de negócio, serviços e repositórios declarados uma única vez.

Registre-se o limite da métrica, apontado pela própria fonte que a autoriza: Devanbu et al. (1996) advertem que medidas internas de reuso podem ser artificialmente infladas — pode-se "inserir artificialmente invocações de função nos diferentes ramos de uma condicional" — e que, "na maioria dos casos, é inviável detectar ocorrências de inflação espúria". A advertência recai sobre qualquer contagem interna, incluindo as deste capítulo, e é retomada em 7.8.

---

## 7.5 PP4 — Custo operacional

> **PP4:** que custos a abordagem impõe, dada a necessidade de um *build*, um *pipeline* e um artefato distintos por variante?

A PP4 é respondida **qualitativamente**, e sua resposta é certa: os custos decorrem da própria arquitetura e são verificáveis por inspeção, sem medição. Nenhuma grandeza de desempenho é aferida, pelas razões da seção 7.5.2.

### 7.5.1 Os custos da metamorfose

**Multiplicação de pipelines e artefatos.** Cada variante exige um *build* próprio e, portanto, um artefato, uma publicação e uma implantação próprios. Um `docker build` por variante substitui um `docker run` por variante. O que na abordagem em runtime é uma variável de ambiente torna-se, aqui, um objeto distinto a construir, versionar, publicar e implantar.

**Perda da reconfiguração em tempo de execução.** Alterar o modo de um artefato exige recompilá-lo e reimplantá-lo. Este é **o preço exato da PP2**: a mesma propriedade que impede qualquer configuração de ressuscitar o ponto de entrada suprimido impede que qualquer configuração o restitua quando isso for desejado. A rigidez em runtime não é efeito colateral da arquitetura — é a arquitetura, vista do outro lado. Onde a *feature flag* compra flexibilidade ao preço da exposição, a metamorfose compra ausência ao preço da flexibilidade.

**Crescimento combinatório das variantes.** Duas propriedades booleanas independentes admitem até quatro artefatos; `k` propriedades admitem até `2^k`. O protótipo, com `k = 2`, já produz quatro (`default`, `internal`, `external` e a linha de base). A abordagem escala mal no número de modos, e a mitigação — restringir as combinações admissíveis a um subconjunto declarado — é problema de linha de produto de software, não resolvido neste trabalho.

**Custo de verificação, e uma lacuna do protótipo.** Cada variante é um artefato distinto e, a rigor, cada uma demandaria sua própria verificação. O protótipo não faz isso: conforme a seção 6.7, o *job* de integração contínua compila com **ambas as propriedades ativas**, isto é, exercita a variante de linha de base — que **não é nenhum dos três artefatos entregues**. A suíte de testes valida o código-fonte, não os artefatos derivados dele. Registra-se como limitação da demonstração, não da arquitetura: nada impede que o *pipeline* execute a suíte contra cada variante, e uma abordagem que promete ausência verificável deveria fazê-lo.

**Rastreabilidade da variante em produção.** Um mesmo código-fonte passa a corresponder a binários distintos e não intercambiáveis. Saber qual variante responde em qual ambiente deixa de ser dedutível do código e passa a depender do registro de implantação.

### 7.5.2 O que não se afere, e por quê

Não se mede tamanho de artefato, tempo de inicialização nem consumo de memória. Essas grandezas responderiam a "o artefato ficou mais leve?", pergunta que este trabalho **não faz**: o objetivo geral reivindica **segurança** e **manutenibilidade**, e em momento algum leveza.

Tampouco haveria o que medir. A seção 7.2.1 estabelece que o bytecode das classes é **idêntico** nas três variantes — o que varia é o registro de *beans* e invocadores gerado na *augmentation*, cujo volume é desprezível frente ao da imagem. A equivalência de peso entre as variantes é, portanto, **consequência dedutível** do resultado da PP2, e não achado a coletar.

Convém enunciar a implicação para que nenhum leitor a infira ao contrário: **a metamorfose não encolhe o artefato entregue.** Ela atua sobre a superfície exposta, não sobre o volume distribuído. A classe do *controller* suprimido continua ocupando o mesmo espaço no JAR da aplicação — inerte, inalcançável e presente.

---

## 7.6 Contraste com a *feature flag* em tempo de execução

A alternativa em runtime mantém a operação no artefato por design: o endpoint é registrado, aparece no documento OpenAPI, responde a requisições HTTP e é recusado por uma verificação condicional. Sua superfície de ataque inclui a operação enquanto ela existir como ponto de entrada (MANADHATA; WING, 2011), e a correção da recusa depende de configuração correta, de lógica correta e da ausência de falhas no mecanismo de configuração.

O contraste com os resultados de 7.2 é direto, e pode ser lido nas três evidências ali reunidas. Na abordagem em runtime, o *bean* e o invocador **são gerados**; a rota **consta** do documento OpenAPI publicado pelo artefato; e a requisição **alcança** um manipulador, que decide recusá-la. Na abordagem metamórfica, nenhuma das três coisas ocorre: não há *bean*, a rota não consta do contrato, e não há manipulador a ser alcançado. Não existe recusa a ser executada, porque não existe rota a ser atingida.

A diferença, contudo, é de **finalidade, não de mérito** — manter o código presente é o que habilita os usos legítimos da *feature flag*, discutidos no capítulo *Trabalhos Relacionados* (seção 3.2).

> **Delimitação.** Este trabalho **não reproduz** a alternativa em runtime. Fazê-lo exigiria construir uma quinta variante, com a `@IfBuildProperty` substituída por verificação condicional, e medi-la — sem que disso resultasse informação nova: a presença do ponto de entrada é propriedade **definidora** daquela abordagem, não hipótese a testar. O contraste acima decorre do que a *feature flag* é, por construção, e do que a seção 7.2 estabeleceu por medição sobre o artefato metamórfico. Registra-se como escolha de escopo, e não como omissão.

---

## 7.7 Discussão

Retomam-se os objetivos específicos e as afirmações da Proposta, indicando o que os dados sustentam.

- **Derivar variantes de um código-fonte único.** Sustentado. As três variantes foram geradas sem qualquer alteração de código, apenas por *flags* de *build*.
- **Ausência da operação alheia ao cliente.** Sustentado **no nível do ponto de entrada**; **não sustentado** no nível da classe compilada, no empacotamento em JVM. A afirmação publicada deve ser a primeira.
- **Redução da superfície de ataque.** Sustentado, em contagem não ponderada de rotas e, adicionalmente, em contagem da lógica alcançável: cada variante de escrita torna inalcançáveis 5 dos 18 métodos próprios da linha de base, e a variante `default`, 11.
- **Reuso sem duplicação.** Sustentado, e agora quantificado: 94,82% e 95,17% de *reuse percent* nas variantes interna e externa, com serviço, repositórios e domínio declarados uma única vez. A ausência de duplicação é estabelecida por inspeção estrutural, não por detecção de clones (seção 7.4.2).
- **Custo da abordagem.** Explicitado, e não minimizado. O preço da ausência verificável é a **rigidez em tempo de execução**: a mesma propriedade que impede uma configuração de ressuscitar o ponto de entrada impede que ela o restitua quando desejado (seção 7.5.1). Somam-se a multiplicação de artefatos, o crescimento combinatório das variantes e o custo de verificar cada uma delas. Nenhuma redução de peso é reivindicada nem observada.
- **Melhor manutenibilidade.** Sustentado por inferência, não por medição direta: as regras de negócio existem em um único ponto e cada alteração se reflete em todas as variantes, condição que Taibi, Lenarduzzi e Pahl (2018) associam à redução do esforço de manutenção. A revisão de casos industriais de Chen, Usman e Badampudi (2024) reporta, na mesma direção, que o esforço para corrigir ou isolar erros é menor em ativos reusáveis — registrando, porém, que a proporção de erros de correção demorada é maior neles, e que a relação entre nível de reuso e esforço não é uniformemente favorável entre os casos. Este trabalho **não mede** manutenibilidade (por esforço de alteração ou densidade de defeitos); afere o **reuso**, que é seu antecedente. A distinção deve permanecer explícita, sob pena de o trabalho afirmar mais do que verificou.

O achado de 7.2 não enfraquece a contribuição: ele a delimita. A arquitetura entrega o que a métrica de superfície de ataque mede — a supressão do ponto de entrada — e não entrega o que ela não mede — a remoção física do bytecode. Reconhecer a fronteira entre as duas coisas é o que distingue uma tese verificada de uma tese apenas enunciada.

---

## 7.8 Ameaças à validade

**Validade de construto.** A contagem de pontos de entrada é não ponderada, enquanto a métrica de referência pondera por dano e custo de acesso (MANADHATA; WING, 2011). A classificação dos DTOs de uso exclusivo como ativos comuns é conservadora e favorece a tese de reuso; a análise de sensibilidade da seção 7.4.1 mede o efeito dessa escolha e o encontra pequeno (1,53 ponto percentual). A **manutenibilidade**, enunciada no objetivo geral, é inferida a partir do reuso e não medida por instrumento próprio. A **resiliência** é enunciada no objetivo como decorrência esperada da menor exposição, e não é aferida por nenhum procedimento deste trabalho.

**Inflação de medidas internas de reuso.** Devanbu et al. (1996) advertem que medidas internas de reuso podem ser artificialmente infladas, e que detectar tal inflação é, na maioria dos casos, inviável. A advertência recai sobre as duas contagens deste capítulo. O *reuse percent* é sensível ao tamanho dos *controllers*: adaptadores mais finos elevariam o percentual sem alterar comportamento algum. O *Reuse per Method* é sensível à granularidade com que o código foi decomposto em funções: um domínio dividido em métodos menores produziria outros números pela mesma lógica. Nenhuma das duas medidas, portanto, deve ser lida como grandeza absoluta ou comparada a valores de outros sistemas.

**Ausência de duplicação, e como ela foi estabelecida.** Nenhum instrumento aqui empregado detecta clones. Duas cópias de uma mesma regra em classes distintas escapariam ao *reuse percent*, que somaria as linhas de ambas, e ao grafo de chamadas, que veria dois nós sem reconhecê-los como cópias. A afirmação sustentada é a de que não há duplicação **no código examinado**, verificada por inspeção estrutural — não a de que a arquitetura a impeça.

**Limites da análise estática (seção 7.3.2).** O grafo de chamadas não observa **reflexão** e resolve o **despacho dinâmico** de forma conservadora, atribuindo a chamada ao proprietário declarado na instrução. As invocações que CDI e JPA realizam por *proxy* não aparecem no bytecode do chamador; nos fluxos medidos isso não altera o resultado, pois os *controllers* invocam o serviço diretamente e o serviço invoca os repositórios diretamente. Os construtores foram excluídos da contagem justamente por esse motivo: o modo interno carrega as entidades por reflexão, e incluí-los os faria aparecer espuriamente como exclusivos do modo externo.

**Validade interna.** As variantes foram construídas com `--no-cache`, a partir do mesmo código-fonte, e o contexto de *build* exclui o diretório `build/` local, de modo que o artefato de produção não herda estado da máquina. A dependência das convenções de nomenclatura do Quarkus, presente na evidência de 7.2.2, **deixou de ser crítica**: a conclusão da PP2 repousa sobre a evidência de 7.2.3 — OpenAPI e atendimento HTTP —, observável por qualquer cliente e independente do *framework*.

Registra-se uma correção de procedimento que a coleta impôs. O trabalho prescrevia verificar a supressão pela resposta `404` à rota do outro modo. A execução mostrou que a rota suprimida devolve `404` **ou** `405`, conforme outro *endpoint* registrado capture a URI (seção 7.2.3). O procedimento anterior produziria falso negativo, e a verificação passou a ter como critério primário a ausência da rota no documento OpenAPI.

As medições de 7.3.2 e 7.4 recaem sobre o **código-fonte** e sobre o **bytecode compilado**, idêntico entre variantes, e não sobre o artefato de produção — escolha justificada em 7.1, mas que constitui, em rigor, um objeto distinto do entregue. A equivalência entre o bytecode compilado localmente e o embarcado na imagem é sustentada pela seção 7.2.1, que os observa iguais quanto às classes, e não por medição independente.

**Validade externa.** Um único protótipo, um único domínio, um único *framework*. A generalização para outras linguagens, para mecanismos de variabilidade distintos ou para BFFs de maior porte não é sustentada pelos dados. A proporção de rotas suprimidas (7.3) é propriedade deste protótipo, não da arquitetura.

**Escopo da propriedade de segurança.** A segurança por ausência é a primeira linha de defesa, não a única. O protótipo não implementa autenticação, autorização por posse de recurso, limitação de abuso nem mascaramento de dados sensíveis. A política de CORS habilita apenas os verbos atendidos pelos *controllers* e parametriza as origens por variável de ambiente; o padrão coringa é escolha deliberada de um projeto-referência, destinada a facilitar a reprodução dos artefatos, e cabe a cada implantação substituí-lo. Por operar em tempo de execução, o CORS não interfere na propriedade aferida por PP2 e PP3, que diz respeito aos pontos de entrada existentes no artefato.

**O contrato exposto em produção.** Da mesma natureza é uma escolha registrada em 7.2.4: os artefatos publicam `/openapi` e `/docs` a qualquer requisitante, pois `quarkus.swagger-ui.always-include=true` mantém a interface disponível fora do modo de desenvolvimento (QUARKUS, 2026e). Um artefato cuja tese é a supressão de pontos de entrada oferece, assim, o mapa completo dos que restaram. Isso **não contradiz** a propriedade aferida — a rota suprimida continua ausente do documento e do artefato, e os endpoints de infraestrutura são excluídos da contagem de 7.3.1 por serem alheios à variabilidade —, mas amplia a superfície de reconhecimento de um atacante. A exposição é, como o coringa de CORS, deliberada em um projeto-referência, cuja finalidade é permitir que terceiros inspecionem as variantes; e é, como ele, o que uma implantação real deve reconsiderar. Registre-se que `always-include` é propriedade de tempo de compilação (QUARKUS, 2026e): também essa decisão se toma no *build*, e não em produção.

---

## 7.9 Evidências pendentes e como obtê-las

> **Nota de trabalho:** cada item abaixo é insumo de uma figura ou tabela do capítulo. As figuras serão produzidas ao final; os comandos que geram a evidência ficam registrados aqui para reprodutibilidade.

| # | Evidência | Onde obter / comando |
|---|---|---|
| 1 | ~~Rotas por variante no documento OpenAPI~~ | ✅ **Coletado.** Cada variante foi executada e interrogada em `GET /openapi`, pelo script `docs/case-study/scripts/evidencia-rotas.py`. Resultados em 7.2.3. |
| 2 | ~~Resposta HTTP à rota do outro modo~~ | ✅ **Coletado.** Mesmo script. ⚠️ O resultado corrigiu a expectativa: a rota suprimida devolve `404` **ou** `405`, conforme outra rota registrada capture a URI. Ver 7.2.3. |
| 3 | ~~Ausência da classe no *build* nativo~~ | ❌ **Fora do escopo.** Registrado como **trabalho futuro** (ver 7.2.4). Nenhuma afirmação do trabalho depende do resultado. |
| 4 | ~~`RSI`/`SSI` e *reuse percent* (PP1)~~ | ✅ **Coletado.** `cloc` 2.08 sobre `src/main/kotlin/**`, pelo script `docs/case-study/scripts/metrica-reuso.py`. Resultados em 7.4.1. |
| 4b | ~~Lógica alcançável por variante (PP3)~~ | ✅ **Coletado.** Grafo de chamadas por `javap` 21.0.11 sobre o bytecode compilado, pelo script `docs/case-study/scripts/grafo-metodos.py`. Resultados em 7.3.2. |
| 5 | ~~Tamanho, *startup* e memória por variante (PP4)~~ | ❌ **Descartado.** A PP4 é respondida qualitativamente (seção 7.5.1). O tamanho da imagem seria dedutível do resultado da PP2 — o bytecode é idêntico entre as variantes — e não sustentaria afirmação alguma do trabalho, que não reivindica leveza. Ver 7.5.2. |
| 6 | ~~Contraste com *feature flag* em runtime~~ | ❌ **Fora do escopo.** A presença do ponto de entrada é propriedade **definidora** da abordagem em runtime, não hipótese a testar. O contraste passa a ser argumentativo (seção 7.6). |

**Figuras previstas para o capítulo:** (a) diagrama das três variantes derivadas do código-fonte único, com o ponto de variação nos adaptadores de entrada; (b) recorte comparativo da saída de `make artifact/analysis`, evidenciando classe presente × ponto de entrada ausente; (c) gráfico de rotas expostas por variante contra a linha de base do *build* completo; (d) subgrafo de chamadas a partir dos três *controllers*, destacando o núcleo comum de sete métodos, os cinco exclusivos de cada modo de escrita e `OrderRepository.save` como ponto de encontro dos dois fluxos.

> **Nota de reprodutibilidade.** Os números de 7.3.2 e 7.4 são produzidos pelos scripts versionados em `docs/case-study/scripts/`, cujo `README.md` registra propósito, requisitos, invocação exata, leitura da saída e o histórico de correções aplicadas a eles.
>
> ⚠️ **Ao implementar este capítulo no TCC**, os dois scripts devem ser **transpostos por inteiro para o documento**, como blocos de código — em apêndice, se longos —, e não referenciados por caminho de arquivo: o trabalho é autocontido, e quem o lê sem acesso ao repositório precisa poder reconstruir cada medição. Os blocos reproduzem os scripts **tal como executados** para produzir os números aqui publicados.
