package com.vendramel.blackjack.control;

import java.util.List;

public record EstadoJogador(String nome, List<String> cartas, int total, boolean estourou) {
}
