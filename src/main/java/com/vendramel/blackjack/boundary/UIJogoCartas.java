package com.vendramel.blackjack.boundary;

import com.vendramel.blackjack.control.CtrlJogarCartas;
import com.vendramel.blackjack.entity.Carta;
import com.vendramel.blackjack.entity.Jogador;
import com.vendramel.blackjack.entity.Resultado;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UIJogoCartas {
    private static final int MIN_JOGADORES = 1;
    private static final int MAX_JOGADORES = 7;

    private final CtrlJogarCartas controle;
    private final Scanner scanner;
    private final PrintStream saida;

    @Autowired
    public UIJogoCartas(CtrlJogarCartas controle) {
        this(controle, System.in, System.out);
    }

    public UIJogoCartas(CtrlJogarCartas controle, InputStream entrada, PrintStream saida) {
        this.controle = controle;
        this.scanner = new Scanner(entrada);
        this.saida = saida;
    }

    public void iniciar() {
        saida.println("=======================================");
        saida.println("        BLACKJACK (21) - CLI");
        saida.println("=======================================");
        boolean jogarNovamente = true;
        while (jogarNovamente) {
            List<String> nomes = lerJogadores();
            controle.novoJogo(nomes);
            jogarRodada();
            jogarNovamente = lerSimNao("\nJogar novamente? (S/N): ");
        }
        saida.println("\nObrigado por jogar! Ate a proxima.");
    }

    private List<String> lerJogadores() {
        int quantidade = lerInteiro(
                "\nQuantos jogadores? (" + MIN_JOGADORES + "-" + MAX_JOGADORES + "): ",
                MIN_JOGADORES, MAX_JOGADORES);
        List<String> nomes = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            saida.print("Nome do jogador " + i + ": ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                nome = "Jogador " + i;
            }
            nomes.add(nome);
        }
        return nomes;
    }

    private void jogarRodada() {
        saida.println("\nCarta visivel do distribuidor: " + controle.getCartaVisivelDistribuidor());
        int quantidade = controle.getQuantidadeJogadores();
        for (int i = 0; i < quantidade; i++) {
            jogarTurno(i);
        }
        controle.encerrarPedidos();
        controle.finalizar();
        mostrarResultados();
    }

    private void jogarTurno(int indice) {
        Jogador jogador = controle.getJogador(indice);
        saida.println("\n--- Vez de " + jogador.getNome() + " ---");
        mostrarMao(jogador);
        while (!controle.jogadorEstourou(indice)) {
            saida.print("(P)edir carta ou (M)anter? ");
            String opcao = scanner.nextLine().trim().toUpperCase();
            if (opcao.startsWith("P")) {
                controle.pedirCarta(indice);
                jogador = controle.getJogador(indice);
                mostrarMao(jogador);
            } else if (opcao.startsWith("M")) {
                break;
            } else {
                saida.println("Opcao invalida. Digite P ou M.");
            }
        }
        if (controle.jogadorEstourou(indice)) {
            saida.println(">> " + jogador.getNome() + " ESTOUROU!");
        }
    }

    private void mostrarMao(Jogador jogador) {
        List<String> cartas = new ArrayList<>();
        for (Carta carta : jogador.getCartas()) {
            cartas.add(carta.toString());
        }
        saida.println("Cartas de " + jogador.getNome() + ": " + String.join(", ", cartas)
                + " (total: " + jogador.calcularTotal() + ")");
    }

    private void mostrarResultados() {
        Jogador distribuidor = controle.getDistribuidor();
        List<String> cartasDist = new ArrayList<>();
        for (Carta carta : distribuidor.getCartas()) {
            cartasDist.add(carta.toString());
        }
        saida.println("\n=======================================");
        saida.println("Mao do distribuidor: " + String.join(", ", cartasDist)
                + " (total: " + distribuidor.calcularTotal() + ")");
        if (distribuidor.estourou()) {
            saida.println(">> Distribuidor ESTOUROU!");
        }
        saida.println("---------------------------------------");
        int quantidade = controle.getQuantidadeJogadores();
        for (int i = 0; i < quantidade; i++) {
            Jogador jogador = controle.getJogador(i);
            Resultado resultado = controle.getResultado(i);
            String situacao = switch (resultado) {
                case VITORIA -> "Venceu";
                case DERROTA -> "Perdeu";
                case EMPATE -> "Empatou";
            };
            saida.println(jogador.getNome() + ": " + jogador.calcularTotal()
                    + " x " + distribuidor.calcularTotal() + " (casa) -> " + situacao);
        }
        saida.println("=======================================");
    }

    private boolean lerSimNao(String mensagem) {
        while (true) {
            saida.print(mensagem);
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (resposta.startsWith("S")) {
                return true;
            }
            if (resposta.startsWith("N")) {
                return false;
            }
            saida.println("Resposta invalida. Digite S ou N.");
        }
    }

    private int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            saida.print(mensagem);
            String linha = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException ignored) {
            }
            saida.println("Valor invalido. Informe um numero entre " + min + " e " + max + ".");
        }
    }
}
