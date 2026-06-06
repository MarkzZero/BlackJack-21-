package com.vendramel.blackjack.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogador {
    private static final int VINTE_E_UM = 21;
    private static final int AJUSTE_AS = 10;

    private final String nome;
    private final List<Carta> cartas = new ArrayList<>();

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void receberCarta(Carta carta) {
        cartas.add(carta);
    }

    public List<Carta> getCartas() {
        return Collections.unmodifiableList(cartas);
    }

    public int calcularTotal() {
        int total = 0;
        int ases = 0;
        for (Carta carta : cartas) {
            total += carta.getValor();
            if (carta.isAs()) {
                ases++;
            }
        }
        while (total > VINTE_E_UM && ases > 0) {
            total -= AJUSTE_AS;
            ases--;
        }
        return total;
    }

    public boolean estourou() {
        return calcularTotal() > VINTE_E_UM;
    }
}
