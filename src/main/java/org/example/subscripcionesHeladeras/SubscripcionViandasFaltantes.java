package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.contacto.Mensaje;

import javax.mail.MessagingException;

@Getter
@Setter
public class SubscripcionViandasFaltantes extends SubscripcionHeladera {
    private int cantidadDeViandas;

    @Override
    public void notificar(Heladera heladera)
    {
        try {
            String asunto = "Faltan Viandas";
            String contenido = "Faltan " + cantidadDeViandas + " para que se llene la heladera " + heladera.getNombre();
            Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
            medioDeContactoElegido.notificar(mensaje);
        } catch (MessagingException exception) {
        }
    }
}
