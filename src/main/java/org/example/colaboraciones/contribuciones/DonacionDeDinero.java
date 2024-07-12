package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;

import java.time.LocalDate;

@Getter
@Setter
public class DonacionDeDinero extends Contribucion {
    private float monto;
    private Integer frecuencia;

    public DonacionDeDinero(TipoDePersona tipo, LocalDate fecha, Integer cantidad) {
        this.setTiposDePersona(tipo);
        this.setFecha(fecha);
        this.monto = cantidad;
    }

    @Override
    public void ejecutarContribucion(){
        //TODO guarda en la DB el dinero
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA)
                    || this.getTiposDePersona().equals(TipoDePersona.JURIDICA);
    }

    @Override
    public float obtenerPuntaje(){
        return monto  * this.getCoeficientePuntaje();
    }

}
