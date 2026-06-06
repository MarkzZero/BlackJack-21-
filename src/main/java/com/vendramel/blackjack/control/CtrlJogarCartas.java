package com.vendramel.blackjack.control;

import com.vendramel.blackjack.entity.Baralho;
import com.vendramel.blackjack.entity.Dealer;
import com.vendramel.blackjack.entity.Jogador;
import com.vendramel.blackjack.entity.Jogo;
import com.vendramel.blackjack.entity.Resultado;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class CtrlJogarCartas {
    private final Supplier<Dealer> dealerFactory;
    private Jogo jogo;

    public CtrlJogarCartas() {
        this(() -> new Dealer(new Baralho()));
    }

    CtrlJogarCartas(Supplier<Dealer> dealerFactory) {
        this.dealerFactory = dealerFactory;
    }

    public void novoJogo(List<String> nomes) {
        List<Jogador> jogadores = new ArrayList<>();
        for (String nome : nomes) {
            jogadores.add(new Jogador(nome));
        }
        this.jogo = new Jogo(dealerFactory.get(), jogadores);
        this.jogo.iniciarJogo();
    }

    public int getQuantidadeJogadores() {
        return jogo.getJogadores().size();
    }

    public String getCartaVisivelDistribuidor() {
        return jogo.getMaoDistribuidor().getCartas().get(0).toString();
    }

    public Jogador getJogador(int indice) {
        return jogo.getJogadores().get(indice);
    }

    public Jogador getDistribuidor() {
        return jogo.getMaoDistribuidor();
    }

    public void pedirCarta(int indice) {
        jogo.pedirCarta(jogo.getJogadores().get(indice));
    }

    public boolean jogadorEstourou(int indice) {
        return jogo.getJogadores().get(indice).estourou();
    }

    public void encerrarPedidos() {
        jogo.parar();
    }

    public void finalizar() {
        jogo.finalizarJogo();
    }

    public Resultado getResultado(int indice) {
        Jogador jogador = jogo.getJogadores().get(indice);
        return jogo.apurarResultado(jogador);
    }

    public boolean isRodadaEmAndamento() {
        return jogo != null && jogo.isLoopJogo();
    }
}
