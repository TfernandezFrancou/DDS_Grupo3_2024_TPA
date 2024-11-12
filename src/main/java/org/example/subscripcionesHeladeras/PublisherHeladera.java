package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.intellij.lang.annotations.JdkConstants;

import javax.mail.MessagingException;
import javax.persistence.*;

@Getter
@Setter
public abstract class PublisherHeladera {

    public abstract void notificarATodos(Heladera heladera) throws MessagingException;
    public abstract void suscribir(SubscripcionHeladera subscripcionHeladera);
}
