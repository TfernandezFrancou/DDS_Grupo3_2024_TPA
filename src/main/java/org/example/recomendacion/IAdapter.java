package org.example.recomendacion;

import org.example.colaboraciones.Ubicacion;

import java.util.List;

public interface IAdapter {
    public List<Ubicacion> consultarUbicaciones(Zona zona);
}
