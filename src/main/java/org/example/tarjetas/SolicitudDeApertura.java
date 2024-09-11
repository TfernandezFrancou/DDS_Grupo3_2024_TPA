package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudDeApertura {

    private Heladera heladera;
    private LocalDateTime fechaCreacion;
    private TarjetaColaborador tarjetaColaborador;

    public SolicitudDeApertura(Heladera heladera, LocalDateTime fechaCreacion, TarjetaColaborador tarjetaColaborador) {
        this.heladera = heladera;
        this.fechaCreacion = fechaCreacion;
        this.tarjetaColaborador = tarjetaColaborador;
    }
}
