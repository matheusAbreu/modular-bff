# Trabalhos Relacionados

> 🟡 **Rascunho** — documento de trabalho, no mesmo estágio dos demais capítulos (`REFERENCIAL-TEORICO.md`, `METODOLOGIA.md`, `DESENVOLVIMENTO.md`). Estrutura e conteúdo sujeitos a revisão antes da consolidação no TCC.
>
> **Situação no TCC:** capítulo que posiciona a Arquitetura Metamórfica frente às abordagens já existentes para servir múltiplos clientes a partir de uma base comum, evidenciando a **lacuna** que ela preenche. Transforma em texto analítico a tabela comparativa do `CASE_STUDY.md` (seção 11).
>
> **Convenção:** citações no sistema autor-data (ABNT NBR 10520).

Servir múltiplos *frontends* a partir de um mesmo contexto de negócio é um problema com soluções conhecidas, cada qual com um compromisso distinto entre **reuso de código**, **segurança** e **complexidade operacional**. Este capítulo examina quatro abordagens recorrentes — repositórios separados, *feature flags* em tempo de execução, microsserviços distintos por cliente e linhas de produto por composição — e as contrasta com a Arquitetura Metamórfica, que é uma linha de produto por **anotação resolvida em tempo de compilação**. Ao final, discute-se por que a escassez de literatura *peer-reviewed* especificamente sobre BFF reforça o ineditismo da proposta.

---

## 3.1 Repositórios separados (um BFF por frontend)

A abordagem canônica do padrão *Backend for Frontend* — documentado por Sam Newman a partir de práticas observadas na indústria (NEWMAN, 2015) — é manter **um projeto por cliente**: cada *frontend* possui seu próprio BFF, com repositório e ciclo de vida independentes. Seu custo aparece quando há muitos *frontends*: a proliferação de BFFs multiplica o esforço de manutenção.

O ponto fraco é o **reuso**. Quando dois ou mais BFFs atendem ao mesmo contexto (por exemplo, aplicações de e-commerce), estruturas idênticas tendem a ser reescritas em cada projeto. A literatura de padrões de microsserviços registra os dois lados dessa tensão: o reuso de API "deve ser considerado com cuidado", pois clientes distintos que compartilham APIs semelhantes obrigam a alterá-las em conjunto a cada mudança; por outro lado, compartilhar um único componente entre projetos reduz o esforço, concentrando cada alteração em um só ponto (TAIBI; LENARDUZZI; PAHL, 2018). Em segurança, a separação física é uma vantagem — cada artefato expõe apenas o seu —, mas a manutenção divergente entre repositórios é um risco a médio prazo.

A Arquitetura Metamórfica preserva a separação física do artefato entregue (cada variante expõe só o que lhe compete), **sem** a duplicação de código-fonte: um repositório único, do qual se derivam os artefatos.

---

## 3.2 Feature flags / feature toggles em tempo de execução

Uma alternativa amplamente empregada para servir modos diferentes de um único artefato é a *feature flag* avaliada em **tempo de execução**: o endpoint existe no binário e uma verificação condicional decide, em runtime, se ele responde. É uma prática consolidada e valiosa na indústria, com propósitos para os quais foi projetada — ativar e desativar funcionalidades sem novo *deploy*, conduzir experimentos (*A/B testing*), fazer *rollout* gradual e reverter mudanças rapidamente. Em todos esses cenários, **manter o código presente no artefato é justamente o que se deseja**, pois é o que permite alternar o comportamento em produção.

A Arquitetura Metamórfica não compete com a *feature flag* nesses objetivos; ela persegue um objetivo **distinto e complementar** — a redução da superfície de ataque por construção. A diferença, portanto, não é de mérito, mas de finalidade e de momento de resolução da variabilidade.

Esse é o contraste central do trabalho. Como a variabilidade da *feature flag* é resolvida em runtime, a operação **permanece no artefato**; a superfície de ataque, definida pelos pontos de entrada presentes no sistema (MANADHATA; WING, 2011), inclui essa operação enquanto ela existir no binário — o que é adequado quando o objetivo é justamente poder ativá-la. Já a metamorfose resolve a variabilidade em tempo de compilação, incluindo ou excluindo componentes que alteram a estrutura do programa resultante (BAUM et al., 2019): quando o objetivo é garantir que uma operação **não** esteja disponível em determinado artefato, ela simplesmente não é compilada. São duas estratégias para problemas diferentes — flexibilidade em runtime, de um lado; ausência verificável no artefato, de outro.

---

## 3.3 Microsserviços distintos por cliente

Uma terceira via é decompor o atendimento a cada cliente em **microsserviços distintos**. Ganha-se separação real e implantação independente, mas ao custo de **duplicação de domínio** entre serviços e de alta complexidade operacional — a infraestrutura distribuída que os microsserviços exigem (TAIBI; LENARDUZZI; PAHL, 2018).

