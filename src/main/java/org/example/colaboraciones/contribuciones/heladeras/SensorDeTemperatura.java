package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

import javax.mail.MessagingException;

@Getter
@Setter
public class SensorDeTemperatura extends Sensor {
    private int temperatura;

    @Override
    public void notificar() throws MessagingException {
        super.notificar();
        this.getHeladera().setTemperaturaActualHeladera(temperatura);
    }
    @Override
    public boolean getEstadoHeladera() throws MessagingException {
        TemperaturaHeladera temperaturaHeladera = super.getHeladera().getTemperaturasDeFuncionamiento();

        if(temperatura > temperaturaHeladera.getTemperaturaMaxima()
                ||temperatura < temperaturaHeladera.getTemperaturaMinima()
        ){
            this.emitirAlerta("Temperatura");//emite alerta por falla de temperatura
        }

        return temperaturaHeladera.getTemperaturaMaxima() > temperatura
                    && temperaturaHeladera.getTemperaturaMinima() <= temperatura;
    }
}
