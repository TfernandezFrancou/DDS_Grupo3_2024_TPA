package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;

@Getter
@Setter
public abstract class SubscripcionHeladera {
    private Persona subscriptor;
    private MedioDeContacto medioDeContactoElegido;

    public abstract void notificar(Heladera heladera);
}
