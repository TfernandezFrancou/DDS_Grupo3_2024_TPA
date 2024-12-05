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

public class RegistrarPersonaVulnerableController extends ContribucionController {
    public static void postRegistrarPersonaVulnerable(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        try{
            PersonaHumana personaVulnerable = parsearPersonaVulnerable(context);

            RegistrarPersonasEnSituacionVulnerable contribucion = new RegistrarPersonasEnSituacionVulnerable();
            contribucion.setFecha(LocalDate.now());
            contribucion.agregarPersona(personaVulnerable);
            contribucion.setColaborador(colaborador);

            verificarPuedeHacerContribucion(contribucion,context);

            contribucion.ejecutarContribucion();

            model.put("exito", "El registro fue exitoso");
            context.render("/views/colaboraciones/registro-persona-vulnerable.mustache", model);
        } catch (Exception exception) {
            exception.printStackTrace();
            model.put("error", exception.getMessage());
            context.render("/views/colaboraciones/registro-persona-vulnerable.mustache", model);
        }
    }

    private static PersonaHumana parsearPersonaVulnerable(@NotNull Context context) throws AlmacenarPersonaVulnerable {
        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");

        String tieneDomicilio = context.formParam("domicilio");
        String domicilioCalle = context.formParam("domicilio-calle");
        String domicilioAltura = context.formParam("domicilio-altura");
        String localidad = context.formParam("domicilio-localidad");
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
        if(apellido != null)
            personaHumana.setApellido(apellido);

        if(tieneDomicilio.equals("si")){
            Direccion direccion1;
            if(domicilioCalle.equals("")){
                direccion1 = null;
            } else if( domicilioAltura.equals("")){
                throw new AlmacenarPersonaVulnerable("La altura de la calle no es válida");
            } else if(localidad.equals("")){
                throw new AlmacenarPersonaVulnerable("La localidad de dirección ingresada no es válida");
            }else{
                direccion1 = new Direccion();
                direccion1.setNombreCalle(domicilioCalle);
                direccion1.setAltura(domicilioAltura);
                direccion1.setLocalidad(localidad.toLowerCase());
                personaHumana.setDireccion(direccion1);
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

        return personaHumana;
    }

    public static void getRegistrarPersonaVulnerable(@NotNull Context context) {
        context.render("/views/colaboraciones/registro-persona-vulnerable.mustache");
    }
}
