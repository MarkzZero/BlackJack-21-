package com.vendramel.blackjack;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.vendramel.blackjack.boundary.UIJogoCartas;
import org.junit.jupiter.api.Test;

class BlackjackApplicationTest {
    @Test
    void runDeveDelegarOControleParaABoundary() {
        UIJogoCartas ui = mock(UIJogoCartas.class);
        BlackjackApplication aplicacao = new BlackjackApplication(ui);

        aplicacao.run("arg1", "arg2");

        verify(ui).iniciar();
    }
}
