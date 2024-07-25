package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.VisitaHeladera;

import java.util.ArrayList;
import java.util.List;

public class RepositorioVisitasTecnicos {
    private List<VisitaHeladera> visitas;

    private static RepositorioVisitasTecnicos instancia = null;
    private RepositorioVisitasTecnicos() {
        this.visitas = new ArrayList<>();
    }

    public static RepositorioVisitasTecnicos getInstancia() {
        if (instancia == null) {
            RepositorioVisitasTecnicos.instancia = new RepositorioVisitasTecnicos();
        }
        return instancia;
    }

    public void agregarVisita(VisitaHeladera visita) {
        this.visitas.add(visita);
    }

    public void quitarVisita(VisitaHeladera visita) {
        this.visitas.remove(visita);
    }
}