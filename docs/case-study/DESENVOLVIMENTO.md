# Desenvolvimento / Implementação do Protótipo

> 🟡 **Rascunho** — documento de trabalho, no mesmo estágio da Metodologia (`METODOLOGIA.md`) e do Referencial Teórico (`REFERENCIAL-TEORICO.md`). Estrutura e conteúdo sujeitos a revisão antes da consolidação no TCC.
>
> **Situação no TCC:** capítulo que descreve o protótipo `modular-bff` como **demonstração** concreta da Arquitetura Metamórfica (atividade 4 do método DSR — ver `METODOLOGIA.md`). Reaproveita e **resume** o estudo de caso (`CASE_STUDY.md`), sem copiá-lo: aqui o foco é evidenciar *como* a arquitetura foi implementada e *como* os artefatos são reproduzidos.
>
> **Convenção:** os trechos de código são recortes fiéis do repositório, apresentados de forma reduzida para ilustrar decisões-chave.

Este capítulo descreve a implementação do protótipo que demonstra a Arquitetura Metamórfica. Parte da organização em camadas hexagonais (seção 6.1), percorre o domínio (6.2), o serviço compartilhado que materializa o reuso (6.3) e a persistência (6.4), e culmina no **mecanismo metamórfico** — a ativação condicional de *controllers* em tempo de compilação (6.5) — e no **pipeline de build por artefato** (6.6). Encerra com a estratégia de testes e qualidade (6.7) e o procedimento de reprodução dos artefatos (6.8), que alimenta o capítulo de Avaliação.

---

## 6.1 Visão geral e organização em camadas

O protótipo é escrito em **Kotlin 2.4 / JVM 21** sobre **Quarkus 3.37**, com persistência em **PostgreSQL** via **Hibernate ORM**, migrações versionadas com **Goose** e documentação **OpenAPI/Swagger** gerada por anotações (detalhes de versões em `CASE_STUDY.md`, seção 2). O código segue a Arquitetura Hexagonal, com o pacote-raiz `edu.uerj`:

```
src/main/kotlin/edu/uerj/
├── domain/              # Entidades de negócio e enums (núcleo)
├── services/            # Casos de uso (OrderService) — compartilhados
├── adapters/
│   ├── api/             # Controllers REST (v1) + DTOs (porta de entrada)
│   └── infrastructure/  # Repositórios JPA (porta de saída)
└── config/              # ApiConfig (OpenAPI) e inicialização
```

Um detalhe técnico habilita essa organização em Kotlin: as classes Kotlin são `final` por padrão, o que é incompatível com o *proxying* exigido por CDI e JPA. O `build.gradle.kts` resolve isso com o plugin `allOpen`, tornando abertas as classes anotadas com `@Path`, `@ApplicationScoped` e `@Entity` — permitindo que o domínio e os adaptadores permaneçam idiomáticos sem `open` manual.

---

## 6.2 Domínio

O domínio modela o ciclo de vida de pedidos de e-commerce em **11 entidades** e **9 estados** (catálogo completo em `CASE_STUDY.md`, seção 4). Duas decisões merecem destaque no capítulo, por concentrarem regra de negócio no núcleo:

- **`OrderStatus` como enum ordenado.** Cada estado carrega um `id` numérico que define a ordem do fluxo (`NEW_ORDER=0 … DELIVERED=8`), além de métodos utilitários (`getById`, `getByDescription`). Essa ordenação é o que torna possível validar transições.
- **Modelo de domínio rico.** A entidade `Order` não é anêmica: o método `checkOrderStatus()` recalcula o status do pedido a partir do menor estado entre suas entregas, mantendo a consistência do agregado. A regra de que um pedido **só avança** de estado (nunca retrocede) é imposta no serviço, comparando os `id` de status.

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
- **Schema gerenciado externamente.** O Hibernate roda com `database.generation=none`; a evolução do schema é responsabilidade das **migrações Goose** versionadas (três migrações, ver `CASE_STUDY.md`, seção 6). Isso desacopla a evolução do banco do ciclo de vida da aplicação.

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

Quando a propriedade não está ativa, **a classe não é compilada no artefato**: não há rota registrada, nem *bean* CDI, nem endpoint a atacar. A superfície resultante por variante é, portanto, determinada no build:

| Build | Controllers presentes | Endpoints além das leituras |
|---|---|---|
| `both=false` (padrão) | General | — (somente leitura) |
| `external=true` | General + External | `POST /v1/order/create` |
| `internal=true` | General + Internal | `PATCH /v1/order/{orderId}/delivery/{deliveryId}/{newStatus}` |
| `both=true` | General + External + Internal | ambos |

