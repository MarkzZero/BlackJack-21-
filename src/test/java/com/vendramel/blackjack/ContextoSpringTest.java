package com.vendramel.blackjack;

import static org.assertj.core.api.Assertions.assertThat;

import com.vendramel.blackjack.boundary.UIJogoCartas;
import com.vendramel.blackjack.control.CtrlJogarCartas;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ContextoSpringTest {
    @MockBean
    private UIJogoCartas uiJogoCartas;

    @Autowired
    private CtrlJogarCartas ctrlJogarCartas;

    @Test
    void contextoDeveCarregarBeans() {
        assertThat(ctrlJogarCartas).isNotNull();
        assertThat(uiJogoCartas).isNotNull();
    }
}
