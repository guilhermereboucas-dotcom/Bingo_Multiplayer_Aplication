package br.edu.eda.bingo.dto;

public class ResultadoBingoDTO {

    private final boolean valido;
    private final String mensagem;

    public ResultadoBingoDTO(boolean valido, String mensagem) {
        this.valido = valido;
        this.mensagem = mensagem;
    }

    public boolean isValido() {
        return valido;
    }

    public String getMensagem() {
        return mensagem;
    }
}