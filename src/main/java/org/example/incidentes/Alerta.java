package org.example.incidentes;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@Getter
@Setter
public class Alerta extends Incidente {
    private String tipoDeAlerta;

    public Alerta(String tipoDeAlerta, Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) throws MessagingException {
        super(heladera, tipoDeIncidente, fechaDeEmision);
        this.tipoDeAlerta = tipoDeAlerta;
    }
}
