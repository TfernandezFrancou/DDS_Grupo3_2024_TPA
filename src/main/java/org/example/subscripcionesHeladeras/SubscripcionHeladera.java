package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;

import javax.mail.MessagingException;

@Getter
@Setter
public abstract class SubscripcionHeladera {
    protected Persona subscriptor;
    protected MedioDeContacto medioDeContactoElegido;

    public abstract void notificar(Heladera heladera) throws MessagingException;
}
