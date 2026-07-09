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

O conceito central do trabalho. A literatura **peer-reviewed** especificamente sobre BFF ainda é escassa (o padrão nasceu na indústria, cunhado por Sam Newman/SoundCloud), o que reforça a **relevância e o ineditismo** do TCC.

### 2.1 Newman, S. — *Building Microservices: Designing Fine-Grained Systems* (O'Reilly, 2ª ed., 2021)

- **Fonte:** O'Reilly Media. ISBN: 978-1492034025.
- **Resumo:** Obra de referência que introduziu e popularizou o padrão **Backend for Frontend**. Descreve o BFF como uma variação do API Gateway dedicada a um único tipo de cliente (frontend), discutindo suas vantagens de personalização e implantação independente e os custos de manutenção quando há muitos frontends.
- **Relação com o TCC:** É a fonte primária do conceito de BFF usado no trabalho. Embora seja um livro técnico (não um paper), é a referência canônica citada por praticamente toda a literatura acadêmica sobre o tema — indispensável na fundamentação.

> **Observação:** para uma referência estritamente acadêmica sobre BFF, vale acompanhar publicações recentes em repositórios como ResearchGate e arXiv (ex.: estudos sobre *"BFF architecture across platforms and its dynamic adaptations"*), mas avalie criticamente o rigor/venue antes de citar, pois muitas ainda são preprints não revisados.

---

## 3. Linhas de Produto de Software e Variabilidade (fundamento da "Arquitetura Metamórfica")

**Este é o núcleo teórico do TCC.** A ideia de "gerar múltiplos produtos a partir de um mesmo código-fonte, ativando/desativando partes por configuração" é exatamente o campo de **Software Product Lines (SPL)** e **Feature-Oriented Software Development (FOSD)**. A "Metamorfose" proposta é uma forma de **variabilidade em tempo de compilação**.

### 3.1a Horcas, J. M.; Pinto, M.; Fuentes, L. — *Empirical Analysis of the Tool Support for Software Product Lines* (Software and Systems Modeling, 2023)

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

## 6. Arquitetura Hexagonal / Clean Architecture (organização do código)

Fundamenta a decisão de estruturar o projeto em Ports & Adapters, isolando o domínio.

### 6.1 Cockburn, A. — *Hexagonal Architecture (Ports and Adapters)*

- **Fonte:** Publicação original do autor (2005), consolidada em *"Hexagonal Architecture Explained"* (Cockburn & Garrido, 2024).
- **Link:** https://alistair.cockburn.us/hexagonal-architecture/
- **Resumo:** Propõe organizar a aplicação em torno de um núcleo de regras de negócio isolado, comunicando-se com o mundo externo (UI, banco, APIs) exclusivamente por **portas** (interfaces) e **adaptadores**. O objetivo é permitir testar o domínio em isolamento e trocar tecnologias externas sem afetar as regras de negócio.
- **Relação com o TCC:** É a fonte canônica da arquitetura adotada no protótipo. Note que é **literatura técnica (grey literature)**, não um paper revisado por pares; para rigor acadêmico, complemente com Martin (2017), *Clean Architecture*, que sistematiza os mesmos princípios.

### 6.2 Martin, R. C. — *Clean Architecture: A Craftsman's Guide to Software Structure and Design* (Prentice Hall, 2017)

- **Fonte:** Prentice Hall / Pearson, 2017. ISBN: 978-0134494166.
- **Resumo:** Sistematiza a separação entre regras de negócio e detalhes de infraestrutura por meio da **Regra de Dependência** (dependências apontam para dentro, do detalhe para a política), unificando os princípios das arquiteturas hexagonal e onion em anéis concêntricos.
- **Relação com o TCC:** Sustenta a decisão arquitetural de isolar `domain`/`services` dos `adapters`, que é o que torna possível o **reuso sem duplicação** — pré-requisito para o compartilhamento das camadas inferiores entre os controllers metamórficos.

---

## 7. Metodologia de Pesquisa — Design Science Research (DSR)

