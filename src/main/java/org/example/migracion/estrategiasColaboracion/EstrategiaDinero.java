package org.example.migracion.estrategiasColaboracion;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;

public class EstrategiaDinero extends EstrategiaColaboracion {
    @Override
    public Contribucion crearContribucion(Integer cantidad) {
        DonacionDeDinero contribucion = new DonacionDeDinero();
        contribucion.setMonto(cantidad);
        return contribucion;
    }
}
