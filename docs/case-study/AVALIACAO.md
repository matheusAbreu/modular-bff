# Avaliação, Resultados e Discussão

> 🟡 **Rascunho** — documento de trabalho, no mesmo estágio de `METODOLOGIA.md`, `DESENVOLVIMENTO.md` e `METRICA-REUSO.md`.
>
> **Situação no TCC:** capítulo que apresenta as **evidências** coletadas sobre o artefato e as confronta com as afirmações da Proposta. Corresponde à atividade 5 do método DSR (ver capítulo *Metodologia*).
>
> **Separação de responsabilidades.** Este capítulo **não define método**: o desenho experimental, as métricas e os procedimentos estão na *Metodologia* (seção 5) e, no caso da métrica de reuso, em `METRICA-REUSO.md`, que será absorvido por ela. Aqui registram-se **os dados obtidos**, sua leitura e seus limites. Toda vez que este capítulo precisar explicar *como* algo foi medido, ele **remete** à Metodologia em vez de reexpor o procedimento.
>
> **Convenções:** citações no sistema autor-data (ABNT NBR 10520); 3ª pessoa acadêmica. Marcadores `[A PREENCHER]` indicam medições ainda não executadas.

Este capítulo apresenta os resultados dos experimentos definidos na Metodologia. A seção 7.1 delimita o objeto medido. A 7.2 responde à PP2 — a pergunta central —, distinguindo o que ocorre no artefato de desenvolvimento e no de produção. A 7.3 responde à PP3, a 7.4 à PP1 e a 7.5 à PP4. A 7.6 contrasta os achados com a alternativa de *feature flag* em tempo de execução. A 7.7 discute os resultados frente aos objetivos e a 7.8 registra as ameaças à validade.

---

## 7.1 Objeto da avaliação

Duas distinções precedem a leitura dos números.

**Qual artefato.** Mede-se, exclusivamente, o **artefato de produção**: o gerado no estágio de *builder* do `Dockerfile`, a partir do código-fonte e das *flags* passadas por `--build-arg`, isolado do estado da máquina do desenvolvedor. As afirmações do trabalho referem-se ao que é **entregue**. O *build* local de desenvolvimento não integra a avaliação.

**Quais variantes.** Três, geradas por `make container/build/variants`:

| Variante | `BFF_API_EXTERNAL_ACTIVE` | `BFF_API_INTERNAL_ACTIVE` |
|---|---|---|
| `bff-default` | `false` | `false` |
| `bff-internal` | `false` | `true` |
| `bff-external` | `true` | `false` |

Todas derivam do **mesmo código-fonte**, sem qualquer alteração entre os *builds* — condição necessária para que as diferenças observadas sejam atribuíveis exclusivamente à resolução da variabilidade em tempo de compilação.

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

### 7.2.3 Leitura do resultado

A PP2 recebe uma resposta **afirmativa quanto ao ponto de entrada** e **negativa quanto à classe**. A distinção não é acessória, e três consequências decorrem dela.

Primeiro, a formulação correta da tese é a que se ancora no **ponto de entrada**, e não na classe. A métrica de superfície de ataque adotada no Referencial Teórico define a superfície pelo conjunto de pontos de entrada por onde os dados entram no sistema a partir do ambiente (MANADHATA; WING, 2011). Onde não há rota registrada, não há via de entrada — e é isso, e apenas isso, que a métrica citada requer. Afirmar que a classe "não existe no binário" seria simultaneamente falso e desnecessário ao argumento.

Segundo, a classe remanescente é **inalcançável pelas vias que a superfície de ataque contabiliza**, mas permanece carregável por reflexão. A segurança por ausência elimina a via de entrada; não promete a remoção física do bytecode no empacotamento em JVM.

