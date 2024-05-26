package org.example.repositorios;

import org.example.contribuciones.heladeras.Heladera;

import java.util.List;

public class RepoHeladera {
    private List<Heladera> heladeras;

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