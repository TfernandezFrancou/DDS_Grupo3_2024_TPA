package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

@Getter
@Setter
public class SubscripcionViandasDisponibles extends SubscripcionHeladera{
    private int cantidadDeViandas;

    @Override
    public void notificar(Heladera heladera)
    {
        //TODO
    }
}
