package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.RegistrarPersonasEnSituacionVulnerable;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.excepciones.AlmacenarPersonaVulnerable;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoPersona;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RegistrarPersonaVulnerableController {
    public static void postRegistrarPersonaVulnerable(@NotNull Context context) {
        try{
            PersonaHumana personaVulnerable = almacenarPersonaVulnerable(context);
            RegistrarPersonasEnSituacionVulnerable contribucion = new RegistrarPersonasEnSituacionVulnerable();
            contribucion.setFecha(LocalDate.now());
            contribucion.agregarPersona(personaVulnerable);

            Colaborador colaborador = obtenerRolColaboradorActual();
            contribucion.setColaborador(colaborador);
            RepoContribucion.getInstancia().agregarContribucion(contribucion);

            Map<String, Object> model = new HashMap<>();
            model.put("exito", "El registro fue exitoso");
            context.render("/views/colaboraciones/registro-persona-vulnerable.mustache", model);
        }catch (Exception exception){
            Map<String, Object> model = new HashMap<>();
            model.put("error", exception.getMessage());
            context.render("/views/colaboraciones/registro-persona-vulnerable.mustache", model);
            exception.printStackTrace();
        }

    }

    private static PersonaHumana almacenarPersonaVulnerable(@NotNull Context context) throws AlmacenarPersonaVulnerable {
        String nombreYApellido = context.formParam("nombre");
        String nombre = nombreYApellido.split(" ")[0];
        String apellido = nombreYApellido.split(" ")[1];
        String tieneDomicilio = context.formParam("domicilio");
        String domicilio = context.formParam("domicilio-texto");
        String documento = context.formParam("documento");
        String numeroDocumento = context.formParam("numero-documento");
        String tieneMenores = context.formParam("menores");
        String cantidadMenores = context.formParam("cantidad-menores");

        PersonaEnSituacionVulnerable personaEnSituacionVulnerable = new PersonaEnSituacionVulnerable();
        if(tieneMenores.equals("si") && Integer.parseInt(cantidadMenores) <= 0){
            throw new AlmacenarPersonaVulnerable("Error al cargar la cantidad de menores");
        }

        personaEnSituacionVulnerable.setTieneMenores(tieneMenores.equals("si"));
        personaEnSituacionVulnerable.setCantMenores(tieneMenores.equals("si") ? Integer.parseInt(cantidadMenores) : 0);
        personaEnSituacionVulnerable.setFechaRegistro(LocalDate.now());

        PersonaHumana personaHumana = new PersonaHumana();
        personaHumana.setNombre(nombre);
        personaHumana.setApellido(apellido);
        if(tieneDomicilio.equals("si")){

            if(domicilio.equals("")) {
                throw new AlmacenarPersonaVulnerable("No se introdujo un domicilio valido");
            }else{
                Direccion direccion = new Direccion();
                String[] partes1 = domicilio.split(",");
                direccion.setLocalidad(partes1[1]);
                String[] partes2 = partes1[0].split(" ");
                direccion.setNombreCalle(partes2[0]);
                direccion.setAltura(partes2[1]);
                personaHumana.setDireccion(direccion);
            }
        }
        personaHumana.setRol(personaEnSituacionVulnerable);

        if(!documento.equals("noTiene")){
            if(numeroDocumento.equals("")){
                throw new AlmacenarPersonaVulnerable("No se introdujo un numero de documento");
            }
            Documento documento1 = new Documento();
            documento1.setNumeroDocumento(numeroDocumento);
            TipoDocumento tipoDocumento = TipoDocumento.valueOf(documento);

            documento1.setTipoDocumento(tipoDocumento);
            personaHumana.setDocumento(documento1);
        }

        RepoPersona.getInstancia().agregar(personaHumana);
        return personaHumana;
    }

    private static Colaborador obtenerRolColaboradorActual(){
        Usuario user = (Usuario) SessionManager.getInstancia().obtenerAtributo("usuario");
        Persona personaUser = user.getColaborador();
        if(personaUser.getRol() == null){
            Colaborador colaboradorRol = new Colaborador();
            colaboradorRol.setEstaActivo(true);
            personaUser.setRol(colaboradorRol);
            // Actualiza persona para obtener id del rol
            personaUser = RepoPersona.getInstancia().actualizarPersona(personaUser);
        } else if(!(personaUser.getRol() instanceof Colaborador)){
            throw new RuntimeException("No puedes registrar a una persona en situacion vulnerable, no eres colaborador");
        }

        return (Colaborador) personaUser.getRol();
    }

    public static void getRegistrarPersonaVulnerable(@NotNull Context context) {
        context.render("/views/colaboraciones/registro-persona-vulnerable.mustache");
    }
}
