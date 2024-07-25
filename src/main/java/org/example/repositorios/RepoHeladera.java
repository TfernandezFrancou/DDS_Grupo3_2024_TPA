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

    public void agregarTodas(List<Heladera> heladeras) {
        this.heladeras.addAll(heladeras);
    }

    public void agregar(Heladera heladera) {
        this.heladeras.add(heladera);
    }

    public void actualizar(Heladera heladera) {
    }

    public void eliminar(Heladera heladera) {
        this.heladeras.remove(heladera);
    }

    public List<Heladera> buscarHeladerasActivas() {
        return heladeras.stream().filter(Heladera::estaActiva).toList();
    }

    public List<Heladera> buscarHeladerasCercanasA(Heladera heladera) {
        // TODO:
        return null;
    }
}