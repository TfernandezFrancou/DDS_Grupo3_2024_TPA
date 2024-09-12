package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;

import javax.mail.MessagingException;

@Getter
@Setter
public class SubscripcionViandasFaltantes extends SubscripcionHeladera {
    private int cantidadDeViandas;

    public SubscripcionViandasFaltantes(Persona persona, MedioDeContacto medioDeContacto, int cantidad) {
        this.subscriptor = persona;
        this.medioDeContactoElegido = medioDeContacto;
        this.cantidadDeViandas = cantidad;
    }

    @Override
    public void notificar(Heladera heladera) throws MessagingException {
        String asunto = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.viandas-faltantes.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.viandas-faltantes.contenido")
            .replace("{cantidad}", String.valueOf(cantidadDeViandas))
            .replace("{heladera}", heladera.getNombre());
        Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
        medioDeContactoElegido.notificar(mensaje);
    }
}
