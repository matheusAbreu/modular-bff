# Proposta: Arquitetura Metamórfica

> 🟡 **Rascunho** — documento de trabalho, no mesmo estágio dos demais capítulos (`REFERENCIAL-TEORICO.md`, `TRABALHOS-RELACIONADOS.md`, `METODOLOGIA.md`, `DESENVOLVIMENTO.md`). É uma **reescrita refinada** da seção *Proposta: Arquitetura Metamórfica* da Proposta original, nutrida com o protótipo já implementado, as citações verificadas em `CITACOES_ABNT.md` e o enquadramento equilibrado de *feature toggles*. Estrutura e conteúdo sujeitos a revisão antes da consolidação no TCC.
>
> **Relação com os demais capítulos:** a fundamentação teórica completa está no Referencial Teórico (`REFERENCIAL-TEORICO.md`, seções 2.3–2.6); a comparação com abordagens alternativas, em Trabalhos Relacionados (`TRABALHOS-RELACIONADOS.md`); a implementação e a reprodução dos artefatos, em Desenvolvimento (`DESENVOLVIMENTO.md`). Este capítulo apresenta o **artefato** e sua contribuição, remetendo a eles para não duplicar.
>
> **Convenção:** citações no sistema autor-data (ABNT NBR 10520).

Este capítulo apresenta a **Arquitetura Metamórfica**: uma estratégia em que um único código-fonte assume formas diferentes conforme o cliente que se propõe a servir, resolvendo em **tempo de compilação** qual conjunto de operações cada artefato conterá. A seção 4.1 delimita o problema; a 4.2 formula a proposta; a 4.3 mostra sua concretização no protótipo; a 4.4 sintetiza a fundamentação; e a 4.5 desenvolve a principal contribuição — a *segurança por ausência*.

---

## 4.1 O problema: especialização × reuso em múltiplos BFFs

O padrão *Backend for Frontend* (BFF) especializa a lógica de agregação de um API Gateway para atender a um único *frontend*, entregando a cada cliente uma API sob medida (TAIBI; LENARDUZZI; PAHL, 2018; NEWMAN, 2021). Ganha-se personalização, implantação e escalabilidade independentes; mas, quando dois ou mais BFFs atendem a um mesmo contexto de negócio — várias aplicações de e-commerce, por exemplo —, grande parte de suas estruturas tende a ser idêntica. Emerge então uma tensão entre **especialização** e **reuso**: quanto mais *frontends*, mais projetos BFF a manter, com estruturas duplicadas.

A literatura de padrões de microsserviços registra os dois lados dessa tensão. O reuso de API "deve ser considerado com cuidado", pois clientes distintos que compartilham APIs semelhantes obrigam a alterá-las em conjunto a cada mudança; por outro lado, compartilhar um único componente entre projetos reduz o esforço, concentrando cada alteração em um só ponto (TAIBI; LENARDUZZI; PAHL, 2018). É exatamente esse ganho de reuso — **sem renunciar à especialização por cliente** — que a arquitetura proposta persegue.

---

## 4.2 A proposta: variabilidade resolvida em tempo de compilação

A proposta reúne três ideias: a robustez de um projeto único que concentra a lógica de negócio; o foco de desempenho que o BFF oferece a cada cliente; e a capacidade de o próprio projeto **identificar qual cliente servir**. Para conciliá-las, propõe-se uma **configuração resolvida em tempo de compilação**: a partir dela, o projeto assume a forma adequada a cada cliente, incluindo somente as operações que lhe competem.

O termo *metamórfica* descreve essa propriedade: o artefato final **muda de forma** conforme a configuração escolhida no *build* — e não conforme uma decisão avaliada em tempo de execução. A operação que não pertence a um dado cliente não é apenas ignorada; ela **deixa de existir** no artefato correspondente. Cada configuração produz, portanto, uma variante distinta do sistema, derivada de uma base comum.

---

## 4.3 Concretização no protótipo

A proposta foi materializada em um protótipo funcional — um BFF de domínio de e-commerce escrito em **Kotlin** sobre o framework **Quarkus**. Nele, a variabilidade em tempo de compilação é obtida com a anotação `@IfBuildProperty` (do Quarkus Arc), aplicada aos controladores de cada modo:

- um **controlador de leitura** permanece sempre presente, em qualquer artefato;
- os controladores das operações **interna** (atualização de status de entrega) e **externa** (criação de pedido) são **incluídos ou removidos** do artefato conforme a configuração selecionada no *build*.

Assim, de um mesmo código-fonte derivam-se artefatos distintos — interno e externo —, cada um contendo apenas as operações que lhe competem, enquanto domínio, serviços e repositórios permanecem **compartilhados** entre todas as variantes. Os detalhes de implementação, a ligação entre a anotação e as variáveis de ambiente, o pipeline de *build* por artefato e o procedimento de verificação (inspeção do JAR) são apresentados no capítulo de **Desenvolvimento**.

---

## 4.4 Fundamentação (síntese)

