package org.example.repositorios;

import org.example.tarjetas.Tarjeta;
import org.example.tarjetas.TarjetaColaborador;
import org.example.tarjetas.TarjetaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepoTarjetas {
    private List<Tarjeta> tarjetas;

    private static RepoTarjetas instancia = null;
    private RepoTarjetas() {
        this.tarjetas = new ArrayList<>();
    }

    public static RepoTarjetas getInstancia() {
        if (instancia == null) {
            RepoTarjetas.instancia = new RepoTarjetas();
        }
        return instancia;
    }

    public void agregarTodas(List<TarjetaHeladera> tarjeta) {
        this.tarjetas.addAll(tarjeta);
    }

    public void agregarTodasTarjetasColaboradores(List<TarjetaColaborador> tarjetaColaborador) {
        this.tarjetas.addAll(tarjetaColaborador);
    }

    public void agregar(Tarjeta tarjeta) {
        this.tarjetas.add(tarjeta);
    }

    public Tarjeta buscarTarjetaPorId(String id) {
        return this.tarjetas.stream()
                .filter((t) -> t.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
