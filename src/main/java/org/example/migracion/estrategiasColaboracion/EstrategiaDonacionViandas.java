package org.example.migracion.estrategiasColaboracion;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;

public class EstrategiaDonacionViandas extends EstrategiaColaboracion{
    @Override
    public Contribucion crearContribucion(Integer cantidad) {
        DonacionDeViandas contribucion = new DonacionDeViandas();
        contribucion.setCantidadDeViandas(cantidad);
        return contribucion;
    }
}
