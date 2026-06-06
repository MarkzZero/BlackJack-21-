package com.vendramel.blackjack.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JogadorTest {
    private static Carta carta(String rank) {
        return new Carta("Copas", rank);
    }

    @Test
    void maoVaziaDeveTotalizarZero() {
        assertThat(new Jogador("Ana").calcularTotal()).isZero();
    }

    @Test
    void receberCartaDeveAcumularCartas() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("2"));
        jogador.receberCarta(carta("3"));
        assertThat(jogador.getCartas()).hasSize(2);
        assertThat(jogador.getNome()).isEqualTo("Ana");
    }

    @Test
    void rn01_asValeOnzeQuandoNaoEstoura() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("A"));
        jogador.receberCarta(carta("K"));
        assertThat(jogador.calcularTotal()).isEqualTo(21);
    }

    @Test
    void rn01_doisAsesContamComo12() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("A"));
        jogador.receberCarta(carta("A"));
        assertThat(jogador.calcularTotal()).isEqualTo(12);
    }

    @Test
    void rn01_asViraUmQuandoEstouraria() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("A"));
        jogador.receberCarta(carta("9"));
        jogador.receberCarta(carta("5"));

        assertThat(jogador.calcularTotal()).isEqualTo(15);
    }

    @Test
    void rn01_apenasOsAsesNecessariosViramUm() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("A"));
        jogador.receberCarta(carta("A"));
        jogador.receberCarta(carta("9"));

        assertThat(jogador.calcularTotal()).isEqualTo(21);
    }

    @Test
    void deveIndicarEstouro() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("K"));
        jogador.receberCarta(carta("Q"));
        jogador.receberCarta(carta("5"));
        assertThat(jogador.calcularTotal()).isEqualTo(25);
        assertThat(jogador.estourou()).isTrue();
    }

    @Test
    void naoDeveIndicarEstouroAbaixoDe21() {
        Jogador jogador = new Jogador("Ana");
        jogador.receberCarta(carta("10"));
        jogador.receberCarta(carta("7"));
        assertThat(jogador.estourou()).isFalse();
    }
}
