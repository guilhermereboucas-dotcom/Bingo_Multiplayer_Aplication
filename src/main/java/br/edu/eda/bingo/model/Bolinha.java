package br.edu.eda.bingo.model;

import java.time.LocalDateTime;

public class Bolinha {

    private final Integer numero;
    private final String letra;
    private final Integer sequencia;
    private final LocalDateTime momentoSorteio;

    public Bolinha(Integer numero, Integer sequencia) {
        this.numero = numero;
        this.sequencia = sequencia;
        this.letra = calcularLetra(numero);
        this.momentoSorteio = LocalDateTime.now();
    }

    public static String calcularLetra(int numero) {
        if (numero >= 1 && numero <= 15) {
            return "B";
        } else if (numero >= 16 && numero <= 30) {
            return "I";
        } else if (numero >= 31 && numero <= 45) {
            return "N";
        } else if (numero >= 46 && numero <= 60) {
            return "G";
        } else if (numero >= 61 && numero <= 75) {
            return "O";
        }

        throw new IllegalArgumentException("Número inválido: " + numero);
    }

    public Integer getNumero() {
        return numero;
    }

    public String getLetra() {
        return letra;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public LocalDateTime getMomentoSorteio() {
        return momentoSorteio;
    }
}