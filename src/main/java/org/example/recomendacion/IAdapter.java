package org.example.recomendacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.colaboraciones.Ubicacion;
import org.example.excepciones.ApiRequestFailedException;

import java.util.List;

public interface IAdapter {
    public List<Ubicacion> consultarUbicaciones(Zona zona) throws JsonProcessingException, ApiRequestFailedException;
}
