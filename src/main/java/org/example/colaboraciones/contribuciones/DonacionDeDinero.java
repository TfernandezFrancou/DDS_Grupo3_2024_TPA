package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;

import java.time.LocalDate;

@Getter
@Setter
public class DonacionDeDinero extends Contribucion {
    private LocalDate fecha;
    private Integer monto;
    private Integer frecuencia;

    @Override
    public void ejecutarContribucion(){
    //TODO
    }

    @Override
    public boolean puedeRealizarContribucion() {
        //TODO
        return false;
    }

}
