# Guia de Fontes Acadêmicas para o TCC

> **Objetivo:** consolidar as principais fontes para localizar e verificar artigos, *papers*, livros e demais referências do trabalho — separadas por **acesso aberto** e **acesso pago/restrito** —, com foco na área de **Computação e Engenharia de Software** (microsserviços, BFF, Linhas de Produto de Software, segurança, arquitetura de software).
>
> **Como usar:** priorize as **fontes abertas** para leitura e download. Para itens só disponíveis em bases pagas, use o **Portal de Periódicos CAPES** (acesso institucional) ou procure a **versão pública/de autor** com Unpaywall e Open Access Button antes de recorrer à compra.
>
> ⚠️ **Integridade acadêmica:** confirme sempre os metadados (autores, ano, *venue*, páginas, DOI) **na fonte de registro** antes de citar. Versões em agregadores e *preprints* podem divergir da versão final revisada por pares. Este guia lista apenas meios **legítimos** de acesso.

---

## Prefácio — Modus Operandi de Pesquisa

> Este é o **procedimento padrão** que adotamos para localizar e selecionar cada referência do trabalho. Deve ser seguido na ordem abaixo. As etapas **4 e 5 só são acionadas** quando a etapa 3 conclui que o material **não** atende ao requisitado — e, nesse caso, **estritamente nesta ordem**.

1. **Pesquisar em fontes abertas — independentemente do idioma.** Começar sempre pelo acesso aberto (seções 1 e 4 deste guia), sem restringir por língua: português, inglês, espanhol ou outra. O idioma não é critério de exclusão nesta etapa.
2. **Verificar a obtenção.** Confirmar se é **possível obter** o texto completo do artigo/*paper* (não apenas o resumo). Usar Unpaywall / Open Access Button / repositórios / *preprints*. Se não houver como ler o material na íntegra, ele não é utilizável como fonte verificada.
3. **Analisar a qualidade e a aderência.** Avaliar o material (rigor, *venue*/revisão por pares, atualidade, relevância) e decidir se **atende ou não** ao que foi requisitado. Aqui o processo **bifurca**:
   - **Caso SIM** → **encerra-se.** A fonte é aceita, registra-se em `REFERENCIAS_CIENTIFICAS.md` (com resumo, relação com o TCC e status de acesso) e a citação em `CITACOES_ABNT.md`.
   - **Caso NÃO** → prosseguir para as etapas **4** e depois **5**, nesta ordem.
4. **Rastrear referências (snowballing).** Buscar nas **referências dos materiais já encontrados** (e, quando útil, em quem os cita) e **repetir todo o processo** a partir da etapa 1 para os novos candidatos.
   > ⚠️ **Importante — evitar loop:** o passo 4 deve ocorrer **SOMENTE uma vez por busca**. As referências rastreadas passam pelas etapas 1–3, mas **não** disparam um novo passo 4 (não se rastreiam as referências das referências). Isso impede uma recursão infinita e mantém o esforço limitado a **um único nível** de profundidade.
5. **Recorrer a fontes sabidamente pagas.** Somente se as etapas anteriores não resolverem, buscar nas bases **pagas/restritas** (seção 2), preferencialmente via **Portal CAPES / acesso institucional** (seções 3 e 4.3) antes de qualquer aquisição.

```
[1] Fontes abertas (qualquer idioma)
        │
        ▼
[2] Consegue obter o texto completo?
        │
        ▼
[3] Qualidade atende ao requisitado? ──── SIM ──► ENCERRA (registrar a fonte)
        │
        NÃO
        │
        ▼
[4] Rastrear referências dos achados ──► repetir [1]–[3] (SOMENTE 1x; sem novo [4])
        │
        ▼
[5] Fontes pagas (via CAPES/institucional)
```

---

## 1. Fontes Abertas (acesso livre / gratuito)

### 1.1 Motores de busca e índices acadêmicos

