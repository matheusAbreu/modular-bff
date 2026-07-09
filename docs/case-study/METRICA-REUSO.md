# Métrica de Reuso (PP1) — método de cálculo

> 🟡 **Rascunho de método.** Define **como** a pergunta de pesquisa **PP1 — Reuso** será respondida no capítulo de Avaliação: qual métrica se adota, de qual literatura ela vem, como os artefatos do protótipo são classificados e qual o procedimento de coleta. **Não contém resultados** — os números são produzidos na execução e registrados na Avaliação.
>
> ✅ **Implementado no TCC** — absorvido pelo capítulo *Metodologia* do arquivo `Proposta TCC …md`, na seção **5.5.2 Métricas de reuso**. **A fonte da verdade passa a ser o TCC**; este documento é mantido apenas como registro de trabalho, e as notas de atribuição de fontes (seção 5) seguem valendo para quem for redigir a Avaliação.
>
> **Convenções:** citações no sistema autor-data (ABNT NBR 10520). As fontes foram obtidas e verificadas segundo o *modus operandi* de `docs/FONTES-ACADEMICAS.md`; o registro completo está em `REFERENCIAS_CIENTIFICAS.md` (seção 8) e as citações diretas em `CITACOES_ABNT.md` (Eixo 6).

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
