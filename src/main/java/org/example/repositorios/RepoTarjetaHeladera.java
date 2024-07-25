package org.example.repositorios;

import org.example.tarjetas.TarjetaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepoTarjetaHeladera {
    private List<TarjetaHeladera> tarjetas;

    private static RepoTarjetaHeladera instancia = null;
    private RepoTarjetaHeladera() {
        this.tarjetas = new ArrayList<>();
    }

    public static RepoTarjetaHeladera getInstancia() {
        if (instancia == null) {
            RepoTarjetaHeladera.instancia = new RepoTarjetaHeladera();
        }
        return instancia;
    }

    public void agregarTodas(List<TarjetaHeladera> tarjeta) {
        this.tarjetas.addAll(tarjeta);
    }

    public void agregar(TarjetaHeladera tarjeta) {
        this.tarjetas.add(tarjeta);
    }

    public TarjetaHeladera buscarTarjetaPorId(String id) {
        return this.tarjetas.stream()
                .filter((t) -> t.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