> ⏳ **Seção em análise pendente.** As obras abaixo fundamentam o método adotado no capítulo de **Metodologia** (`METODOLOGIA.md`), que atualmente descreve a DSR **sem citação**. Foram localizadas em bases reais e tiveram os metadados conferidos na fonte (venue, DOI/ISBN), mas **ainda não foram lidas em profundidade nem integradas ao texto**. Antes de citá-las no TCC: (1) validar edição/páginas na fonte; (2) ler ao menos uma referência de processo (vom Brocke, Hevner & Maedche) e uma de enquadramento/rigor (Baskerville et al. ou Dresch et al.); (3) inserir as citações nos pontos correspondentes da Metodologia (definição da DSR e as seis atividades).
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
- **Acesso:** 🟢 **Público.** Cópias em PDF amplamente disponíveis (Semantic Scholar, ResearchGate, repositórios). A versão de registro (Taylor & Francis/ACM) é paga.
- **Resumo:** Propõe e demonstra a metodologia DSRM, estruturada em **seis atividades**: identificação do problema e motivação; definição dos objetivos da solução; projeto e desenvolvimento; demonstração; avaliação; e comunicação.
- **Relação com o TCC:** Fonte **original** do modelo das seis atividades usado na seção *Etapas da Pesquisa* da Metodologia. Mantida de forma **redundante** ao lado das referências recentes (7.1) para preservar a proveniência exata dessa estrutura. Embora seja de 2007, tem acesso público e é a citação canônica do processo DSR. ⏳ *Análise pendente.*

### 7.3 Baskerville, R.; Baiyere, A.; Gregor, S.; Hevner, A.; Rossi, M. — *Design Science Research Contributions: Finding a Balance between Artifact and Theory* (JAIS, 2018)

- **Fonte:** Journal of the Association for Information Systems, v. 19, n. 5, p. 358–376, 2018. DOI: 10.17705/1jais.00495
- **Link (página oficial, AIS eLibrary):** https://aisel.aisnet.org/jais/vol19/iss5/3/
- **Link (versão pública — publisher's version, repositório CBS):** https://research-api.cbs.dk/ws/files/59353297/abayomi_baiyere_et_al_design_science_research_contributions_publishersversion.pdf
- **Acesso:** 🟢 **Público.** A versão do editor está aberta no repositório institucional da Copenhagen Business School. A página do AIS eLibrary bloqueia acesso automatizado (HTTP 403), mas o PDF é obtível pelo repositório CBS.
- **Resumo:** Discute o que constitui uma contribuição aceitável em DSR — o artefato, a teoria de projeto ou ambos — e oferece orientações e recomendações acionáveis para autores e revisores posicionarem e avaliarem o rigor de trabalhos de *design science*.
- **Relação com o TCC:** Referência **recente e revisada por pares** que sustenta o enquadramento da contribuição deste trabalho como **artefato** (arquitetura + protótipo) e ajuda a justificar os critérios de avaliação. Substitui o papel de processo/rigor antes atribuído a Peffers et al. (2007). ⏳ *Análise pendente.*

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

## Resumo do Mapeamento Tema × Referência

| Conceito do TCC | Referências de apoio |
|---|---|
| Microsserviços e API Gateway | 1.1 Pahl & Jamshidi; 1.2 Taibi, Lenarduzzi & Pahl |
| Backend for Frontend (BFF) | 2.1 Newman |
| Arquitetura Metamórfica (variabilidade em build-time) | 3.1a Horcas, Pinto & Fuentes (2023); 3.1b Hunsen et al. (2016); 3.2 Apel & Kästner; 3.3 Kästner et al.; 3.4 Adkins et al. |
| Compilação condicional vs. feature flag em runtime | 4.1 Rahman et al.; 4.2 Mahdavi-Hezaveh et al. (2021); 4.3 Mahdavi-Hezaveh et al. (2022) |
| Segurança por redução de superfície de ataque | 5.1 Manadhata & Wing; 5.2 SMS Securing Microservices; 5.3 SoK Microservice Security |
| Arquitetura Hexagonal / Clean Architecture | 6.1 Cockburn; 6.2 Martin |

---

## Como refinar esta lista

- **Priorize** as referências 1.1, 1.2, 3.1a, 3.1b, 3.3, 5.1, 5.2 e 6.2 — são as que dão fundamentação teórica mais direta e de maior peso (venues consolidados) à contribuição original do trabalho.
- Para os itens em **arXiv** e **ResearchGate**, procure a versão publicada em conferência/periódico (use o DOI ou o Google Scholar) antes de citar, para garantir a versão *peer-reviewed*.
- Ferramentas úteis para expandir: **Connected Papers** (mapa de citações a partir de Horcas et al. 2023, El-Sharkawy et al. 2019 e Manadhata & Wing 2011) e **Semantic Scholar** (para achar trabalhos recentes que citam essas obras).
