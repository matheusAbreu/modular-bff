# Referências Científicas de Apoio ao TCC

> **Tema do TCC:** *Reutilização de código para criar múltiplos projetos do tipo BFF (Backend for Frontend)* — a proposta de uma **Arquitetura Metamórfica**, em que um único código-fonte gera múltiplos artefatos especializados por meio de configuração em **tempo de compilação**, reduzindo duplicação e superfície de ataque.

Este documento reúne artigos e obras científicas que **corroboram os fundamentos teóricos** do trabalho. Cada entrada traz a citação completa, o link da fonte e um resumo em português, seguido de uma nota sobre como se relaciona com o TCC.

> ⚠️ **Nota de integridade acadêmica:** todas as referências abaixo foram localizadas em bases e repositórios reais (ACM, IEEE, Springer, arXiv, SciTePress). Ainda assim, **confirme cada citação na fonte original** antes de inseri-la no TCC — verifique páginas, DOI e ano na versão publicada, pois metadados de agregadores podem variar.

---

## 1. Microsserviços e API Gateway (contexto do problema)

Fundamentam a Introdução e a Justificativa: por que microsserviços motivam a agregação de dados, e por que o API Gateway/BFF surge como solução.

### 1.1 Pahl, C.; Jamshidi, P. — *Microservices: A Systematic Mapping Study* (CLOSER 2016)

- **Fonte:** Proceedings of the 6th International Conference on Cloud Computing and Services Science (CLOSER), 2016. DOI: 10.5220/0005785501370146
- **Link:** https://www.scitepress.org/papers/2016/57855/
- **Resumo:** Estudo de mapeamento sistemático que analisa 21 trabalhos publicados até o fim de 2015 sobre a emergência do padrão de microsserviços. Consolida os principais temas da área — granularidade de serviços, mecanismos de comunicação, práticas de implantação e escalabilidade — e posiciona os microsserviços frente às tecnologias de nuvem e contêineres, apontando direções de pesquisa.
- **Relação com o TCC:** É uma das referências seminais para caracterizar academicamente a arquitetura de microsserviços, base do cenário descrito na Introdução (múltiplas fontes de dados descentralizadas que o BFF vem resolver).

### 1.2 Taibi, D.; Lenarduzzi, V.; Pahl, C. — *Architectural Patterns for Microservices: A Systematic Mapping Study* (CLOSER 2018)

- **Fonte:** Proceedings of the 8th International Conference on Cloud Computing and Services Science (CLOSER), 2018.
- **Link:** https://www.scitepress.org/papers/2018/67983/
- **Resumo:** Mapeamento sistemático dos padrões arquiteturais de microsserviços relatados na literatura, classificando-os por objetivo (orquestração, escalabilidade, segurança) e discutindo padrões como **API Gateway**, agregadores e proxies, além de seus benefícios e desvantagens.
- **Relação com o TCC:** Dá suporte acadêmico à discussão sobre **API Gateway** e sua relação/contraste com o BFF, tema central da Justificativa do trabalho.

---

## 2. Backend for Frontend (BFF)

O conceito central do trabalho. A literatura **peer-reviewed** especificamente sobre BFF ainda é escassa (o padrão nasceu na indústria — documentado por Sam Newman a partir de práticas na REA e no SoundCloud, que credita o termo a Phil Calçado), o que reforça a **relevância e o ineditismo** do TCC.

### 2.1 Newman, S. — *Building Microservices: Designing Fine-Grained Systems* (O'Reilly, 2ª ed., 2021) — ❌ DESCARTADO / NÃO CITADO

> **Decisão:** esta obra **não é citada nem listada** nas Referências do TCC. Adota-se exclusivamente o artigo verificado **NEWMAN (2015)** (item 2.2) como fonte do padrão BFF. O registro abaixo é mantido apenas como memória da avaliação da fonte. Para reverter a decisão, seria preciso obter o livro e verificá-lo na fonte primária.

- **Fonte:** O'Reilly Media. ISBN: 978-1492034025.
- **Resumo:** Obra de referência que popularizou o padrão **Backend for Frontend**. Descreve o BFF como uma variação do API Gateway dedicada a um único tipo de cliente (frontend), discutindo suas vantagens de personalização e implantação independente e os custos de manutenção quando há muitos frontends.
- ⚠️ **NÃO VERIFICADO NA FONTE.** O resumo acima foi redigido a partir de conhecimento secundário; **não temos o PDF do livro** em `docs/refs/` nem citação direta em `CITACOES_ABNT.md`. Foi esse o motivo do descarte: o artigo de 2015 cobre o mesmo papel com verificação na fonte.

> **Observação:** para uma referência estritamente acadêmica sobre BFF, vale acompanhar publicações recentes em repositórios como ResearchGate e arXiv (ex.: estudos sobre *"BFF architecture across platforms and its dynamic adaptations"*), mas avalie criticamente o rigor/venue antes de citar, pois muitas ainda são preprints não revisados.

### 2.2 Newman, S. — *Backends For Frontends* (artigo do autor, 2015) — ✅ VERIFICADO / ABERTO

- **Fonte:** artigo de Sam Newman em samnewman.io, publicado em 18 nov. 2015. Subtítulo: *Single-purpose Edge Services for UIs and external parties*.
- **Link (aberto):** https://samnewman.io/patterns/architectural/bff/
- **Acesso:** 🟢 Público — página do próprio autor. **PDF no acervo:** `docs/refs/2.2 - Backends For Frontends.pdf` (23 p.), conferido página a página.
- **Resumo (verificado na fonte):** Newman caracteriza o BFF como um *edge service* de **propósito único**, dedicado a uma experiência/cliente específico ("one backend per user experience"), em contraste com backends/gateways de propósito geral que servem múltiplos clientes; situa a origem do padrão em observações na **REA** e no **SoundCloud**; e destaca os benefícios de otimização específica por cliente e de **autonomia das equipes** de UI (que passam a manter frontend e BFF juntos). A diretriz "*one experience, one BFF*" é por ele **atribuída a Stewart Gleadow** (creditando Phil Calçado e Mustafa Sezgin) — não a si próprio.
- **Nuance confirmada:** Newman credita o **termo "BFF" a Phil Calçado** (ex-SoundCloud); ele **documentou/popularizou** o padrão, não o "cunhou". Por isso os capítulos usam "documentado por Sam Newman", e não "cunhado por".
- **Relação com o TCC:** fonte **primária, pública e verificável** do conceito de BFF; passa a ser a referência citada nos capítulos no lugar do livro (2.1), que não temos como verificar. Sustenta a caracterização do BFF como backend dedicado a um único tipo de cliente usada no Referencial Teórico e na Proposta.
- ⚠️ **Atenção ao ano:** é o **artigo (2015)**, distinto da 2ª edição do livro (2021). Citar como NEWMAN (2015).

---

## 3. Linhas de Produto de Software e Variabilidade (fundamento da "Arquitetura Metamórfica")

**Este é o núcleo teórico do TCC.** A ideia de "gerar múltiplos produtos a partir de um mesmo código-fonte, ativando/desativando partes por configuração" é exatamente o campo de **Software Product Lines (SPL)** e **Feature-Oriented Software Development (FOSD)**. A "Metamorfose" proposta é uma forma de **variabilidade em tempo de compilação**.

