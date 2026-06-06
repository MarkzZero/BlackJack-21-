package com.vendramel.blackjack.control;

import com.vendramel.blackjack.entity.Baralho;
import com.vendramel.blackjack.entity.Carta;
import com.vendramel.blackjack.entity.Dealer;
import com.vendramel.blackjack.entity.Jogador;
import com.vendramel.blackjack.entity.Resultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CtrlJogarCartasTest {

    private CtrlJogarCartas controle;

    @BeforeEach
    void setUp() {
        controle = new CtrlJogarCartas();
        controle.novoJogo(List.of("Ana", "Bruno"));
    }

    @Test
    void deveRetornarNomeDoJogador() {
        Jogador jogador = controle.getJogador(0);
        assertThat(jogador.getNome()).isEqualTo("Ana");
    }

    @Test
    void deveRetornarNomeDoSegundoJogador() {
        assertThat(controle.getJogador(1).getNome()).isEqualTo("Bruno");
    }

    @Test
    void jogadorDeveReceberCartaAoSolicitar() {
        controle.pedirCarta(0);
        assertThat(controle.getJogador(0).getCartas()).hasSize(3);
    }

    @Test
    void distribuidorDeveTerTotalAposFinalizacao() {
        controle.encerrarPedidos();
        controle.finalizar();
        assertThat(controle.getDistribuidor().calcularTotal()).isGreaterThanOrEqualTo(17);
    }

    @Test
    void deveRetornarResultadoDoJogador() {
        controle.encerrarPedidos();
        controle.finalizar();
        Resultado resultado = controle.getResultado(0);
        assertThat(resultado).isIn(Resultado.VITORIA, Resultado.DERROTA, Resultado.EMPATE);
    }
}