package com.vendramel.blackjack.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DealerTest {
    @Mock
    private Baralho baralho;

    private Dealer dealer;

    @BeforeEach
    void setUp() {
        dealer = new Dealer(baralho);
    }

    @Test
    void embaralharDeveDelegarAoBaralho() {
        dealer.embaralhar();
        verify(baralho).embaralhar();
    }

    @Test
    void comprarCartaDeveDelegarERetornarADoBaralho() {
        Carta carta = new Carta("Espadas", "Q");
        when(baralho.comprarCarta()).thenReturn(carta);

        assertThat(dealer.comprarCarta()).isSameAs(carta);
        verify(baralho).comprarCarta();
    }

    @Test
    void getQtdRestanteDeveDelegarAoBaralho() {
        when(baralho.getQtdRestante()).thenReturn(42);
        assertThat(dealer.getQtdRestante()).isEqualTo(42);
    }

    @Test
    void getBaralhoDeveRetornarOBaralhoAdministrado() {
        assertThat(dealer.getBaralho()).isSameAs(baralho);
    }
}
