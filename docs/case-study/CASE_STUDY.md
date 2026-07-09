# Estudo de Caso: Modular BFF com Arquitetura Hexagonal

## 1. Contextualização

### Problema
Sistemas frontend precisam agregar dados de múltiplos domínios (pedidos, clientes, produtos, entregas, pagamentos). Uma API monolítica dificulta evolução independente, testes isolados e a flexibilidade de expor diferentes contratos para diferentes clientes.

### Solução Proposta
Implementar um **Backend For Frontend (BFF)** modular com suporte a dois modos de operação — **interno** e **externo** — de modo que diferentes consumidores da API acessem apenas as operações pertinentes ao seu contexto, sem alterações no código-fonte.

---

## 2. Tecnologias Utilizadas

| Camada            | Tecnologia                          |
|-------------------|-------------------------------------|
| Linguagem         | Kotlin 2.4.0 + JVM 21               |
| Framework         | Quarkus 3.37.0 (Reactive)           |
| Banco de Dados    | PostgreSQL + Hibernate ORM (JDBC)   |
| Migrações         | Goose                               |
| Testes            | JUnit 5 + MockK + REST Assured      |
| Qualidade         | KtLint + Detekt                     |
| Documentação      | OpenAPI 3.0 / Swagger UI            |
| Containerização   | Docker Compose                      |

---

## 3. Arquitetura

O projeto aplica a **Arquitetura Hexagonal (Ports & Adapters)**, isolando as regras de negócio de frameworks, banco de dados e demais tecnologias externas.

```
src/main/kotlin/edu/uerj/
├── domain/              # Entidades de negócio e enums
├── services/            # Regras de negócio (casos de uso)
├── adapters/
│   ├── api/             # Controllers REST (porta de entrada)
│   │   ├── v1/          # Endpoints versionados
│   │   └── dto/         # Data Transfer Objects
│   └── infrastructure/  # Repositórios JPA (porta de saída)
└── config/              # Configurações e inicialização
```

### Design Metamórfico: Ativação Condicional de Controllers em Tempo de Compilação

Os controllers `OrderExternalController` e `OrderInternalController` utilizam a anotação `@IfBuildProperty` do Quarkus Arc:

```kotlin
// Controller interno — só existe no artefato se BFF_API_INTERNAL_ACTIVE=true no build
@IfBuildProperty(name = "modular-bff.api.mode.internal.enable", stringValue = "true")
class OrderInternalController ...

// Controller externo — só existe no artefato se BFF_API_EXTERNAL_ACTIVE=true no build
@IfBuildProperty(name = "modular-bff.api.mode.external.enable", stringValue = "true")
class OrderExternalController ...
```

Configurado via variáveis de ambiente injetadas em `application.properties`:

```properties
modular-bff.api.mode.internal.enable=${BFF_API_INTERNAL_ACTIVE:false}
modular-bff.api.mode.external.enable=${BFF_API_EXTERNAL_ACTIVE:false}
```

#### Por que "Metamórfico"?

O artefato final gerado pelo build **muda de forma** dependendo dos valores presentes **em tempo de compilação** — não em runtime. O endpoint simplesmente não existe no binário se a flag não estiver ativa. Isso é fundamentalmente diferente de uma condicional `if` no código: não há rota registrada, nenhum bean CDI instanciado, nenhuma superfície de ataque presente.

```
Build com BFF_API_INTERNAL_ACTIVE=true  →  artefato contém PATCH /v1/order/.../status
Build com BFF_API_EXTERNAL_ACTIVE=true  →  artefato contém POST  /v1/order/create
Build padrão (ambos false)              →  nenhum dos dois existe
```

Isso implica que **deploys internos e externos requerem pipelines CI/CD distintos**, gerando artefatos diferentes a partir do mesmo código-fonte.

---

### Visão Geral das Superfícies de Ataque

| Controller               | Ativação          | Exposição        | Operação                         |
|--------------------------|-------------------|------------------|----------------------------------|
| `OrderGeneralController` | Sempre ativo      | Interna e externa| Leitura de pedidos               |
| `OrderExternalController`| Build flag        | Clientes externos| Criação de pedidos               |
| `OrderInternalController`| Build flag        | Rede interna     | Atualização de status de entrega |

