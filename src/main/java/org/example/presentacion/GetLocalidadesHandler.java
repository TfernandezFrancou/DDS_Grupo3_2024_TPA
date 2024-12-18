package org.example.presentacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.example.presentacion.dtos.LocalidadDTO;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.personas.PersonaHumana;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.repositorios.RepoPersona;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetLocalidadesHandler  implements Handler {

    private final RepoPersona repoPersona;

    public GetLocalidadesHandler() {
        this.repoPersona = RepoPersona.getInstancia();
    }

    @Override
    public void handle(@NotNull Context context) {

        List<PersonaHumana> personasEnSituacionDeCalle = repoPersona.obtenerPersonasEnSituacionVulnerable();


        List<LocalidadDTO> respuesta = new ArrayList<>();

        for (PersonaHumana persona : personasEnSituacionDeCalle) {


            PersonaEnSituacionVulnerable rol = (PersonaEnSituacionVulnerable) persona.getRol();
            if(rol.getTarjetaHeladera() != null){
                List<Uso> usos = rol.getTarjetaHeladera().getUsos();

                for (Uso uso : usos) {

                    Heladera heladera = uso.getHeladera();
                    Direccion direccion = heladera.getDireccion();

                    String nombreLocalidad = direccion.getLocalidad();
                    agregarLocalidad(persona, respuesta, nombreLocalidad);

                }
            }
        }

        respuesta.forEach(localidadDTO -> {
                    int cantidadDePersonas = localidadDTO.getNombresYApellidosDePersonas().size();
                    localidadDTO.setCantidadDePersonas(cantidadDePersonas);
                });

        context.json(respuesta);

    }

    private static void agregarLocalidad(PersonaHumana persona, List<LocalidadDTO> respuesta, String nombreLocalidad) {
        //busco si ya se registro la localiad
        Optional<LocalidadDTO> localidadExistente = respuesta.stream()
                .filter(localidadDTO1 -> localidadDTO1.getNombreLocalidad().equals(nombreLocalidad))
                .findFirst();

        if(localidadExistente.isPresent()){
            LocalidadDTO localidadDTO1 = localidadExistente.get();

            //si no exite lo agrego
            if(!localidadDTO1.getNombresYApellidosDePersonas().contains(persona.getNombre())){
                localidadDTO1.agregarPersona(persona.getNombre());//= nombre + ' ' + apellido
            }

        }else {
            LocalidadDTO localidadDTO = new LocalidadDTO();
            localidadDTO.setNombreLocalidad(nombreLocalidad);
            localidadDTO.agregarPersona(persona.getNombre());//= nombre + ' ' + apellido
            respuesta.add(localidadDTO);
        }
    }

}