Em segurança, a decomposição tem um efeito ambivalente: embora isole serviços, ela **amplia** o número de pontos de entrada do sistema como um todo, o que torna a segurança de arquiteturas de microsserviços mais desafiadora; a literatura aponta, inclusive, um foco de pesquisa desequilibrado em favor dos ataques externos, recomendando maior atenção às ameaças internas (HANNOUSSE; YAHIOUCHE, 2021). A Arquitetura Metamórfica responde a essa recomendação na origem — garantindo que operações internas sequer sejam compiladas no artefato externo — sem incorrer no custo distribuído de múltiplos serviços, já que mantém domínio e serviços compartilhados em um único código-fonte.

---

## 3.4 Linhas de produto por composição × por anotação

A proposta insere-se no campo das Linhas de Produto de Software (SPL), que distingue duas famílias de implementação de variabilidade: as abordagens **baseadas em composição** (orientação a *features*, orientação a aspectos, composição de componentes) e as **baseadas em anotação** (parâmetros, pré-processadores) (APEL et al., 2013). A via da anotação — da qual a compilação condicional é o exemplo clássico — é empiricamente dominante em sistemas reais (HUNSEN et al., 2016).

A Arquitetura Metamórfica é uma instância **baseada em anotação** resolvida no *build*: a `@IfBuildProperty` marca os *controllers* de cada modo, com a decisão ligada em tempo de compilação (APEL et al., 2013), e o efeito — variantes distintas geradas de um código único — é o mesmo descrito para a compilação condicional (HUNSEN et al., 2016; BAUM et al., 2019). A distinção frente às abordagens por composição é de granularidade e de momento: em vez de compor módulos ou aspectos, seleciona-se, em tempo de compilação, quais adaptadores de entrada existirão no artefato, mantendo intactas as camadas inferiores. A classificação de Apel et al. (2013) — que situa a programação orientada a aspectos entre as abordagens de composição e os pré-processadores entre as de anotação — trata a escolha do mecanismo de variabilidade como decisão de engenharia com vantagens e desvantagens conhecidas; as abordagens por anotação, em particular, são amplamente usadas na prática por serem simples e suportadas nativamente por muitos ambientes de programação (APEL et al., 2013).

---

## 3.5 Síntese comparativa

O quadro a seguir resume o compromisso de cada abordagem — versão analítica da tabela do `CASE_STUDY.md` (seção 11):

| Abordagem | Reuso de código | Segurança | Complexidade operacional |
|---|---|---|---|
| Repositórios separados | ❌ Código duplicado | 🟡 Separação física, mas manutenção divergente | 🟡 Vários repositórios e pipelines |
| *Feature flag* em runtime | ✅ Mesmo código | 🟡 Endpoint **presente** no binário — por design, para permitir ativação em runtime | 🟢 Um artefato, variável de ambiente |
| Microsserviços distintos | ❌ Duplicação de domínio | 🟡 Separação real, custo alto | 🔴 Infraestrutura distribuída |
| **Arquitetura Metamórfica** | ✅ Mesmo código-fonte | ✅ Endpoint **não existe** no artefato | 🟡 Pipelines distintos por artefato |

Cada abordagem otimiza um objetivo diferente. Os repositórios separados e os microsserviços distintos priorizam a separação, ao custo do reuso; a *feature flag* em runtime preserva o reuso e mantém o endpoint presente no binário — adequado ao seu propósito de ativação em produção. A Arquitetura Metamórfica ocupa um ponto ainda não coberto por essas alternativas — **reuso de código-fonte com ausência do endpoint no artefato** —, pagando por isso o custo de pipelines distintos por variante, a ser dimensionado no capítulo de Avaliação (PP4).

---

## 3.6 A lacuna: escassez de literatura peer-reviewed sobre BFF

O padrão *Backend for Frontend* nasceu na indústria (NEWMAN, 2015) e, embora amplamente praticado, é pouco tratado em literatura acadêmica revisada por pares — frequentemente aparece de forma indireta, sob a discussão de *API Gateway* e agregadores (TAIBI; LENARDUZZI; PAHL, 2018). Essa escassez tem duas implicações. Primeiro, **reforça o ineditismo** de um trabalho que sistematiza uma estratégia arquitetural específica para BFFs. Segundo, obriga a fundamentação a apoiar-se em campos maduros e adjacentes — Linhas de Produto de Software, variabilidade por compilação condicional e superfície de ataque —, exatamente o que este trabalho faz ao enquadrar a metamorfose como derivação de produtos (APEL et al., 2013) com efeito de segurança mensurável (MANADHATA; WING, 2011).

---

> **Referência opcional (sem PDF no acervo `docs/refs/`):** a afirmação de que *feature toggles* são **amplamente adotadas e valorizadas na indústria** está no texto de forma não citada. Para embasá-la formalmente, obter o PDF de **RAHMAN et al. (2016)**, verificar o trecho e registrar em `CITACOES_ABNT.md` (seguindo o *modus operandi* de `FONTES-ACADEMICAS.md`).
