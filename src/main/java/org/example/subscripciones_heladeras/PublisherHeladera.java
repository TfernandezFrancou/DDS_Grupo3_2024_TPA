package org.example.subscripciones_heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.mail.MessagingException;

@Getter
@Setter
public abstract class PublisherHeladera {

    public abstract void notificarATodos(Heladera heladera) throws MessagingException;
    public abstract void suscribir(SubscripcionHeladera subscripcionHeladera);
}
