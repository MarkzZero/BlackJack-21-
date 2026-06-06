package com.vendramel.blackjack.entity;

public class Dealer {
    private final Baralho baralho;

    public Dealer(Baralho baralho) {
        this.baralho = baralho;
    }

    public void embaralhar() {
        baralho.embaralhar();
    }

    public Carta comprarCarta() {
        return baralho.comprarCarta();
    }

    public int getQtdRestante() {
        return baralho.getQtdRestante();
    }

    public Baralho getBaralho() {
        return baralho;
    }
}
