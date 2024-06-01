package org.example.migracion.estrategiasColaboracion;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.RegistrarPersonasEnSituacionVulnerable;

public class EstrategiaEntregarTarjetas extends EstrategiaColaboracion{
    @Override
    public Contribucion crearContribucion(Integer cantidad) {
        RegistrarPersonasEnSituacionVulnerable contribucion = new RegistrarPersonasEnSituacionVulnerable();
        contribucion.setTarjetasEntregadas(cantidad);
        return contribucion;
    }
}
