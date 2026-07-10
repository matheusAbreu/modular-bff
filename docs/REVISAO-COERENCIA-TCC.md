# Revisão de coerência do TCC final — incongruências encontradas

> **Data:** 10 jul. 2026, após a implementação de todos os capítulos (1–8), das Referências e do Apêndice A.
>
> **Escopo:** leitura do arquivo `Proposta TCC …md` procurando **contradições internas** — afirmações de um capítulo que outro capítulo desmente. Não se revisou estilo, ortografia nem completude.
>
> **Estado:** **C1 e C2 corrigidas** em 10 jul. 2026, com base no capítulo de Avaliação. As demais notas (I1–I3, F1–F3) permanecem abertas.
>
> **Resultado:** nenhuma incongruência invalida resultado ou conclusão. Há **duas contradições reais** (C1, C2), **três imprecisões de contagem** (I1–I3) e **três folgas de redação** (F1–F3).

---

## C1 — ✅ **CORRIGIDA** · A Proposta dizia que os *controllers* são "removidos do artefato"

**Onde:** seção 4.3, segundo item da lista.

> "os controladores das operações **interna** […] e **externa** […] são **incluídos ou removidos** do artefato conforme a configuração selecionada no *build*."

**O que contradiz:** a seção 6.5 afirma que "o bytecode do controller permanece no JAR da aplicação, inerte e inalcançável", e a 7.2.1 estabelece isso por medição — o título da seção é, literalmente, *O bytecode da classe permanece em todas as variantes*. A própria 4.5, duas seções adiante, escreve: "o que a compilação suprime **não é a classe** do *controller* […] e sim o **ponto de entrada**".

É o último resíduo da tese antiga, sobrevivendo no capítulo que a formula. Um leitor que pare na 4.3 sai com a afirmação que o trabalho refutou.

**Correção aplicada.** A 4.3 passou a dizer: "os controladores […] **têm, ou não têm, ponto de entrada** no artefato, conforme a configuração selecionada no *build*: quando a propriedade não está ativa, nenhum *bean*, invocador ou rota é gerado para eles."

---

## C2 — ✅ **CORRIGIDA** · A Proposta afirmava que "basta inspecionar o artefato" para verificar a ausência

**Onde:** seção 4.5, primeiro item da lista.

> "A ausência é **verificável**: basta inspecionar o artefato, como demonstra o capítulo **Avaliação**."

**O que contradiz:** a Avaliação estabelece exatamente o oposto quanto à suficiência da inspeção. A seção 7.2.5 declara que a evidência do bytecode é "**corroborativa, não fundacional**", porque depende de convenções internas do Quarkus; e que "a conclusão repousa sobre a evidência de 7.2.3 — a ausência da rota no documento OpenAPI e no atendimento HTTP". A Metodologia (5.5.1, passo 2) chama a inspeção de camada **explicativa**, e não primária.

Inspecionar o artefato **não basta**: mostra *por que* a rota não existe, não *que* ela não existe. Verificar exige **executar** a variante e interrogá-la.

**Correção aplicada.** A 4.5 passou a dizer: "A ausência é **verificável sobre o artefato entregue**: a rota não consta do contrato que a variante publica, e uma requisição a ela não alcança manipulador algum. É o que estabelece o capítulo **Avaliação**."

**Nota correlata — também corrigida.** A abertura da 6.8 dizia "verificável **por inspeção** do artefato de produção", afirmando a mesma suficiência; passou a "verificável **sobre** o artefato de produção". A qualificação da inspeção como camada explicativa já constava do parágrafo seguinte, e foi mantida ali — uma tentativa de repeti-la na abertura gerou duplicação, desfeita.

---

## I1 — A figura do fluxo de *build* promete três artefatos; a tabela ao lado lista quatro

**Onde:** seção 6.6, marcador `[FIGURA A INSERIR — fluxo de build por variante]`.

> "Diagrama do código-fonte único ramificando-se […] em **três artefatos distintos** conforme as *flags*"

A tabela imediatamente acima do marcador arrola **quatro** alvos: `bff-default`, `bff-external`, `bff-internal` e `bff` (linha de base). A figura da Avaliação (7.1) descreve corretamente "quatro artefatos".

**Correção sugerida:** uniformizar para quatro, ou dizer "três variantes de interesse, mais a linha de base".

---

## I2 — "Três variantes de produção" onde foram construídas quatro

**Onde:** seções 7.2.1, 7.2.2 e 7.5.2.

