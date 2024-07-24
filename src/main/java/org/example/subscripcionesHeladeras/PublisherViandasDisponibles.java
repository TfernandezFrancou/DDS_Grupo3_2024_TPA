package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.util.List;

@Getter
@Setter
public class PublisherViandasDisponibles extends PublisherHeladera {
    private List<SubscripcionViandasDisponibles> subscripciones;

    @Override
    public void suscribir(SubscripcionHeladera subscripcion)
    {
        subscripciones.add((SubscripcionViandasDisponibles) subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera)
    {
        for(SubscripcionViandasDisponibles subscripcion : subscripciones){
            if(heladera.getViandasEnHeladera() == subscripcion.getCantidadDeViandas())
                subscripcion.notificar(heladera);
        }
    }


}
