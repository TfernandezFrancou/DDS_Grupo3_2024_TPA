package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoPersona;

public abstract class ContribucionController {

    /*public static void actualizarPuntajeUsuarioActual(Context context, Contribucion contribucion){
        Persona colaboradorPersona = obtenerPersonaColaboradorActual(context);
        Colaborador colaborador = RepoPersona.getInstancia().getRolColaboradorById(colaboradorPersona.getRol().getIdrol());

        colaborador.agregarContribucion(contribucion);//se persiste la contribucion
        colaborador.calcularPuntuaje();
        colaboradorPersona.setRol(colaborador);//update
        colaboradorPersona = RepoPersona.getInstancia().actualizarPersona(colaboradorPersona);
        actualizarPersonaColaboradorActual(context, colaboradorPersona);
    }*/

    public static Colaborador obtenerRolColaboradorActual(Context context){
        Persona colaboradorPersona = obtenerPersonaColaboradorActual(context);
        RepoPersona.getInstancia().actualizarPersona(colaboradorPersona);
        return RepoPersona.getInstancia().getRolColaboradorById(colaboradorPersona.getRol().getIdrol());
    }

    private static Persona obtenerPersonaColaboradorActual(Context context){
        Usuario user = context.attribute("usuario");
        Persona personaUser = user.getColaborador();
        personaUser = RepoPersona.getInstancia().buscarPorId(personaUser.getIdPersona());
        if(personaUser.getRol() == null){
            Colaborador colaboradorRol = new Colaborador();
            colaboradorRol.setEstaActivo(true);
            personaUser.setRol(colaboradorRol);
            // Actualiza persona para obtener id del rol
            personaUser = RepoPersona.getInstancia().actualizarPersona(personaUser);
        } else if(!(personaUser.getRol() instanceof Colaborador)){
            throw new RuntimeException("Esta funcionalidad está disponible solo para personas con el rol de colaborador");
        }

        user.setColaborador(personaUser);//update
        return personaUser;
    }

}