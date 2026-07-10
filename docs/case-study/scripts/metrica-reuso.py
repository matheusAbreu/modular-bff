#!/usr/bin/env python3
"""Reuse percent em linhas de código (PP1).

Calcula, para cada variante do modular-bff:
  - Reuse Percent = RSI / (RSI + SSI) * 100        (FRAKES; TERRY, 1996)
  - a análise de sensibilidade que reclassifica os DTOs de uso exclusivo do
    modo externo como ativos específicos daquela variante.

Reporta ainda o Reuse Level, métrica **abandonada** pelo trabalho — a saída é
mantida apenas como registro do que motivou o abandono (ver METRICA-REUSO.md,
seção 6.1). Ela não alimenta o texto.

Escopo: src/main/kotlin/** (exclui testes, código gerado, migrações e configuração).
LOC = linhas físicas, descontadas linhas em branco e comentários (cloc).

Uso e leitura da saída: docs/case-study/scripts/README.md.
"""

import csv
import io
import shutil
import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[3]
SRC = REPO / "src/main/kotlin"
PKG = SRC / "edu/uerj"

EXTERNO = "adapters/api/v1/OrderExternalController.kt"
INTERNO = "adapters/api/v1/OrderInternalController.kt"

# DTOs usados por um único modo (todos do externo; o interno não possui exclusivos).
# Usados apenas na análise de sensibilidade.
DTOS_EXCLUSIVOS_EXTERNO = [
    "adapters/api/dto/order/NewOrderDTO.kt",
    "adapters/api/dto/item/ItemInNewOrderDTO.kt",
    "adapters/api/dto/payment/PaymentInNewOrderDTO.kt",
]


def loc_por_arquivo():
    """{caminho relativo a edu/uerj: LOC}, via cloc.

    O `cloc --by-file --csv` grava a linha de totais com "SUM" no campo 1 e o
    campo `filename` vazio; ambos os testes são necessários para descartá-la.
    """
    if not shutil.which("cloc"):
        sys.exit("erro: cloc não encontrado (no Fedora: sudo dnf install -y cloc)")

    saida = subprocess.run(
        ["cloc", str(SRC), "--by-file", "--csv", "--quiet"],
        capture_output=True, text=True, check=True, cwd=REPO,
    ).stdout

    loc = {}
    for linha in csv.reader(io.StringIO(saida)):
        if len(linha) < 5 or linha[0] in ("language", "SUM") or not linha[1]:
            continue
        caminho = Path(linha[1]).resolve().relative_to(PKG)
        loc[str(caminho)] = int(linha[4])
    return loc


def reuse_percent(rsi, ssi):
    return rsi / (rsi + ssi) * 100


def tabela(titulo, rsi, especificos):
    print(f"── {titulo} " + "─" * max(0, 60 - len(titulo)))
    print(f"{'variante':<14}{'RSI':>7}{'SSI':>7}{'reuse %':>11}")
    for variante, ssi in especificos.items():
        print(f"{variante:<14}{rsi:>7}{ssi:>7}{reuse_percent(rsi, ssi):>10.2f}%")
    print()


def reuse_level(loc):
    """Reuse Level de Frakes e Terry (1996) — MÉTRICA ABANDONADA.

    Item de nível superior = variante; itens de nível inferior = classes ativas.
    E = itens criados para uso geral da linha de produto (o núcleo comum).
    M = itens NÃO externos usados mais de uma vez.

    O único item não externo de cada variante é o seu controller anotado, que é
    ponto de entrada e não é invocado por ninguém: M = 0 e IRL = 0, por
    construção. É uma das três razões do abandono (METRICA-REUSO.md, 6.1).
    """
    classes = len(loc)                       # um tipo de topo por arquivo
    variantes = {
        "default":  classes - 2,             # nenhum controller anotado ativo
        "internal": classes - 1,
        "external": classes - 1,
    }
    print("── Reuse Level (ABANDONADO — registro apenas) " + "─" * 16)
    print(f"{'variante':<14}{'L':>5}{'E':>5}{'M':>5}{'ERL':>8}{'IRL':>7}{'TRL':>7}")
    for variante, L in variantes.items():
        E = L if variante == "default" else L - 1
        M = 0
        print(f"{variante:<14}{L:>5}{E:>5}{M:>5}{E / L:>8.3f}{0.0:>7.3f}{E / L:>7.3f}")
    print("\nO IRL nulo não é defeito de cálculo: decorre da classificação exigida\n"
          "pela própria definição da métrica. Ver METRICA-REUSO.md, seção 6.1.\n")


def main():
    versao = subprocess.run(["cloc", "--version"], capture_output=True, text=True,
                            check=True).stdout.strip()
    print(f"cloc: {versao}\n")

    loc = loc_por_arquivo()
    total = sum(loc.values())
    loc_ext, loc_int = loc[EXTERNO], loc[INTERNO]
    comum = total - loc_ext - loc_int

    print("── LOC (src/main/kotlin) " + "─" * 37)
    print(f"total .......................... {total:>5}")
    print(f"OrderExternalController ........ {loc_ext:>5}")
    print(f"OrderInternalController ........ {loc_int:>5}")
    print(f"base comum (RSI) .............. {comum:>5}\n")

    tabela("Reuse Percent", comum, {
        "default": 0,
        "internal": loc_int,
        "external": loc_ext,
        "linha-base": loc_ext + loc_int,
    })

    loc_dtos = sum(loc[d] for d in DTOS_EXCLUSIVOS_EXTERNO)
    tabela(f"Sensibilidade: DTOs exclusivos ({loc_dtos} LOC) → variante externa",
           comum - loc_dtos, {
               "default": 0,
               "internal": loc_int,
               "external": loc_ext + loc_dtos,
           })

    reuse_level(loc)


if __name__ == "__main__":
    main()
