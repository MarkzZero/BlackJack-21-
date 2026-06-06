package com.vendramel.blackjack.control;

import com.vendramel.blackjack.entity.Baralho;
import com.vendramel.blackjack.entity.Carta;
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

    public EstadoJogador getEstadoJogador(int indice) {
        return paraEstado(jogo.getJogadores().get(indice));
    }

    public EstadoJogador getEstadoDistribuidor() {
        return paraEstado(jogo.getMaoDistribuidor());
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

    public ResultadoJogador getResultado(int indice) {
        Jogador jogador = jogo.getJogadores().get(indice);
        Resultado resultado = jogo.apurarResultado(jogador);
        return new ResultadoJogador(
                jogador.getNome(),
                jogador.calcularTotal(),
                jogo.getMaoDistribuidor().calcularTotal(),
                descrever(resultado));
    }

    public boolean isRodadaEmAndamento() {
        return jogo != null && jogo.isLoopJogo();
    }

    private EstadoJogador paraEstado(Jogador jogador) {
        List<String> cartas = new ArrayList<>();
        for (Carta carta : jogador.getCartas()) {
            cartas.add(carta.toString());
        }
        return new EstadoJogador(jogador.getNome(), cartas, jogador.calcularTotal(), jogador.estourou());
    }

    private String descrever(Resultado resultado) {
        return switch (resultado) {
            case VITORIA -> "Venceu";
            case DERROTA -> "Perdeu";
            case EMPATE -> "Empatou";
        };
    }
}
