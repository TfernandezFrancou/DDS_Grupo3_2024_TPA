package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import java.util.List;

@Getter
@Setter
public class PublisherDesperfecto extends PublisherHeladera{
    private List<SubscripcionDesperfecto> subscripciones;

    @Override
    public void suscribir(SubscripcionHeladera subscripcion)
    {
        subscripciones.add((SubscripcionDesperfecto) subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera)
    {
        for(SubscripcionDesperfecto subscripcion : subscripciones)
        {
            subscripcion.notificar(heladera);
        }
    }
}
