package com.vendramel.blackjack.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.vendramel.blackjack.control.CtrlJogarCartas;
import com.vendramel.blackjack.control.EstadoJogador;
import com.vendramel.blackjack.control.ResultadoJogador;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
class UIJogoCartasTest {
    @Mock
    private CtrlJogarCartas controle;

    private String executar(String entrada) {
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        UIJogoCartas ui = new UIJogoCartas(
                controle,
                new ByteArrayInputStream(entrada.getBytes()),
                new PrintStream(saida, true));
        ui.iniciar();
        return saida.toString();
    }

    private void stubFluxoBasico() {
        when(controle.getQuantidadeJogadores()).thenReturn(1);
        when(controle.getCartaVisivelDistribuidor()).thenReturn("K de Copas");
        when(controle.getEstadoJogador(0))
                .thenReturn(new EstadoJogador("Ana", List.of("2 de Copas", "3 de Ouros"), 5, false));
        when(controle.getEstadoDistribuidor())
                .thenReturn(new EstadoJogador("Distribuidor", List.of("K de Copas", "7 de Paus"), 17, false));
        when(controle.getResultado(0))
                .thenReturn(new ResultadoJogador("Ana", 5, 17, "Perdeu"));
    }

    @Test
    void devePedirCartaEDepoisManter() {
        stubFluxoBasico();
        when(controle.jogadorEstourou(0)).thenReturn(false);

        String saida = executar("1\nAna\nP\nM\nN\n");

        verify(controle).novoJogo(List.of("Ana"));
        verify(controle).pedirCarta(0);
        verify(controle).encerrarPedidos();
        verify(controle).finalizar();
        assertThat(saida).contains("BLACKJACK");
        assertThat(saida).contains("Vez de Ana");
        assertThat(saida).contains("Perdeu");
        assertThat(saida).contains("Obrigado");
    }

    @Test
    void deveTratarEntradasInvalidas() {
        stubFluxoBasico();
        when(controle.jogadorEstourou(0)).thenReturn(false);

        String saida = executar("0\nabc\n1\nBob\nX\nM\ntalvez\nN\n");

        verify(controle).novoJogo(List.of("Bob"));
        verify(controle, never()).pedirCarta(anyInt());
        assertThat(saida).contains("Valor invalido");
        assertThat(saida).contains("Opcao invalida");
        assertThat(saida).contains("Resposta invalida");
    }

    @Test
    void deveSinalizarEstouroDoJogador() {
        stubFluxoBasico();
        when(controle.jogadorEstourou(0)).thenReturn(false, true);

        String saida = executar("1\nAna\nP\nN\n");

        verify(controle).pedirCarta(0);
        assertThat(saida).contains("ESTOUROU");
    }

    @Test
    void devePermitirJogarVariasRodadas() {
        stubFluxoBasico();
        when(controle.jogadorEstourou(0)).thenReturn(false);

        String saida = executar("1\nAna\nM\nS\n1\nAna\nM\nN\n");

        verify(controle, times(2)).novoJogo(List.of("Ana"));
        assertThat(saida).contains("Obrigado");
    }

    @Test
    void deveUsarNomePadraoQuandoNomeVazio() {
        stubFluxoBasico();
        when(controle.jogadorEstourou(0)).thenReturn(false);

        executar("1\n\nM\nN\n");

        verify(controle).novoJogo(List.of("Jogador 1"));
    }

    @Test
    void deveSinalizarEstouroDoDistribuidor() {
        when(controle.getQuantidadeJogadores()).thenReturn(1);
        when(controle.getCartaVisivelDistribuidor()).thenReturn("K de Copas");
        when(controle.getEstadoJogador(0))
                .thenReturn(new EstadoJogador("Ana", List.of("10 de Copas", "7 de Ouros"), 17, false));
        when(controle.jogadorEstourou(0)).thenReturn(false);
        when(controle.getEstadoDistribuidor())
                .thenReturn(new EstadoJogador("Distribuidor",
                        List.of("K de Copas", "10 de Paus", "5 de Ouros"), 25, true));
        when(controle.getResultado(0)).thenReturn(new ResultadoJogador("Ana", 17, 25, "Venceu"));

        String saida = executar("1\nAna\nM\nN\n");

        assertThat(saida).contains("Distribuidor ESTOUROU!");
        assertThat(saida).contains("Venceu");
    }

    @Test
    void deveConduzirTurnosDeVariosJogadores() {
        when(controle.getQuantidadeJogadores()).thenReturn(2);
        when(controle.getCartaVisivelDistribuidor()).thenReturn("K de Copas");
        when(controle.getEstadoJogador(0))
                .thenReturn(new EstadoJogador("Ana", List.of("10 de Copas", "7 de Ouros"), 17, false));
        when(controle.getEstadoJogador(1))
                .thenReturn(new EstadoJogador("Bruno", List.of("9 de Paus", "8 de Ouros"), 17, false));
        when(controle.jogadorEstourou(0)).thenReturn(false);
        when(controle.jogadorEstourou(1)).thenReturn(false);
        when(controle.getEstadoDistribuidor())
                .thenReturn(new EstadoJogador("Distribuidor", List.of("K de Copas", "7 de Paus"), 17, false));
        when(controle.getResultado(0)).thenReturn(new ResultadoJogador("Ana", 17, 17, "Empatou"));
        when(controle.getResultado(1)).thenReturn(new ResultadoJogador("Bruno", 17, 17, "Empatou"));

        String saida = executar("2\nAna\nBruno\nM\nM\nN\n");

        verify(controle).novoJogo(List.of("Ana", "Bruno"));
        assertThat(saida).contains("Vez de Ana");
        assertThat(saida).contains("Vez de Bruno");
        assertThat(saida).contains("Bruno: 17 x 17");
    }
}
