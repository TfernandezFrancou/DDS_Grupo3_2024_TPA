package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDeTemperatura extends Sensor {
    private int temperatura;

    @Override
    public boolean getEstadoHeladera(){
        TemperaturaHeladera temperaturaHeladera = super.getHeladera().getTemperaturasDeFuncionamiento();
        return temperaturaHeladera.getTemperaturaMaxima() > temperatura
                    && temperaturaHeladera.getTemperaturaMinima() <= temperatura;
    }
}
