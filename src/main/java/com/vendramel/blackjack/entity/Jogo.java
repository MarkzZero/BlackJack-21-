package com.vendramel.blackjack.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jogo {
    private static final int CARTAS_INICIAIS = 2;
    private static final int LIMITE_DISTRIBUIDOR = 17;
    private static final int VINTE_E_UM = 21;
    static final String NOME_DISTRIBUIDOR = "Distribuidor";

    private final Dealer dealer;
    private final List<Jogador> jogadores;
    private final Jogador maoDistribuidor;
    private boolean loopJogo;

    public Jogo(Dealer dealer, List<Jogador> jogadores) {
        this(dealer, jogadores, new Jogador(NOME_DISTRIBUIDOR));
    }

    public Jogo(Dealer dealer, List<Jogador> jogadores, Jogador maoDistribuidor) {
        if (jogadores == null || jogadores.isEmpty()) {
            throw new IllegalArgumentException("É necessário pelo menos um jogador.");
        }
        this.dealer = dealer;
        this.jogadores = new ArrayList<>(jogadores);
        this.maoDistribuidor = maoDistribuidor;
    }

    public void iniciarJogo() {
        dealer.embaralhar();
        for (int rodada = 0; rodada < CARTAS_INICIAIS; rodada++) {
            for (Jogador jogador : jogadores) {
                jogador.receberCarta(dealer.comprarCarta());
            }
            maoDistribuidor.receberCarta(dealer.comprarCarta());
        }
        loopJogo = true;
    }

    public void pedirCarta(Jogador jogador) {
        jogador.receberCarta(dealer.comprarCarta());
    }

    public void parar() {
        loopJogo = false;
    }

    public void finalizarJogo() {
        while (maoDistribuidor.calcularTotal() < LIMITE_DISTRIBUIDOR && dealer.getQtdRestante() > 0) {
            maoDistribuidor.receberCarta(dealer.comprarCarta());
        }
        loopJogo = false;
    }

    public Resultado apurarResultado(Jogador jogador) {
        int totalJogador = jogador.calcularTotal();
        int totalCasa = maoDistribuidor.calcularTotal();
        if (totalJogador > VINTE_E_UM) {
            return Resultado.DERROTA;
        }
        if (totalCasa > VINTE_E_UM || totalJogador > totalCasa) {
            return Resultado.VITORIA;
        }
        if (totalJogador < totalCasa) {
            return Resultado.DERROTA;
        }
        return Resultado.EMPATE;
    }

    public boolean isLoopJogo() {
        return loopJogo;
    }

    public List<Jogador> getJogadores() {
        return Collections.unmodifiableList(jogadores);
    }

    public Jogador getMaoDistribuidor() {
        return maoDistribuidor;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