Do ponto de vista teórico, a configuração em tempo de compilação insere-se no campo das **Linhas de Produto de Software** (SPL), no qual múltiplos produtos são derivados de uma base comum pela resolução de sua variabilidade; cada artefato (interno/externo) é um **produto derivado** na fase de *product derivation* (HORCAS; PINTO; FUENTES, 2023). Entre as duas famílias de implementação de variabilidade — baseada em composição e baseada em anotação —, a proposta pertence à **segunda**: a `@IfBuildProperty` é um parâmetro de configuração resolvido no *build*, mesmo princípio da compilação condicional consolidada na indústria (HUNSEN et al., 2016; BAUM et al., 2019). A viabilidade de preservar as camadas inferiores compartilhadas apoia-se, ainda, no princípio de isolar as regras de negócio dos detalhes de framework, mantendo a anotação de variabilidade nos adaptadores de entrada (MARTIN, 2017).

> O desenvolvimento completo desses conceitos — SPL, variabilidade por anotação, compilação condicional, superfície de ataque e arquitetura hexagonal — está no **Referencial Teórico** (`REFERENCIAL-TEORICO.md`, seções 2.3–2.6).

---

## 4.5 Segurança por ausência

A eliminação de código em tempo de compilação confere à arquitetura metamórfica uma propriedade de segurança que abordagens em tempo de execução não alcançam. A tese pode ser enunciada de forma direta: **a maneira mais segura de proteger um endpoint é garantir que ele não exista no artefato implantado.**

O argumento apoia-se no conceito de **superfície de ataque**. A segurança de um sistema pode ser aferida pelo conjunto de recursos e pontos de entrada por meio dos quais um atacante pode influenciá-lo — quanto maior essa superfície, mais formas de ataque existem e mais inseguro o sistema tende a ser (MANADHATA; WING, 2011). Por operar no nível de projeto, essa métrica pode ser empregada deliberadamente por arquitetos para reduzir a exposição (MANADHATA; WING, 2011). Ao remover no *build* as rotas que não competem a determinado cliente, a arquitetura metamórfica reduz por construção o número de pontos de entrada do artefato: onde a rota não existe, não há *bean* instanciado, não há configuração capaz de reativá-la por engano e não há vulnerabilidade a explorar.

Convém contrastar essa *segurança por ausência* com a *segurança por proteção*, na qual o endpoint existe no binário e se defende por camadas de autenticação, autorização e limitação de requisições. A segurança por proteção é necessária, mas depende da correção contínua de cada camada. Quanto à **feature flag em tempo de execução**, a distinção é de **finalidade, não de mérito**: manter a operação no artefato é justamente o que habilita seus usos legítimos e valiosos — ativar funcionalidades sem novo *deploy*, conduzir experimentos e realizar *rollout* gradual. A arquitetura metamórfica não compete com a *feature flag* nesses objetivos; persegue um objetivo **distinto e complementar** — garantir que a operação que não compete a um cliente sequer exista no artefato dele. Onde a *feature flag* preserva o código por design, a metamorfose o remove em tempo de compilação: são respostas a problemas diferentes (ver `TRABALHOS-RELACIONADOS.md`, seção 3.2).

A pertinência dessa propriedade acentua-se em arquiteturas distribuídas. Em microsserviços, a segurança torna-se mais desafiadora justamente pelo grande número de pontos de entrada gerados pela decomposição do sistema, e a literatura aponta um foco de pesquisa desequilibrado em favor dos ataques externos, recomendando maior atenção às ameaças internas (HANNOUSSE; YAHIOUCHE, 2021). A separação entre modos interno e externo responde a essa recomendação na origem: em vez de confiar que o artefato externo saberá recusar operações internas em tempo de execução, garante-se que tais operações sequer sejam compiladas nele.

Cabe delimitar o alcance da estratégia. A segurança por ausência elimina uma classe inteira de riscos — a de um endpoint existir onde não deveria —, mas **não substitui** as demais camadas de proteção: autenticação do solicitante, autorização por posse do recurso, limitação de abuso e mascaramento de dados sensíveis permanecem necessários. O modelo adequado é tratá-la como a **primeira linha de defesa**, sobre a qual as demais camadas se constroem.

---

## 4.6 Síntese

A Arquitetura Metamórfica combina duas propriedades que as alternativas usuais não reúnem simultaneamente: **reuso de código-fonte** (domínio, serviços e repositórios compartilhados) e **ausência, no artefato, das operações que não competem ao cliente**. O custo dessa combinação — pipelines e artefatos distintos por variante — e as evidências de reuso, de redução de superfície de ataque e de ausência verificável no binário são objeto do capítulo de **Avaliação**, guiado pelas perguntas de pesquisa definidas na Metodologia (`METODOLOGIA.md`).

---

> **Pendências de redação:**
> - **Figuras:** a Proposta original traz placeholders `[Trocar imagem]` (comparação API Gateway × BFF; cenário de BFFs duplicados; esquema da configuração em tempo de compilação). Produzir os diagramas definitivos ao consolidar.
> - **Voz do texto:** este rascunho adota a 3ª pessoa acadêmica, enquanto a Proposta original usa 1ª pessoa ("proponho"). Uniformizar conforme o padrão da instituição ao montar o documento final.
> - **Desduplicação:** ao promover o Referencial Teórico a capítulo, confirmar que a fundamentação aqui permanece apenas em síntese, remetendo a ele.