### 3.1a Horcas, J. M.; Pinto, M.; Fuentes, L. — *Empirical Analysis of the Tool Support for Software Product Lines* (Software and Systems Modeling, 2023) — ❌ DESCARTADO / NÃO CITADO

- ⛔ **Revisão (27 nov. 2026).** Referência **considerada sem valia** para o trabalho: o artigo avalia o **suporte ferramental** às SPL, e não a **aplicabilidade** da abordagem — que é o que o TCC precisa sustentar. Foi **substituída** por 3.5 em todos os documentos e removida das listas de referências do TCC e de `CITACOES_ABNT.md` (27 nov. 2026; ROADMAP, item 12).
- **Candidatos descartados** (27 nov. 2026): BERGER et al. (2020, *Empirical Software Engineering*, DOI 10.1007/s10664-019-09787-6) e KRÜGER; BERGER (2020, ESEC/FSE, DOI 10.1145/3368089.3409684) — **nenhum dos dois aborda SPL** como objeto de estudo (tratam da adoção de gestão de variabilidade e dos custos de estratégias de reuso).
- ✅ **Substituto adotado:** APEL et al. (2013), *Feature-oriented software product lines* — ver **3.5**.
- O registro original segue abaixo, como histórico.

- **Fonte:** Software and Systems Modeling (SoSyM), v. 22, p. 377–414, 2023. DOI: 10.1007/s10270-022-01011-2
- **Link:** https://link.springer.com/article/10.1007/s10270-022-01011-2
- **Resumo:** Estudo empírico recente que avalia a maturidade das ferramentas de linhas de produto de software, identificando as características e atividades essenciais ao desenvolvimento de SPLs — desde a **modelagem de variabilidade** até a **derivação do produto** final. Analisa domínios de aplicação com alta variabilidade e mapeia o suporte ferramental existente para cada fase do ciclo de SPL.
- **Relação com o TCC:** Fornece uma fundamentação **atual** para o conceito central do trabalho — derivar **múltiplos produtos de software de uma base comum** por meio de configuração de variabilidade. A "Arquitetura Metamórfica" é, formalmente, uma instância de derivação de produtos: cada artefato (interno/externo) é um produto distinto derivado do mesmo código-fonte pela seleção de features em tempo de build.

### 3.1b Hunsen, C.; Zhang, B.; Siegmund, J.; Kästner, C.; Leßenich, O.; Becker, M.; Apel, S. — *Preprocessor-Based Variability in Open-Source and Industrial Software Systems: An Empirical Study* (Empirical Software Engineering, 2016)

- **Fonte:** Empirical Software Engineering (EMSE), v. 21, n. 2, p. 449–482, 2016. DOI: 10.1007/s10664-015-9360-1
- **Link (versão de registro, Springer — acesso pago):** https://doi.org/10.1007/s10664-015-9360-1
- **Link (versão pública — PDF dos autores):** https://www.se.cs.uni-saarland.de/publications/docs/HZS+16.pdf
- **Resumo:** Estudo empírico que analisa quantitativamente o uso de variabilidade **baseada em anotação** (pré-processador/compilação condicional, os `#ifdef`) em **27 sistemas** de código aberto e industriais. Mede métricas como a fração de código variável, granularidade e dispersão das anotações, mostrando que a compilação condicional é um mecanismo **amplamente adotado na prática** tanto em projetos open source quanto na indústria (com fração de código variável ainda maior nos sistemas industriais).
- **Relação com o TCC:** Corrobora empiricamente que a **variabilidade em tempo de compilação por anotação** — a mesma categoria do `@IfBuildProperty` do Quarkus e da "Metamorfose Rasa" proposta — não é uma curiosidade acadêmica, mas um mecanismo consolidado e mensurável em sistemas reais. Fortalece a fundamentação da Arquitetura Metamórfica com evidência de larga escala. **Acesso aberto** (PDF hospedado pelos autores) e mais recente que a referência clássica de 2008.

### 3.2 Apel, S.; Kästner, C. — *An Overview of Feature-Oriented Software Development* (Journal of Object Technology, 2009)

- **Fonte:** Journal of Object Technology, v. 8, n. 5, p. 49–84, 2009. DOI: 10.5381/jot.2009.8.5.c5
- **Link:** https://www.jot.fm/issues/issue_2009_07/column5/
- **Resumo:** Artigo de visão geral sobre o Desenvolvimento de Software Orientado a Features (FOSD), apresentando o conceito de *feature* como unidade de funcionalidade que estrutura a variabilidade de um sistema, e revisando técnicas para modularizar e compor features.
- **Relação com o TCC:** Introduz, de forma concisa e citável, o vocabulário (feature, variabilidade, configuração) que dá rigor à descrição da arquitetura proposta e às abordagens "rasa/profunda" de metamorfose.

### 3.3 Baum, D.; Sixtus, C.; Vogelsberg, L.; Eisenecker, U. — *Understanding Conditional Compilation through Integrated Representation of Variability and Source Code* (SPLC 2019)

- **Fonte:** Proceedings of the 23rd International Systems and Software Product Line Conference (SPLC), Paris, 2019. DOI: 10.1145/3307630.3342387. Preprint: arXiv:1908.08375.
- **Link:** https://arxiv.org/abs/1908.08375
- **Resumo:** Investiga a **compilação condicional** (o mecanismo clássico de `#ifdef` do pré-processador C) como técnica de introdução de variabilidade em produtos de software, e propõe uma representação integrada entre a variabilidade e o código-fonte para facilitar sua compreensão e análise.
- **Relação com o TCC:** Dá lastro científico à decisão de **eliminar código em tempo de compilação** em vez de em runtime. Corrobora diretamente o argumento do "Design Metamórfico": o artefato final *muda de forma* conforme a configuração de build, e o trecho não selecionado simplesmente não existe no binário.

### 3.4 Adkins, B.; et al. — *From Conditional Compilation to Aspects: A Case Study in Software Product Lines Migration*

- **Fonte:** Estudo de caso sobre migração de linhas de produto (disponível em ResearchGate/Academia.edu).
- **Link:** https://www.researchgate.net/publication/220030972
- **Resumo:** Estudo de caso comparando a implementação de variabilidade via **compilação condicional** com alternativas modulares (aspectos), discutindo as vantagens e limitações de cada técnica para manter uma linha de produtos.
- **Relação com o TCC:** Útil para a seção de **comparação de alternativas** do estudo de caso (repositórios separados vs. feature flag em runtime vs. compilação condicional), evidenciando que a escolha do ponto de variabilidade é um problema de engenharia bem estudado.

### 3.5 Apel, S.; Batory, D.; Kästner, C.; Saake, G. — *Feature-Oriented Software Product Lines: Concepts and Implementation* (Springer, 2013) — ✅ ADOTADO (substitui 3.1a)