> **Atenção**: O `OrderGeneralController` não possui `@IfBuildProperty`, portanto está ativo em **qualquer** artefato gerado, independentemente do modo.

---

## 4. Domínio do Problema

O sistema gerencia o ciclo de vida completo de pedidos, composto por **11 entidades** e **9 estados de pedido**.

### Entidades do Domínio

| Entidade          | Responsabilidade                              |
|-------------------|-----------------------------------------------|
| `Customer`        | Dados cadastrais do cliente                   |
| `Order`           | Pedido com status, total, desconto e frete    |
| `Product`         | Catálogo de produtos (SKU como chave primária)|
| `Contact`         | Contatos do cliente (principal e secundários) |
| `Payment`         | Método de pagamento e parcelamento            |
| `Delivery`        | Rastreamento de entrega                       |
| `FiscalNote`      | Nota fiscal (DANFE)                           |
| `OrderItem`       | Itens de um pedido                            |
| `ProductImage`    | Imagens de produto em base64                  |
| `OrderAddress`    | Endereço de entrega do pedido                 |
| `CustomerAddress` | Endereços cadastrados do cliente              |

### Ciclo de Vida do Pedido

```
NEW_ORDER → CONFIRMED → PAYMENT_PENDING → PAID
→ PREPARING → SHIPPED → OUT_FOR_DELIVERY → DELIVERED
```

---

## 5. Endpoints da API

| Modo     | Método  | Rota                                              | Descrição                    |
|----------|---------|---------------------------------------------------|------------------------------|
| Geral    | `GET`   | `/v1/order/{orderId}`                             | Busca pedido por ID          |
| Geral    | `GET`   | `/v1/order/list?limitPerPage={n}&page={n}`        | Listagem paginada de pedidos |
| Externo  | `POST`  | `/v1/order/create`                                | Cria novo pedido             |
| Interno  | `PATCH` | `/v1/order/{orderId}/delivery/{deliveryId}/{status}` | Atualiza status do pedido |

**Restrições de paginação**: o parâmetro `limitPerPage` aceita valores entre **1 e 500**, com padrão **30** (aplicado pelo controller quando o parâmetro é omitido). Valores fora dessa faixa são saneados pelo `OrderService`, que adota um *fallback* de **50**. O parâmetro `page` (padrão **0**) é multiplicado por `limitPerPage` para calcular o deslocamento (paginação baseada em página).

**Endpoints de infraestrutura**:
- `GET /health` — Health check
- `GET /openapi` — Schema OpenAPI
- `GET /docs` — Swagger UI

---

## 6. Esquema do Banco de Dados

O schema é gerenciado com **Goose** via migrações versionadas:

| Migração                       | Tabelas criadas                                                   |
|--------------------------------|-------------------------------------------------------------------|
| `20260421203638` — Clientes e Produtos | `clientes`, `produtos`                                  |
| `20260428105955` — Pedidos e Endereços | `pedidos`, `contatos`, `enderecos_clientes`, `enderecos_pedidos`, `imagens_produtos` |
| `20260428111817` — Pagamentos e Entregas | `pagamentos`, `notas_fiscais`, `entregas`, `itens_pedido` |

---

## 7. Estratégia de Testes

| Tipo             | Ferramentas            | Cobertura                                    |
|------------------|------------------------|----------------------------------------------|
| Testes unitários | JUnit 5 + MockK        | `OrderService`, controllers isolados         |
| Testes de API    | REST Assured           | Endpoints HTTP com asserções Hamcrest        |
| Builders de teste| Classes `*Builder`     | `OrderBuilder`, `CustomerBuilder`, `ProductBuilder`, `DeliveryBuilder` |
| Cobertura        | JaCoCo                 | Relatório de cobertura integrado ao build    |

O uso de **builders de teste** desacoplou a criação de dados de teste da estrutura das entidades, facilitando manutenção e legibilidade dos testes.

---

## 8. Desafios e Decisões Técnicas

