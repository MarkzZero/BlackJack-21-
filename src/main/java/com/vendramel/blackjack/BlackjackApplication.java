package com.vendramel.blackjack;

import com.vendramel.blackjack.boundary.UIJogoCartas;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlackjackApplication implements CommandLineRunner {
    private final UIJogoCartas uiJogoCartas;

    public BlackjackApplication(UIJogoCartas uiJogoCartas) {
        this.uiJogoCartas = uiJogoCartas;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlackjackApplication.class, args);
    }

    @Override
    public void run(String... args) {
        uiJogoCartas.iniciar();
    }
}
