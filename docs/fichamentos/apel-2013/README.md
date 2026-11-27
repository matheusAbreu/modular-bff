# Fichamentos — Apel et al. (2013)

APEL, Sven; BATORY, Don; KÄSTNER, Christian; SAAKE, Gunter. **Feature-oriented software product lines**: concepts and implementation. Berlin; Heidelberg: Springer, 2013. DOI: 10.1007/978-3-642-37521-7.

> Material de **estudo**, não texto do TCC. Cada fichamento resume o capítulo com palavras próprias, seção a seção. Só as **definições formais** aparecem no original, por serem curtas e citáveis, e cada uma vem com tradução livre. As páginas são as **impressas** no livro. Figuras e tabelas são indicadas por `🖼️`, com uma descrição do que mostram.
>
> Registro da obra no acervo: `docs/case-study/REFERENCIAS_CIENTIFICAS.md`, 3.5. As citações já validadas para o TCC estão em `docs/case-study/CITACOES_ABNT.md`, nos Eixos 3 e 4.

| Capítulo | Arquivo | Páginas |
|---|---|---|
| 3 — Basic Concepts, Classification, and Quality Criteria | [cap-03.md](cap-03.md) | 47–63 |
| 4 — Classic, Language-Based Variability Mechanisms | [cap-04.md](cap-04.md) | 65–97 |
| 5 — Classic, Tool-Driven Variability Mechanisms | [cap-05.md](cap-05.md) | 99–127 |
| 6 — Advanced, Language-Based Variability Mechanisms | [cap-06.md](cap-06.md) | 129–173 |
| 7 — Advanced, Tool-Driven Variability Mechanisms | [cap-07.md](cap-07.md) | 175–188 |

A síntese da Parte II (cap. 3 a 7), em forma de tabela técnica → onde aparece no TCC, está no fim de [cap-07.md](cap-07.md).

## Achados para o TCC (ainda não aplicados)

| Achado | Fonte | Onde poderia entrar |
|---|---|---|
| Um ramo ou repositório por produto é desaconselhado para variabilidade de *features* | 5.1.3, p. 103–104 | Trabalhos Relacionados: alternativa de repositórios separados |
| Componentes exigem cola manual por produto; a derivação não é automática | 4.4.2, p. 92–93 | Trabalhos Relacionados |
| O sistema de *build* orquestra outros mecanismos de variabilidade | 5.2.4, p. 108–109 | 6.6 (*Build* por artefato) |
| O Java não tem compilação condicional nativa de classes e métodos | 5.3.3, p. 113–114 | 2.4: diferença de mecanismo do Quarkus |
| Anotações disciplinadas (alinhadas à sintaxe) | 5.3.4, p. 116–118 | 2.4 / 3.4: o `@IfBuildProperty` é disciplinado |
| Erros que surgem só em certas combinações de *features* | 5.3.6, p. 121–124 | 7.5.1 / Ameaças à validade |
| Parâmetros são difíceis de rastrear | 3.2.2, p. 54–55; 4.1.1, p. 68–69 | 7.6: contraste com a *feature flag* |
| Modelo de *features* integrado valida combinações e automatiza a derivação | 7.3, p. 182–184 | 8: Trabalhos futuros |
| FOP e AOP ainda sobretudo acadêmicas | 6.5, p. 167–168 | Trabalhos Relacionados: por que não composição |
