package com.vendramel.blackjack.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CartaTest {
    @Test
    void deveExporNaipeRankEToString() {
        Carta carta = new Carta("Copas", "A");
        assertThat(carta.getNaipe()).isEqualTo("Copas");
        assertThat(carta.getRank()).isEqualTo("A");
        assertThat(carta.toString()).isEqualTo("A de Copas");
    }

    @ParameterizedTest
    @CsvSource({
            "2, 2", "3, 3", "4, 4", "5, 5", "6, 6", "7, 7", "8, 8", "9, 9", "10, 10",
            "J, 10", "Q, 10", "K, 10", "A, 11"
    })
    void deveCalcularValorDaCarta(String rank, int valorEsperado) {
        assertThat(new Carta("Ouros", rank).getValor()).isEqualTo(valorEsperado);
    }

    @Test
    void deveIdentificarOAs() {
        assertThat(new Carta("Paus", "A").isAs()).isTrue();
        assertThat(new Carta("Paus", "K").isAs()).isFalse();
    }
}
