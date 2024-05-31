package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;

public abstract class Sensor {
    @Getter
    private Heladera heladera;

    public void agregar(Heladera heladera) {
        this.heladera = heladera;
    }

    public void quitar(Heladera heladera) {
        this.heladera = null;
    }

    public void notificar(){
        heladera.actualizarEstadoHeladera(this);
    }

    public abstract boolean getEstadoHeladera();
}
