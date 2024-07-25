package org.example.colaboraciones.contribuciones.heladeras;

import org.example.incidentes.Incidente;

import java.time.LocalDate;

public class VisitaHeladera {
    private LocalDate fechaVisita;
    private String trabajoRealizado;
    private String foto;
    private Boolean incidenteSolucionado;
    private Boolean trabajoCompletado;
    private Incidente incidente;

    public VisitaHeladera(LocalDate fechaVisita, String trabajoRealizado, String foto, Boolean incidenteSolucionado, Boolean trabajoCompletado, Incidente incidente) {
        this.fechaVisita = fechaVisita;
        this.trabajoRealizado = trabajoRealizado;
        this.foto = foto;
        this.incidenteSolucionado = incidenteSolucionado;
        this.trabajoCompletado = trabajoCompletado;
        this.incidente = incidente;
    }
}