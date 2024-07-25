package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.AperturaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAperturasHeladera {
    private List<AperturaHeladera> aperturas;

    private static RepositorioAperturasHeladera instancia = null;

    private RepositorioAperturasHeladera() {
        this.aperturas = new ArrayList<>();
    }

    public static RepositorioAperturasHeladera getInstancia() {
        if (instancia == null) {
            RepositorioAperturasHeladera.instancia = new RepositorioAperturasHeladera();
        }
        return instancia;
    }

    public void agregarApertura(AperturaHeladera apertura) {
        this.aperturas.add(apertura);
    }

    public void quitarApertura(AperturaHeladera apertura) {
        this.aperturas.remove(apertura);
    }
}