| Fonte | O que é | Link | Observação |
|---|---|---|---|
| **Google Acadêmico (Google Scholar)** | Maior buscador acadêmico; indexa *papers*, teses, livros, *preprints* e patentes, com contagem de citações e "Citado por". | https://scholar.google.com | Ótimo ponto de partida. Use o botão **"Citar"** (gera ABNT) e os *links* laterais de PDF (`[PDF]`) para versões abertas. Nem tudo indexado é revisado por pares — avalie o *venue*. |
| **Semantic Scholar** | Índice com IA (Allen Institute); resumos, grafo de citações, *TLDR* automático e API pública. | https://www.semanticscholar.org | Excelente para achar versões em PDF abertas e trabalhos relacionados. |
| **OpenAlex** | Catálogo aberto e gratuito de trabalhos, autores e *venues* (sucessor do Microsoft Academic Graph). | https://openalex.org | Base aberta com API; boa para mapear citações sem paywall. |
| **BASE (Bielefeld Academic Search Engine)** | Um dos maiores buscadores de documentos acadêmicos em acesso aberto (repositórios do mundo todo). | https://www.base-search.net | Foca em conteúdo *open access* de repositórios institucionais. |
| **CORE** | Agrega milhões de artigos de acesso aberto de repositórios e periódicos. | https://core.ac.uk | Bom para baixar PDFs de repositórios institucionais. |
| **dblp** | Bibliografia de referência em **Ciência da Computação**; lista completa e confiável de publicações por autor/*venue*. | https://dblp.org | Padrão-ouro para **conferir metadados** de papers de Computação (autores, ano, páginas, DOI). |

### 1.2 Repositórios de *preprints* e artigos abertos

| Fonte | O que é | Link | Observação |
|---|---|---|---|
| **arXiv** | Repositório de *preprints* (inclui `cs.SE`, `cs.DC`, `cs.CR`). | https://arxiv.org | Muitos papers de SE/segurança têm *preprint* aqui (ex.: refs 3.3, 5.2, 5.3 do TCC). Atenção: *preprint* pode não ser a versão final. |
| **DOAJ (Directory of Open Access Journals)** | Diretório de periódicos **100% acesso aberto** e revisados por pares. | https://doaj.org | Garante que o periódico é *open access* legítimo (não predatório). |
| **SciELO** | Biblioteca de periódicos científicos da América Latina; forte em português. | https://www.scielo.org | Útil para referências em **português** e periódicos brasileiros. |
| **PLOS / PMC** | *Public Library of Science* e *PubMed Central* — abertos (mais foco em ciências da vida, mas há interdisciplinar). | https://plos.org · https://www.ncbi.nlm.nih.gov/pmc/ | Menos relevante para SE, mas útil para métricas/segurança aplicada. |
| **Zenodo / Figshare** | Repositórios abertos de artefatos, *datasets* e versões de autor (mantidos por CERN/editoras). | https://zenodo.org · https://figshare.com | Bom para artefatos de pesquisa e material suplementar citável (com DOI). |

### 1.3 Ferramentas de acesso aberto e descoberta

| Ferramenta | Para que serve | Link |
|---|---|---|
| **Unpaywall** | Extensão de navegador que encontra a **versão legal e gratuita** de um artigo a partir do DOI. | https://unpaywall.org |
| **Open Access Button** | Localiza cópias abertas ou solicita ao autor. | https://openaccessbutton.org |
| **Connected Papers** | Gera um **grafo visual** de trabalhos relacionados a partir de um artigo semente. | https://www.connectedpapers.com |
| **Research Rabbit** | Descoberta de literatura e monitoramento de citações ("Spotify dos papers"). | https://www.researchrabbit.ai |
| **Lens.org** | Busca de artigos e patentes com análises; camada gratuita robusta. | https://www.lens.org |

### 1.4 Livros e teses abertos

| Fonte | O que é | Link |
|---|---|---|
| **Google Livros (Google Books)** | Busca em livros; prévias e, para obras de domínio público, texto integral. | https://books.google.com |
| **DOAB (Directory of Open Access Books)** | Diretório de livros acadêmicos em acesso aberto. | https://www.doabooks.org |
| **BDTD (Biblioteca Digital Brasileira de Teses e Dissertações)** | Teses e dissertações brasileiras em acesso aberto (IBICT). | https://bdtd.ibict.br |
| **OpenDOAR** | Diretório mundial de repositórios institucionais de acesso aberto. | https://v2.sherpa.ac.uk/opendoar/ |

---

## 2. Fontes Pagas / Restritas (acesso via assinatura ou instituição)

> Estas bases concentram grande parte da literatura **revisada por pares** de Computação. Muitas vezes o acesso é possível **gratuitamente** pelo **Portal de Periódicos CAPES** (login institucional). Sempre tente Unpaywall/versão de autor antes de comprar.

### 2.1 Bibliotecas de editoras e sociedades (núcleo de SE/Computação)

| Fonte | O que é | Link | Relevância para o TCC |
|---|---|---|---|
| **IEEE Xplore** | Biblioteca do IEEE; **essencial** em Computação, redes e segurança. | https://ieeexplore.ieee.org | Alta — ex.: ref 5.1 (Manadhata & Wing, *IEEE TSE*). |
| **ACM Digital Library** | Biblioteca da ACM; conferências e periódicos de SE. | https://dl.acm.org | Alta — ex.: refs 3.3 (SPLC), 4.1 (MSR), 4.3. |
| **SpringerLink** | Periódicos e livros da Springer. | https://link.springer.com | Alta — ex.: refs 3.1a (*SoSyM*), 3.1b (*EMSE*), 4.2, 7.1, 7.4. |
| **ScienceDirect (Elsevier)** | Periódicos da Elsevier. | https://www.sciencedirect.com | Alta — ex.: refs 4.3 (*IST*), 5.2 (*Computer Science Review*). |
| **Wiley Online Library** | Periódicos da Wiley. | https://onlinelibrary.wiley.com | Média — engenharia de software e sistemas. |
| **Taylor & Francis Online** | Periódicos da T&F. | https://www.tandfonline.com | Média — ex.: ref 7.2 (*JMIS*, Peffers et al.). |
| **SciTePress** | Editora de conferências (CLOSER, ICSOFT etc.). | https://www.scitepress.org | Alta — ex.: refs 1.1 e 1.2 (CLOSER). Vários PDFs são **gratuitos** no próprio site. |
| **AIS eLibrary (AISeL)** | Biblioteca da *Association for Information Systems*. | https://aisel.aisnet.org | Média — ex.: ref 7.3 (*JAIS*). Parte do conteúdo é aberto. |
| **JSTOR** | Acervo histórico multidisciplinar. | https://www.jstor.org | Baixa/Média — obras mais antigas e de gestão. |

### 2.2 Bases de indexação e métricas (descoberta; texto geralmente restrito)

| Fonte | O que é | Link | Observação |
|---|---|---|---|
| **Scopus (Elsevier)** | Maior base de resumos e citações revisada; métricas de autor/*venue*. | https://www.scopus.com | Excelente para **revisão sistemática** e triagem; acesso via CAPES. |
| **Web of Science (Clarivate)** | Base de citações consagrada; fator de impacto (JCR). | https://www.webofscience.com | Idem; útil para justificar a relevância de um *venue*. |
| **Dimensions** | Base de citações com camada gratuita de busca. | https://www.dimensions.ai | Parte gratuita razoável; texto integral depende da fonte. |

---

## 3. Acesso via Brasil (institucional)

| Fonte | O que é | Link | Observação |
|---|---|---|---|
| **Portal de Periódicos CAPES** | Principal porta de acesso **gratuito** (via instituição) a IEEE, ACM, Springer, Elsevier, Scopus, Web of Science etc. | https://www.periodicos.capes.gov.br | **Use primeiro** para obter o texto pago sem custo. Acesso pelo login da universidade ou pela **Comunidade Acadêmica Federada (CAFe)**. |
| **CAFe (RNP)** | Federação de identidade que autentica o acesso remoto ao Portal CAPES pela sua universidade. | https://www.rnp.br/servicos/identidade/cafe | Permite acessar as bases de casa, com o login institucional. |
| **Biblioteca da sua instituição / COMUT** | Empréstimo entre bibliotecas e comutação bibliográfica para itens não disponíveis. | (consultar a biblioteca da universidade) | Alternativa legal quando não há versão aberta. |

---

## 4. Fontes Ibero-americanas e em Espanhol (América Latina)

> Seguindo a lógica regional da seção anterior: portais e agregadores **muito conhecidos entre falantes de espanhol**, cobrindo América Latina, Caribe, Espanha e Portugal. A maioria é de **acesso aberto**. Úteis para achar literatura regional, referências em espanhol e periódicos ibero-americanos de Computação/Engenharia.

### 4.1 Portais e agregadores de acesso aberto

| Fonte | O que é | Link | Acesso / Observação |
|---|---|---|---|
| **Redalyc** | *Red de Revistas Científicas de América Latina y el Caribe, España y Portugal* (UAEM, México). Hemeroteca com texto integral. | https://www.redalyc.org | 🟢 Aberto. Um dos maiores acervos em espanhol/português; ótimo para artigos regionais. |
| **SciELO** | *Scientific Electronic Library Online* — rede pan-latino-americana (inclui **SciELO España**). | https://www.scielo.org | 🟢 Aberto. Já citado na seção 1; forte em espanhol e português. |
| **Dialnet** | Portal da *Universidad de La Rioja* (Espanha); um dos maiores repositórios de literatura científica **em espanhol** (artigos, teses, livros, atas). | https://dialnet.unirioja.es | 🟢 Aberto (metadados) / texto conforme a fonte. **Muito popular** entre falantes de espanhol. |
| **REDIB** | *Red Iberoamericana de Innovación y Conocimiento Científico* (CSIC + parceiros). | https://redib.org | 🟢 Aberto. Agregador iberoamericano de revistas e produção científica. |
| **LA Referencia** | Rede federada de **repositórios institucionais** de publicações científicas da América Latina (RedCLARA). | https://www.lareferencia.info | 🟢 Aberto. Análogo regional ao OpenDOAR/CORE; agrega teses e artigos de repositórios nacionais. |
| **RECOLECTA** | Agregador de repositórios de acesso aberto da **Espanha** (FECYT). | https://recolecta.fecyt.es | 🟢 Aberto. Porta de entrada aos repositórios institucionais espanhóis. |
| **CLACSO (Biblioteca/Repositorio)** | *Consejo Latinoamericano de Ciencias Sociales*; grande repositório aberto. | https://biblioteca.clacso.edu.ar | 🟢 Aberto. Foco em ciências sociais (relevância menor p/ Computação, mas referência famosa). |

### 4.2 Catálogos de qualidade / avaliação de revistas

| Fonte | O que é | Link | Observação |
|---|---|---|---|
| **Latindex** | *Sistema Regional de Información en Línea para Revistas Científicas de América Latina, el Caribe, España y Portugal* (UNAM). | https://www.latindex.org | Não hospeda texto: **valida a qualidade** de revistas ibero-americanas (Catálogo 2.0). Análogo regional ao DOAJ para filtrar periódicos legítimos. |
| **REDIB — rankings** | Indicadores e *rankings* de revistas iberoamericanas. | https://redib.org | Útil para justificar a relevância de um *venue* regional. |

### 4.3 Portas de acesso institucional por país (análogos ao Portal CAPES)

| País | Serviço | Link | Observação |
|---|---|---|---|
| **México** | **CONRICYT** — *Consorcio Nacional de Recursos de Información Científica y Tecnológica*. | https://www.conricyt.mx | Acesso institucional a IEEE, Springer, Elsevier etc. (equivalente mexicano do CAPES). |
| **Argentina** | **Biblioteca Electrónica de Ciencia y Tecnología** (MINCyT). | https://www.biblioteca.mincyt.gob.ar | Acesso institucional a bases pagas. |
| **Colômbia** | **SciELO Colombia / repositórios universitários** e consórcios locais. | https://www.scielo.org.co | Acesso regional e institucional. |
| **Chile / regional** | **BEIC e consórcios universitários** (via **RedCLARA / LA Referencia**). | https://www.redclara.net | Federação regional de acesso e repositórios. |

> **Nota de relevância para o TCC:** a literatura *peer-reviewed* em espanhol especificamente sobre **BFF/microsserviços** é limitada, mas Redalyc, SciELO, Dialnet e LA Referencia são bons para achar **estudos de caso e revisões** regionais em Engenharia de Software; use **Latindex/REDIB** para confirmar que a revista é séria antes de citar.

---

## 5. Fluxo recomendado de busca e verificação

1. **Descobrir:** buscar no **Google Acadêmico** e/ou **Semantic Scholar**; expandir com **Connected Papers** a partir de um artigo-chave.
2. **Filtrar por qualidade:** conferir o *venue* no **dblp** (Computação) e checar se o periódico é legítimo no **DOAJ**/**Scopus**.
3. **Obter o texto (grátis primeiro):** tentar **Unpaywall** / **Open Access Button** / **arXiv** / repositório institucional; se não houver, usar o **Portal CAPES**.
4. **Verificar metadados:** confirmar autores, ano, páginas e **DOI** na **fonte de registro** (editora) — não no agregador.
5. **Registrar:** adicionar a `REFERENCIAS_CIENTIFICAS.md` (com resumo + relação com o TCC + **status de acesso**) e a citação ABNT em `CITACOES_ABNT.md`.

