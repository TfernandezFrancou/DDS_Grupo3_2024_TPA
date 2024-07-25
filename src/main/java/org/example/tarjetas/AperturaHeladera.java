package org.example.tarjetas;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDateTime;

public class AperturaHeladera {
    private Heladera heladera;
    private TarjetaColaborador tarjeta;
    private LocalDateTime horarioApertura;
    //private Persona colaborador;

    public AperturaHeladera(Heladera heladera, TarjetaColaborador tarjeta, LocalDateTime horarioApertura) {
        this.heladera = heladera;
        this.tarjeta = tarjeta;
        this.horarioApertura = horarioApertura;
    }
}