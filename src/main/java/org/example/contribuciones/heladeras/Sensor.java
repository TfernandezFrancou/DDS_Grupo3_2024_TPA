package org.example.contribuciones.heladeras;

public abstract class Sensor {
    private Heladera heladera;

    public void agregar(Heladera heladera) {
        this.heladera = heladera;
    }

    public void quitar(Heladera heladera) {
        this.heladera = null;
    }

    private void notificar() {
        // TODO
    }
}