| Desafio                             | Decisão Tomada                                                     |
|-------------------------------------|--------------------------------------------------------------------|
| Modularidade da API                 | Controllers ativados condicionalmente via `@IfBuildProperty`       |
| Desacoplamento de persistência      | `BaseRepository<T>` genérico como base para todos os repositórios  |
| Testabilidade sem infraestrutura    | Repositórios mockados com MockK + builders de dados de teste       |
| Evolução segura do schema           | Migrações versionadas com Goose                                    |
| Consistência de estilo              | KtLint + Detekt com configuração centralizada em `config/detekt/`  |
| Documentação automática da API      | SmallRye OpenAPI gera schema a partir das anotações JAX-RS         |

---

## 9. Conclusões

- A **arquitetura hexagonal** isolou efetivamente as regras de negócio de frameworks e banco de dados, tornando o domínio testável de forma independente.
- O padrão **BFF modular** separou responsabilidades entre clientes internos e externos sem duplicação de código.
- A **ativação condicional de controllers** via CDI nativo do Quarkus eliminou lógica condicional em runtime, mantendo o código limpo e previsível.
- Os **builders de teste** padronizaram a criação de dados, reduzindo o custo de manutenção dos testes ao longo da evolução do domínio.

---

## 10. Comandos Principais

```bash
make deps/install    # Instala dependências (Gradle + Goose)
make docker/up       # Sobe container PostgreSQL
make db/migrate-up   # Aplica migrações do banco
make db/seed         # Popula dados de teste
make dev             # Inicia Quarkus em modo dev (Live Coding)
make test            # Executa todos os testes
make lint            # Executa KtLint
make build           # Gera artefato JAR
```

### Inspecionar a estrutura do artefato gerado

O Quarkus gera um layout de artefato próprio em `build/quarkus-app/`. Para listar o conteúdo do JAR da aplicação:

**Com `unzip`** (requer apenas o pacote `unzip`):
```bash
# Listar todas as entradas do JAR principal
unzip -l build/quarkus-app/app/modular-bff-1.0.0-SNAPSHOT.jar

# Filtrar apenas as classes do projeto
unzip -l build/quarkus-app/app/modular-bff-1.0.0-SNAPSHOT.jar | grep "edu/uerj"

# Filtrar apenas os controllers
unzip -l build/quarkus-app/app/modular-bff-1.0.0-SNAPSHOT.jar | grep "adapters/api/v1"
```

**Com `jar tf`** (requer JDK completo — pacote `java-devel`):
```bash
# Listar todas as entradas do JAR principal
jar tf build/quarkus-app/app/modular-bff-1.0.0-SNAPSHOT.jar

# Filtrar apenas as classes do projeto
jar tf build/quarkus-app/app/modular-bff-1.0.0-SNAPSHOT.jar | grep "edu/uerj"

# Filtrar apenas os controllers
jar tf build/quarkus-app/app/modular-bff-1.0.0-SNAPSHOT.jar | grep "adapters/api/v1"
```

Isso permite verificar **quais controllers foram compilados** no artefato, validando o efeito do Design Metamórfico. Por exemplo, em um build com `BFF_API_INTERNAL_ACTIVE=false`, a classe `OrderInternalController` não aparecerá na saída:

```
# Saída esperada com BFF_API_EXTERNAL_ACTIVE=true e BFF_API_INTERNAL_ACTIVE=false
edu/uerj/adapters/api/v1/OrderGeneralController.class
edu/uerj/adapters/api/v1/OrderExternalController.class
# OrderInternalController ausente — não existe no binário
```

---

## 11. Design Metamórfico como Estratégia de Segurança por Ausência

### Tese Central

> *"A forma mais segura de proteger um endpoint é garantir que ele não exista no binário implantado."*

O Design Metamórfico não é apenas uma decisão de modularidade — é uma **estratégia de segurança estrutural**. Ao eliminar endpoints no momento do build, o projeto elimina também todas as classes de vulnerabilidade associadas a eles: não há rota para atacar, nenhum bean CDI para explorar, nenhuma misconfiguration possível que ressuscite o endpoint acidentalmente.

