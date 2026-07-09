# Roadmap dos Capítulos do TCC — Próximos Passos

> **Objetivo deste documento:** mapear os capítulos ainda ausentes na proposta (`docs/case-study/Proposta TCC …md`) e organizar os próximos passos para concebê-los, aproveitando o protótipo `modular-bff` já existente, o estudo de caso (`docs/case-study/CASE_STUDY.md`) e as referências científicas já reunidas (`docs/case-study/REFERENCIAS_CIENTIFICAS.md`, `docs/case-study/CITACOES_ABNT.md`, PDFs em `docs/refs/`).
>
> **Insumos de apoio criados durante a redação:** o capítulo de **Metodologia** já rascunhado (`docs/case-study/METODOLOGIA.md`) e o **guia de fontes acadêmicas** com o *modus operandi* de pesquisa (`docs/FONTES-ACADEMICAS.md`).

---

## 1. Situação atual

O que a proposta **já** cobre:

- **Introdução** — microsserviços, API Gateway e o surgimento do BFF (fundamentada).
- **Justificativa** — vantagens do BFF e o problema de reuso/manutenção.
- **Objetivos** — geral e específicos.
- **Organização do Trabalho**.
- **Proposta: Arquitetura Metamórfica** — com *Fundamentação teórica* (SPL, variabilidade por anotação) e *Segurança por ausência*.
- **Referências** (ABNT).

O que **falta** para transformar a proposta num TCC completo:

- ~~Referencial Teórico como capítulo próprio~~ → **implementado no TCC** (arquivo original `Proposta TCC …md`, como capítulo *Referencial Teórico* antes da Proposta). Rascunho de trabalho mantido em `REFERENCIAL-TEORICO.md`.
- ~~Trabalhos Relacionados~~ → **rascunho concluído** em `TRABALHOS-RELACIONADOS.md`.
- ~~Metodologia~~ → **rascunho concluído** em `METODOLOGIA.md`.
- ~~Desenvolvimento / Implementação do Protótipo~~ → **rascunho concluído** em `DESENVOLVIMENTO.md`.
- Avaliação, Resultados e Discussão.
- Conclusão e Trabalhos Futuros.

---

## 2. Estrutura-alvo do TCC

Sugestão de sumário completo (✅ existe · 🟡 existe parcialmente · ⬜ a criar):

1. ✅ Introdução
2. ✅ Referencial Teórico — **implementado no TCC** (`Proposta TCC …md`); rascunho de trabalho em `REFERENCIAL-TEORICO.md`
3. 🟡 Trabalhos Relacionados — rascunho em `TRABALHOS-RELACIONADOS.md`
4. 🟡 Proposta: Arquitetura Metamórfica — rascunho refinado em `PROPOSTA-ARQUITETURA-METAMORFICA.md` (original preservado)
5. 🟡 Metodologia — rascunho em `METODOLOGIA.md`
6. 🟡 Desenvolvimento / Implementação do Protótipo — rascunho em `DESENVOLVIMENTO.md`
7. ⬜ Avaliação, Resultados e Discussão
8. ⬜ Conclusão e Trabalhos Futuros
9. ✅ Referências

> Observação: *Justificativa*, *Objetivos* e *Organização do Trabalho* permanecem como seções da Introdução (ou como capítulo introdutório), conforme o modelo da instituição.

---

## 3. Mapa dos capítulos a desenvolver

### 3.1 Referencial Teórico — ✅ implementado no TCC

> **Status:** inserido no arquivo original `Proposta TCC …md` como capítulo *Referencial Teórico* (seções 2.1–2.7), antes da *Proposta*. A *Fundamentação teórica* embutida foi desduplicada (virou remissão) e a *Organização do Trabalho* foi atualizada para "seis partes". Integridade preservada: Apel & Kästner (2009) — não verificado — foi reatribuído a Horcas, Pinto e Fuentes (2023); Newman citado como (2015). **Follow-ups editoriais:** (a) sobreposição entre a *Introdução* e as seções 2.1/2.2 (microsserviços/BFF); (b) sobreposição entre a *Segurança por ausência* da Proposta e a seção 2.5; (c) níveis de cabeçalho (o capítulo está em `##` e a *Proposta* em `###`); (d) decidir se o livro Newman (2021), agora não citado no corpo, sai da lista.

