package org.example.presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.excepciones.ContribucionNoPermitidaException;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoPersona;
import org.example.validadores.VerificadorContribucion;

import java.util.Objects;

public abstract class ContribucionController {

    protected ContribucionController(){}

    public static Colaborador obtenerRolColaboradorActual(Context context) throws ContribucionNoPermitidaException {
        Persona colaboradorPersona = obtenerPersonaColaboradorActual(context);
        RepoPersona.getInstancia().actualizarPersona(colaboradorPersona);
        return RepoPersona.getInstancia().getRolColaboradorById(colaboradorPersona.getRol().getIdrol());
    }

    private static Persona obtenerPersonaColaboradorActual(Context context) throws ContribucionNoPermitidaException {
        Usuario user = context.attribute("usuario");
        Persona personaUser = Objects.requireNonNull(user).getColaborador();
        personaUser = RepoPersona.getInstancia().buscarPorId(personaUser.getIdPersona());
        if(personaUser.getRol() == null){
            Colaborador colaboradorRol = new Colaborador();
            colaboradorRol.setEstaActivo(true);
            personaUser.setRol(colaboradorRol);
            // Actualiza persona para obtener id del rol
            personaUser = RepoPersona.getInstancia().actualizarPersona(personaUser);
        } else if(!(personaUser.getRol() instanceof Colaborador)){
            throw new ContribucionNoPermitidaException("Esta funcionalidad está disponible solo para personas con el rol de colaborador");
        }

        user.setColaborador(personaUser);//update
        return personaUser;
    }

    public static void verificarPuedeHacerContribucion(Contribucion contribucion, Context context) throws ContribucionNoPermitidaException {
        if(!VerificadorContribucion.getInstancia()
                .puedeRealizarContribucion(
                        obtenerPersonaColaboradorActual(context),
                        contribucion)
        ){
            throw new ContribucionNoPermitidaException("No tienes permitido realizar este tipo de contribución");
        }
    }

}