package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.util.ArrayList;
import java.util.List;

public class RepoHeladera {
    private List<Heladera> heladeras;

    private static RepoHeladera instancia = null;
    private RepoHeladera() {
        this.heladeras = new ArrayList<>();
    }

    public static RepoHeladera getInstancia() {
        if (instancia == null) {
            RepoHeladera.instancia = new RepoHeladera();
        }
        return instancia;
    }

    public void agregarHeladera(Heladera heladera) {
        this.heladeras.add(heladera);
    }

    public void eliminarHeladera(Heladera heladera) {
        this.heladeras.remove(heladera);
    }

    public List<Heladera> buscarHeladerasActivas() {
        return heladeras.stream().filter(Heladera::estaActiva).toList();
    }
}