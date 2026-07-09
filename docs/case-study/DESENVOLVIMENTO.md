# Desenvolvimento / Implementação do Protótipo

> ✅ **Implementado no TCC** — transposto para o arquivo `Proposta TCC …md` como capítulo *Desenvolvimento* (seções 6.1–6.8), entre a *Metodologia* e as *Referências*. **A fonte da verdade passa a ser o TCC**; este documento é mantido apenas como registro de trabalho.
>
> Diferenças aplicadas na transposição: as afirmações sobre o comportamento do Quarkus passaram a ser **citadas na documentação oficial** (QUARKUS, 2026a–d), conforme a regra obrigatória de `FONTES-ACADEMICAS.md`; a seção 6.8 passou a **distinguir o documentado do observado**, já que a estrutura interna do `fast-jar` não consta da documentação; as remissões por caminho de arquivo viraram referências de seção; e os cinco marcadores de figura acompanharam o texto.

Este capítulo descreve a implementação do protótipo que demonstra a Arquitetura Metamórfica. Parte da organização em camadas hexagonais (seção 6.1), percorre o domínio (6.2), o serviço compartilhado que materializa o reuso (6.3) e a persistência (6.4), e culmina no **mecanismo metamórfico** — a ativação condicional de *controllers* em tempo de compilação (6.5) — e no **pipeline de build por artefato** (6.6). Encerra com a estratégia de testes e qualidade (6.7) e o procedimento de reprodução dos artefatos (6.8), que alimenta o capítulo de Avaliação.

---

## 6.1 Visão geral e organização em camadas

O protótipo é escrito em **Kotlin 2.4 / JVM 21** sobre **Quarkus 3.37**, com persistência em **PostgreSQL** via **Hibernate ORM**, migrações versionadas com **Goose** e documentação **OpenAPI/Swagger** gerada por anotações. As versões exatas do ambiente de referência constam da seção 5.5.3. O código segue a Arquitetura Hexagonal, com o pacote-raiz `edu.uerj`:

```
src/main/kotlin/edu/uerj/
├── domain/              # Entidades de negócio e enums (núcleo)
├── services/            # Casos de uso (OrderService) — compartilhados
├── adapters/
│   ├── api/             # Controllers REST (v1) + DTOs (porta de entrada)
│   └── infrastructure/  # Repositórios JPA (porta de saída)
└── config/              # ApiConfig (OpenAPI) e inicialização
```

> 🖼️ **[FIGURA A INSERIR — camadas hexagonais]** Representação em anéis ou hexágono das camadas `domain`, `services` e `adapters`, com as portas de entrada (*controllers*) e de saída (repositórios), destacando que a anotação de variabilidade vive apenas no adaptador de entrada.

Um detalhe técnico habilita essa organização em Kotlin: as classes Kotlin são `final` por padrão, o que é incompatível com o *proxying* exigido por CDI e JPA. O `build.gradle.kts` resolve isso com o plugin `allOpen`, tornando abertas as classes anotadas com `@Path`, `@ApplicationScoped` e `@Entity` — permitindo que o domínio e os adaptadores permaneçam idiomáticos sem `open` manual.

---

## 6.2 Domínio

O domínio modela o ciclo de vida de pedidos de e-commerce em **11 entidades** — `Customer`, `Contact`, `CustomerAddress`, `Order`, `OrderAddress`, `OrderItem`, `Product`, `ProductImage`, `Payment`, `Delivery` e `FiscalNote` —, organizadas em torno do agregado `Order`.

> 🖼️ **[FIGURA A INSERIR — modelo de domínio]** Diagrama de classes das 11 entidades, evidenciando o agregado `Order` e suas associações com `Delivery`, `OrderItem`, `Payment` e `OrderAddress`.

Duas decisões merecem destaque, por concentrarem regra de negócio no núcleo:

- **`OrderStatus` como enum ordenado.** O enum define nove estados, cada um com um `id` que fixa a ordem do fluxo: `NEW_ORDER` (0), `WAITING_PAYMENT` (1), `UNDER_FRAUD_ANALYSIS` (2), `PAID` (3), `INVOICE_BEGINNING` (4), `INVOICED` (5), `SEPARATION_BEGINNING` (6), `SEPARATION_FINISHED` (7) e `DELIVERED` (8). É esse `id` que torna possível validar transições: o `OrderService` só aceita um novo status cujo `id` seja maior que o atual, de modo que o pedido avança e nunca retrocede. O enum expõe ainda os utilitários `getById` e `getByDescription`.
- **Modelo de domínio rico.** A entidade `Order` não é anêmica: o método `checkOrderStatus()` recalcula o status do pedido a partir do menor estado entre suas entregas, mantendo a consistência do agregado.