> A fundamentação teórica dessa técnica (variabilidade por anotação em tempo de compilação, SPL) está em `REFERENCIAL-TEORICO.md` (seções 2.3–2.4); a leitura de segurança (redução de superfície de ataque) está em 2.5.

---

## 6.6 Configuração e pipeline de build por artefato

### Configuração (ligação anotação ↔ ambiente)

As propriedades lidas pela anotação são resolvidas a partir de variáveis de ambiente, com padrão `false`, em `application.properties`:

```properties
modular-bff.api.mode.internal.enable=${BFF_API_INTERNAL_ACTIVE:false}
modular-bff.api.mode.external.enable=${BFF_API_EXTERNAL_ACTIVE:false}
```

Como se trata de uma **build property**, o valor precisa estar definido **no momento da compilação** — não em runtime. É isso que torna a variabilidade estrutural, e não uma condicional avaliada em execução.

### Build por artefato

O `Dockerfile` recebe as flags como `ARG` e as injeta no `gradle assemble`, produzindo um artefato já "metamorfoseado":

```dockerfile
ARG BFF_API_EXTERNAL_ACTIVE
ARG BFF_API_INTERNAL_ACTIVE
RUN gradle assemble -g /tmp/cache --no-daemon
```

O `Makefile` padroniza as três variantes e sua execução em portas distintas:

| Alvo | Flags | Porta |
|---|---|---|
| `make container/build` | external + internal | 9000 |
| `make container/build/external` | external | 9001 |
| `make container/build/internal` | internal | 9002 |

> **Implicação central:** deploys interno e externo exigem **pipelines/artefatos distintos** gerados do mesmo código-fonte — o custo operacional da abordagem, a ser dimensionado no capítulo de Avaliação (PP4).

---

## 6.7 Estratégia de testes, qualidade e CI

- **Testes:** JUnit 5 + **MockK** (unidade) e **REST Assured** (API), com um teste dedicado por controller (`OrderExternalControllerTest`, `OrderInternalControllerTest`, `OrderGeneralControllerTest`) e por repositório; **builders** de dados de teste desacoplam a criação de fixtures da estrutura das entidades. Cobertura via **JaCoCo**.
- **Qualidade:** **KtLint** (estilo) e **Detekt** (análise estática), com configuração centralizada.
- **CI (GitHub Actions):** três jobs — `lint`, `test` (sobe Docker + Goose e roda `make ci/test`) e `build` (compila com `BFF_API_EXTERNAL_ACTIVE=true` e `BFF_API_INTERNAL_ACTIVE=true`).

---

## 6.8 Reprodução dos artefatos (ponte para a Avaliação)

O efeito da metamorfose é **verificável por inspeção do artefato**. Após um build com uma dada configuração, lista-se o conteúdo do JAR e confirma-se a presença/ausência de cada controller (procedimento completo em `CASE_STUDY.md`, seção 10):

```bash
# Build externo e inspeção
unzip -l build/quarkus-app/app/modular-bff-*.jar | grep "adapters/api/v1"
# Esperado: OrderGeneralController e OrderExternalController presentes;
#           OrderInternalController AUSENTE do binário
```

Esse é o insumo direto do experimento 1 da Avaliação (PP2 — *ausência verificável*) e a base para as medições de superfície de ataque (PP3) e de reuso (PP1).

---

> **Pendências de redação e melhorias identificadas (tratar em Avaliação / Ameaças à Validade / Trabalhos Futuros):**
> - **CORS permissivo** (`quarkus.http.cors.origins=*`, com métodos `PUT/DELETE` não usados) contradiz parcialmente a narrativa de redução de superfície de ataque — registrar como limitação/ameaça e recomendar restrição por ambiente.
> - **`init.sh`** contém trecho frágil (`-Dquarkus-args= "$RUN_MODULE"`, com `$RUN_MODULE` indefinido) — limpar antes de descrever o runtime do container em definitivo.
> - **`OrderRepository.countOrders()`** usa *native query* com nome de tabela (`FROM Pedidos p`), destoando do JPQL do restante — inconsistência menor a mencionar ou corrigir.
> - **`@Transactional` no nível da classe** cobre também leituras (`getOrder`, `listAll`) — possível refinamento.
> - **Métricas de reuso (PP1):** quantificar shared (domínio + serviços + repositórios + DTOs) vs. especializado (3 controllers) — cálculo a executar no capítulo de Avaliação.
> - **Diagramas:** produzir as figuras de camadas e do fluxo de build (os textos-base estão aqui e no `CASE_STUDY.md`).
