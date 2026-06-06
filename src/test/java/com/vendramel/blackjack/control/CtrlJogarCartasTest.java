package com.vendramel.blackjack.control;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.vendramel.blackjack.entity.Carta;
import com.vendramel.blackjack.entity.Dealer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CtrlJogarCartasTest {
    @Mock
    private Dealer dealer;

    private CtrlJogarCartas controle;

    @BeforeEach
    void setUp() {
        controle = new CtrlJogarCartas(() -> dealer);
        lenient().when(dealer.getQtdRestante()).thenReturn(52);
    }

    private static Carta carta(String rank) {
        return new Carta("Copas", rank);
    }

    @Test
    void novoJogoDeveDistribuirEExporOEstado() {
        when(dealer.comprarCarta()).thenReturn(carta("10"), carta("9"), carta("8"), carta("7"));

        controle.novoJogo(List.of("Ana"));

        assertThat(controle.getQuantidadeJogadores()).isEqualTo(1);
        EstadoJogador estado = controle.getEstadoJogador(0);
        assertThat(estado.nome()).isEqualTo("Ana");
        assertThat(estado.cartas()).hasSize(2);
        assertThat(estado.total()).isEqualTo(18);
        assertThat(estado.estourou()).isFalse();
        assertThat(controle.getCartaVisivelDistribuidor()).isEqualTo("9 de Copas");
        assertThat(controle.isRodadaEmAndamento()).isTrue();
    }

    @Test
    void deveSuportarVariosJogadores() {
        when(dealer.comprarCarta()).thenReturn(
                carta("2"), carta("3"), carta("4"), carta("5"), carta("6"), carta("7"));

        controle.novoJogo(List.of("Ana", "Bruno"));

        assertThat(controle.getQuantidadeJogadores()).isEqualTo(2);
        assertThat(controle.getEstadoJogador(1).nome()).isEqualTo("Bruno");
    }

    @Test
    void pedirCartaDeveAtualizarMaoEDetectarEstouro() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("2"), carta("10"), carta("3"), carta("10"));

        controle.novoJogo(List.of("Ana"));
        controle.pedirCarta(0);

        assertThat(controle.jogadorEstourou(0)).isTrue();
        assertThat(controle.getEstadoJogador(0).cartas()).hasSize(3);
    }

    @Test
    void encerrarPedidosDeveFinalizarOLoop() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("9"), carta("8"), carta("7"));

        controle.novoJogo(List.of("Ana"));
        controle.encerrarPedidos();

        assertThat(controle.isRodadaEmAndamento()).isFalse();
    }

    @Test
    void finalizarDeveAplicarRN02EProduzirResultadoDeVitoria() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("5"), carta("9"), carta("6"), carta("5"), carta("2"));

        controle.novoJogo(List.of("Ana"));
        controle.finalizar();

        ResultadoJogador resultado = controle.getResultado(0);
        assertThat(resultado.nome()).isEqualTo("Ana");
        assertThat(resultado.total()).isEqualTo(19);
        assertThat(resultado.totalDistribuidor()).isEqualTo(18);
        assertThat(resultado.situacao()).isEqualTo("Venceu");
        assertThat(controle.getEstadoDistribuidor().total()).isEqualTo(18);
    }

    @Test
    void resultadoDeveSerEmpatadoQuandoTotaisIguais() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("10"), carta("8"), carta("8"));

        controle.novoJogo(List.of("Ana"));
        controle.finalizar();

        assertThat(controle.getResultado(0).situacao()).isEqualTo("Empatou");
    }

    @Test
    void resultadoDeveSerPerdidoQuandoCasaTemTotalMaior() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("10"), carta("5"), carta("9"));

        controle.novoJogo(List.of("Ana"));
        controle.finalizar();

        assertThat(controle.getResultado(0).situacao()).isEqualTo("Perdeu");
    }

    @Test
    void isRodadaEmAndamentoDeveSerFalsoAntesDeIniciar() {
        assertThat(controle.isRodadaEmAndamento()).isFalse();
    }
}
