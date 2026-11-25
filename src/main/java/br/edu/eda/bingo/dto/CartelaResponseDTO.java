package br.edu.eda.bingo.dto;

import br.edu.eda.bingo.model.Cartela;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CartelaResponseDTO {
    private String id;
    private String nomeJogador;
    private int[][] numeros;
    private boolean[][] marcados;

    public static CartelaResponseDTO from(Cartela cartela) {
        return new CartelaResponseDTO(cartela.getId(), cartela.getNomeJogador(),  cartela.getNumeros(), cartela.getMarcados());
    }
}
