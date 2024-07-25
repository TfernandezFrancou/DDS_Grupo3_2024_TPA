package org.example.repositorios;

import org.example.incidentes.Alerta;

import java.util.ArrayList;
import java.util.List;

public class RepoAlertas {

    private List<Alerta> alertas;

    private static RepoAlertas instancia = null;

    private RepoAlertas() {
        this.alertas = new ArrayList<>();
    }

    public static RepoAlertas getInstancia() {
        if (instancia == null) {
            RepoAlertas.instancia = new RepoAlertas();
        }
        return instancia;
    }

    public void agregarAlerta(Alerta alerta) {
        this.alertas.add(alerta);
    }

    public void eliminarAlerta(Alerta alerta) {
        this.alertas.remove(alerta);
    }

}
