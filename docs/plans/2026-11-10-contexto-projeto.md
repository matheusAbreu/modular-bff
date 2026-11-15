# Contexto — modular-bff (sem tarefa de implementação ainda)

## Context
Pedido: "contextualize-se sobre o projeto". Nenhuma mudança solicitada; este arquivo registra o entendimento e aguarda a próxima tarefa.

## Projeto
- Protótipo de TCC (UERJ): BFF modular em Quarkus + Kotlin, hexagonal (domain / services / adapters api+infrastructure / config), PostgreSQL + Goose.
- Tese ("Arquitetura Metamórfica"): uma base comum gera variantes por cliente via `@IfBuildProperty` resolvido em build — rota de outro cliente não tem bean/invocador/rota ("segurança por ausência").
  - `OrderExternalController` (POST /v1/order/create) ← `modular-bff.api.mode.external.enable`
  - `OrderInternalController` (PATCH /v1/order/{id}/delivery/{id}/{status}) ← `...internal.enable`
  - `OrderGeneralController` (GET) sempre presente. Flags vêm de `BFF_API_*_ACTIVE` / `--build-arg` no Dockerfile.
- TCC final: `docs/case-study/Proposta TCC - Reutilização ... BFF.md` (todos os capítulos implementados, até Apêndice A).
- Perguntas de pesquisa: PP1 reuso (LOC, `metrica-reuso.py`), PP2 rotas ausentes (`evidencia-rotas.py`), PP3 grafo de métodos (`grafo-metodos.py`) — scripts congelados desde 10 jul. 2026.

## Regras do autor a respeitar
- Número no TCC só nasce de script versionado em `docs/case-study/scripts/` (Python); editar script transposto obriga revisar TCC.
- Registro neutro, 3ª pessoa, sem autoelogio; remissões por seção; NEWMAN (2015) como fonte única de BFF.
- Somente artefato de produção (build Docker) conta na avaliação.

## Estado atual
- Branch `case-study`; mudanças não commitadas: linha em branco no Makefile; remoção de `![][image6]`/`![][image7]` na seção 4.2 do TCC; `docs/notes/` não rastreado.
- Auditoria 28 out. 2026 (`docs/notes/2026-10-28-auditoria-repositorio.md`): PP1/PP3 reproduzem; achados abertos A1 (versão Quarkus publicada 3.37.0 ≠ Arc 3.32.4 embarcado), A2–A7 (resíduos de texto), B1–B3 (código: DELIVERED inalcançável, limite da listagem, EXPOSE 9000 vs 8081).
- Outros documentos de controle: `docs/ROADMAP-CAPITULOS-TCC.md`, `docs/REVISAO-COERENCIA-TCC.md`.

## Próximo passo
1. Criar `docs/plans/` (não existe ainda) e gravar este conteúdo como `docs/plans/2026-11-10-contexto-projeto.md` (nome no padrão `AAAA-MM-DD-assunto.md`, igual a `docs/notes/`).
2. Daqui em diante, planos desta sessão vivem em `docs/plans/`.
3. Não fazer commit; aguardar a próxima tarefa.

## Verificação
- `ls docs/plans/` mostra o arquivo; `git status` o lista como não rastreado.
