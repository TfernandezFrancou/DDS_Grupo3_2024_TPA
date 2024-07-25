package org.example.incidentes;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDateTime;

public abstract class Incidente {
    private Heladera heladera;
    private String tipoDeIncidente;
    private LocalDateTime fechaDeEmision;

    public void avisarATecnico() {
        // TODO:
    }
}
