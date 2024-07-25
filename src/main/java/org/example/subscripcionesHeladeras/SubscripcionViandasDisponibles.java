package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.contacto.Mensaje;

import javax.mail.MessagingException;

@Getter
@Setter
public class SubscripcionViandasDisponibles extends SubscripcionHeladera {
    private int cantidadDeViandas;

    @Override
    public void notificar(Heladera heladera) {
        try {
            String asunto = "Viandas Disponibles";
            String contenido = "Quedan " + cantidadDeViandas + " viandas disponibles en la heladera " + heladera.getNombre();
            Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
            medioDeContactoElegido.notificar(mensaje);
        } catch (MessagingException exception) {
        }
    }
}