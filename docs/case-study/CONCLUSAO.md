# Conclusão e Trabalhos Futuros

> ✅ **Implementado no TCC** — transposto para o arquivo `Proposta TCC …md` como capítulo *Conclusão e Trabalhos Futuros* (seções 8.1–8.5), entre a *Avaliação* e as *Referências*. **A fonte da verdade passa a ser o TCC**; este documento é mantido apenas como registro de trabalho.
>
> Diferenças aplicadas na transposição: cabeçalhos rebaixados um nível; bloco de meta removido; *Organização do Trabalho* passou a descrever **onze partes** mais o apêndice. Nenhuma fonte nova foi introduzida — todas as sete citadas já constavam das Referências.
>
> **Convenções:** citações no sistema autor-data (ABNT NBR 10520); 3ª pessoa acadêmica; registro neutro — o capítulo retoma o que foi feito e o que ficou por fazer, sem qualificar o mérito do próprio trabalho.
>
> **Regra deste capítulo:** nada aqui pode afirmar mais do que o capítulo 7 verificou. Cada retomada remete à seção que a sustenta.

Este capítulo retoma os objetivos enunciados no início do trabalho, confronta-os com o que a Avaliação estabeleceu, delimita o que ficou fora do alcance da pesquisa e aponta as continuações que dela decorrem.

---

## 8.1 Retomada dos objetivos

O **objetivo geral** propunha uma arquitetura de projeto que gerasse múltiplas aplicações a partir de um mesmo código-fonte, mais seguras e de melhor manutenibilidade ao suprimirem os trechos que não lhes competem, com ganho esperado de resiliência decorrente da menor exposição.

Os **objetivos específicos** eram dois, e ambos foram cumpridos:

**Apresentar conceitualmente a Arquitetura Metamórfica.** O capítulo *Proposta* formula a arquitetura e o *Referencial Teórico* a ancora em campos consolidados: a derivação de produtos das Linhas de Produto de Software (APEL et al., 2013), a variabilidade por anotação resolvida em tempo de compilação (HUNSEN et al., 2016; BAUM et al., 2019), a superfície de ataque definida pelo conjunto de pontos de entrada (MANADHATA; WING, 2011) e o isolamento das regras de negócio frente aos detalhes de *framework* (MARTIN, 2017). O capítulo *Trabalhos Relacionados* a posiciona frente a quatro alternativas e delimita a lacuna que ela ocupa.

**Apresentar um protótipo conceito.** O `modular-bff` foi implementado, e dele derivaram-se quatro artefatos — três variantes e uma linha de base — sem qualquer alteração de código entre os *builds*. O capítulo *Desenvolvimento* descreve a implementação e o procedimento de reprodução.

---

## 8.2 O que a pesquisa estabeleceu

As quatro perguntas de pesquisa receberam resposta, e convém enunciá-las com a precisão que a Avaliação impôs.

**A supressão do ponto de entrada é verificável e não reversível (PP2).** O artefato de um modo não contém o *bean* CDI, o invocador REST nem a rota da operação do outro modo. A rota não consta do contrato publicado pela variante, e uma requisição a ela não alcança manipulador algum. Nenhuma variável de ambiente, nenhum parâmetro de execução e nenhuma reconfiguração em tempo de implantação reintroduzem o que não foi registrado no *build* (seção 7.2).

**A supressão não alcança a classe compilada.** O bytecode do *controller* permanece no JAR da aplicação, inerte e inalcançável (seção 7.2.1). A tese original — "o endpoint não existe no binário" — era falsa ao pé da letra e foi reformulada em termos de **ponto de entrada**, unidade que a métrica de superfície de ataque adotada de fato contabiliza (MANADHATA; WING, 2011). A reformulação não enfraqueceu o argumento: tornou-o mais aderente à métrica que o funda.

**A superfície de ataque diminui, e a lógica atrás dela deixa de ter caminho (PP3).** Cada variante de escrita expõe três rotas de negócio, contra quatro da linha de base. Mais que isso: suprimir o *controller* de um modo torna inalcançáveis, a partir da rede, os cinco métodos que estavam atrás daquela rota; a variante sem modos ativos torna inalcançáveis onze dos dezoito métodos próprios da linha de base (seção 7.3).

**O reuso é quantificável e a duplicação não existe no código examinado (PP1).** A especialização de um BFF inteiro custou menos de noventa linhas: 94,82% e 95,17% do código de cada variante é comum às demais. As regras de negócio residem em uma única classe de serviço, os repositórios e o domínio são únicos, e nenhuma validação, cálculo ou fluxo é reescrito por variante (seção 7.4).

**O custo é a rigidez em tempo de execução (PP4).** Alterar o modo de um artefato exige recompilá-lo e reimplantá-lo. É o preço exato da propriedade aferida pela PP2: a mesma resolução em tempo de compilação que impede uma configuração de ressuscitar o ponto de entrada impede que ela o restitua quando isso for desejado. Somam-se a multiplicação de *pipelines* e artefatos, o crescimento combinatório das variantes e o custo de verificar cada uma (seção 7.5).

**Uma propriedade não reivindicada foi observada.** O contrato que cada variante publica é derivado dos *endpoints* nela registrados, e a interface de documentação é projeção daquele contrato. A metamorfose atinge, assim, também aquilo que o artefato **diz de si**: nenhum esforço de manutenção documental é exigido, e nenhuma divergência entre documento e comportamento é possível (seção 7.2.4).

---

## 8.3 O que a pesquisa não estabeleceu

A delimitação a seguir não é concessão feita ao final: é parte do resultado.

