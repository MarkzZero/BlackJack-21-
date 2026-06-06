package com.vendramel.blackjack.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Baralho {
    private static final String[] NAIPES = {"Copas", "Ouros", "Paus", "Espadas"};
    private static final String[] RANKS =
            {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    private final List<Carta> cartas = new ArrayList<>();
    private final Random random;

    public Baralho() {
        this(new Random());
    }

    public Baralho(Random random) {
        this.random = random;
        for (String naipe : NAIPES) {
            for (String rank : RANKS) {
                cartas.add(new Carta(naipe, rank));
            }
        }
    }

    public void embaralhar() {
        Collections.shuffle(cartas, random);
    }

    public Carta comprarCarta() {
        if (cartas.isEmpty()) {
            throw new IllegalStateException("O baralho está vazio.");
        }
        return cartas.remove(cartas.size() - 1);
    }

    public int getQtdRestante() {
        return cartas.size();
    }
}
