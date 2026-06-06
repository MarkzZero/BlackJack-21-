package com.vendramel.blackjack.entity;

public class Carta {
    private final String naipe;
    private final String rank;

    public Carta(String naipe, String rank) {
        this.naipe = naipe;
        this.rank = rank;
    }

    public String getNaipe() {
        return naipe;
    }

    public String getRank() {
        return rank;
    }

    public int getValor() {
        return switch (rank) {
            case "A" -> 11;
            case "K", "Q", "J", "10" -> 10;
            default -> Integer.parseInt(rank);
        };
    }

    public boolean isAs() {
        return "A".equals(rank);
    }

    @Override
    public String toString() {
        return rank + " de " + naipe;
    }
}
