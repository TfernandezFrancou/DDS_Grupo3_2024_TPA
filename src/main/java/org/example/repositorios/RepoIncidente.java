package org.example.repositorios;

import org.example.incidentes.Incidente;

import java.util.ArrayList;
import java.util.List;

public class RepoIncidente {
    private List<Incidente> incidentes;

    private static RepoIncidente instancia = null;

    private RepoIncidente() {
        this.incidentes = new ArrayList<>();
    }

    public static RepoIncidente getInstancia() {
        if (instancia == null) {
            RepoIncidente.instancia = new RepoIncidente();
        }
        return instancia;
    }

    public void agregar(Incidente incidente) {
        this.incidentes.add(incidente);
    }

    public void eliminar(Incidente incidente) {
        this.incidentes.remove(incidente);
    }
}