package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDeMovimiento extends Sensor {
    private boolean estaActivado;

    @Override
    public boolean getEstadoHeladera(){
        return !estaActivado;
    }
}
