package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDateTime;

@Getter
@Setter
public class Apertura {
    private int idApertura;
    private  TarjetaColaborador tarjeta;
    private Heladera heladera;
    private LocalDateTime fechahoraCreacion;

    private TipoDeApertura tipoDeApertura;

    public Apertura(TarjetaColaborador tarjeta, Heladera heladera, LocalDateTime fechahoraCreacion, TipoDeApertura tipoDeApertura) {
        this.tarjeta = tarjeta;
        this.heladera = heladera;
        this.fechahoraCreacion = fechahoraCreacion;
        this.tipoDeApertura = tipoDeApertura;
    }
}
