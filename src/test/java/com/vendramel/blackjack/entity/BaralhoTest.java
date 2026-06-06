package com.vendramel.blackjack.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class BaralhoTest {
    @Test
    void deveIniciarCom52Cartas() {
        assertThat(new Baralho().getQtdRestante()).isEqualTo(52);
    }

    @Test
    void comprarCartaDeveRemoverERetornarCarta() {
        Baralho baralho = new Baralho();
        Carta carta = baralho.comprarCarta();
        assertThat(carta).isNotNull();
        assertThat(baralho.getQtdRestante()).isEqualTo(51);
    }

    @Test
    void deveComprarTodasAsCartasEDepoisLancarExcecao() {
        Baralho baralho = new Baralho();
        for (int i = 0; i < 52; i++) {
            assertThat(baralho.comprarCarta()).isNotNull();
        }
        assertThat(baralho.getQtdRestante()).isZero();
        assertThatThrownBy(baralho::comprarCarta)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("vazio");
    }

    @Test
    void embaralharDeveManterAs52CartasEAlterarAOrdem() {
        Baralho original = new Baralho(new Random(7));
        Baralho embaralhado = new Baralho(new Random(7));
        embaralhado.embaralhar();

        List<String> ordemOriginal = drenar(original);
        List<String> ordemEmbaralhada = drenar(embaralhado);

        assertThat(ordemEmbaralhada).hasSize(52);
        assertThat(ordemEmbaralhada).containsExactlyInAnyOrderElementsOf(ordemOriginal);
        assertThat(ordemEmbaralhada).isNotEqualTo(ordemOriginal);
    }

    private static List<String> drenar(Baralho baralho) {
        List<String> cartas = new ArrayList<>();
        while (baralho.getQtdRestante() > 0) {
            cartas.add(baralho.comprarCarta().toString());
        }
        return cartas;
    }
}
