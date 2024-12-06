package org.example.presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.contribuciones.RegistrarPersonasEnSituacionVulnerable;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.excepciones.AlmacenarPersonaVulnerable;
import org.example.excepciones.ContribucionNoPermitidaException;
import org.example.personas.PersonaHumana;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.PersonaEnSituacionVulnerable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RegistrarPersonaVulnerableController extends ContribucionController {


    private static final String VIEW_REGISTRAR_VULNERABLE = "/views/colaboraciones/registro-persona-vulnerable.mustache";
    public static void postRegistrarPersonaVulnerable(@NotNull Context context) throws ContribucionNoPermitidaException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        try{
            PersonaHumana personaVulnerable = crearPersonaVulnerable(context);

            RegistrarPersonasEnSituacionVulnerable contribucion = new RegistrarPersonasEnSituacionVulnerable();
            contribucion.setFecha(LocalDate.now());
            contribucion.agregarPersona(personaVulnerable);
            contribucion.setColaborador(colaborador);

            verificarPuedeHacerContribucion(contribucion,context);

            contribucion.ejecutarContribucion();

            model.put("exito", "El registro fue exitoso");
            context.render(VIEW_REGISTRAR_VULNERABLE, model);
        } catch (Exception exception) {
            exception.printStackTrace();
            model.put("error", exception.getMessage());
            context.render(VIEW_REGISTRAR_VULNERABLE, model);
        }
    }

    private static PersonaHumana crearPersonaVulnerable(@NotNull Context context) throws AlmacenarPersonaVulnerable {
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
        assert tieneMenores != null;
        if(tieneMenores.equals("si")) {
            assert cantidadMenores != null;
            if (Integer.parseInt(cantidadMenores) <= 0) {
                throw new AlmacenarPersonaVulnerable("Error al cargar la cantidad de menores");
            }
        }

        personaEnSituacionVulnerable.setTieneMenores(tieneMenores.equals("si"));
        personaEnSituacionVulnerable.setCantMenores(tieneMenores.equals("si") ? Integer.parseInt(cantidadMenores) : 0);
        personaEnSituacionVulnerable.setFechaRegistro(LocalDate.now());

        PersonaHumana personaHumana = new PersonaHumana();
        personaHumana.setNombre(nombre);
        if(apellido != null)
            personaHumana.setApellido(apellido);

        assert tieneDomicilio != null;
        if(tieneDomicilio.equals("si")){
            Direccion direccion1 ;
            assert domicilioCalle != null;
            if(!domicilioCalle.equals("")) {
                assert domicilioAltura != null;
                if( domicilioAltura.equals("")){
                    throw new AlmacenarPersonaVulnerable("La altura de la calle no es válida");
                } else {
                    assert localidad != null;
                    if(localidad.equals("")){
                        throw new AlmacenarPersonaVulnerable("La localidad de dirección ingresada no es válida");
                    }else{
                        direccion1 = new Direccion();
                        direccion1.setNombreCalle(domicilioCalle);
                        direccion1.setAltura(domicilioAltura);
                        direccion1.setLocalidad(localidad.toLowerCase());
                        personaHumana.setDireccion(direccion1);
                    }
                }
            }
        }
        personaHumana.setRol(personaEnSituacionVulnerable);

        assert documento != null;
        if(!documento.equals("noTiene")){
            assert numeroDocumento != null;
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
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        context.render(VIEW_REGISTRAR_VULNERABLE, model);
    }
}
