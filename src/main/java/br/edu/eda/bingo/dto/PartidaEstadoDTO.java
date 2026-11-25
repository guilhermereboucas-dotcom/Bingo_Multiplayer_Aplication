package br.edu.eda.bingo.dto;


import br.edu.eda.bingo.model.Bolinha;
import br.edu.eda.bingo.model.Partida;
import br.edu.eda.bingo.model.StatusPartida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaEstadoDTO {

    private StatusPartida status;
    private Bolinha ultimaBola;
    private List<Integer> historicosorteados;
    private int totalJogadores;

    public static PartidaEstadoDTO from(Partida partida) {
        return  new  PartidaEstadoDTO(partida.getStatus(),
                partida.getUltimaBola(),
                new ArrayList<>(partida.getHistoricoSorteados()),
                partida.getTotalJogadores());
    }
}