> 🖼️ **[FIGURA A INSERIR — ciclo de vida do pedido]** Máquina de estados do `OrderStatus`, de `NEW_ORDER` a `DELIVERED`, indicando que as transições só ocorrem no sentido crescente do `id`.

Esse domínio é **idêntico em todos os artefatos** — interno, externo ou padrão —, pois não contém nenhuma anotação de variabilidade. É a base compartilhada sobre a qual a metamorfose ocorre apenas nas camadas externas.

---

## 6.3 Serviço compartilhado: o reuso sem duplicação

O `OrderService` (anotado `@ApplicationScoped` e `@Transactional`) concentra os casos de uso e é **injetado nos três controllers**. É a peça central da tese de reuso: a mesma lógica serve a clientes distintos, que diferem apenas no ponto de entrada.

| Caso de uso (`OrderService`) | Consumido por | Operação |
|---|---|---|
| `createOrder(newOrder)` | `OrderExternalController` | Criação de pedido |
| `updateStatus(orderId, deliveryId, status)` | `OrderInternalController` | Atualização de status de entrega |
| `getOrder(id)` · `listAll(limit, page)` | `OrderGeneralController` | Leitura de pedidos |

O `createOrder` reúne validações de negócio compartilhadas (existência de cliente, endereço e contato; conferência de preço do produto contra a base; agrupamento de itens em entregas; cálculo de totais e descontos por método de pagamento). O `updateStatus` aplica a regra de avanço de estado e delega o recálculo ao próprio agregado (`checkOrderStatus`). **Nenhuma dessas regras é duplicada por modo** — a especialização vive apenas nos controllers, que são finos.

---

## 6.4 Persistência

A porta de saída é implementada por repositórios sobre uma classe-base:

- **`BaseRepository`** expõe o `EntityManager` da unidade de persistência `"bff"` (injetado como `Instance<EntityManager>` e resolvido sob demanda). Os repositórios concretos (`OrderRepository`, `CustomerRepository`, …) o estendem e escrevem consultas JPQL/nativas explícitas — uma escolha deliberada por controle direto, em vez de uma camada de abstração como Panache.
- **Schema gerenciado externamente.** O Hibernate roda com `database.generation=none`; a evolução do schema é responsabilidade das **migrações Goose** versionadas — três, que criam respectivamente as tabelas de clientes e produtos; as de pedidos, contatos, endereços e imagens de produto; e as de pagamentos, notas fiscais, entregas e itens de pedido. Isso desacopla a evolução do banco do ciclo de vida da aplicação.

> 🖼️ **[FIGURA A INSERIR — schema do banco]** Diagrama entidade-relacionamento das tabelas criadas pelas três migrações, agrupadas por migração de origem.

---

## 6.5 Adaptadores de API e o mecanismo metamórfico

A porta de entrada tem **três controllers**, todos sob a rota base `/v1/order`, que se distinguem pela presença — ou ausência — da anotação de variabilidade:

- **`OrderGeneralController`** — **sem** `@IfBuildProperty`: está presente em **qualquer** artefato. Expõe as leituras (`GET /{orderId}`, `GET /list`).
- **`OrderExternalController`** — anotado para o modo externo; expõe `POST /create`.
- **`OrderInternalController`** — anotado para o modo interno; expõe `PATCH /{orderId}/delivery/{deliveryId}/{newStatus}`.

A anotação é o coração da arquitetura. Aplicada na classe do controller, ela instrui o Quarkus Arc a **incluir o *bean* no artefato apenas se a propriedade estiver `true` em tempo de build**:

```kotlin
@Path("/v1/order")
@IfBuildProperty(name = "modular-bff.api.mode.external.enable", stringValue = "true")
class OrderExternalController(private val orderService: OrderService) { … }
```

Quando a propriedade não está ativa, **o ponto de entrada não existe no artefato**: o Quarkus Arc não registra o *bean* CDI, não gera o invocador REST e não publica a rota. A anotação atua na fase de *augmentation*, posterior à compilação: o bytecode da classe permanece no JAR da aplicação, inerte e inalcançável, enquanto o *bean* e o invocador — que são o que torna a rota atingível — não são gerados. Nenhuma variável de ambiente, nenhum parâmetro de execução e nenhuma reconfiguração em tempo de *deploy* reintroduzem o que não foi registrado no *build*.

A distinção é deliberada e coerente com a métrica que fundamenta a proposta: a superfície de ataque é definida pelo conjunto de **pontos de entrada**, e não pelo conjunto de classes compiladas (seção 2.5). A superfície resultante por variante é, portanto, determinada no *build*:

