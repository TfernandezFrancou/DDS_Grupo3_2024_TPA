package org.example.migracion.estrategiasColaboracion;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;

public class EstrategiaDistribucionDeViandas extends EstrategiaColaboracion{
    @Override
    public Contribucion crearContribucion(Integer cantidad) {
        DistribucionDeViandas contribucion = new DistribucionDeViandas();
        contribucion.setCantidad(cantidad);
        return contribucion;
    }
}
