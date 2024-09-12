package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PublisherViandasDisponibles extends PublisherHeladera {
    private List<SubscripcionViandasDisponibles> subscripciones;

    public PublisherViandasDisponibles() {
        this.subscripciones = new ArrayList<>();
    }

    @Override
    public void suscribir(SubscripcionHeladera subscripcion) {
        subscripciones.add((SubscripcionViandasDisponibles) subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera) throws MessagingException {
        for(SubscripcionViandasDisponibles subscripcion : subscripciones){
            if(heladera.getViandasEnHeladera() == subscripcion.getCantidadDeViandas())
                subscripcion.notificar(heladera);
        }
    }
}