- **Fonte:** Berlin; Heidelberg: Springer, 2013. ISBN 978-3-642-37520-0 (impresso); 978-3-642-37521-7 (e-book). DOI: 10.1007/978-3-642-37521-7 (conferidos na página de créditos do PDF).
- **Link (registro):** https://doi.org/10.1007/978-3-642-37521-7
- **Acesso:** 🟡 Livro impresso (exemplar do autor). **PDF no acervo** (para consulta): `docs/refs/3.5 - Feature-Oriented Software Product Lines - Concepts and Implementation (Apel et al. 2013).pdf`. As páginas registradas são as **impressas** no livro.
- **Resumo (verificado no texto integral):** Livro-texto de SPL orientadas a *features*. Parte I (cap. 1–2): o que são SPL, suas promessas e o processo de desenvolvimento em **engenharia de domínio** (*development for reuse*) e **engenharia de aplicação** (*development with reuse*), além dos caminhos de adoção (proativo, extrativo, reativo). Cap. 3: dimensões da implementação de variabilidade — **tempo de ligação** (compilação, carga, execução), tecnologia (linguagem × ferramenta) e representação (**anotação × composição**). Cap. 4–5: mecanismos clássicos, incluindo **parâmetros em runtime**, sistemas de *build* e **pré-processadores** (compilação condicional).
- **Relação com o TCC:** é a **fonte principal do fundamento de SPL** e substitui Horcas, Pinto e Fuentes (3.1a, descartado) em todas as suas funções. Aplicado no TCC nas seções 2.3, 2.5, 2.7, 3.4, 3.6, 4.4, 4.5, 7.6 e 8:
  1. **definição de variabilidade** como derivação de produtos de uma base comum (Def. 3.1, p. 48) e de produto como seleção válida de *features* (Def. 2.2, p. 19);
  2. **processos de engenharia** de domínio e de aplicação (p. 21–22);
  3. **variabilidade por anotação** (Def. 3.4, p. 51) — a categoria do `@IfBuildProperty` —, em contraste com a composição (Def. 3.5);
  4. **tempo de compilação** (Def. 3.2, p. 49): o código desnecessário pode ser removido do produto, ao custo de o produto deixar de ser variável depois de implantado — sustenta a PP4;
  5. **segurança**: incluir funcionalidade desnecessária no produto entregue "*may also be considered as potential security threat*" (p. 49), e, na variabilidade por parâmetros em runtime, a funcionalidade desativada continua sendo entregue e "*shipping unused code opens unnecessary potential targets for attacks*" (p. 66–67) — **primeira fonte de SPL do acervo a ligar a variabilidade em compilação à redução de alvos de ataque** (complementa 5.1) e a sustentar o **contraste com *feature flags*** (seção 4);
  6. **adoção extrativa** (p. 40): partir de produtos existentes e refatorá-los em uma linha de produto — o cenário de múltiplos BFFs já separados.
- **Status:** obra de 2013, **fora da janela de dez anos**; aceita pelo **mérito** (livro-texto de referência da área, citado por Berger et al. (2020) entre os "*textbook SPLE methods*"), no mesmo regime do Peffers et al. (7.2). A **redundância recente** para a compilação condicional é dada por Hunsen et al. (2016, 3.1b) e Baum et al. (2019, 3.3).
- ⚠️ **Ressalva de escopo.** O exemplo de segurança do livro é de *buffer overflow* em código C nativo, e a passagem é argumentativa, não uma medição. O livro **não** trata de BFF, de Quarkus nem de superfície de ataque no sentido de Manadhata e Wing; a métrica continua ancorada em 5.1. Não atribuir ao livro mais do que ele afirma.

---

## 4. Feature Toggles / Feature Flags (contraste com a abordagem metamórfica)

O TCC (e o CASE_STUDY.md) argumenta explicitamente que a compilação condicional é **mais segura que feature flags em runtime**. Estes artigos fundamentam essa comparação.

### 4.1 Rahman, M. T.; Querel, L.-P.; Rigby, P. C.; Adams, B. — *Feature Toggles: Practitioner Practices and a Case Study* (MSR 2016)

- **Fonte:** Proceedings of the 13th International Conference on Mining Software Repositories (MSR), 2016. DOI: 10.1145/2901739.2901745
- **Link:** https://dl.acm.org/doi/10.1145/2901739.2901745
- **Resumo:** Caracteriza o uso de *feature toggles* na indústria a partir de práticas de profissionais e de um estudo de caso, mostrando como as flags são usadas para controlar funcionalidades em produção e discutindo seu ciclo de vida.
- **Relação com o TCC:** Fundamenta a descrição da alternativa "feature flag em runtime" que o TCC compara com o Design Metamórfico, demonstrando que essa prática é real e amplamente adotada.

### 4.2 Mahdavi-Hezaveh, R.; Dremann, J.; Williams, L. — *Software Development with Feature Toggles: Practices Used by Practitioners* (Empirical Software Engineering, 2021)

- **Fonte:** Empirical Software Engineering, v. 26, 2021. DOI: 10.1007/s10664-020-09901-z
- **Link:** https://link.springer.com/article/10.1007/s10664-020-09901-z
- **Resumo:** Identifica 17 práticas em quatro categorias (gerenciamento, inicialização, implementação e limpeza) para o uso eficaz de feature toggles, evidenciando que gerenciá-las é difícil e propenso a erros.
- **Relação com o TCC:** Reforça, com evidência empírica, o argumento de que a abordagem em runtime carrega **complexidade e risco operacional** — o endpoint *existe* no binário e depende de configuração correta —, justamente o que o Design Metamórfico elimina por construção.

### 4.3 Mahdavi-Hezaveh, R.; Ajmeri, N.; Williams, L. — *Feature Toggles as Code: Heuristics and Metrics for Structuring Feature Toggles* (Information and Software Technology, 2022)

- **Fonte:** Information and Software Technology, v. 145, 2022. DOI: 10.1016/j.infsof.2021.106813
- **Link:** https://dl.acm.org/doi/10.1016/j.infsof.2021.106813
- **Resumo:** A partir de repositórios open source, propõe heurísticas e métricas para estruturar feature toggles, tratando-as explicitamente como potencial **dívida técnica** que precisa ser removida após o uso, pois aumentam a complexidade e os caminhos de execução do código.
- **Relação com o TCC:** Corrobora o argumento central do capítulo de segurança do estudo de caso: flags em runtime deixam **código morto e caminhos de execução extras** no binário (superfície de ataque e dívida técnica), enquanto a exclusão em tempo de compilação não deixa esse resíduo.

---

## 5. Segurança por Redução de Superfície de Ataque (a "tese de segurança por ausência")

Fundamenta o Capítulo 11 do estudo de caso: *"A forma mais segura de proteger um endpoint é garantir que ele não exista no binário implantado."*

### 5.1 Manadhata, P. K.; Wing, J. M. — *An Attack Surface Metric* (IEEE Transactions on Software Engineering, 2011)

- **Fonte:** IEEE Transactions on Software Engineering, v. 37, n. 3, p. 371–386, 2011. DOI: 10.1109/TSE.2010.60
- **Link (versão de registro, IEEE — acesso pago):** https://ieeexplore.ieee.org/document/5482589
- **Link (versão pública — relatório técnico CMU-CS-05-155):** http://reports-archive.adm.cs.cmu.edu/anon/2005/CMU-CS-05-155.pdf
- **Resumo:** Trabalho seminal que formaliza a **superfície de ataque** de um sistema como métrica de segurança, medindo o grau de liberdade que um atacante tem para influenciar a execução do sistema (via métodos, canais e itens de dados de entrada/saída). Demonstra que **reduzir a superfície de ataque** reduz o risco de segurança, de forma independente de linguagem.
- **Relação com o TCC:** É o **fundamento científico direto** da "segurança por ausência". A remoção de endpoints em tempo de build reduz mensuravelmente a superfície de ataque do artefato — exatamente o efeito que Manadhata e Wing formalizam e recomendam.

