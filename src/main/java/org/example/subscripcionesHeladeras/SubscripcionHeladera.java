package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.Persona;

@Getter
@Setter
public abstract class SubscripcionHeladera {
    private Persona subscriptor;

    public abstract void notificar(Heladera heladera);
}
