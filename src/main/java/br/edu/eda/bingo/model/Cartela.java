
package br.edu.eda.bingo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cartela {

    private final String id;
    private final String nomeJogador;
    private final int[][] numeros;
    private final boolean[][] marcados;
    private final Map<Integer, int[]> indicePosicoes;
    private int acertos;

    public Cartela(String nomeJogador, int[][] numeros) {

        if (numeros == null || numeros.length != 5) {
            throw new IllegalArgumentException("A cartela deve ter 5 linhas.");
        }

        for (int[] linha : numeros) {
            if (linha == null || linha.length != 5) {
                throw new IllegalArgumentException(
                        "Cada linha deve possuir 5 números."
                );
            }
        }

        this.id = UUID.randomUUID().toString();
        this.nomeJogador = nomeJogador;
        this.numeros = numeros;
        this.marcados = new boolean[5][5];
        this.indicePosicoes = new HashMap<>();
        this.acertos = 1;

        // Espaço central gratuito
        marcados[2][2] = true;

        // Indexa a posição de cada número
        for (int linha = 0; linha < 5; linha++) {
            for (int coluna = 0; coluna < 5; coluna++) {

                int numero = numeros[linha][coluna];

                if (numero != 0) {
                    indicePosicoes.put(
                            numero,
                            new int[]{linha, coluna}
                    );
                }
            }
        }
    }

    public boolean marcarNumero(int numero) {

        int[] posicao = indicePosicoes.get(numero);

        if (posicao == null) {
            return false;
        }

        int linha = posicao[0];
        int coluna = posicao[1];

        if (marcados[linha][coluna]) {
            return false;
        }

        marcados[linha][coluna] = true;
        acertos++;

        return true;
    }

    public boolean possuiNumero(int numero) {
        return indicePosicoes.containsKey(numero);
    }

    public boolean verificarVitoria(TipoVitoria tipo) {

        return switch (tipo) {
            case LINHA -> checarLinhas();
            case COLUNA -> checarColunas();
            case DIAGONAL -> checarDiagonais();
            case CARTELA_CHEIA -> acertos == 25;
        };
    }

    private boolean checarLinhas() {

        for (int linha = 0; linha < 5; linha++) {

            boolean completa = true;

            for (int coluna = 0; coluna < 5; coluna++) {
                if (!marcados[linha][coluna]) {
                    completa = false;
                    break;
                }
            }

            if (completa) {
                return true;
            }
        }

        return false;
    }

    private boolean checarColunas() {

        for (int coluna = 0; coluna < 5; coluna++) {

            boolean completa = true;

            for (int linha = 0; linha < 5; linha++) {
                if (!marcados[linha][coluna]) {
                    completa = false;
                    break;
                }
            }

            if (completa) {
                return true;
            }
        }

        return false;
    }

    private boolean checarDiagonais() {

        boolean principal = true;
        boolean secundaria = true;

        for (int i = 0; i < 5; i++) {

            if (!marcados[i][i]) {
                principal = false;
            }

            if (!marcados[i][4 - i]) {
                secundaria = false;
            }
        }

        return principal || secundaria;
    }

    public String getId() {
        return id;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public int[][] getNumeros() {
        return numeros;
    }

    public boolean[][] getMarcados() {
        return marcados;
    }
}
