package org.example.personas;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.example.excepciones.ApiRequestFailedException;
import org.example.recomendacion.IAdapter;
import org.example.recomendacion.Zona;
import org.example.colaboraciones.Ubicacion;

import java.util.List;

@Getter
@Setter
public class PersonaJuridica extends Persona{
    private String razonSocial;
    private TipoJuridico tipo;
    private String rubro;
    private IAdapter recomendadorDeUbicaciones;


    public PersonaJuridica(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public List<Ubicacion> obtenerUbicaciones(Zona zona) throws JsonProcessingException, ApiRequestFailedException {
        return recomendadorDeUbicaciones.consultarUbicaciones(zona);
    }

    @Override
    public String getNombre() {
        return this.razonSocial;
    }
}