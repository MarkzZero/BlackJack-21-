# Blackjack (21) — CLI · Arquitetura BCE

Jogo de **Blackjack (21)** em linha de comando, implementado em **Java 21** com **Spring Boot 3**
e **Gradle (Kotlin DSL)**, seguindo a arquitetura **BCE (Boundary–Control–Entity)** — congruente
com o diagrama de classes do trabalho.

> Disciplina *Engenharia de Software III* — Prof. Wilson Vendramel.

## Arquitetura BCE

A aplicação é organizada em três estereótipos. **Não é MVC**: as regras de comunicação do BCE são
respeitadas estritamente (o ator fala só com a Boundary; a Boundary fala só com a Control; a Control
coordena as Entities; **Boundary e Entity nunca se comunicam diretamente**).

| Estereótipo | Pacote | Classe(s) | Responsabilidade |
|---|---|---|---|
| **Boundary** | `boundary` | `UIJogoCartas` | Interface CLI com o jogador (entrada/saída). |
| **Control** | `control` | `CtrlJogarCartas` (+ DTOs `EstadoJogador`, `ResultadoJogador`) | Coordena o caso de uso "Jogar Cartas". |
| **Entity** | `entity` | `Jogo`, `Dealer`, `Baralho`, `Jogador`, `Carta`, `Resultado` | Domínio do jogo. |

Relacionamentos (conforme o diagrama):

```
UIJogoCartas ──usa──▶ CtrlJogarCartas ┄┄▶ Jogo
                                         ├─ 1 Dealer ──administra──▶ 1 Baralho ──▶ 0..52 Carta
                                         └─ 1..* Jogador ──tem──▶ 0..* Carta
```

O `Dealer` apenas **administra o Baralho** (não possui cartas próprias); a **mão da casa** é um
`Jogador` (`maoDistribuidor`) mantido pelo `Jogo`.

## Regras de negócio

- **RN01 — Valor do Às:** o Às vale **11**, mas passa a valer **1** se o total da mão exceder 21
  (apenas os Áses necessários são reduzidos). Implementada em `Jogador.calcularTotal()`.
- **RN02 — Jogada do distribuidor:** a casa é **obrigada a comprar carta até somar ≥ 17** e **para
  assim que atinge** esse valor. Implementada em `Jogo.finalizarJogo()`.

Demais regras padrão: 2 cartas iniciais, pedir/manter, estouro (> 21), comparação com a casa
(vitória / derrota / empate).

## Pré-requisitos

- **Java 21** (JDK).
- Não é necessário instalar o Gradle — o projeto inclui o **Gradle Wrapper** (`gradlew` / `gradlew.bat`).

## Como executar

### Windows (PowerShell)
```powershell
.\gradlew.bat bootRun
```

### Linux/macOS
```bash
./gradlew bootRun
```

Ou gere e rode o JAR executável:
```bash
./gradlew bootJar
java -jar build/libs/vendramel-blackjack-1.0.0.jar
```

## Testes e cobertura

```bash
./gradlew test jacocoTestReport
```

- **JUnit 5 + Mockito** (via `spring-boot-starter-test`).
- Relatório de cobertura (JaCoCo): `build/reports/jacoco/test/html/index.html`.
- Cobertura atual: **~98% de instruções / ~97% de ramos**.

## Estrutura

```
src/main/java/com/vendramel/blackjack/
├── BlackjackApplication.java        # @SpringBootApplication + CommandLineRunner
├── boundary/UIJogoCartas.java
├── control/CtrlJogarCartas.java
├── control/EstadoJogador.java       # DTO
├── control/ResultadoJogador.java    # DTO
└── entity/{Carta,Baralho,Dealer,Jogador,Jogo,Resultado}.java
```