---

## 6. Mapa rápido: fontes já usadas no TCC × onde foram obtidas

| *Venue* / tipo | Base | Acesso praticado |
|---|---|---|
| CLOSER (refs 1.1, 1.2) | SciTePress | Aberto no site da editora |
| *SoSyM*, *EMSE* (refs 3.1a, 3.1b) | SpringerLink | Pago; **3.1b** tem PDF de autor aberto |
| SPLC (ref 3.3) | ACM DL / arXiv | *Preprint* aberto no arXiv |
| MSR, *IST* (refs 4.1, 4.3) | ACM DL / ScienceDirect | Pago (CAPES) |
| *IEEE TSE* (ref 5.1) | IEEE Xplore | Pago; **relatório técnico CMU** aberto |
| *Computer Science Review* (ref 5.2) | ScienceDirect | Pago; *preprint* aberto no arXiv |
| DSR (refs 7.1–7.5) | Springer / T&F / AISeL / Bookman | Misto — ver status de acesso na seção 7 de `REFERENCIAS_CIENTIFICAS.md` |

---

> **Nota:** este guia lista **meios legítimos** de acesso. Repositórios que distribuem artigos protegidos por direito autoral sem autorização (ex.: sites de pirataria) foram deliberadamente omitidos; para itens pagos, use o Portal CAPES, a versão de autor ou o empréstimo entre bibliotecas.
