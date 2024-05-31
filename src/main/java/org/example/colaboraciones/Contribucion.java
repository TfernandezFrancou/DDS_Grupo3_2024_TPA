package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;

public abstract class Contribucion {
    @Getter
    @Setter
    private TipoDePersona tiposDePersona;

    public abstract void ejecutarContribucion();

    public abstract boolean puedeRealizarContribucion();

}
