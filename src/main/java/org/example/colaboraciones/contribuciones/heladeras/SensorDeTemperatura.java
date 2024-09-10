package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.broker.Broker;
import org.example.incidentes.Alerta;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Getter
@Setter
public class SensorDeTemperatura extends Sensor {
    private int temperatura;

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
