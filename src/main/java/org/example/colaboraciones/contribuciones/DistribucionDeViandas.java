package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.time.LocalDate;

@Getter
@Setter
public class DistribucionDeViandas extends Contribucion {
    private Heladera origen;
    private Heladera destino;
    private Integer cantidad;
    private String motivo;

    public DistribucionDeViandas(TipoDePersona tipo, LocalDate fecha, Integer cantidad) {
        this.setTiposDePersona(tipo);
        this.setFecha(fecha);
        this.cantidad = cantidad;
    }

    @Override
    public void ejecutarContribucion(){
        //TODO actualiza en la DB las viandas
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA);
    }

    @Override
    public float obtenerPuntaje(){
        return cantidad * this.getCoeficientePuntaje();
    }
}
