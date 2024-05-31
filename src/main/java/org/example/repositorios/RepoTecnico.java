package org.example.repositorios;

import org.example.personas.Tecnico;

import java.util.ArrayList;
import java.util.List;

public class RepoTecnico {
    private List<Tecnico> tecnicos;

    private static RepoTecnico instancia = null;
    private RepoTecnico() {
        this.tecnicos = new ArrayList<>();
    }

    public static RepoTecnico getInstancia() {
        if (instancia == null) {
            RepoTecnico.instancia = new RepoTecnico();
        }
        return instancia;
    }

    public void agregarTecnico(Tecnico tecnico) {
        this.tecnicos.add(tecnico);
    }

    public void eliminarTecnico(Tecnico tecnico) {
        this.tecnicos.remove(tecnico);
    }
}