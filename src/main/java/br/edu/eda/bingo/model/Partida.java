package br.edu.eda.bingo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.UUID;

public class Partida {

    private final String id;
    private StatusPartida status;
    private TipoVitoria tipoVitoria;
    private final Queue<Integer> globo;
    private final NavigableSet<Integer> historicoSorteados;
    private Bolinha ultimaBola;
    private final Map<String, Cartela> cartelas;
    private final List<Cartela> vencedores;

    public Partida() {
        this.id = UUID.randomUUID().toString();
        this.status = StatusPartida.AGUARDANDO_JOGADORES;
        this.tipoVitoria = TipoVitoria.CARTELA_CHEIA;
        this.globo = new LinkedList<>();
        this.historicoSorteados = new TreeSet<>();
        this.cartelas = new ConcurrentHashMap<>();
        this.vencedores = new CopyOnWriteArrayList<>();
    }

    public void inicializarGlobo(int maxBolas) {

        List<Integer> numeros = new ArrayList<>();

        for (int i = 1; i <= maxBolas; i++) {
            numeros.add(i);
        }

        Collections.shuffle(numeros);

        globo.clear();
        globo.addAll(numeros);
    }

    public Integer extrairProximaBola() {

        Integer numero = globo.poll();

        if (numero != null) {
            historicoSorteados.add(numero);
        }

        return numero;
    }

    public boolean bolaJaFoiSorteada(int numero) {
        return historicoSorteados.contains(numero);
    }

    public void registrarCartela(Cartela cartela) {
        cartelas.put(cartela.getId(), cartela);
    }

    public void setTipoVitoria(TipoVitoria tipoVitoria) {
        this.tipoVitoria = tipoVitoria;
    }

    public void setStatus(StatusPartida status) {
        this.status = status;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public TipoVitoria getTipoVitoria() {
        return tipoVitoria;
    }

    public NavigableSet<Integer> getHistoricoSorteados() {
        return historicoSorteados;
    }

    public void setUltimaBola(Bolinha ultimaBola) {
        this.ultimaBola = ultimaBola;
    }

    public Cartela buscarCartela(String cartelaId) {
        return cartelas.get(cartelaId);
    }

    public void registrarVencedor(Cartela cartela) {
        vencedores.add(cartela);
    }
}