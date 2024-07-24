package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.mail.MessagingException;

@Getter
@Setter
public class SubscripcionViandasDisponibles extends SubscripcionHeladera {
    private int cantidadDeViandas;

    @Override
    public void notificar(Heladera heladera) {
        try {
            this.getMedioDeContactoElegido().notificar("Viandas disponibles", "Quedan "
                    + cantidadDeViandas + " viandas disponibles en la heladera: " + heladera.getNombre());
        } catch (MessagingException exception) {

        }
    }
}