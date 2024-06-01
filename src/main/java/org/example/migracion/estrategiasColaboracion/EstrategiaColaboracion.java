package org.example.migracion.estrategiasColaboracion;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;

public abstract class EstrategiaColaboracion {

    public abstract Contribucion crearContribucion(Integer cantidad);

    public Contribucion cargarColaboracion(Integer cantidad){
        Contribucion contribucionAGuardar = this.crearContribucion(cantidad);
        contribucionAGuardar.setTiposDePersona(TipoDePersona.HUMANA);
        return contribucionAGuardar;
    }
}