### 5.2 Hannousse, A.; Yahiouche, S. — *Securing Microservices and Microservice Architectures: A Systematic Mapping Study* (Computer Science Review, 2021)

- **Fonte:** Computer Science Review, v. 41, art. 100415, 2021. DOI: 10.1016/j.cosrev.2021.100415
- **Link (versão de registro, Elsevier — acesso pago):** https://doi.org/10.1016/j.cosrev.2021.100415
- **Link (versão pública — preprint arXiv):** https://arxiv.org/abs/2003.07262
- **Resumo:** Mapeamento sistemático dos desafios e soluções de segurança específicos de arquiteturas de microsserviços, catalogando ameaças (superfície de ataque ampliada, autenticação/autorização entre serviços) e mecanismos de mitigação.
- **Relação com o TCC:** Contextualiza academicamente por que a **superfície de ataque** é uma preocupação de primeira ordem em arquiteturas distribuídas/BFF, dando peso ao ganho de segurança da abordagem metamórfica.

### 5.3 *SoK: Security of Microservice Applications — A Practitioners' Perspective on Challenges and Best Practices* (arXiv, 2022)

- **Fonte:** arXiv:2202.01612, 2022.
- **Link:** https://arxiv.org/abs/2202.01612
- **Resumo:** *Systematization of Knowledge* (SoK) sobre segurança de aplicações baseadas em microsserviços, consolidando desafios e boas práticas segundo a perspectiva de profissionais, incluindo controle de acesso, isolamento e minimização de exposição.
- **Relação com o TCC:** Reforça a seção "O que o Design Metamórfico não resolve" — a lista de camadas complementares (autenticação, autorização/IDOR, rate limiting) do estudo de caso está alinhada com as boas práticas consolidadas nesta obra.

---

## 6. Arquitetura Hexagonal / Clean Architecture e Integração Contínua (organização e entrega do código)

Fundamenta a decisão de estruturar o projeto em Ports & Adapters, isolando o domínio (6.1, 6.2), e o *pipeline* de integração contínua e de *build* por artefato que entrega o protótipo (6.3).

### 6.1 Cockburn, A. — *Hexagonal Architecture (Ports and Adapters)*

- **Fonte:** Publicação original do autor (2005), consolidada em *"Hexagonal Architecture Explained"* (Cockburn & Garrido, 2024).
- **Link:** https://alistair.cockburn.us/hexagonal-architecture/
- **Resumo:** Propõe organizar a aplicação em torno de um núcleo de regras de negócio isolado, comunicando-se com o mundo externo (UI, banco, APIs) exclusivamente por **portas** (interfaces) e **adaptadores**. O objetivo é permitir testar o domínio em isolamento e trocar tecnologias externas sem afetar as regras de negócio.
- **Relação com o TCC:** É a fonte canônica da arquitetura adotada no protótipo. Note que é **literatura técnica (grey literature)**, não um paper revisado por pares; para rigor acadêmico, complemente com Martin (2017), *Clean Architecture*, que sistematiza os mesmos princípios.

### 6.2 Martin, R. C. — *Clean Architecture: A Craftsman's Guide to Software Structure and Design* (Prentice Hall, 2017)

- **Fonte:** Prentice Hall / Pearson, 2017. ISBN: 978-0134494166.
- **Resumo:** Sistematiza a separação entre regras de negócio e detalhes de infraestrutura por meio da **Regra de Dependência** (dependências apontam para dentro, do detalhe para a política), unificando os princípios das arquiteturas hexagonal e onion em anéis concêntricos.
- **Relação com o TCC:** Sustenta a decisão arquitetural de isolar `domain`/`services` dos `adapters`, que é o que torna possível o **reuso sem duplicação** — pré-requisito para o compartilhamento das camadas inferiores entre os controllers metamórficos.

### 6.3 Shahin, M.; Ali Babar, M.; Zhu, L. — *Continuous Integration, Delivery and Deployment: A Systematic Review on Approaches, Tools, Challenges and Practices* (IEEE Access, 2017) — ✅ VERIFICADO

- **Escopo no capítulo:** ancora o vocabulário de CI/CD usado nas seções 6.6 (build por artefato), 6.7 (integração contínua) e 7.5 (PP4, custo de verificação por variante).
- **Fonte:** IEEE Access, v. 5, p. 3909–3943, 2017. DOI: 10.1109/ACCESS.2017.2685629 (metadados confirmados no Crossref).
- **Link (versão de autor, aberta):** https://arxiv.org/abs/1703.07019
- **Acesso:** 🟢 Público — periódico *open access*; versão de autor no arXiv, identificada no cabeçalho do próprio PDF como o artigo da IEEE Access. **PDF no acervo:** `docs/refs/6.3 - Continuous Integration, Delivery and Deployment - A Systematic Review.pdf`. ⚠️ A paginação do PDF do arXiv (1–32) **não** coincide com a do periódico; citar por seção.
- **Resumo (verificado no texto integral):** Revisão sistemática de 69 artigos revisados por pares (2004 a jun. 2016) sobre as "práticas contínuas". Define e distingue as três: **CI**, integração frequente do trabalho da equipe, com *build* e testes automatizados; **CDE** (*Continuous DElivery*), manter a aplicação sempre em estado pronto para produção após passar por testes e verificações automatizados, com a implantação final manual; **CD** (*Continuous Deployment*), implantar automaticamente cada mudança em produção por meio de um *deployment pipeline*. Identifica trinta abordagens e ferramentas — entre elas, reduzir o tempo de *build* e teste em CI — e fatores críticos de adoção, com destaque para **teste (esforço e tempo)**.
- **Relação com o TCC:** fornece as **definições revisadas por pares** de CI, entrega e implantação contínuas, hoje usadas sem citação no TCC (seções 6.7 e 7.5.1, `CASE_STUDY.md`). Sustenta ainda o argumento de custo da PP4: a abordagem metamórfica multiplica os artefatos a construir e verificar, e o artigo registra **tempo de *build* e esforço de teste** como desafios centrais dessas práticas.
- ⚠️ **Ressalva de escopo.** O artigo **não** trata de variabilidade em tempo de compilação nem de múltiplos artefatos derivados de um mesmo código-fonte, e não formula o princípio *build once, deploy many* (que é de Humble e Farley, 2010, fora do acervo). A corroboração é das **definições** e dos **desafios gerais**, não da estratégia de *build* por variante.

---

## 7. Metodologia de Pesquisa — Design Science Research (DSR)

