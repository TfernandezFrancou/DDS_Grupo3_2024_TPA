package org.example.colaboraciones;

public abstract class Contribucion {
    private TipoDePersona tiposDePersona;

    public abstract void ejecutarContribucion();

    public boolean puedeRealizarContribucion(){
        // TODO puedeRealizarContribucion()
        return false;
    }
}
