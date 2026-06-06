package com.vendramel.blackjack.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JogoTest {
    @Mock
    private Dealer dealer;

    @BeforeEach
    void baralhoComCartasPorPadrao() {
        lenient().when(dealer.getQtdRestante()).thenReturn(52);
    }

    private static Carta carta(String rank) {
        return new Carta("Copas", rank);
    }

    @Test
    void construtorDeveRejeitarListaVaziaOuNula() {
        assertThatThrownBy(() -> new Jogo(dealer, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Jogo(dealer, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void iniciarJogoDeveEmbaralharEDistribuirDuasCartas() {
        when(dealer.comprarCarta()).thenReturn(carta("10"), carta("5"), carta("9"), carta("6"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));

        jogo.iniciarJogo();

        verify(dealer).embaralhar();
        verify(dealer, times(4)).comprarCarta();
        assertThat(ana.getCartas()).hasSize(2);
        assertThat(jogo.getMaoDistribuidor().getCartas()).hasSize(2);
        assertThat(jogo.isLoopJogo()).isTrue();
    }

    @Test
    void iniciarJogoDeveDistribuirParaVariosJogadores() {
        when(dealer.comprarCarta()).thenReturn(
                carta("2"), carta("3"), carta("4"),
                carta("5"), carta("6"), carta("7"));
        Jogo jogo = new Jogo(dealer, List.of(new Jogador("Ana"), new Jogador("Bruno")));

        jogo.iniciarJogo();

        verify(dealer, times(6)).comprarCarta();
        assertThat(jogo.getJogadores().get(0).getCartas()).hasSize(2);
        assertThat(jogo.getJogadores().get(1).getCartas()).hasSize(2);
        assertThat(jogo.getMaoDistribuidor().getCartas()).hasSize(2);
    }

    @Test
    void pedirCartaDeveAdicionarUmaCartaAoJogador() {
        when(dealer.comprarCarta()).thenReturn(
                carta("2"), carta("3"), carta("4"), carta("5"), carta("10"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.pedirCarta(ana);

        assertThat(ana.getCartas()).hasSize(3);
        assertThat(ana.calcularTotal()).isEqualTo(16);
    }

    @Test
    void pararDeveEncerrarOLoop() {
        when(dealer.comprarCarta()).thenReturn(carta("2"));
        Jogo jogo = new Jogo(dealer, List.of(new Jogador("Ana")));
        jogo.iniciarJogo();
        assertThat(jogo.isLoopJogo()).isTrue();

        jogo.parar();

        assertThat(jogo.isLoopJogo()).isFalse();
    }

    @Test
    void rn02_distribuidorDevePedirAteAtingir17() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("5"), carta("9"), carta("6"), carta("5"), carta("2"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.finalizarJogo();

        assertThat(jogo.getMaoDistribuidor().calcularTotal()).isEqualTo(18);
        verify(dealer, times(6)).comprarCarta();
        assertThat(jogo.isLoopJogo()).isFalse();
        assertThat(jogo.apurarResultado(ana)).isEqualTo(Resultado.VITORIA);
    }

    @Test
    void rn02_distribuidorNaoPedeSeJaAtingiu17() {
        when(dealer.comprarCarta()).thenReturn(
                carta("9"), carta("10"), carta("8"), carta("7"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.finalizarJogo();

        assertThat(jogo.getMaoDistribuidor().calcularTotal()).isEqualTo(17);
        verify(dealer, times(4)).comprarCarta();
        assertThat(jogo.apurarResultado(ana)).isEqualTo(Resultado.EMPATE);
    }

    @Test
    void apurarResultadoDeveDarDerrotaQuandoJogadorEstoura() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("2"), carta("10"), carta("3"), carta("10"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();
        jogo.pedirCarta(ana);

        assertThat(ana.estourou()).isTrue();
        assertThat(jogo.apurarResultado(ana)).isEqualTo(Resultado.DERROTA);
    }

    @Test
    void apurarResultadoDeveDarVitoriaQuandoCasaEstoura() {
        when(dealer.comprarCarta()).thenReturn(
                carta("10"), carta("10"), carta("9"), carta("6"), carta("10"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.finalizarJogo();

        assertThat(jogo.getMaoDistribuidor().calcularTotal()).isEqualTo(26);
        assertThat(jogo.apurarResultado(ana)).isEqualTo(Resultado.VITORIA);
    }

    @Test
    void getDealerDeveRetornarODealer() {
        Jogo jogo = new Jogo(dealer, List.of(new Jogador("Ana")));
        assertThat(jogo.getDealer()).isSameAs(dealer);
    }

    @Test
    void rn01_asContaComoOnzeNoFluxoOrquestrado() {
        when(dealer.comprarCarta()).thenReturn(carta("A"), carta("A"), carta("9"), carta("6"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.finalizarJogo();

        assertThat(ana.calcularTotal()).isEqualTo(20);
        assertThat(jogo.getMaoDistribuidor().calcularTotal()).isEqualTo(17);
        verify(dealer, times(4)).comprarCarta();
        assertThat(jogo.apurarResultado(ana)).isEqualTo(Resultado.VITORIA);
    }

    @Test
    void rn01_asDoJogadorViraUmAposPedirNoFluxo() {
        when(dealer.comprarCarta()).thenReturn(
                carta("A"), carta("7"), carta("9"), carta("7"), carta("5"));
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.pedirCarta(ana);

        assertThat(ana.calcularTotal()).isEqualTo(15);
        assertThat(ana.estourou()).isFalse();
    }

    @Test
    void rn02_distribuidorParaQuandoBaralhoEsgota() {
        when(dealer.comprarCarta()).thenReturn(carta("10"), carta("5"), carta("9"), carta("6"));
        when(dealer.getQtdRestante()).thenReturn(0);
        Jogador ana = new Jogador("Ana");
        Jogo jogo = new Jogo(dealer, List.of(ana));
        jogo.iniciarJogo();

        jogo.finalizarJogo();

        assertThat(jogo.getMaoDistribuidor().calcularTotal()).isEqualTo(11);
        verify(dealer, times(4)).comprarCarta();
    }
}