Terceiro, a evidência do bytecode gerado (7.2.2) é **corroborativa**, não fundacional: os nomes `..._Bean` e `...$quarkusrestinvoker$...` são convenções internas do Quarkus Arc e do RESTEasy Reactive, sujeitas a mudança entre versões. A evidência primária deve ser a **ausência da rota** — observável no documento OpenAPI da variante e no comportamento HTTP —, que independe do framework. `[A PREENCHER: extração do documento OpenAPI por variante e verificação de resposta 404 na rota do outro modo.]`

> **Forma forte da tese.** Resta verificar se o *build* nativo (GraalVM) elimina também o bytecode da classe por *dead code elimination*. Caso elimine, a afirmação "o endpoint não existe no binário" torna-se literalmente verdadeira para o artefato nativo, e o trabalho passa a distinguir a forma fraca (JVM: ponto de entrada ausente) da forma forte (nativo: classe ausente). `[A PREENCHER: resultado do build nativo.]`

---

## 7.3 PP3 — Redução da superfície de ataque

> **PP3:** a abordagem reduz o número de pontos de entrada expostos por artefato, comparada a um *build* que contém todos os controllers?

Contam-se as rotas REST de negócio expostas por variante, conforme a tabela da seção 7.2.2. Excluem-se da contagem os endpoints de infraestrutura (`/health`, `/openapi`, `/docs`), presentes em toda variante e alheios à variabilidade.

| Variante | Rotas de negócio | Redução frente ao *build* completo |
|---|---|---|
| *Build* completo (ambos os modos) | 4 | — (linha de base) |
| `bff-external` | 3 | 1 rota (25%) |
| `bff-internal` | 3 | 1 rota (25%) |
| `bff-default` | 2 | 2 rotas (50%) |

A redução é modesta em valor absoluto porque o protótipo expõe poucas operações; o que o número dimensiona não é a magnitude do ganho, mas sua **natureza**: a rota suprimida deixa de integrar o conjunto de pontos de entrada do artefato, em vez de nele permanecer sob proteção. Em um BFF de porte industrial, a proporção de operações privativas de um modo tende a ser maior, e com ela a redução — extrapolação que **este trabalho não mede** e que fica registrada em 7.8.

> **Nota de escopo.** A métrica de Manadhata e Wing (2011) pondera os pontos de entrada por dano potencial e custo de acesso. A contagem acima é **não ponderada**, o que a torna uma aproximação. Adotá-la integralmente exigiria estimar dano e custo por rota, o que não se fez.

---

## 7.4 PP1 — Reuso sem duplicação

> **PP1:** é possível derivar múltiplos artefatos BFF especializados a partir de um único código-fonte, sem duplicar regras de negócio, serviços e repositórios?

O método de cálculo — as métricas *reuse percent* e *reuse level* (FRAKES; TERRY, 1996), a classificação dos ativos e o procedimento de coleta — está definido na Metodologia. Os valores obtidos:

| Variante | `RSI` (LOC comum) | `SSI` (LOC específico) | *Reuse Percent* |
|---|---|---|---|
| `bff-default` | `[A PREENCHER]` | 0 | 100% (caso-limite; ver ressalva) |
| `bff-internal` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |
| `bff-external` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |

| Variante | `L` | `E` | `M` | `ERL` | `IRL` | `TRL` |
|---|---|---|---|---|---|---|
| `bff-internal` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |
| `bff-external` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |

Independentemente dos números, um fato estrutural já está estabelecido pelo código: as regras de negócio residem em uma única classe de serviço, `OrderService`, injetada nos **três** controllers, e as portas de saída em repositórios igualmente únicos. A especialização por modo vive apenas nos adaptadores de entrada. Nenhuma validação, cálculo ou fluxo é reescrito por variante. `[A PREENCHER: análise de sensibilidade reclassificando os DTOs de uso exclusivo.]`

> **Ressalva sobre a variante `default`.** Com `SSI = 0`, o *reuse percent* é 100% por construção. O valor é correto e substantivamente vazio: apenas confirma que a variante não possui código específico.

---

## 7.5 PP4 — Custo operacional

