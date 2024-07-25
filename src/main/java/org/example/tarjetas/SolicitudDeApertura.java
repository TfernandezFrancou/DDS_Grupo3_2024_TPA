package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.roles.Colaborador;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudDeApertura {
//    private Colaborador colaborador;
//    private Contribucion contribucion;
    private Heladera heladera;
    private LocalDateTime fechaCreacion;
    private TarjetaColaborador tarjetaColaborador;

    public SolicitudDeApertura(Heladera heladera, LocalDateTime fechaCreacion, TarjetaColaborador tarjetaColaborador) {
        this.heladera = heladera;
        this.fechaCreacion = fechaCreacion;
        this.tarjetaColaborador = tarjetaColaborador;
    }
}