- **Objetivo:** consolidar os conceitos que sustentam o trabalho, hoje dispersos na *Fundamentação teórica* da Proposta.
- **Conteúdo sugerido:**
  - Microsserviços e API Gateway (contexto).
  - Backend for Frontend (BFF).
  - Linhas de Produto de Software (SPL): domínio × aplicação, derivação de produto, variabilidade **baseada em anotação × baseada em composição**.
  - Compilação condicional / variabilidade em tempo de *build*.
  - Superfície de ataque e segurança por redução de exposição.
  - Arquitetura Hexagonal (Ports & Adapters) / Clean Architecture.
- **Próximos passos:** mover os parágrafos de fundamentação já escritos para este capítulo e expandi-los; garantir uma subseção por conceito.
- **Apoio já disponível:** todas as citações em `CITACOES_ABNT.md` (Eixos 1, 3 e 5); refs 1.1, 1.2, 3.1a, 3.1b, 3.2, 3.3, 5.1, 5.2, 6.2.
- **Pronto quando:** cada conceito usado na Proposta tiver base citável neste capítulo.

### 3.2 Trabalhos Relacionados — 🟡 rascunho concluído

> **Status:** capítulo rascunhado em `docs/case-study/TRABALHOS-RELACIONADOS.md`, contrastando quatro abordagens — repositórios separados, *feature flags* em runtime, microsserviços distintos e SPL por composição — com a metamorfose, mais a síntese comparativa e a discussão da lacuna sobre BFF. Todas as afirmações estão ancoradas em referências do acervo (`docs/refs/`); o capítulo trata *feature toggles* de forma equilibrada, reconhecendo seu valor na indústria. **Pendência opcional:** obter o PDF de Rahman et al. (2016) para citar formalmente a adoção das toggles.

- **Objetivo:** posicionar a Arquitetura Metamórfica frente a abordagens existentes e evidenciar a lacuna que ela preenche.
- **Conteúdo sugerido — comparar com:**
  - Repositórios separados (um BFF por frontend).
  - *Feature flags* em tempo de execução.
  - Microsserviços distintos por cliente.
  - Linhas de produto por composição (FOP/AOP).
  - A tabela comparativa (reuso × segurança × complexidade operacional) já esboçada no `CASE_STUDY.md` (seção 11) é o ponto de partida.
- **Próximos passos:** transformar a tabela do estudo de caso em texto analítico, citando a literatura; destacar que a literatura *peer-reviewed* específica sobre BFF é escassa (reforça o ineditismo).
- **Apoio:** refs 4.1–4.3 (feature toggles), 3.1a/3.1b/3.3 (SPL/variabilidade), 1.2 (padrões de microsserviços).
- **Pronto quando:** houver um quadro comparativo justificado e a lacuna estiver explícita.

### 3.3 Metodologia — 🟡 rascunho concluído

> **Status:** capítulo rascunhado em `docs/case-study/METODOLOGIA.md` (classificação da pesquisa, DSR como método, 4 perguntas de pesquisa, 6 etapas DSR mapeadas aos capítulos, procedimentos de avaliação, ambiente/ferramentas e quadro de amarração objetivo→método→evidência). **Pendência:** integrar as citações de DSR (refs 7.1–7.5 de `REFERENCIAS_CIENTIFICAS.md`, ainda em *análise pendente*) e reancorar as "seis atividades" em Peffers et al. (2007).

- **Objetivo:** descrever como o trabalho foi conduzido e como a proposta será avaliada.
- **Abordagem recomendada:** **Design Science Research (DSR)** — pesquisa que produz e avalia um *artefato* (a arquitetura + o protótipo) para resolver um problema real. Alternativa: estudo de caso / prova de conceito.
- **Conteúdo sugerido:**
  - Classificação da pesquisa (aplicada, exploratória, DSR).
  - Etapas: identificação do problema → definição de objetivos → projeto do artefato (arquitetura) → demonstração (protótipo) → avaliação (métricas) → comunicação.
  - Definição das **métricas de avaliação** (ver 3.5) e do procedimento experimental (builds com diferentes *flags*).
  - Ferramentas e ambiente (Kotlin/Quarkus, Docker, inspeção de artefato).
- **Próximos passos:** ler as referências de DSR reunidas na seção 7 de `REFERENCIAS_CIENTIFICAS.md` e inserir as citações no texto; as perguntas de pesquisa PP1–PP4 já estão definidas.
- **Pronto quando:** cada objetivo específico tiver um método de verificação associado. ✅

### 3.4 Desenvolvimento / Implementação do Protótipo — 🟡 rascunho concluído

> **Status:** capítulo rascunhado em `docs/case-study/DESENVOLVIMENTO.md`, com as divergências doc × código já reconciliadas em `README.md` e `CASE_STUDY.md`. **Pendências:** produzir as figuras e tratar as melhorias sinalizadas no rodapé do capítulo — CORS permissivo, `init.sh`, `countOrders`, `@Transactional` em leituras e as métricas de reuso.