> **PP4:** que custos a abordagem impõe?

O custo estrutural é **qualitativo e certo**: cada variante exige um *build* próprio e, portanto, um pipeline e um artefato distintos. Não há um binário único parametrizável em tempo de execução — essa é precisamente a propriedade que a arquitetura troca por segurança. Um `docker build` por variante substitui um `docker run` por variante.

Os custos quantitativos por variante:

| Variante | Tamanho da imagem | Tempo de *startup* | Memória residente |
|---|---|---|---|
| `bff-default` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |
| `bff-internal` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |
| `bff-external` | `[A PREENCHER]` | `[A PREENCHER]` | `[A PREENCHER]` |

Espera-se que as diferenças sejam desprezíveis, uma vez que o bytecode das classes é idêntico entre as variantes (seção 7.2.1) e apenas os beans e invocadores gerados diferem. Medir esses valores permanece pertinente, ainda que o resultado esperado seja a equivalência: o objetivo geral do trabalho reivindica ganhos de **segurança** e de **manutenibilidade** — não de tamanho do artefato. Uma eventual equivalência de peso entre as variantes não contraria o objetivo; apenas confirma que a metamorfose atua sobre a superfície exposta, e não sobre o volume entregue. `[A PREENCHER]`

---

## 7.6 Contraste com a *feature flag* em tempo de execução

A alternativa em runtime mantém a operação no artefato por design: o endpoint é registrado, aparece no documento OpenAPI, responde a requisições HTTP e é recusado por uma verificação condicional. Sua superfície de ataque inclui a operação enquanto ela existir como ponto de entrada (MANADHATA; WING, 2011), e a correção da recusa depende de configuração correta, de lógica correta e da ausência de falhas no mecanismo de configuração.

O contraste com os resultados de 7.2.2 é direto: na abordagem metamórfica não há recusa a ser executada, porque não há rota a ser atingida. A diferença, contudo, é de **finalidade, não de mérito** — manter o código presente é o que habilita os usos legítimos da *feature flag*, discutidos no capítulo *Trabalhos Relacionados* (seção 3.2). `[A PREENCHER (opcional): reproduzir a variante com verificação em runtime e registrar a presença da rota no OpenAPI e a resposta 403.]`

---

## 7.7 Discussão

Retomam-se os objetivos específicos e as afirmações da Proposta, indicando o que os dados sustentam.

- **Derivar variantes de um código-fonte único.** Sustentado. As três variantes foram geradas sem qualquer alteração de código, apenas por *flags* de *build*.
- **Ausência da operação alheia ao cliente.** Sustentado **no nível do ponto de entrada**; **não sustentado** no nível da classe compilada, no empacotamento em JVM. A afirmação publicada deve ser a primeira.
- **Redução da superfície de ataque.** Sustentado, em contagem não ponderada de rotas.
- **Reuso sem duplicação.** Sustentado estruturalmente; quantificação pendente.
- **Melhor manutenibilidade.** Sustentado por inferência, não por medição direta: as regras de negócio existem em um único ponto e cada alteração se reflete em todas as variantes, condição que Taibi, Lenarduzzi e Pahl (2018) associam à redução do esforço de manutenção. A revisão de casos industriais de Chen, Usman e Badampudi (2024) reporta, na mesma direção, que o esforço para corrigir ou isolar erros é menor em ativos reusáveis — registrando, porém, que a proporção de erros de correção demorada é maior neles, e que a relação entre nível de reuso e esforço não é uniformemente favorável entre os casos. Este trabalho **não mede** manutenibilidade (por esforço de alteração ou densidade de defeitos); afere o **reuso**, que é seu antecedente. A distinção deve permanecer explícita, sob pena de o trabalho afirmar mais do que verificou.

O achado de 7.2 não enfraquece a contribuição: ele a delimita. A arquitetura entrega o que a métrica de superfície de ataque mede — a supressão do ponto de entrada — e não entrega o que ela não mede — a remoção física do bytecode. Reconhecer a fronteira entre as duas coisas é o que distingue uma tese verificada de uma tese apenas enunciada.