| Variante | Pontos de entrada presentes | Endpoints além das leituras |
|---|---|---|
| `default` (ambas `false`) | General | — (somente leitura) |
| `external` | General + External | `POST /v1/order/create` |
| `internal` | General + Internal | `PATCH /v1/order/{orderId}/delivery/{deliveryId}/{newStatus}` |
| linha de base (ambas `true`) | General + External + Internal | ambos |

> A fundamentação teórica dessa técnica — variabilidade por anotação em tempo de compilação e Linhas de Produto de Software — está no capítulo **Referencial Teórico** (seções 2.3 e 2.4); a leitura de segurança, na seção 2.5.

---

## 6.6 Configuração e pipeline de build por artefato

### Configuração (ligação anotação ↔ ambiente)

As propriedades lidas pela anotação são resolvidas a partir de variáveis de ambiente, com padrão `false`, em `application.properties`:

```properties
modular-bff.api.mode.internal.enable=${BFF_API_INTERNAL_ACTIVE:false}
modular-bff.api.mode.external.enable=${BFF_API_EXTERNAL_ACTIVE:false}
```

Como se trata de uma **build property**, o valor precisa estar definido **no momento da compilação** — não em runtime. É isso que torna a variabilidade estrutural, e não uma condicional avaliada em execução.

A política de CORS segue o mesmo princípio de não expor o que não é usado: os métodos habilitados limitam-se aos verbos efetivamente atendidos pelos *controllers*, e as origens permitidas são parametrizadas por variável de ambiente, cabendo a cada implantação restringi-las.

```properties
quarkus.http.cors.origins=${BFF_CORS_ORIGINS:*}
quarkus.http.cors.methods=GET,POST,PATCH,OPTIONS
```

O valor padrão das origens é o coringa `*`, escolha **deliberada** de um protótipo cuja finalidade é servir de projeto-referência: qualquer pessoa que reproduza os artefatos no futuro consegue exercitá-los a partir de qualquer *frontend*, sem configuração prévia. A parametrização por variável de ambiente é o que permite, em uma implantação real, substituir o coringa pelas origens do cliente atendido.

O CORS é, ademais, configuração de **tempo de execução**, e não de compilação: ele não remove pontos de entrada, apenas restringe quem pode alcançá-los a partir de um navegador. Não se confunde, portanto, com a segurança por ausência — é uma das camadas complementares descritas na seção 4.5.

### Build por artefato

O `Dockerfile` recebe as flags como `ARG` e as injeta no `gradle assemble`, produzindo um artefato já "metamorfoseado":

```dockerfile
ARG BFF_API_EXTERNAL_ACTIVE
ARG BFF_API_INTERNAL_ACTIVE
RUN gradle assemble -g /tmp/cache --no-daemon
```

Os valores dos `ARG` ficam disponíveis como variáveis de ambiente durante a compilação, e é delas que o `application.properties` resolve as propriedades de *build*. Quando uma *flag* não é informada, a propriedade assume o padrão `false` e a variante não expõe as rotas do modo correspondente. O contexto de *build* exclui o diretório de saída local, o cache do sistema de *build* e os metadados do repositório, de modo que o artefato de produção não herda estado da máquina do desenvolvedor — condição necessária para que as medições da Avaliação recaiam sobre aquilo que seria efetivamente entregue.

O `Makefile` padroniza a geração das variantes, sua inspeção e sua execução. Os alvos de *build* produzem imagens distintas a partir do mesmo código-fonte, sem reaproveitamento de cache:

| Alvo | `BFF_API_EXTERNAL_ACTIVE` | `BFF_API_INTERNAL_ACTIVE` | Imagem |
|---|---|---|---|
| `make container/build/default` | `false` | `false` | `bff-default` |
| `make container/build/external` | `true` | `false` | `bff-external` |
| `make container/build/internal` | `false` | `true` | `bff-internal` |
| `make container/build` | `true` | `true` | `bff` (linha de base) |

> 🖼️ **[FIGURA A INSERIR — fluxo de build por variante]** Diagrama do código-fonte único ramificando-se, no estágio de *builder*, em três artefatos distintos conforme as *flags*, com o ponto de variação situado nos adaptadores de entrada.

O alvo `make container/build/variants` encadeia as três primeiras, e `make artifact/analysis` inspeciona as imagens resultantes. A execução de cada variante é feita pelos alvos `container/run/*`, que mapeiam portas distintas no hospedeiro (`9000` para a linha de base, `9001` para a externa e `9002` para a interna) sobre a mesma porta interna do contêiner.

