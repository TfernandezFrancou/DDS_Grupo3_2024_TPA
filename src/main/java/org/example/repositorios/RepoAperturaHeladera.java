package org.example.repositorios;

import org.example.tarjetas.AperturaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepoAperturaHeladera {
    private List<AperturaHeladera> aperturas;

    private static RepoAperturaHeladera instancia = null;

    private RepoAperturaHeladera() {
        this.aperturas = new ArrayList<>();
    }

    public static RepoAperturaHeladera getInstancia() {
        if (instancia == null) {
            RepoAperturaHeladera.instancia = new RepoAperturaHeladera();
        }
        return instancia;
    }

    public void agregar(AperturaHeladera apertura) {
        this.aperturas.add(apertura);
    }

    public void eliminar(AperturaHeladera apertura) {
        this.aperturas.remove(apertura);
    }
}
