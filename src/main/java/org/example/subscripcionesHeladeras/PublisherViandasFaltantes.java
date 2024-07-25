package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.mail.MessagingException;
import java.util.List;

@Getter
@Setter
public class PublisherViandasFaltantes extends PublisherHeladera{
    private List<SubscripcionViandasFaltantes> subscripciones;

    @Override
    public void suscribir(SubscripcionHeladera subscripcion) {
        subscripciones.add((SubscripcionViandasFaltantes) subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera) throws MessagingException {
        for(SubscripcionViandasFaltantes subscripcion : subscripciones){
            if(heladera.faltanteParaLlenar() == subscripcion.getCantidadDeViandas())
                subscripcion.notificar(heladera);
        }
    }
}