> **Implicação central:** deploys interno e externo exigem **pipelines e artefatos distintos** gerados do mesmo código-fonte — o custo operacional da abordagem, dimensionado no capítulo de **Avaliação** (PP4).

---

## 6.7 Estratégia de testes, qualidade e CI

- **Testes:** JUnit 5 + **MockK** (unidade) e **REST Assured** (API), com um teste dedicado por controller (`OrderExternalControllerTest`, `OrderInternalControllerTest`, `OrderGeneralControllerTest`) e por repositório; **builders** de dados de teste desacoplam a criação de fixtures da estrutura das entidades. Cobertura via **JaCoCo**.
- **Qualidade:** **KtLint** (estilo) e **Detekt** (análise estática), com configuração centralizada.
- **CI (GitHub Actions):** três jobs — `lint`, `test` (sobe a infraestrutura, aplica as migrações e roda `make ci/test`) e `build`, que compila com ambas as *flags* ativas por meio de `make debug/build`. Note-se que o job de *build* exercita o **artefato de desenvolvimento**, cuja função é validar a compilação; os artefatos de produção são gerados pelos alvos de contêiner descritos na seção 6.6.

---

## 6.8 Reprodução dos artefatos (ponte para a Avaliação)

O efeito da metamorfose é **verificável por inspeção do artefato de produção** — a imagem gerada pelo `Dockerfile`, e não o resultado do *build* local, que não integra o desenho experimental (ver capítulo **Metodologia**, seção 5.5).

A verificação ocorre em duas camadas, e é importante distinguir onde cada uma atua. O JAR da aplicação (`app/*.jar`) contém o bytecode produzido pelo compilador Kotlin e, portanto, **as três classes de *controller* em qualquer variante**:

```bash
jar tf /deployments/app/*.jar | grep "adapters/api/v1"
# OrderExternalController.class · OrderGeneralController.class · OrderInternalController.class
```

Já o artefato gerado na fase de *augmentation* do Quarkus contém apenas os *beans* e invocadores efetivamente registrados. É nele que a metamorfose é observável:

```bash
jar tf /deployments/quarkus/generated-bytecode.jar | grep "adapters/api/v1"
# Na variante externa: OrderExternalController_Bean e seu invocador estão presentes;
# o OrderInternalController_Bean e seu invocador NÃO existem.
```

O alvo `make artifact/analysis` executa essa inspeção nas três variantes. A camada corroborativa acima explica **por que** a rota não existe; a evidência primária — a ausência da rota no documento OpenAPI e no atendimento HTTP — independe de convenções internas do *framework* e é apresentada no capítulo **Avaliação**.

Esse procedimento é o insumo direto da PP2 (*ausência do ponto de entrada*) e a base para as medições de superfície de ataque (PP3) e de reuso (PP1), definidas na Metodologia.

---

> **Pendências restantes:**
> - **Figuras.** Cinco pontos do capítulo trazem o marcador `🖼️ [FIGURA A INSERIR]`, cada um com a descrição do que a imagem deve mostrar: camadas hexagonais (6.1), modelo de domínio e ciclo de vida do pedido (6.2), schema do banco (6.4) e fluxo de *build* por variante (6.6). Os marcadores acompanham o texto na transposição para o TCC e devem ser substituídos pelas imagens definitivas antes da versão final.
>
> **Resolvidas:**
> - ~~Remissões a `CASE_STUDY.md`~~ → convertidas. A seção 6.1 remete à seção 5.5.3 (ambiente); as seções 6.2 e 6.4 absorveram o conteúdo — catálogo das 11 entidades, os nove estados do `OrderStatus` e o escopo das três migrações — em vez de apontar para fora do trabalho.
>
> **Decisões registradas (não são pendências):**
> - **CORS.** Os métodos foram restringidos aos verbos atendidos pelos *controllers* e as origens passaram a ser parametrizadas por variável de ambiente. O padrão coringa (`*`) é **escolha deliberada** de um projeto-referência: garante que qualquer pessoa reproduza os artefatos sem configuração prévia. Descrito na seção 6.6.
> - **Melhorias de código não aplicadas.** O trecho frágil do `init.sh`, a *native query* de `OrderRepository.countOrders()` e o `@Transactional` no nível da classe do `OrderService` foram identificados e **deliberadamente não alterados**: são pormenores que não afetam a análise de segurança por ausência nem qualquer medição das perguntas de pesquisa.
> - **Métricas de reuso (PP1).** O método de cálculo deixou de ser pendência deste capítulo: está definido no capítulo **Metodologia** (seção 5.5.2). Resta apenas executar a coleta, no capítulo de **Avaliação**.
