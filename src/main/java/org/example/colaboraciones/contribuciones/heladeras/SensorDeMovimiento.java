package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.excepciones.EmailNoRegistradoException;

import javax.mail.MessagingException;
import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class SensorDeMovimiento extends Sensor {
    @Column(columnDefinition = "INT")
    private boolean estaActivado;

    @Override
    public boolean getEstadoHeladera() throws MessagingException, EmailNoRegistradoException {
        if(estaActivado){
            this.emitirAlerta("Fraude");//emite la alerta por fraude
        }

        return !estaActivado;
    }
}
