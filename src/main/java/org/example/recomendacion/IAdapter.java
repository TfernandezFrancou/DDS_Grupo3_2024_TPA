package org.example.recomendacion;

import org.example.Ubicacion;

import java.util.List;

public interface IAdapter {
    public List<Ubicacion> consultarUbicaciones(Zona zona);
}
