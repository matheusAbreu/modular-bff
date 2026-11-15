# Notas de leitura e auditoria

Este diretório reúne **notas de passagem** pelo repositório: o registro do que foi lido, reexecutado e conferido numa data, com os achados que daí resultaram.

> **O que estas notas não são.** Não são rascunho de capítulo, não são fonte da verdade de nada e não entram no TCC. A fonte da verdade dos capítulos é o arquivo `case-study/Proposta TCC …md`, conforme a seção 5 do `ROADMAP-CAPITULOS-TCC.md`. Um achado registrado aqui só altera o trabalho quando o autor decide corrigi-lo — e, nesse momento, a correção vai para o capítulo e aqui fica apenas a remissão.

## Convenção

- **Um arquivo por passagem**, nomeado `AAAA-MM-DD-assunto.md`. A data é a da coleta, não a da redação.
- **Achado aponta para onde vive o problema:** por seção, quando se trata do TCC ou dos documentos de apoio; por `arquivo:linha`, quando se trata de código.
- **Separar o verificado do inferido.** Todo achado declara como foi estabelecido: reexecução de script, inspeção de artefato, leitura de código ou leitura de documento.
- **Número nenhum nasce aqui.** Os valores citados são os que os programas de `case-study/scripts/` produziram na data, nunca medição *ad hoc* de terminal — a mesma regra que vale para o TCC (ver o `README.md` daquele diretório).
- **Achado resolvido não é apagado.** Ganha a marca de resolvido e a remissão ao lugar onde a correção foi aplicada, pela mesma razão que o *Histórico de correções* dos scripts existe.

## Relação com os documentos vizinhos

Dois documentos do repositório revisam o trabalho, e seus escopos não se sobrepõem:

| Documento | O que examina |
|---|---|
| `../REVISAO-COERENCIA-TCC.md` | **Contradições internas ao TCC** — afirmações de um capítulo que outro capítulo desmente. Leitura do documento contra si mesmo. |
| Notas deste diretório | **O publicado contra o repositório** — código, *build*, artefatos e reexecução dos scripts, confrontados com o que o texto afirma. |

Um achado do segundo tipo pode revelar-se, depois, também do primeiro; quando isso acontece, registra-se nos dois lugares, com remissão mútua.

## Índice

| Nota | Assunto |
|---|---|
| [2026-10-28-auditoria-repositorio.md](2026-10-28-auditoria-repositorio.md) | Primeira passagem completa: reprodução das medições de PP1 e PP3, divergências entre o ambiente publicado e o do repositório, e achados de código não registrados. |