> ✅ **Pendência resolvida.** As referências **7.2** (processo) e **7.3** (enquadramento da contribuição) foram obtidas, verificadas no texto completo e integradas ao capítulo de Metodologia; acrescentou-se a **7.6**, que dá enquadramento **recente e específico de Engenharia de Software**. As demais (7.1, 7.4, 7.5) permanecem sem verificação e **não devem ser citadas**.
>
> ⚠️ **Nota sobre a idade das fontes.** Peffers et al. (7.2) é de 2007. Como no caso das métricas de reuso (seção 8), adota-se **redundância deliberada**: 7.3 (2018) atesta que o modelo de processo de Peffers et al. segue sendo referência corrente na literatura de DSR, e 7.6 (2020) sustenta a adequação do paradigma ao campo da Engenharia de Software — que é o campo deste trabalho, e não o de Sistemas de Informação, de onde a DSR provém.
>
> ⚠️ **Nota sobre a composição das referências:** privilegiaram-se fontes **mais recentes** (2018–2020) — vom Brocke, Hevner & Maedche (7.1) e Baskerville et al. (7.3) — em lugar dos marcos clássicos, mas o **Peffers et al. (2007) foi mantido de forma redundante** (7.2), por ser a fonte original do modelo das **seis atividades** usado na seção *Etapas da Pesquisa*. Assim, preserva-se a proveniência exata dessa estrutura sem abrir mão de embasamento atualizado. Igualmente foi dispensado apenas o Hevner et al. (2004), cujo papel de enquadramento passou ao vom Brocke, Hevner & Maedche (2020).

Fundamentam a escolha da **Design Science Research** como método condutor — a pesquisa orientada à construção e à avaliação de um *artefato* (a Arquitetura Metamórfica e o protótipo).

### 7.1 vom Brocke, J.; Hevner, A.; Maedche, A. — *Introduction to Design Science Research* (2020)

- **Fonte:** In: vom Brocke, J.; Hevner, A.; Maedche, A. (eds.). *Design Science Research. Cases*. Cham: Springer, 2020, p. 1–13. DOI: 10.1007/978-3-030-46781-4_1
- **Link (versão de registro, Springer — acesso pago):** https://link.springer.com/chapter/10.1007/978-3-030-46781-4_1
- **Acesso:** 🟡 Capítulo de livro **pago** no Springer. Cópias de autor disponíveis em repositórios institucionais (Univ. Münster, Univ. Liechtenstein) e ResearchGate — *grey literature*, verificar se corresponde à versão final antes de citar.
- **Resumo:** Capítulo introdutório recente que sintetiza os conceitos centrais da *Design Science Research* — o paradigma de resolução de problemas por criação de artefatos inovadores — e apresenta o processo e os componentes essenciais da DSR para orientar estudos de caso aplicados.
- **Relação com o TCC:** Substituto **atual** do enquadramento clássico de Hevner et al. (2004); fundamenta a definição da DSR e a natureza prescritiva (orientada a artefato) da contribuição, com terminologia de processo aplicável à seção *Etapas da Pesquisa*. ⏳ *Análise pendente.*

### 7.2 Peffers, K.; Tuunanen, T.; Rothenberger, M. A.; Chatterjee, S. — *A Design Science Research Methodology for Information Systems Research* (JMIS, 2007)

- **Fonte:** Journal of Management Information Systems, v. 24, n. 3, p. 45–77, 2007. DOI: 10.2753/MIS0742-1222240302
- **Link (versão de registro, Taylor & Francis — acesso pago):** https://www.tandfonline.com/doi/abs/10.2753/MIS0742-1222240302
- **Link (versão pública — PDF via Semantic Scholar):** https://www.semanticscholar.org/paper/A-Design-Science-Research-Methodology-for-Systems-Peffers-Tuunanen/186e57ffadbe45a30de067b0259357cded8db968
- **Acesso:** 🟢 **Público.** Reprint em PDF obtido em repositório aberto (indico.cern.ch). A versão de registro (Taylor & Francis) é paga. **PDF no acervo:** `docs/refs/7.2 - A Design Science Research Methodology for Information Systems Research.pdf`.
- **Resumo (verificado na fonte):** Sintetiza sete trabalhos anteriores num modelo de processo composto de **seis atividades em sequência nominal** (p. 52): identificação do problema e motivação; definição dos objetivos de uma solução; projeto e desenvolvimento; demonstração; avaliação; e comunicação (p. 46). Demonstra e avalia a metodologia por meio de quatro estudos de caso.
- **Relação com o TCC:** fonte **original e verificada** do modelo das seis atividades usado na seção *Etapas da Pesquisa* da Metodologia. ✅ *Verificado; citações inseridas.*
- ⚠️ **Ambiguidade de ano.** O volume 24, n. 3 do JMIS é de inverno de 2007–08; parte da literatura — inclusive Baskerville et al. (7.3) — cita a obra como **2008**. Este trabalho adota **2007**, conforme a data de registro do DOI. Manter a forma uniforme em todas as citações.

### 7.3 Baskerville, R.; Baiyere, A.; Gregor, S.; Hevner, A.; Rossi, M. — *Design Science Research Contributions: Finding a Balance between Artifact and Theory* (JAIS, 2018)

