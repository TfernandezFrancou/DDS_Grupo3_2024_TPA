package org.example.repositorios;

import org.example.contribuciones.heladeras.TarjetaHeladera;

import java.util.List;

public class RepoTarjetaHeladera {
    private List<TarjetaHeladera> tarjetas;

    public void agregarTarjeta(TarjetaHeladera tarjeta) {
        this.tarjetas.add(tarjeta);
    }

    public TarjetaHeladera buscarTarjetaPorId(String id) {
        return this.tarjetas.stream()
                .filter((t) -> t.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }
}
