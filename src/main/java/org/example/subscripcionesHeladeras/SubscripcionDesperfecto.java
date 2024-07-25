package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.contacto.Mensaje;

import javax.mail.MessagingException;

@Getter
@Setter
public class SubscripcionDesperfecto extends SubscripcionHeladera {

    @Override
    public void notificar(Heladera heladera) throws MessagingException {
        String asunto = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.desperfecto.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.desperfecto.contenido")
            .replace("{heladera}", heladera.getNombre());
        Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
        medioDeContactoElegido.notificar(mensaje);
    }
}