**Manutenibilidade não foi medida.** O objetivo geral a reivindica; o trabalho afere o **reuso**, que é seu antecedente. Concentrar o código comum em um único ponto reduz o esforço de manutenção (TAIBI; LENARDUZZI; PAHL, 2018), relação que Chen, Usman e Badampudi (2024) observam em casos industriais — com evidência mista. Não se mediu esforço de alteração nem densidade de defeitos.

**Resiliência não foi aferida.** O objetivo geral a enuncia como decorrência esperada da menor exposição, e é assim que deve ser lida — nunca como resultado.

**A ausência de duplicação foi estabelecida por inspeção, não por instrumento.** Nenhuma medida deste trabalho detecta clones: duas cópias de uma mesma regra em classes distintas escapariam ao *reuse percent* e ao grafo de chamadas. Afirma-se que a duplicação não existe no código examinado, não que a arquitetura a impeça (seção 7.4.2).

**A generalização não é sustentada pelos dados.** Um protótipo, um domínio, um *framework*. A proporção de rotas suprimidas é propriedade deste protótipo, não da arquitetura.

**A segurança por ausência é a primeira linha de defesa, não a única.** Não substitui autenticação, autorização por posse de recurso, limitação de abuso nem mascaramento de dados sensíveis.

---

## 8.4 Trabalhos futuros

As continuações abaixo decorrem diretamente do que ficou por fazer, e não de especulação.

**Verificar a forma forte da tese no *build* nativo.** A *dead code elimination* do GraalVM possivelmente elimina o bytecode da classe inalcançável. Se o fizer, a afirmação "o endpoint não existe no binário" torna-se literalmente verdadeira para o artefato nativo, e o trabalho passaria a distinguir uma forma fraca (JVM: ponto de entrada ausente) de uma forma forte (nativo: classe ausente). A hipótese não foi testada (seção 7.2.5).

**Exercitar a suíte de testes contra cada variante entregue.** A integração contínua do protótipo compila com ambas as propriedades ativas, isto é, exercita a linha de base — que não é nenhum dos artefatos entregues. Uma abordagem que promete ausência verificável deveria verificar o que entrega (seção 7.5.1).

**Domar o crescimento combinatório.** `k` propriedades booleanas independentes admitem até `2^k` variantes. Restringir as combinações admissíveis a um subconjunto declarado é problema conhecido das Linhas de Produto de Software — modelos de *features* com restrições —, e não foi enfrentado aqui.

**Levar a metamorfose às camadas inferiores.** Neste trabalho a variabilidade vive apenas nos adaptadores de entrada. Suprimir também serviços e repositórios privativos de um modo exigiria granularidade mais grossa ou variabilidade por composição, e ampliaria a redução da lógica alcançável medida pela PP3.

**Medir a manutenibilidade que o trabalho apenas infere.** Esforço de alteração e densidade de defeitos, aferidos ao longo da evolução de uma linha de produto real, converteriam em resultado o que hoje é antecedente.

**Aplicar a arquitetura a outro domínio e a outro mecanismo de variabilidade**, para separar o que é propriedade da arquitetura do que é propriedade do Quarkus.

**Construir as camadas complementares de segurança** — autenticação, autorização por posse de recurso, limitação de abuso — sobre a base que a supressão do ponto de entrada estabelece, e avaliar sua interação.

---

## 8.5 Considerações finais

O trabalho parte de uma tensão prática: quanto mais *frontends*, mais projetos BFF a manter, com estruturas duplicadas. Propõe resolvê-la deslocando a decisão sobre *o que cada aplicação contém* do tempo de execução para o tempo de compilação, e demonstra, sobre um protótipo, que a decisão assim tomada não é reversível pelas vias que a superfície de ataque contabiliza.

O percurso obrigou a corrigir a própria tese. A formulação inicial afirmava que o endpoint não existiria no binário; a inspeção dos artefatos mostrou que a classe permanece, e que o que desaparece é o ponto de entrada. A distinção entre as duas coisas — entre a classe presente e inerte e a via de entrada ausente — é o tipo de precisão que um capítulo de avaliação existe para produzir. O contraste com a *feature flag* em tempo de execução sobrevive intacto: lá, o ponto de entrada existe e responde.

Resta o que a arquitetura cobra. Ela troca flexibilidade por ausência, e essa troca não é gratuita: exige um artefato por modo, um *pipeline* por artefato, e a renúncia a reconfigurar em produção o que se decidiu no *build*. Onde a *feature flag* compra flexibilidade ao preço da exposição, a metamorfose compra ausência ao preço da flexibilidade. Qual das duas serve depende da pergunta que se faz — e este trabalho procurou deixar as duas perguntas distinguíveis.

---

> **Pendências de redação:**
> - **Cross-check de citações:** o capítulo cita APEL et al. (2013), HUNSEN et al. (2016), BAUM et al. (2019), MANADHATA; WING (2011), MARTIN (2017), TAIBI; LENARDUZZI; PAHL (2018) e CHEN; USMAN; BADAMPUDI (2024) — todas já presentes nas Referências do TCC. Nenhuma fonte nova é introduzida.
> - **Ao transpor:** rebaixar cabeçalhos (`##` → `###`), remover este bloco de meta, atualizar a *Organização do Trabalho* de dez para **onze partes** e descrever o capítulo no sumário narrativo.
> - **Verificar** se a seção 8.2 não duplica a 7.7 (*Discussão*): a 7.7 confronta afirmação por afirmação com os dados; a 8.2 retoma o resultado consolidado. A fronteira é tênue e deve ser revista no arquivo final.
