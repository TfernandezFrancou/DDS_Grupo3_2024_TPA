package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;

import java.time.LocalDate;

@Getter
@Setter
public class DonacionDeDinero extends Contribucion {
    private LocalDate fecha;
    private Integer monto;
    private Integer frecuencia;

    @Override
    public void ejecutarContribucion(){
        //TODO guarda en la DB el dinero
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA)
                    || this.getTiposDePersona().equals(TipoDePersona.JURIDICA);
    }

}