- **Objetivo:** descrever o protótipo `modular-bff` como *demonstração* concreta da arquitetura.
- **Conteúdo sugerido (reaproveitar o `CASE_STUDY.md`, sem duplicá-lo):**
  - Domínio escolhido (pedidos de e-commerce): 11 entidades, 9 estados.
  - Camadas hexagonais (`domain`, `services`, `adapters/api`, `adapters/infrastructure`, `config`).
  - O mecanismo metamórfico: `@IfBuildProperty` nos controllers `OrderInternalController` / `OrderExternalController`; `OrderGeneralController` sempre ativo.
  - Configuração via `application.properties` e variáveis `BFF_API_INTERNAL_ACTIVE` / `BFF_API_EXTERNAL_ACTIVE`.
  - Pipeline de *build* por artefato (interno vs externo).
- **Próximos passos:**
  - Incluir trechos de código-chave (o controller anotado, o `application.properties`).
  - Registrar os comandos de *build* de cada variante.
  - Preparar diagramas (o `CASE_STUDY.md` tem os textos; faltam figuras).
- **Apoio:** `CASE_STUDY.md` (seções 2–10), código-fonte, ref 6.2 (Clean Architecture) para justificar o posicionamento das anotações nos adaptadores.
- **Pronto quando:** um leitor conseguir reproduzir os dois artefatos a partir do capítulo.

### 3.5 Avaliação, Resultados e Discussão

> **Capítulo central da contribuição empírica.** É aqui que a "segurança por ausência" deixa de ser tese e vira evidência.

- **Objetivo:** demonstrar, com dados, que a arquitetura entrega o que promete.
- **Experimentos/medições sugeridos:**
  1. **Ausência verificável de endpoints** — construir o artefato externo e o interno e inspecionar o JAR (`jar tf` / `unzip -l | grep adapters/api/v1`), mostrando que o controller do outro modo **não existe** no binário. É a prova direta da tese (procedimento já descrito no `CASE_STUDY.md`, seção 10).
  2. **Redução da superfície de ataque** — quantificar os pontos de entrada expostos por artefato (nº de rotas/endpoints) e comparar com um *build* que contém todos os controllers; enquadrar com a métrica de superfície de ataque (MANADHATA; WING, 2011).
  3. **Reuso de código** — medir a proporção de código compartilhado (domínio, serviços, repositórios) vs. especializado (controllers, DTOs) — LOC ou nº de classes —, sustentando o ganho de reuso.
  4. **Custo operacional** — registrar que a abordagem exige *pipelines*/artefatos distintos; opcionalmente medir tamanho do artefato, tempo de *startup* e memória por variante.
  5. **(Opcional) Comparação com *feature flag* em runtime** — evidenciar que, na alternativa em runtime, o endpoint permanece no binário (reproduzir e inspecionar).
- **Discussão:** relacionar os números aos objetivos; retomar o contraste segurança por ausência × por proteção.
- **Apoio:** refs 5.1, 5.2 (segurança/superfície de ataque), 3.1b (variabilidade em sistemas reais), 4.1–4.3 (feature toggles).
- **Pronto quando:** cada afirmação da Proposta (reuso, segurança, ausência no binário) tiver um resultado que a confirme ou refute.

### 3.6 Ameaças à Validade *(subseção de 3.5 ou seção própria)*

- **Objetivo:** discutir os limites do estudo (padrão em TCCs orientados a evidência).
- **Conteúdo sugerido:** protótipo único e domínio único (validade externa); escopo do "Design Metamórfico" que **não** cobre autenticação/autorização/rate limiting (já delimitado na seção *Segurança por ausência*); métricas escolhidas.

### 3.7 Conclusão e Trabalhos Futuros

- **Objetivo:** retomar objetivos × resultados e apontar continuidade.
- **Conteúdo sugerido:**
  - Síntese: a arquitetura reusou código sem duplicação e reduziu a superfície de ataque por construção.
  - **Trabalhos futuros:** metamorfose que atinge camadas mais profundas (domínio/repositórios) — a ideia de granularidade mais grossa/composição descrita em `CITACOES_ABNT.md` (Eixo 3); automação da geração de *pipelines* por variante; aplicação a outro domínio; medição de segurança em produção; integração das camadas complementares (JWT/OAuth2, rate limiting).
- **Pronto quando:** cada objetivo específico for explicitamente respondido.

---

## 4. Checklist priorizado de próximos passos

