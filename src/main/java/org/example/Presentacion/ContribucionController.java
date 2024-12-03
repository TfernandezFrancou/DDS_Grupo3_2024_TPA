package org.example.Presentacion;

import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoPersona;

public abstract class ContribucionController {

    public static void actualizarPuntajeUsuarioActual(Contribucion contribucion){
        Persona colaboradorPersona = obtenerPersonaColaboradorActual();
        Colaborador colaborador = RepoPersona.getInstancia().getRolColaboradorById(colaboradorPersona.getRol().getIdrol());
        contribucion.setColaborador(colaborador);

        colaborador.agregarContribucion(contribucion);
        colaborador.calcularPuntuaje();
        colaboradorPersona.setRol(colaborador);//update
        colaboradorPersona = RepoPersona.getInstancia().actualizarPersona(colaboradorPersona);
        actualizarPersonaColaboradorActual(colaboradorPersona);
    }

    public static Colaborador obtenerRolColaboradorActual(){
        Persona colaboradorPersona = obtenerPersonaColaboradorActual();
        return RepoPersona.getInstancia().getRolColaboradorById(colaboradorPersona.getRol().getIdrol());
    }

    private static Persona obtenerPersonaColaboradorActual(){
        Usuario user = (Usuario) SessionManager.getInstancia().obtenerAtributo("usuario");
        Persona personaUser = user.getColaborador();
        if(personaUser.getRol() == null){
            Colaborador colaboradorRol = new Colaborador();
            colaboradorRol.setEstaActivo(true);
            personaUser.setRol(colaboradorRol);
            // Actualiza persona para obtener id del rol
            personaUser = RepoPersona.getInstancia().actualizarPersona(personaUser);
        } else if(!(personaUser.getRol() instanceof Colaborador)){
            throw new RuntimeException("Esta funcionalidad está disponible solo para personas con el rol de colaborador");
        }

        return personaUser;
    }

    private static void actualizarPersonaColaboradorActual(Persona persona){
        Usuario user = (Usuario) SessionManager.getInstancia().obtenerAtributo("usuario");
         user.setColaborador(persona);
    }
}
