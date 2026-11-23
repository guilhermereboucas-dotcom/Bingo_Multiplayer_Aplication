package br.edu.eda.bingo.service;

import br.edu.eda.bingo.model.Bolinha;
import br.edu.eda.bingo.model.Cartela;
import br.edu.eda.bingo.model.Partida;
import br.edu.eda.bingo.model.StatusPartida;
import br.edu.eda.bingo.model.TipoVitoria;
import br.edu.eda.bingo.dto.ResultadoBingoDTO;
import br.edu.eda.bingo.exception.RegraNegocioException;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BingoService {

    private Partida partidaAtual;

    private final SimpMessagingTemplate messagingTemplate;

    public BingoService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 1. Iniciar uma nova partida
    public synchronized Partida iniciarNovaPartida(TipoVitoria tipo) {

        Partida novaPartida = new Partida();

        novaPartida.setTipoVitoria(tipo);

        novaPartida.inicializarGlobo(75);

        novaPartida.setStatus(StatusPartida.EM_ANDAMENTO);

        this.partidaAtual = novaPartida;

        messagingTemplate.convertAndSend(
                "/topic/status-sala",
                partidaAtual
        );

        return partidaAtual;
    }

    // 2. Gerar cartela para um jogador
    public Cartela gerarCartelaParaJogador(String nomeJogador) {

        if (partidaAtual == null ||
                partidaAtual.getStatus() != StatusPartida.EM_ANDAMENTO) {

            throw new RegraNegocioException(
                    "Não existe uma partida em andamento."
            );
        }

        int[][] numeros = new int[5][5];

        int[] inicio = {1, 16, 31, 46, 61};

        for (int coluna = 0; coluna < 5; coluna++) {

            List<Integer> valores = new ArrayList<>();

            for (int i = inicio[coluna]; i < inicio[coluna] + 15; i++) {
                valores.add(i);
            }

            Collections.shuffle(valores);

            int quantidade = (coluna == 2) ? 4 : 5;

            int indice = 0;

            for (int linha = 0; linha < 5; linha++) {

                if (linha == 2 && coluna == 2) {
                    numeros[linha][coluna] = 0;
                    continue;
                }

                numeros[linha][coluna] = valores.get(indice);

                indice++;

            }
        }

        Cartela cartela = new Cartela(nomeJogador, numeros);

        partidaAtual.registrarCartela(cartela);

        return cartela;
    }

    // 3. Sortear uma bola
    public synchronized Bolinha sortearBola() {

        if (partidaAtual == null ||
                partidaAtual.getStatus() != StatusPartida.EM_ANDAMENTO) {

            throw new RegraNegocioException(
                    "Não existe uma partida em andamento."
            );
        }

        Integer numero = partidaAtual.extrairProximaBola();

        if (numero == null) {

            partidaAtual.setStatus(StatusPartida.FINALIZADA);

            throw new RegraNegocioException(
                    "Todas as bolas já foram sorteadas."
            );
        }

        int sequencia = partidaAtual.getHistoricoSorteados().size();

        Bolinha bolinha = new Bolinha(numero, sequencia);

        partidaAtual.setUltimaBola(bolinha);

        messagingTemplate.convertAndSend(
                "/topic/bola-sorteada",
                bolinha
        );

        return bolinha;
    }

    // 4. Marcar número na cartela
    public boolean marcarNumeroNaCartela(String cartelaId, int numero) {

        if (partidaAtual == null ||
                partidaAtual.getStatus() != StatusPartida.EM_ANDAMENTO) {

            throw new RegraNegocioException(
                    "Não existe uma partida em andamento."
            );
        }

        if (!partidaAtual.bolaJaFoiSorteada(numero)) {

            throw new RegraNegocioException(
                    "O número " + numero + " ainda não foi sorteado."
            );
        }

        Cartela cartela = partidaAtual.buscarCartela(cartelaId);

        if (cartela == null) {

            throw new RegraNegocioException(
                    "Cartela não encontrada."
            );
        }

        return cartela.marcarNumero(numero);
    }

    // 5. Validar vitória
    public synchronized ResultadoBingoDTO validarVitoria(String cartelaId) {

        if (partidaAtual == null ||
                partidaAtual.getStatus() != StatusPartida.EM_ANDAMENTO) {

            throw new RegraNegocioException(
                    "Não existe uma partida em andamento."
            );
        }

        Cartela cartela = partidaAtual.buscarCartela(cartelaId);

        if (cartela == null) {

            throw new RegraNegocioException(
                    "Cartela não encontrada."
            );
        }

        // Sincroniza as marcações com o histórico oficial
        for (Integer numero : partidaAtual.getHistoricoSorteados()) {

            if (cartela.possuiNumero(numero)) {
                cartela.marcarNumero(numero);
            }
        }

        boolean venceu = cartela.verificarVitoria(
                partidaAtual.getTipoVitoria()
        );

        if (venceu) {

            partidaAtual.registrarVencedor(cartela);

            partidaAtual.setStatus(StatusPartida.FINALIZADA);

            ResultadoBingoDTO resultado = new ResultadoBingoDTO(
                    true,
                    "BINGO! " + cartela.getNomeJogador() + " venceu!"
            );

            messagingTemplate.convertAndSend(
                    "/topic/vencedor",
                    resultado
            );

            messagingTemplate.convertAndSend(
                    "/topic/status-sala",
                    partidaAtual
            );

            return resultado;
        }

        return new ResultadoBingoDTO(
                false,
                "Cartela ainda não completou o padrão de vitória."
        );
    }
}