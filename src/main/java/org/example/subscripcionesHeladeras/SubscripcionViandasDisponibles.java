package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.contacto.Mensaje;

import javax.mail.MessagingException;

@Getter
@Setter
public class SubscripcionViandasDisponibles extends SubscripcionHeladera {
    private int cantidadDeViandas;

    @Override
    public void notificar(Heladera heladera) throws MessagingException {
        String asunto = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.viandas-disponibles.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.viandas-disponibles.contenido")
            .replace("{cantidad}", String.valueOf(cantidadDeViandas))
            .replace("{heladera}", heladera.getNombre());
        Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
        medioDeContactoElegido.notificar(mensaje);
    }
}