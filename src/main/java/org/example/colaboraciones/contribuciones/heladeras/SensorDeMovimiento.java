package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.broker.Broker;
import org.example.incidentes.Alerta;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Getter
@Setter
public class SensorDeMovimiento extends Sensor {
    private boolean estaActivado;

    @Override
    public boolean getEstadoHeladera() throws MessagingException {
        if(estaActivado){
            this.emitirAlerta("Fraude");//emite la alerta por fraude
        }

        return !estaActivado;
    }
}
