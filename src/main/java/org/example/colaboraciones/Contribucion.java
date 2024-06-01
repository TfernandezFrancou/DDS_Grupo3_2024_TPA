package org.example.colaboraciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Contribucion {
    private TipoDePersona tiposDePersona;
    private float coeficientePuntaje;


    public abstract void ejecutarContribucion();

    public abstract boolean puedeRealizarContribucion();


    public abstract float obtenerPuntaje();
}