---

## 7.8 Ameaças à validade

**Validade de construto.** A contagem de pontos de entrada é não ponderada, enquanto a métrica de referência pondera por dano e custo de acesso (MANADHATA; WING, 2011). A classificação dos DTOs de uso exclusivo como ativos comuns é conservadora e favorece a tese de reuso; a análise de sensibilidade prevista em 7.4 mede o efeito dessa escolha. A **manutenibilidade**, enunciada no objetivo geral, é inferida a partir do reuso e não medida por instrumento próprio. A **resiliência** é enunciada no objetivo como decorrência esperada da menor exposição, e não é aferida por nenhum procedimento deste trabalho.

**Validade interna.** As três variantes foram construídas com `--no-cache`, a partir do mesmo código-fonte, e o contexto de *build* exclui o diretório `build/` local, de modo que o artefato de produção não herda estado da máquina. Persiste a dependência das convenções de nomenclatura do Quarkus na evidência de 7.2.2 — mitigada, quando executada, pela evidência de OpenAPI/HTTP.

**Validade externa.** Um único protótipo, um único domínio, um único *framework*. A generalização para outras linguagens, para mecanismos de variabilidade distintos ou para BFFs de maior porte não é sustentada pelos dados. A proporção de rotas suprimidas (7.3) é propriedade deste protótipo, não da arquitetura.

**Escopo da propriedade de segurança.** A segurança por ausência é a primeira linha de defesa, não a única. O protótipo não implementa autenticação, autorização por posse de recurso, limitação de abuso nem mascaramento de dados sensíveis. Registre-se ainda que a configuração de CORS do protótipo é permissiva (`quarkus.http.cors.origins=*`, com métodos não utilizados pelos controllers), o que **contradiz parcialmente** a narrativa de redução de exposição e deve ser corrigido ou explicitamente assumido como limitação da implementação.

---

## 7.9 Evidências pendentes e como obtê-las

> **Nota de trabalho:** cada item abaixo é insumo de uma figura ou tabela do capítulo. As figuras serão produzidas ao final; os comandos que geram a evidência ficam registrados aqui para reprodutibilidade.

| # | Evidência | Onde obter / comando |
|---|---|---|
| 1 | Rotas por variante no documento OpenAPI | Extrair o *schema* gerado em *build* de dentro de cada imagem, ou subir cada variante e requisitar `GET /openapi`. Recorte esperado: a rota do outro modo ausente. |
| 2 | Resposta HTTP à rota do outro modo | Subir `bff-external` e requisitar `PATCH /v1/order/1/delivery/1/PAID`; esperado `404`. Idem, espelhado, para `bff-internal` e `POST /v1/order/create`. |
| 3 | Ausência da classe no *build* nativo | `./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true` com cada par de *flags*; inspecionar o executável em busca do símbolo do controller. |
| 4 | `RSI`/`SSI` e contagem de classes (PP1) | Contador de LOC que distinga comentários (`cloc`, `scc` ou `tokei`) sobre `src/main/kotlin/**`, segundo a classificação da Metodologia. Registrar a versão da ferramenta. |
| 5 | Tamanho, *startup* e memória por variante (PP4) | `docker images` para o tamanho; log de inicialização do Quarkus para o tempo; `docker stats` em regime para a memória. |
| 6 | Contraste com *feature flag* em runtime (opcional) | Ramo experimental substituindo `@IfBuildProperty` por verificação condicional; repetir as evidências 1 e 2. |

**Figuras previstas para o capítulo:** (a) diagrama das três variantes derivadas do código-fonte único, com o ponto de variação nos adaptadores de entrada; (b) recorte comparativo da saída de `make artifact/analysis`, evidenciando classe presente × ponto de entrada ausente; (c) gráfico de rotas expostas por variante contra a linha de base do *build* completo.
