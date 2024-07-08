package org.example.personas;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.example.excepciones.ApiRequestFailedException;
import org.example.personas.contacto.MedioDeContacto;
import org.example.recomendacion.IAdapter;
import org.example.recomendacion.Zona;
import org.example.colaboraciones.Ubicacion;

import java.util.List;

@Getter
@Setter
public class PersonaJuridica extends Colaborador{
    private String razonSocial;
    private TipoJuridico tipo;
    private String rubro;
    private IAdapter recomendadorDeUbicaciones;


    public PersonaJuridica() {
    }
    public PersonaJuridica(List<MedioDeContacto> mediosDeContacto) {
        super(mediosDeContacto);
    }

    public List<Ubicacion> obtenerUbicaciones(Zona zona) throws JsonProcessingException, ApiRequestFailedException {
        return recomendadorDeUbicaciones.consultarUbicaciones(zona);
    }

    @Override
    public String getNombre() {
        return razonSocial;
    }
}
