package org.example.colaboraciones.contribuciones.heladeras;

import org.example.personas.Persona;

import java.time.LocalDateTime;

public class AperturaHeladera {
    private Heladera heladera;
    private Persona colaborador;
    private LocalDateTime horarioApertura;

    public AperturaHeladera(Heladera heladera, Persona colaborador, LocalDateTime horarioApertura) {
        this.heladera = heladera;
        this.colaborador = colaborador;
        this.horarioApertura = horarioApertura;
    }
}