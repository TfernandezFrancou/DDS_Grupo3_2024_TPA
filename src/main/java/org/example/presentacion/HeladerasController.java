package org.example.presentacion;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*;
import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.HacerseCargoDeUnaHeladera;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.excepciones.ContribucionNoPermitidaException;
import org.example.excepciones.DireccionException;
import org.example.excepciones.HeladeraException;
import org.example.excepciones.ImagenURLException;
import org.example.incidentes.Incidente;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoUbicacion;
import org.example.subscripciones_heladeras.SubscripcionDesperfecto;
import org.example.subscripciones_heladeras.SubscripcionHeladera;
import org.example.subscripciones_heladeras.SubscripcionViandasDisponibles;
import org.example.subscripciones_heladeras.SubscripcionViandasFaltantes;
import org.example.validadores.VerificadorImagenURL;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class HeladerasController extends ContribucionController {

    private static final Logger log = LoggerFactory.getLogger(HeladerasController.class);

    private static final String VIEW_REGISTRAR = "views/heladeras/registrar.mustache";
    private static final String VIEW_REPORTE = "views/heladeras/reporte.mustache";

    private static final String PATH_HELADERAS = "/heladeras/";
    private static final String MAPS_API_KEY = Configuracion.obtenerProperties("maps.apiKey");
    private static final String ERROR_FIELD = "error";
    private static final String MEDIO_DE_CONTACT_FIELD = "medioDeContacto";

    public static void getVisualizar(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        try {
            var heladeras = RepoHeladeras.getInstancia().obtenerTodas();
            model.put("listaheladeras", heladeras);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al buscar las heladeras: {}", e.getMessage());
        }
        context.render("views/heladeras/visualizar.mustache", model);
    }

    public static void getCaracteristicas(@NotNull Context context) throws ContribucionNoPermitidaException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        Optional<Heladera> resultadoBusqueda = Optional.empty();
        try {
            Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
            resultadoBusqueda = RepoHeladeras.getInstancia().buscarPorId(idHeladera);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al buscar la heladera: {}", e.getMessage());
        }

        if (resultadoBusqueda.isPresent()){
            var heladera = resultadoBusqueda.get();
            model.put("heladera", heladera);
            model.put("tiempoActiva", heladera.obtenerMesesActivos());

            Colaborador colaborador = obtenerRolColaboradorActual(context);

            model.put("mediosDeContacto", colaborador.getPersona().getMediosDeContacto());

            List<SubscripcionHeladera> subscripciones = RepoHeladeras.getInstancia().obtenerSubscripcionesPorPersona(
                heladera.getIdHeladera(),
                colaborador.getPersona().getIdPersona()
            );
            for (SubscripcionHeladera subscripcion : subscripciones) {
                if (subscripcion instanceof SubscripcionDesperfecto) {
                    model.put("suscripcionDesperfecto", subscripcion);
                } else if (subscripcion instanceof SubscripcionViandasFaltantes) {
                    model.put("suscripcionFaltantes", subscripcion);
                } else if (subscripcion instanceof SubscripcionViandasDisponibles) {
                    model.put("suscripcionDisponibles", subscripcion);
                }
            }

            context.render("views/heladeras/caracteristicas.mustache", model);
        }else{
            context.status(404);
        }
    }

    public static void getRegistrar(@NotNull Context context)  {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        context.render(VIEW_REGISTRAR, model);
    }

    public static void postRegistrar(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        String nombre = context.formParam("nombre");
        String calle = context.formParam("calle");
        String altura = context.formParam("altura");
        String localidad = context.formParam("localidad");
        Integer capacidad = context.formParamAsClass("capacidad", Integer.class).get();

        Direccion direccion = new Direccion(calle, altura, localidad);

        GeoApiContext ctx = new GeoApiContext
                .Builder()
                .apiKey(MAPS_API_KEY)
                .build();
        String query = calle + " " + altura + " " + localidad;
        if(log.isInfoEnabled()){
            log.info("QUERY: {}", query);
        }
        GeocodingResult[] results = GeocodingApi.geocode(ctx, query).await();
        if (results.length == 0) {
            throw new DireccionException("No se encontro la direccion");
        }
        GeocodingResult result = results[0];
        ctx.shutdown();

        Ubicacion ubicacion = new Ubicacion(
            (float) result.geometry.location.lat,
            (float) result.geometry.location.lng
        );
        if(log.isInfoEnabled()){
            log.info("Latitud: {} - Longitud: {}", ubicacion.getLatitud(),ubicacion.getLongitud());
        }

        Heladera heladera = new Heladera(nombre, ubicacion, direccion, capacidad);

        Colaborador colaborador = obtenerRolColaboradorActual(context);

        HacerseCargoDeUnaHeladera hacerseCargoDeUnaHeladera = new HacerseCargoDeUnaHeladera(colaborador, List.of(heladera));
        try{
            verificarPuedeHacerContribucion(hacerseCargoDeUnaHeladera,context);
        } catch (Exception e){
            e.printStackTrace();
            model.put(ERROR_FIELD, e.getMessage());
            context.render(VIEW_REGISTRAR, model);
        }

        RepoUbicacion.getInstancia().agregar(ubicacion);
        hacerseCargoDeUnaHeladera.ejecutarContribucion();

        context.redirect("/heladeras");
    }

    public static void getReporte(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);
        context.render(VIEW_REPORTE, model);
    }

    public static void postReporte(@NotNull Context context) throws ImagenURLException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);

        String idHeladeraStr = context.formParam("idHeladera");
        Heladera heladera = null;
        try {
            assert idHeladeraStr != null;
            Integer idHeladera = Integer.parseInt(idHeladeraStr);
            Optional<Heladera> heladeraOP = RepoHeladeras.getInstancia().buscarPorId(idHeladera);
            if(heladeraOP.isEmpty()){
                throw new HeladeraException("No existe la heladera ocn id: "+ idHeladera);
            } else{
                heladera = heladeraOP.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al buscar la heladera: {}", e.getMessage());
            model.put(ERROR_FIELD, "Error al buscar la heladera:" + e.getMessage());
            context.render(VIEW_REPORTE, model);
            return;
        }
        LocalDateTime fechaParseada = null;
        try {
            String fechaFalla = context.formParam("fechaFalla");
            assert fechaFalla != null;
            fechaParseada = LocalDate.parse(fechaFalla).atTime(0, 0, 0);
        } catch (Exception e) {
           log.error("Error al parsear la fecha: {}", e.getMessage());
            model.put(ERROR_FIELD, e.getMessage());
            context.render(VIEW_REPORTE, model);
            return;
        }

        String fotoUrl = context.formParam("foto");
        String descripcion = context.formParam("descripcion");

        VerificadorImagenURL verificadorImagenURL = VerificadorImagenURL.getInstancia();
        verificadorImagenURL.verifyImagen(fotoUrl);

        try{
            Colaborador colaborador = obtenerRolColaboradorActual(context);
            colaborador.reportarFallaTecnica(descripcion,fotoUrl,heladera, fechaParseada);
        } catch (Exception e){
          e.printStackTrace();

            model.put(ERROR_FIELD, e.getMessage());
            context.render(VIEW_REPORTE, model);
            return;//salgo para que no redirija a /heladeras
        }

        context.redirect("/heladeras");
    }


    public static void getAlertas(@NotNull Context context){
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        List<Incidente> resultados = null;

        try {
            Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
            model.put("idHeladera", idHeladera);
            resultados = RepoIncidente.getInstancia().buscarPorHeladera(idHeladera);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al buscar los incidentes: {}", e.getMessage());
        }

        // Este mapeo lo hago para formatear la fecha
        assert resultados != null;
        model.put("alertas", resultados.stream().map(alerta -> {
            Map<String, String> mapped = new HashMap<>();
            mapped.put("idIncidente", String.valueOf(alerta.getIdIncidente()));
            mapped.put("tipoDeIncidente", alerta.getTipoDeIncidente());
            LocalDateTime fecha = alerta.getFechaDeEmision();
            String formatted = fecha.getDayOfMonth() + "/" + fecha.getMonthValue() + "/" + fecha.getYear();
            mapped.put("fechaDeEmision", formatted);
            return mapped;
        }).toList());

        context.render("views/heladeras/alertas.mustache", model);
    }

    public static void getAlerta(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        int idincidente = Integer.parseInt(context.pathParam("idAlerta"));
        Incidente incidente = RepoIncidente.getInstancia().obtenerIncidentePorId(idincidente);

        model.put("incidente", incidente);
        context.render("/views/heladeras/detalleFalla.mustache", model);
    }

    public static void postSuscripcionDesperfectos(@NotNull Context context) throws ContribucionNoPermitidaException {
        Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        Integer idMedioDeContacto = context.formParamAsClass(MEDIO_DE_CONTACT_FIELD, Integer.class).get();
        Persona persona = colaborador.getPersona();
        persona.getMediosDeContacto()
                .stream()
                .filter(m -> m.getIdMedioDeContacto() == idMedioDeContacto)
                .findFirst()
                .ifPresent(medioDeContacto ->
                    RepoHeladeras.getInstancia().buscarPorId(idHeladera).ifPresent(heladera -> {
                        SubscripcionDesperfecto s = new SubscripcionDesperfecto(heladera, persona, medioDeContacto);
                        heladera.getPublisherViandasFaltantes().suscribir(s);
                    })
                );
        context.redirect(PATH_HELADERAS + idHeladera);
    }

    public static void postSuscripcionViandasFaltantes(@NotNull Context context) throws ContribucionNoPermitidaException {
        Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
        Integer cantidad = context.formParamAsClass("cantidad", Integer.class).get();
        Integer idMedioDeContacto = context.formParamAsClass(MEDIO_DE_CONTACT_FIELD, Integer.class).get();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        Persona persona = colaborador.getPersona();
        persona.getMediosDeContacto()
                .stream()
                .filter(m -> m.getIdMedioDeContacto() == idMedioDeContacto)
                .findFirst()
                .ifPresent(medioDeContacto ->
                    RepoHeladeras.getInstancia().buscarPorId(idHeladera).ifPresent(heladera -> {
                        SubscripcionViandasFaltantes s = new SubscripcionViandasFaltantes(heladera, persona, medioDeContacto, cantidad);
                        heladera.getPublisherViandasFaltantes().suscribir(s);
                    })
                );
        context.redirect(PATH_HELADERAS + idHeladera);
    }

    public static void postSuscripcionViandasDisponibles(@NotNull Context context) throws ContribucionNoPermitidaException {
        Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
        Integer cantidad = context.formParamAsClass("cantidad", Integer.class).get();
        Integer idMedioDeContacto = context.formParamAsClass(MEDIO_DE_CONTACT_FIELD, Integer.class).get();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        Persona persona = colaborador.getPersona();
        persona.getMediosDeContacto()
                .stream()
                .filter(m -> m.getIdMedioDeContacto() == idMedioDeContacto)
                .findFirst()
                .ifPresent(medioDeContacto ->
                    RepoHeladeras.getInstancia().buscarPorId(idHeladera).ifPresent(heladera -> {
                        SubscripcionViandasDisponibles s = new SubscripcionViandasDisponibles(heladera, persona, medioDeContacto, cantidad);
                        heladera.getPublisherViandasFaltantes().suscribir(s);
                    })
                );
        context.redirect(PATH_HELADERAS+ idHeladera);
    }

    public static void cancelarSuscripcion(@NotNull Context context) {
        Integer idHeladera = context.pathParamAsClass("heladera", Integer.class).get();
        Integer idSuscripcion = context.pathParamAsClass("suscripcion", Integer.class).get();
        RepoHeladeras.getInstancia().eliminarSuscripcion(idSuscripcion);
        context.redirect(PATH_HELADERAS + idHeladera);
    }
}