package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

@Getter
@Setter
public class SubscripcionDesperfecto extends SubscripcionHeladera {

    @Override
    public void notificar(Heladera heladera) {
        //TODO notificar
        try {
        } catch (Exception exception) {

        }
    }
}
