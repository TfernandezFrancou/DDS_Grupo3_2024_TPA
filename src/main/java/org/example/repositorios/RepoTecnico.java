package org.example.repositorios;

import org.example.personas.Tecnico;

import java.util.List;

public class RepoTecnico {
    private List<Tecnico> tecnicos;

    public void agregarTecnico(Tecnico tecnico) {
        this.tecnicos.add(tecnico);
    }

    public void eliminarTecnico(Tecnico tecnico) {
        this.tecnicos.remove(tecnico);
    }
}