Isso contrasta com a abordagem mais comum de **segurança por proteção**, onde o endpoint existe mas tenta se defender com camadas de autenticação, rate limiting e validação. A segurança por proteção é válida, mas é inerentemente mais frágil — cada camada pode ser contornada, mal configurada ou vulnerável a um CVE futuro.

---

### Comparação com Alternativas

| Abordagem | Reuso de código | Segurança | Complexidade operacional |
|---|---|---|---|
| Repositórios separados | ❌ Código duplicado | 🟡 Separação física, mas manutenção divergente | 🟡 Dois repos, dois pipelines |
| Runtime feature flag (`if enabled`) | ✅ Mesmo código | 🔴 Endpoint **existe** no binário — vulnerável a misconfiguration | 🟢 Um artefato, variável de ambiente |
| Microsserviços distintos | ❌ Duplicação de domínio | 🟡 Separação real, mas custo operacional alto | 🔴 Infraestrutura distribuída |
| **Design Metamórfico** | ✅ Mesmo código-fonte | ✅ Endpoint **não existe** no binário implantado | 🟡 Pipelines distintos por artefato |

A diferença crítica em relação ao **runtime feature flag** merece destaque. Considere o cenário em que `BFF_API_INTERNAL_ACTIVE` é implementado como uma verificação em runtime:

```kotlin
// Abordagem NÃO usada no projeto — feature flag em runtime
@PATCH
fun updateStatus(...): Response {
    if (!config.internalModeEnabled) {
        return Response.status(403).build()  // endpoint existe, apenas rejeita
    }
    ...
}
```

Nesse modelo, o endpoint existe no binário externo. Uma variável de ambiente mal configurada, um bug na lógica de verificação, ou uma vulnerabilidade no framework de configuração seriam suficientes para expor a operação de atualização de status a clientes externos.

Com `@IfBuildProperty`, esse cenário é impossível por construção — não há código para ser ativado acidentalmente.

---

### Reuso sem Duplicação

O design metamórfico só é viável porque as camadas abaixo dos controllers são **completamente compartilhadas**:

```
┌─────────────────────────────────────────────────┐
│              Código-Fonte Único                  │
├──────────────┬──────────────────────────────────┤
│ OrderExternal│ OrderInternal │ OrderGeneral      │  ← Controllers (build-time)
│ Controller   │ Controller    │ Controller        │
├──────────────┴───────────────┴───────────────────┤
│              OrderService                        │  ← Serviço compartilhado
├──────────────────────────────────────────────────┤
│  OrderRepo  CustomerRepo  ProductRepo  ...       │  ← Repositórios compartilhados
├──────────────────────────────────────────────────┤
│  Order  Customer  Product  Payment  Delivery...  │  ← Domínio compartilhado
└──────────────────────────────────────────────────┘
```

A mesma regra de negócio em `OrderService.createOrder()` serve o cliente externo. A mesma `OrderService.updateStatus()` serve o sistema interno. Não há duplicação de validações, cálculos ou fluxos — apenas pontos de entrada diferentes, ativados ou removidos no build.

---

### O que o Design Metamórfico não resolve

É importante delimitar o escopo da estratégia. O design metamórfico **reduz a superfície de ataque**, mas não substitui camadas complementares de segurança:

| Responsabilidade | Resolvida pelo Design Metamórfico? |
|---|---|
| Endpoint de um modo não existir no outro artefato | ✅ Sim — por construção |
| Autenticação do solicitante | ❌ Requer implementação de JWT/OAuth2 |
| Autorização por ownership de recurso (IDOR) | ❌ Requer validação no serviço ou filtro |
| Proteção contra abuso (rate limiting) | ❌ Requer API Gateway ou middleware |
| Mascaramento de PII na resposta | ❌ Requer projeção nos DTOs |
| CORS restrito por domínio | ❌ Requer configuração por ambiente |

O modelo correto é tratar o Design Metamórfico como a **primeira linha de defesa** — garantindo que o artefato externo simplesmente não contenha operações internas — e construir as demais camadas de segurança sobre essa base.
