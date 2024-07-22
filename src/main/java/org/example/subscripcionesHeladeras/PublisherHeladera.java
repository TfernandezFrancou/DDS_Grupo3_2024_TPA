package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

@Getter
@Setter
public abstract class PublisherHeladera {
    public abstract void notificarATodos(Heladera heladera);
    public abstract void suscribir(SubscripcionHeladera subscripcionHeladera);
}
