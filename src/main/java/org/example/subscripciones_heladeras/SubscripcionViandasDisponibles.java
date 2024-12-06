package org.example.subscripciones_heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoMensajes;

import javax.mail.MessagingException;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class SubscripcionViandasDisponibles extends SubscripcionHeladera {
    private int cantidadDeViandas;

    public SubscripcionViandasDisponibles(Heladera heladera, Persona persona, MedioDeContacto medioDeContacto, int cantidad) {
        this.setHeladera(heladera);
        this.subscriptor = persona;
        this.medioDeContactoElegido = medioDeContacto;
        this.cantidadDeViandas = cantidad;
    }

    public SubscripcionViandasDisponibles() {
    }

    @Override
    public void notificar(Heladera heladera) throws MessagingException {
        String asunto = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.viandas-disponibles.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.viandas-disponibles.contenido")
            .replace("{cantidad}", String.valueOf(cantidadDeViandas))
            .replace("{heladera}", heladera.getNombre());
        Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
        medioDeContactoElegido.notificar(mensaje);
        RepoMensajes.getInstancia().agregarMensaje(mensaje);
    }
}