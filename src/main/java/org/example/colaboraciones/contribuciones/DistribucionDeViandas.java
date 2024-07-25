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
    public void ejecutarContribucion() throws Exception {
        super.ejecutarContribucion();
        origen.notificarCambioViandas(0, cantidad);
        destino.notificarCambioViandas(cantidad, 0);
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA);
    }

    @Override
    public float getCoeficientePuntaje() {
        return 1;
    }

    @Override
    public float obtenerPuntaje(){
        return cantidad * this.getCoeficientePuntaje();
    }
}