A 7.1 declara **quatro** variantes, incluindo a linha de base. A 7.2.1 diz que a inspeção "devolve, nas **três** variantes de produção, o mesmo conteúdo"; a tabela da 7.2.2 arrola apenas três; a 7.5.2 afirma que o bytecode é "idêntico nas **três** variantes".

Não é erro de fato — a inspeção de bytecode (`make artifact/analysis`) de fato cobriu três imagens —, mas o texto oscila entre três e quatro sem dizer por quê, e a tabela da 7.2.3, logo adiante, mostra as **quatro**.

**Correção sugerida:** ou dizer "nas três variantes inspecionadas" e justificar a exclusão da linha de base, ou estender a inspeção e a tabela à quarta.

---

## I3 — A Proposta reduz o conjunto de artefatos a "interno e externo"

**Onde:** seção 4.3, parágrafo final.

> "de um mesmo código-fonte derivam-se artefatos distintos — **interno e externo** —"

O trabalho deriva quatro: `default`, `internal`, `external` e a linha de base. A variante `default` tem papel próprio na Avaliação (é a que suprime 11 dos 18 métodos alcançáveis) e a linha de base é o termo de comparação de toda a PP3.

**Correção sugerida:** mencionar as variantes de interesse e a linha de base, ou usar formulação genérica ("artefatos distintos, um por modo").

---

## F1 — A síntese da Proposta enuncia um custo mais estreito do que a Avaliação apurou

**Onde:** seção 4.6.

> "O custo dessa combinação — **pipelines e artefatos distintos por variante** — […] são objeto do capítulo Avaliação."

A 7.5.1 apura **cinco** custos, e nomeia como principal um que a 4.6 não menciona: a **perda da reconfiguração em tempo de execução**, "o preço exato da PP2". A Conclusão (8.2) o promove a resposta da PP4.

Não é contradição — a 4.6 não nega os demais —, mas o capítulo que apresenta a proposta subestima o preço que ela cobra, e o leitor só descobre o preço integral três capítulos adiante.

---

## F2 — Sobreposição entre a Introdução e as seções 2.1/2.2

**Onde:** Introdução (parágrafos sobre microsserviços e API Gateway) e Referencial Teórico, seções 2.1 e 2.2.

Ambas apresentam a decomposição em microsserviços, o controle descentralizado de dados e o surgimento do API Gateway, com as mesmas fontes (PAHL; JAMSHIDI, 2016; TAIBI; LENARDUZZI; PAHL, 2018).

**Já registrado** como *follow-up (a)* na seção 3.1 do `ROADMAP-CAPITULOS-TCC.md`, e **ainda aberto**. A duplicação é herdada da estrutura original da proposta.

---

## F3 — Dois marcadores `[Trocar imagem]` na Introdução

**Onde:** Introdução, imagens `image1` e `image2`.

Permanecem como *placeholders* explícitos no corpo do texto. Somam-se às nove figuras `[FIGURA A INSERIR]` — cinco no Desenvolvimento, quatro na Avaliação —, todas ainda sem legenda nem numeração ABNT.

**Já registrado** nos itens 7d e 9 do checklist do ROADMAP.

---

## O que foi verificado e está consistente

Registra-se, para que a próxima passagem não refaça o trabalho:

- **Remissões internas.** As 22 remissões emitidas pelo capítulo 7 apontam para seções existentes, inclusive as que atravessam capítulos (`3.2`, `5.4`, `5.5.2`, `5.5.3`, `5.5.4`, `6.2`, `6.7`).
- **Numeração da Metodologia.** A renumeração (5.5.3 = *Reuse per Method*; 5.5.4 = *Ambiente*) foi propagada a todas as remissões, inclusive à da seção 6.1.
- **Contagem de partes.** "Onze partes, seguidas de um apêndice" corresponde aos capítulos existentes.
- **Citações × Referências.** Toda fonte citada nos capítulos 7 e 8 tem entrada nas Referências. Frakes e Kang (2005) foi removida ao ficar órfã. Devanbu et al. (1996), Ponta, Plate e Sabetta (2018) e QUARKUS (2026e) foram acrescentadas.
- **Fontes complementares.** Ordem alfabética correta (Cockburn, Newman, Quarkus), e as cinco entradas do Quarkus em sequência `2026a`–`2026e`.
- **Tese reformulada.** Nenhuma ocorrência remanescente de "não existe no binário", "não é compilada" ou "deixa de existir" — exceto o caso C1, que usa "removidos".
- **PP4.** Nenhuma promessa de medir tamanho, *startup* ou memória sobreviveu em qualquer capítulo.
- **Estatuto das tabelas.** A tabela de pontos de entrada da 6.5 declara-se como projeto ("por construção") e remete a verificação empírica à 7.2.
