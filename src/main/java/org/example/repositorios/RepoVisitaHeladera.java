package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.VisitaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepoVisitaHeladera {
    private List<VisitaHeladera> visitas;

    private static RepoVisitaHeladera instancia = null;
    private RepoVisitaHeladera() {
        this.visitas = new ArrayList<>();
    }

    public static RepoVisitaHeladera getInstancia() {
        if (instancia == null) {
            RepoVisitaHeladera.instancia = new RepoVisitaHeladera();
        }
        return instancia;
    }

    public void agregar(VisitaHeladera visita) {
        this.visitas.add(visita);
    }
}
