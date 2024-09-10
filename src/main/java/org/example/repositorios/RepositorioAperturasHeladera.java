package org.example.repositorios;

import lombok.Getter;
import org.example.tarjetas.AperturaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepositorioAperturasHeladera {
    @Getter
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

    public void clean(){
        this.aperturas.clear();
    }
}