1. [x] **Promover o Referencial Teórico a capítulo** — **implementado no TCC** (`Proposta TCC …md`); *Fundamentação teórica* da Proposta já desduplicada (passou a remeter ao novo capítulo).
2. [x] **Escrever Trabalhos Relacionados** a partir da tabela comparativa do `CASE_STUDY.md` — rascunho em `TRABALHOS-RELACIONADOS.md`.
3. [x] **Definir a Metodologia (DSR)** e amarrar cada objetivo específico a um método de verificação — rascunho em `METODOLOGIA.md`.
4. [x] **Redigir o Desenvolvimento** com trechos de código, `application.properties` e comandos de *build* — rascunho em `DESENVOLVIMENTO.md`.
5. [ ] **Executar a Avaliação** — começar pelo experimento 1 (inspeção do JAR), que é rápido e prova a tese central.
6. [ ] **Coletar métricas** de reuso e de superfície de ataque (experimentos 2 e 3).
7. [ ] **Escrever Resultados/Discussão e Ameaças à Validade**.
8. [ ] **Concluir** com trabalhos futuros.
9. [ ] **Produzir as figuras** (o `CASE_STUDY.md` traz os textos; faltam os diagramas — inclusive substituir os placeholders `[Trocar imagem]` da proposta).

> **Sugestão de ordem de execução:** 1 → 5 (o experimento de inspeção do JAR dá o resultado mais forte com o menor esforço e ajuda a calibrar todo o resto) → 3/4 → 2 → 6/7 → 8 → 9.
>
> **Progresso:** o **Referencial Teórico** já está **implementado no TCC** (arquivo original); **Trabalhos Relacionados**, **Metodologia** e **Desenvolvimento** seguem em rascunho. **Próximo alvo:** esqueleto da **Avaliação, Resultados e Discussão** — redigível agora com marcadores `[RESULTADO A PREENCHER]`; a execução dos experimentos fica para uma sessão dedicada.

---

## 5. Riscos e cuidados

- **Consistência de citações:** manter a convenção autor-data alinhada entre a Proposta e os documentos de apoio (atenção às versões de registro vs. preprint de 5.1 e 5.2, já anotadas em `REFERENCIAS_CIENTIFICAS.md`).
- **Reconciliação original × refinado (pendente):** existem duas versões da proposta — o arquivo original `Proposta TCC …md` (intacto) e o rascunho refinado `PROPOSTA-ARQUITETURA-METAMORFICA.md`. Ao consolidar, decidir se o refinado **substitui** a seção no original ou se **coexistem**, e uniformizar a **voz** (o original usa 1ª pessoa; o refinado, 3ª). Ponto específico a alinhar: as **citações do Newman** — os rascunhos e os documentos de apoio já usam o **artigo verificado NEWMAN (2015)** (`docs/refs/2.2`), enquanto o original ainda lista o **livro NEWMAN (2021)**, não verificado. Adotar 2015 em tudo, ou obter/verificar o livro caso se prefira citá-lo.
- **Não duplicar o estudo de caso:** o `CASE_STUDY.md` é insumo; o capítulo de Desenvolvimento deve resumi-lo e referenciá-lo, não copiá-lo.
- **Escopo da segurança:** manter sempre explícito que a segurança por ausência é a *primeira linha de defesa*, não a única.
- **Reprodutibilidade:** registrar versões e comandos exatos para que a avaliação seja repetível.

---

## 6. Insumos já disponíveis (mapa rápido)

| Necessidade | Onde buscar |
|---|---|
| Conceitos e citações (ABNT) | `docs/case-study/CITACOES_ABNT.md`, `docs/case-study/REFERENCIAS_CIENTIFICAS.md` |
| PDFs das referências | `docs/refs/` |
| Detalhes do protótipo (arquitetura, entidades, endpoints, build) | `docs/case-study/CASE_STUDY.md` |
| Tese de segurança por ausência e tabela comparativa | `docs/case-study/CASE_STUDY.md` (seção 11) e a seção *Segurança por ausência* da Proposta |
| Referencial Teórico redigido | `docs/case-study/REFERENCIAL-TEORICO.md` |
| Trabalhos Relacionados redigido | `docs/case-study/TRABALHOS-RELACIONADOS.md` |
| Metodologia (DSR) redigida | `docs/case-study/METODOLOGIA.md` |
| Desenvolvimento redigido | `docs/case-study/DESENVOLVIMENTO.md` |
| Onde buscar fontes + *modus operandi* de pesquisa | `docs/FONTES-ACADEMICAS.md` |
| Código-fonte | raiz do repositório (`src/main/kotlin/edu/uerj/...`) |