- **Fonte:** Journal of the Association for Information Systems, v. 19, n. 5, p. 358–376, 2018. DOI: 10.17705/1jais.00495
- **Link (página oficial, AIS eLibrary):** https://aisel.aisnet.org/jais/vol19/iss5/3/
- **Link (versão pública — publisher's version, repositório CBS):** https://research-api.cbs.dk/ws/files/59353297/abayomi_baiyere_et_al_design_science_research_contributions_publishersversion.pdf
- **Acesso:** 🟢 **Público.** Versão do editor aberta no repositório institucional da Copenhagen Business School. **PDF no acervo:** `docs/refs/7.3 - Design Science Research Contributions - Finding a Balance between Artifact and Theory.pdf`.
- **Resumo (verificado na fonte):** Discute o que constitui contribuição aceitável em DSR — o **artefato de projeto**, a **teoria de projeto**, ou ambos — e oferece recomendações acionáveis para autores e revisores. Ao revisar a literatura de processo, registra que "Peffers, Tuunanen, Rothenberger e Chatterjee propõem um método e um esboço de processo para conduzir DSR".
- **Relação com o TCC:** cumpre **duas funções**. (i) **Enquadramento da contribuição:** sustenta posicionar a contribuição deste trabalho no **artefato** (arquitetura + protótipo), sem reivindicar formulação de teoria de projeto. (ii) **Atualidade do processo:** por endossar explicitamente o modelo de Peffers et al., serve de corroboração recente (2018) de que a estrutura de seis atividades permanece referência corrente. ✅ *Verificado; citações inseridas.*
- ⚠️ **Gênero do texto.** Trata-se de um **editorial** do JAIS, e não de artigo de pesquisa. É revisado e citável, mas o gênero deve ser considerado ao ponderar seu peso como evidência.

### 7.6 Engström, E.; Storey, M.-A.; Runeson, P.; Höst, M.; Baldassarre, M. T. — *How software engineering research aligns with design science: a review* (Empirical Software Engineering, 2020) — ✅ VERIFICADO

- **Fonte:** Empirical Software Engineering, v. 25, p. 2630–2660, 2020. DOI: 10.1007/s10664-020-09818-7
- **Link (aberto):** https://link.springer.com/content/pdf/10.1007/s10664-020-09818-7.pdf · *Preprint:* arXiv:1904.12742
- **Acesso:** 🟢 Público (*open access* híbrido). **PDF no acervo:** `docs/refs/7.6 - How software engineering research aligns with design science.pdf`.
- **Resumo (verificado na fonte):** Revisão que aplica a lente de *design science* à pesquisa em Engenharia de Software, avaliando se ela ajuda a resumir e a apreciar contribuições da área. Registra que, "à semelhança de outras ciências do projeto, boa parte da pesquisa em engenharia de software visa projetar soluções para problemas práticos em contexto real" (p. 2630) e que a *design science*, "embora há tempos sugerida como paradigma útil para a pesquisa em engenharia de software, não é comumente usada" para enquadrá-la (p. 2652).
- **Relação com o TCC:** é o enquadramento **recente e do campo correto**. A DSR provém de Sistemas de Informação; este trabalho é de Engenharia de Software. Engström et al. (2020) sustentam a adequação do paradigma a pesquisas de ES que projetam soluções para problemas práticos — exatamente a natureza da Arquitetura Metamórfica e de seu protótipo. Fecha, junto de 7.3, a redundância de fontes recentes que ampara a citação de Peffers et al. (2007).
- ⚠️ **Limite do apoio.** O artigo **não** descreve nem endossa as seis atividades do DSRM; sua contribuição é de enquadramento do paradigma. A corroboração do modelo de processo cabe a 7.3, não a esta fonte.

### 7.4 Wieringa, R. J. — *Design Science Methodology for Information Systems and Software Engineering* (Springer, 2014)

- **Fonte:** Berlin: Springer, 2014. ISBN: 978-3-662-43838-1. DOI: 10.1007/978-3-662-43839-8
- **Link:** https://doi.org/10.1007/978-3-662-43839-8
- **Acesso:** 🔴 **Livro pago** (Springer). Sem versão aberta integral; há apenas materiais avulsos do autor (slides, capítulos de amostra) em domínio público.
- **Resumo:** Livro-texto que oferece diretrizes para a prática de *design science* em SI e Engenharia de Software, articulando o ciclo de projeto do artefato com o ciclo de investigação empírica (*design cycle* e *empirical cycle*).
- **Relação com o TCC:** Dá suporte à distinção entre projetar o artefato e avaliá-lo empiricamente, útil para amarrar a proposta (projeto) à avaliação (evidência). ⏳ *Análise pendente.*

### 7.5 Dresch, A.; Lacerda, D. P.; Antunes Júnior, J. A. V. — *Design Science Research: Método de Pesquisa para Avanço da Ciência e Tecnologia* (Bookman, 2015)

- **Fonte:** Porto Alegre: Bookman, 2015. ISBN: 978-85-8260-298-0.
- **Link:** https://books.google.com/books/about/Design_Science_Research.html?id=matYBQAAQBAJ
- **Acesso:** 🔴 **Livro comercial** (Bookman/Grupo A). Sem versão aberta; apenas prévia no Google Books e acesso institucional (ex.: Minha Biblioteca). Versão em **português**, conveniente para citação em ABNT.
- **Resumo:** Referência **em português** que sistematiza a *Design Science Research* como método de pesquisa, apresentando seus fundamentos, etapas e critérios de rigor para a produção de artefatos que resolvem problemas de engenharia e gestão.
- **Relação com o TCC:** Referência em língua portuguesa conveniente para a redação e citação da Metodologia no padrão ABNT, alinhada ao público do TCC. ⏳ *Análise pendente.*

---

## 8. Métricas de Reuso (fundamento do cálculo de PP1)

> Fundamentam o **método de cálculo da métrica de reuso**, detalhado em `METRICA-REUSO.md`. Todas as fontes foram obtidas e conferidas no texto completo.
>
> ⚠️ **Nota sobre a idade das fontes.** As formulações originais das métricas (8.1, de 1996; 8.2, de 2005) são as fontes canônicas e permanecem as **referências das fórmulas**. Para não apoiar a avaliação apenas em literatura de três décadas, adota-se deliberadamente uma **redundância**: 8.4 (2016) e 8.3 (2024) demonstram que as mesmas métricas seguem em uso na literatura empírica — a primeira quanto ao `RSI`, a segunda quanto ao `reuse level` —, compondo a linha 1996 → 2016 → 2024. Trata-se de corroboração, não de substituição: cada citação deve indicar a fonte apropriada ao que afirma.

### 8.1 Frakes, W.; Terry, C. — *Software Reuse: Metrics and Models* (ACM Computing Surveys, 1996) — ✅ VERIFICADO

- **Fonte:** ACM Computing Surveys, v. 28, n. 2, p. 415–435, 1996. DOI: 10.1145/234528.234531
- **Link (versão de registro, ACM — acesso pago):** https://dl.acm.org/doi/10.1145/234528.234531
- **Link (versão de autor, aberta):** relatório técnico TR-95-07, Virginia Tech — http://eprints.cs.vt.edu/archive/00000422/
- **Acesso:** 🟢 Aberto pela versão de autor. **PDF no acervo:** `docs/refs/8.1 - Software Reuse and Reusability Metrics and Models (TR-95-07).pdf`, conferido nas seções *Business Reuse Metrics*, *Amount of Reuse* e *Reuse Level*.
- **Resumo (verificado na fonte):** Survey que classifica métricas e modelos de reuso em seis tipos (custo-benefício, avaliação de maturidade, **quantidade de reuso**, modos de falha, avaliação de reusabilidade e métricas de biblioteca). Documenta o **reuse percent** de Poulin et al. — `RSI/(RSI+SSI) × 100` — e define os **reuse level metrics**: dado um item de nível superior composto de `L` itens de nível inferior, dos quais `E` vêm de repositório externo e `M` são internos usados mais de uma vez, tem-se `ERL = E/L`, `IRL = M/L` e `TRL = ERL + IRL`, todos entre 0 e 1.
- **Relação com o TCC:** É a **fonte direta das fórmulas** usadas para responder à PP1. O trabalho transpõe `RSI`/`SSI` da distinção "código herdado × código novo" para "base comum da linha de produto × adaptador específico da variante" — transposição declarada e justificada em `METRICA-REUSO.md` (seção 2.1).
- ⚠️ **Atenção à citação de segunda mão:** o *reuse percent* é de **Poulin et al. (1993)**, cuja fonte primária **não foi obtida**. Citar via Frakes e Terry (1996).

### 8.2 Frakes, W. B.; Kang, K. — *Software Reuse Research: Status and Future* (IEEE TSE, 2005) — ✅ VERIFICADO · ⚠️ **NÃO MAIS CITADO NO TCC**

> **Decisão (10 jul. 2026).** Esta fonte sustentava a **triangulação** entre *reuse percent* e *reuse level*. Com o abandono do *reuse level* (ver `METRICA-REUSO.md`, seção 6.1), o argumento desapareceu e a citação ficou órfã: a entrada foi **removida das Referências do TCC**. O registro é mantido aqui como memória da avaliação da fonte. Para reintroduzi-la, seria preciso um argumento que ela de fato sustente.

- **Fonte:** IEEE Transactions on Software Engineering, v. 31, n. 7, p. 529–536, jul. 2005. DOI: 10.1109/TSE.2005.85
- **Link (aberto):** https://www.cse.chalmers.se/~feldt/courses/sple/papers/frakes_2005_sw_reuse_status_and_future.pdf
- **Acesso:** 🟢 Público. **PDF no acervo:** `docs/refs/8.2 - Software Reuse Research - Status and Future.pdf`.
- **Resumo (verificado na fonte):** Panorama da pesquisa em reuso de software. Define reuso como o uso de software existente para construir novo software e *reusabilidade* como a propriedade que indica a probabilidade de reuso. Reafirma a classificação de métricas de reuso (tipos de reuso, bibliotecas, custo-benefício, maturidade, **quantidade de reuso**, modos de falha, reusabilidade) e registra que "medição e experimentação em reuso e engenharia de domínio é uma área em que muito mais trabalho é necessário". Situa **engenharia de domínio como sinônimo de engenharia de linhas de produto**, cuja premissa é que sistemas novos são, em geral, **variantes** de sistemas já construídos.
- **Relação com o TCC:** Dá enquadramento recente e revisado por pares (i) à escolha de triangular duas métricas em vez de reportar um número único e (ii) à leitura da Arquitetura Metamórfica como engenharia de domínio/linha de produto. Note-se que o artigo cita o **Koala** como método que integra componentes **em tempo de compilação**, precedente pertinente à variabilidade em *build-time*.

### 8.3 Chen, X.; Usman, M.; Badampudi, D. — *Understanding and evaluating software reuse costs and benefits from industrial cases — A systematic literature review* (Information and Software Technology, 2024) — ✅ VERIFICADO

- **Fonte:** Information and Software Technology, v. 171, art. 107451, 2024. DOI: 10.1016/j.infsof.2024.107451
- **Link (aberto, CC BY):** https://doi.org/10.1016/j.infsof.2024.107451
- **Acesso:** 🟢 Público — artigo *open access* sob licença CC BY. **PDF no acervo:** `docs/refs/8.3 - Understanding and evaluating software reuse costs and benefits (IST 2024).pdf`.
- **Resumo (verificado na fonte):** Revisão sistemática sobre custos e benefícios do reuso relatados em casos industriais, com 30 estudos primários. Identifica nove benefícios e seis custos, sendo **melhor qualidade** e **produtividade** os mais investigados; entre as práticas de reuso, **linhas de produto de software** figuram no topo das que mais contribuem para os benefícios. Registra que o esforço para corrigir ou isolar erros é **reduzido** em ativos reusáveis, embora a proporção de erros que exigem correção mais longa seja maior neles. Vários estudos primários medem reuso pelas métricas de **External Reuse Level (ERL)** e **External Reuse Frequency**, relacionando-as a qualidade, produtividade e esforço.
- ⚠️ **Revisão (10 jul. 2026).** A função (i) abaixo **caducou**: o *reuse level* foi abandonado como métrica do trabalho, e esta fonte **não é mais citada** para atestar-lhe a vigência. Permanece citada **apenas** pela função (ii) — o vínculo entre reuso e manutenibilidade em casos industriais, com evidência mista —, na seção 5.6.1 do TCC. O texto original segue abaixo, como registro.
- **Relação com o TCC:** cumpre **duas funções**. (i) **Atualidade das métricas:** demonstra que o *reuse level* de Frakes e Terry (8.1) continua sendo instrumento corrente na literatura empírica revisada por pares — a escolha metodológica de `METRICA-REUSO.md` não repousa em fonte obsoleta. Os autores citam Frakes e Terry (ref. [24] do artigo) entre as fontes do benefício de produtividade. (ii) **Sustentação do objetivo geral:** ao vincular reuso a **manutenibilidade** e qualidade em casos industriais, dá lastro à reformulação do objetivo do trabalho, que trocou a promessa de artefatos "mais leves" pela de melhor manutenibilidade.
- ⚠️ **Limites do apoio.** O artigo corrobora o **reuse level**, mas **não** discute o *reuse percent* de Poulin et al. (`RSI/(RSI+SSI)`), que permanece ancorado apenas em 8.1. Além disso, sua evidência é **mista**: um dos casos reporta relação marginalmente **negativa** entre `ERL` e a razão esforço/linhas de código, e outro estudo primário observa decaimento de desempenho com reuso. Ao citá-lo, não apresentar os achados como uniformemente favoráveis.

### 8.4 Irshad, M.; Petersen, K.; Torkar, R.; Afzal, W. — *Capturing Cost Avoidance through Reuse: Systematic Literature Review and Industrial Evaluation* (EASE 2016) — ✅ VERIFICADO

- **Fonte:** Proceedings of the 20th International Conference on Evaluation and Assessment in Software Engineering (EASE), Limerick, 2016, p. 1–12. DOI: 10.1145/2915970.2915989
- **Link (versão de registro, ACM — acesso pago):** https://doi.org/10.1145/2915970.2915989
- **Link (versão de autor, aberta):** https://torkar.github.io/pdfs/MohsinEaseCameraReady.pdf
- **Acesso:** 🟢 Aberto pela versão de autor (*camera-ready*; o DOI impresso no PDF é um marcador de rascunho — usar o DOI de registro acima). **PDF no acervo:** `docs/refs/8.4 - Capturing Cost Avoidance through Reuse (EASE 2016).pdf`.
- **Resumo (verificado na fonte):** Revisão sistemática sobre a captura de *cost avoidance* por meio do reuso, seguida de avaliação em ambiente industrial. Reconstrói o modelo de Poulin et al., no qual o custo direto evitado é estimado por `DCA = RSI × (1 − RCR) × NCC`, sendo `RSI` as linhas de código reusadas sem modificação e `RCR` o custo relativo do reuso; registra também a métrica de custo evitado em serviço/manutenção. Cita Frakes ao caracterizar os tipos de métrica de reuso — custos, **níveis de reuso** e reusabilidade — como "fundação para construir, manter e melhorar os processos de reuso e os produtos reusáveis".
- **Relação com o TCC:** é a fonte que **sustenta a continuidade do `RSI`** — a grandeza do *reuse percent* adotado em `METRICA-REUSO.md` — em literatura revisada por pares posterior a Frakes e Terry (1996), preenchendo a lacuna deixada por 8.3, que corrobora o *reuse level* mas não o *reuse percent*. Junto de 8.3 (2024), compõe a linha temporal que evidencia a **permanência do método** ao longo das décadas: 1996 (formulação) → 2016 (uso e avaliação industrial) → 2024 (uso em revisão sistemática).
- **Status:** **fonte principal** para a vigência do `RSI`. A data (2016) situa-se na fronteira do critério de dez anos, mas o critério de aceitação é o **mérito**: revisão sistemática seguida de avaliação em contexto industrial, publicada em conferência revisada por pares (EASE), com texto integral verificado. Não há, no acervo, fonte mais recente que cumpra esse papel.
- ⚠️ **Ressalva de escopo.** O artigo emprega `RSI` dentro de um modelo de **custo evitado**, e não da fórmula de *reuse percent*. A corroboração é da **grandeza** e de sua vigência, não da razão `RSI/(RSI+SSI)`. Não atribuir ao artigo o que ele não afirma.

### 8.5 Devanbu, P.; Karstu, S.; Melo, W.; Thomas, W. — *Analytical and Empirical Evaluation of Software Reuse Metrics* (ICSE 1996) — ✅ verificado

- **Registro:** In: INTERNATIONAL CONFERENCE ON SOFTWARE ENGINEERING, 18., 1996, Berlim. **Proceedings** [...]. IEEE Press, 1996. p. 189-199. DOI: 10.1109/ICSE.1996.493415.
- **Acesso:** 🟢 Versão de registro (IEEE) paga; **relatório técnico de autor aberto** no repositório institucional DRUM (Univ. of Maryland): CS-TR-3505 / UMIACS-95-82, jul. 1995. A própria folha de rosto do relatório declara "To appear in the Proc. of the 18th Int'l Conf. on Software Engineering, Berlin, Germany, 1996". ⚠️ **O metadado do repositório informa "1998", data incorreta** — citar 1996 (ICSE), como a folha de rosto atesta.
  Link: https://drum.lib.umd.edu/handle/1903/749
- **Resumo (verificado no texto integral):** Avalia, analítica e empiricamente, quão bem as métricas de reuso publicadas medem o benefício de reuso ("tempo, dinheiro e qualidade"). Propõe oito "propriedades" (axiomas) que qualquer medida de benefício de reuso deveria satisfazer e confronta as métricas existentes com elas. Empiricamente, os autores construíram uma ferramenta (GEN++) para extrair as métricas de reuso a partir de **código C++**.
- **Relação com o TCC:** é a fonte que **autoriza a granularidade de método** e a **medição de reuso a partir das chamadas no código**. Dois pontos são decisivos:
  1. a definição de componente — `c_j` = "*A software component (module, class, **function**, subsystem)*" — inclui explicitamente a **função** entre os itens contáveis;
  2. o método de medição é declaradamente **baseado em chamadas**: mede-se o benefício de reuso "*searching the source code of S for instances of language-based reuse such as **subroutine calls***", e define-se `Cu(S, c)` como o **número de vezes** que o componente `c` é usado no sistema `S`.
- **Uso previsto:** fundamentar o **Reuse per Method** (ver `METRICA-REUSO.md`, seção 7) quanto à granularidade de função e à leitura de reuso por invocação, sem ter de apresentá-los como invenção deste trabalho.
- ⚠️ **Ressalva de escopo — e uma ameaça à validade que o próprio artigo levanta.** Os autores advertem que medidas internas de reuso podem ser **artificialmente infladas**: "*one can artificially insert function invocations into the different branches of a conditional*", e "*in most cases, it is infeasible to detect occurrences of spurious inflation*". A advertência **aplica-se ao Reuse per Method** e deve ser reportada nas ameaças à validade: a contagem de métodos alcançáveis é sensível à granularidade com que o código foi decomposto em funções. Registrar também que o artigo **não** propõe métrica de alcançabilidade a partir de pontos de entrada — isso vem de 5.4.

---

### 5.4 Ponta, S. E.; Plate, H.; Sabetta, A. — *Beyond Metadata: Code-centric and Usage-based Analysis of Known Vulnerabilities in Open-source Software* (ICSME 2018) — ✅ verificado

> Listado fora da ordem por pertencer ao **Eixo 5** (segurança), embora tenha sido localizado durante a busca do Eixo 8.

- **Registro:** In: IEEE INTERNATIONAL CONFERENCE ON SOFTWARE MAINTENANCE AND EVOLUTION (ICSME), 34., 2018, Madri. **Proceedings** [...]. IEEE, 2018. DOI: 10.1109/ICSME.2018.00054.
- **Acesso:** 🟢 Versão de registro (IEEE) paga; ***preprint* de autor aberto no arXiv**, identificado no próprio PDF como "[PRE-PRINT]" da versão ICSME 2018. Link: https://arxiv.org/abs/1806.05893
- **Resumo (verificado no texto integral):** Método *code-centric* para detectar, avaliar e mitigar vulnerabilidades em bibliotecas de código aberto. Define **construto de programa** como "*a structural element of the source code characterized by a type (e.g., package, class, constructor, **method**)*". Combina análise estática e dinâmica para determinar a **alcançabilidade** dos construtos vulneráveis: a análise estática computa `R_ai`, "*the set of all constructs […] that are found reachable starting from the application a*", por meio de "*a graph of all library constructs reachable from the application constructs*". Define ainda métricas como a *Reachable Body Stability*, razão calculada **sobre o conjunto de construtos alcançáveis**.
- **Relação com o TCC:** é o precedente revisado por pares para a **técnica** do Reuse per Method — contagem em granularidade de **método/construto**, alcançabilidade computada por **grafo de chamadas a partir dos pontos de entrada da aplicação**, e **razões definidas sobre o conjunto alcançável**. O artigo enquadra o problema, ademais, como consequência do **reuso** ("the gains obtained from the reuse of community-developed libraries may be offset by the cost of timely detecting […] their vulnerabilities") e liga alcançabilidade a **explorabilidade** — que é precisamente o vínculo entre a PP1 e a PP3 neste trabalho.
- ⚠️ **Ressalva de escopo.** Os autores medem a estabilidade de bibliotecas entre versões, **não** reuso entre variantes de um mesmo código-fonte, e o texto verificado fala em **explorabilidade** (*exploitability*), não em "superfície de ataque" — este termo permanece ancorado em 5.1 (Manadhata e Wing). A corroboração é da **técnica**, não do Reuse per Method em si. Não atribuir ao artigo mais do que ele afirma.

---

## Resumo do Mapeamento Tema × Referência

| Conceito do TCC | Referências de apoio |
|---|---|
| Microsserviços e API Gateway | 1.1 Pahl & Jamshidi; 1.2 Taibi, Lenarduzzi & Pahl |
| Backend for Frontend (BFF) | 2.2 Newman (artigo 2015, verificado) — fonte única adotada; 2.1 Newman (livro 2021) descartado |
| Arquitetura Metamórfica (variabilidade em build-time) | 3.5 Apel et al. (2013); 3.1b Hunsen et al. (2016); 3.2 Apel & Kästner; 3.3 Kästner et al.; 3.4 Adkins et al. |
| Compilação condicional vs. feature flag em runtime | 4.1 Rahman et al.; 4.2 Mahdavi-Hezaveh et al. (2021); 4.3 Mahdavi-Hezaveh et al. (2022) |
| Segurança por redução de superfície de ataque | 5.1 Manadhata & Wing; 3.5 Apel et al. (código não usado como alvo de ataque); 5.2 SMS Securing Microservices; 5.3 SoK Microservice Security |
| Arquitetura Hexagonal / Clean Architecture | 6.1 Cockburn; 6.2 Martin |
| Integração e entrega contínua; custo de *build*/teste por variante (PP4) | 6.3 Shahin, Ali Babar & Zhu (2017) |
| Medição de reuso (PP1) e vigência do método | 8.1 Frakes & Terry (fórmulas); 8.2 Frakes & Kang; 8.4 Irshad et al. (`RSI`); 8.3 Chen, Usman & Badampudi (`reuse level`, manutenibilidade) |

---

## Como refinar esta lista

- **Priorize** as referências 1.1, 1.2, 2.2, 3.5, 3.1b, 3.3, 5.1, 5.2 e 6.2 — são as que dão fundamentação teórica mais direta e de maior peso (venues consolidados) à contribuição original do trabalho.
- Para os itens em **arXiv** e **ResearchGate**, procure a versão publicada em conferência/periódico (use o DOI ou o Google Scholar) antes de citar, para garantir a versão *peer-reviewed*.
- Ferramentas úteis para expandir: **Connected Papers** (mapa de citações a partir de Apel et al. 2013, El-Sharkawy et al. 2019 e Manadhata & Wing 2011) e **Semantic Scholar** (para achar trabalhos recentes que citam essas obras).
