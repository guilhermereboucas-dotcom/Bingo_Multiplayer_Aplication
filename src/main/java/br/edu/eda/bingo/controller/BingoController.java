
package br.edu.eda.bingo.controller;

import br.edu.eda.bingo.model.Bolinha;
import br.edu.eda.bingo.model.Cartela;
import br.edu.eda.bingo.model.Partida;
import br.edu.eda.bingo.model.TipoVitoria;
import br.edu.eda.bingo.dto.ResultadoBingoDTO;
import br.edu.eda.bingo.service.BingoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bingo")
public class BingoController {

    private final BingoService bingoService;

    public BingoController(BingoService bingoService) {
        this.bingoService = bingoService;
    }


    @PostMapping("/iniciar")
    public ResponseEntity<Partida> iniciarPartida(
            @RequestBody IniciarPartidaRequest request) {

        Partida partida = bingoService.iniciarNovaPartida(
                request.tipoVitoria()
        );

        return ResponseEntity.ok(partida);
    }


    @PostMapping("/cartelas")
    public ResponseEntity<Cartela> gerarCartela(
            @RequestBody CriarCartelaRequest request) {

        Cartela cartela = bingoService.gerarCartelaParaJogador(
                request.nomeJogador()
        );

        return ResponseEntity.ok(cartela);
    }


    @PostMapping("/sortear")
    public ResponseEntity<Bolinha> sortearBola() {

        Bolinha bolinha = bingoService.sortearBola();

        return ResponseEntity.ok(bolinha);
    }


    @PostMapping("/marcar")
    public ResponseEntity<Boolean> marcarNumero(
            @RequestBody MarcarNumeroRequest request) {

        boolean marcado = bingoService.marcarNumeroNaCartela(
                request.cartelaId(),
                request.numero()
        );

        return ResponseEntity.ok(marcado);
    }


    @PostMapping("/gritar-bingo")
    public ResponseEntity<ResultadoBingoDTO> validarBingo(
            @RequestBody ValidarBingoRequest request) {

        ResultadoBingoDTO resultado = bingoService.validarVitoria(
                request.cartelaId()
        );

        return ResponseEntity.ok(resultado);
    }



    public record IniciarPartidaRequest(TipoVitoria tipoVitoria) {}

    public record CriarCartelaRequest(String nomeJogador) {}

    public record MarcarNumeroRequest(String cartelaId, int numero) {}

    public record ValidarBingoRequest(String cartelaId) {}
}
