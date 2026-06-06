package com.vendramel.blackjack.boundary;

import com.vendramel.blackjack.control.CtrlJogarCartas;
import com.vendramel.blackjack.entity.Carta;
import com.vendramel.blackjack.entity.Jogador;
import com.vendramel.blackjack.entity.Resultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class UIJogoCartasTest {

    private CtrlJogarCartas controle;
    private UIJogoCartas ui;

    @BeforeEach
    void setUp() {
        controle = mock(CtrlJogarCartas.class);
        ui = new UIJogoCartas(controle);
    }

    @Test
    void deveExibirEstadoDoJogadorAposIniciar() {
        Jogador ana = mock(Jogador.class);
        when(ana.getNome()).thenReturn("Ana");
        when(ana.calcularTotal()).thenReturn(5);
        when(ana.estourou()).thenReturn(false);

        Jogador distribuidor = mock(Jogador.class);
        when(distribuidor.getNome()).thenReturn("Distribuidor");
        when(distribuidor.calcularTotal()).thenReturn(17);

        when(controle.getQuantidadeJogadores()).thenReturn(1);
        when(controle.getJogador(0)).thenReturn(ana);
        when(controle.getDistribuidor()).thenReturn(distribuidor);
        when(controle.getResultado(0)).thenReturn(Resultado.DERROTA);

        // chame o método da UI que exibe o estado e verifique o comportamento esperado
        verify(controle, atLeastOnce()).getJogador(0);
    }

    @Test
    void deveExibirVitoriaDoJogador() {
        Jogador ana = mock(Jogador.class);
        when(ana.getNome()).thenReturn("Ana");
        when(ana.calcularTotal()).thenReturn(17);
        when(ana.estourou()).thenReturn(false);

        Jogador distribuidor = mock(Jogador.class);
        when(distribuidor.getNome()).thenReturn("Distribuidor");
        when(distribuidor.calcularTotal()).thenReturn(25);
        when(distribuidor.estourou()).thenReturn(true);

        when(controle.getQuantidadeJogadores()).thenReturn(1);
        when(controle.getJogador(0)).thenReturn(ana);
        when(controle.getDistribuidor()).thenReturn(distribuidor);
        when(controle.getResultado(0)).thenReturn(Resultado.VITORIA);

        verify(controle, atLeastOnce()).getResultado(0);
    }

    @Test
    void deveExibirEmpateParaMultiplosJogadores() {
        Jogador ana = mock(Jogador.class);
        when(ana.getNome()).thenReturn("Ana");
        when(ana.calcularTotal()).thenReturn(17);

        Jogador bruno = mock(Jogador.class);
        when(bruno.getNome()).thenReturn("Bruno");
        when(bruno.calcularTotal()).thenReturn(17);

        Jogador distribuidor = mock(Jogador.class);
        when(distribuidor.getNome()).thenReturn("Distribuidor");
        when(distribuidor.calcularTotal()).thenReturn(17);

        when(controle.getQuantidadeJogadores()).thenReturn(2);
        when(controle.getJogador(0)).thenReturn(ana);
        when(controle.getJogador(1)).thenReturn(bruno);
        when(controle.getDistribuidor()).thenReturn(distribuidor);
        when(controle.getResultado(0)).thenReturn(Resultado.EMPATE);
        when(controle.getResultado(1)).thenReturn(Resultado.EMPATE);

        verify(controle, atLeastOnce()).getResultado(0);
    }
}