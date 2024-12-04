package org.example.Presentacion;


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*;
import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.HacerseCargoDeUnaHeladera;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.FallaTecnica;
import org.example.incidentes.Incidente;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoUbicacion;
import org.example.subscripcionesHeladeras.SubscripcionDesperfecto;
import org.example.subscripcionesHeladeras.SubscripcionHeladera;
import org.example.subscripcionesHeladeras.SubscripcionViandasDisponibles;
import org.example.subscripcionesHeladeras.SubscripcionViandasFaltantes;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HeladerasController extends ContribucionController {

    public static void getVisualizar(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        try {
            var heladeras = RepoHeladeras.getInstancia().obtenerTodas();
            model.put("listaheladeras", heladeras);
        } catch (Exception e) {
            System.err.println("Error al buscar las heladeras: " + e);
        }
        context.render("views/heladeras/visualizar.mustache", model);
    }

    public static void getCaracteristicas(@NotNull Context context) throws Exception {
        Optional<Heladera> resultadoBusqueda = Optional.empty();
        try {
            Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
            resultadoBusqueda = RepoHeladeras.getInstancia().buscarPorId(idHeladera);
        } catch (Exception e) {
            System.err.println("Error al buscar la heladera: " + e);
        }

        if (resultadoBusqueda.isPresent()){
            var heladera = resultadoBusqueda.get();
            Map<String, Object> model = new HashMap<>();
            model.put("heladera", heladera);
            model.put("tiempoActiva", heladera.obtenerMesesActivos());

            Colaborador colaborador = obtenerRolColaboradorActual(context);
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

    public static void getRegistrar(@NotNull Context context) throws Exception {
        context.render("views/heladeras/registrar.mustache");
    }

    public static void postRegistrar(@NotNull Context context) throws Exception {
        String nombre = context.formParam("nombre");
        String calle = context.formParam("calle");
        String altura = context.formParam("altura");
        String localidad = context.formParam("localidad");
        Integer capacidad = context.formParamAsClass("capacidad", Integer.class).get();

        Direccion direccion = new Direccion(calle, altura, localidad);

        GeocodingResult result = null;
        try {
            GeoApiContext ctx = new GeoApiContext.Builder().apiKey("AIzaSyDobH49Ls6a9qadpi4gctKSWX1YAnBtsVA").build();
            String query = calle + " " + altura + " " + localidad;
            System.out.println("QUERY: " + query);
            GeocodingResult[] results = GeocodingApi.geocode(ctx, query).await();
            if (results.length == 0) {
                throw new Exception("No se encontro la direccion");
            }
            result = results[0];
            ctx.shutdown();
        } catch (Exception e) {
            System.err.println("Error al buscar en google maps: " + e);
        }

        Ubicacion ubicacion = new Ubicacion(
            (float) result.geometry.location.lat,
            (float) result.geometry.location.lng
        );
        System.out.println("Latitud: " + ubicacion.getLatitud() + " - Longitud: " + ubicacion.getLongitud());
        RepoUbicacion.getInstancia().agregar(ubicacion);

        Heladera heladera = new Heladera(nombre, ubicacion, direccion, capacidad);

        Colaborador colaborador = obtenerRolColaboradorActual(context);

        HacerseCargoDeUnaHeladera hacerseCargoDeUnaHeladera = new HacerseCargoDeUnaHeladera(colaborador, List.of(heladera));
        hacerseCargoDeUnaHeladera.ejecutarContribucion();

        context.redirect("/heladeras");
    }

    public static void getReporte(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);
        context.render("views/heladeras/reporte.mustache", model);
    }

    public static void postReporte(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);

        String idHeladeraStr = context.formParam("idHeladera");
        Heladera heladera = null;
        try {
            assert idHeladeraStr != null;
            Integer idHeladera = Integer.parseInt(idHeladeraStr);
            heladera = RepoHeladeras.getInstancia().buscarPorId(idHeladera).get();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al buscar la heladera:" + e);
            model.put("error", "Error al buscar la heladera:" + e.getMessage());
            context.render("views/heladeras/reporte.mustache", model);
            return;
        }
//        LocalDateTime fechaParseada = null;
//        try {
//            String fechaFalla = context.formParam("fechaFalla");
//            assert fechaFalla != null;
//            fechaParseada = LocalDate.parse(fechaFalla).atTime(0, 0, 0);
//        } catch (Exception e) {
//            System.err.println("Error al parsear la fecha:" + e);
//            context.status(400);
//            return;
//        }

        String fotoUrl = context.formParam("foto");
        String tipoFalla = context.formParam("tipoFalla");
        String descripcion = context.formParam("descripcion");
        try{
            Colaborador colaborador = obtenerRolColaboradorActual(context);
            colaborador.reportarFallaTecnica(descripcion,fotoUrl,heladera);
        } catch (Exception e){
          e.printStackTrace();

            model.put("error", e.getMessage());
            context.render("views/heladeras/reporte.mustache", model);
            return;//salgo para que no redirija a /heladeras
        }

        context.redirect("/heladeras");
    }


    public static void getAlertas(@NotNull Context context) throws Exception {
        List<Incidente> resultados = null;

        Map<String, Object> model = new HashMap<>();
        try {
            Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
            model.put("idHeladera", idHeladera);
            resultados = RepoIncidente.getInstancia().buscarPorHeladera(idHeladera);
        } catch (Exception e) {
            System.err.println("Error al buscar los incidentes: " + e);
        }

        // Este mapeo lo hago para formatear la fecha
        model.put("alertas", resultados.stream().map((alerta) -> {
            Map<String, String> mapped = new HashMap<>();
            mapped.put("tipoDeIncidente", alerta.getTipoDeIncidente());
            LocalDateTime fecha = alerta.getFechaDeEmision();
            String formatted = fecha.getDayOfMonth() + "/" + fecha.getMonthValue() + "/" + fecha.getYear();
            mapped.put("fechaDeEmision", formatted);
            return mapped;
        }).toList());

        context.render("views/heladeras/alertas.mustache", model);
    }

    public static void postSuscripcionDesperfectos(@NotNull Context context) throws Exception {
        Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        Persona persona = colaborador.getPersona();
        persona.getMediosDeContacto().stream().findFirst().ifPresent(medioDeContacto -> {
            RepoHeladeras.getInstancia().buscarPorId(idHeladera).ifPresent(heladera -> {
                SubscripcionDesperfecto s = new SubscripcionDesperfecto(heladera, persona, medioDeContacto);
                heladera.getPublisherViandasFaltantes().suscribir(s);
            });
        });
        context.redirect("/heladeras/" + idHeladera);
    }

    public static void cancelarSuscripcionDesperfectos(@NotNull Context context) throws Exception {
    }

    public static void postSuscripcionViandasFaltantes(@NotNull Context context) throws Exception {
        Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
        Integer cantidad = context.formParamAsClass("cantidad", Integer.class).get();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        Persona persona = colaborador.getPersona();
        persona.getMediosDeContacto().stream().findFirst().ifPresent(medioDeContacto -> {
            RepoHeladeras.getInstancia().buscarPorId(idHeladera).ifPresent(heladera -> {
                SubscripcionViandasFaltantes s = new SubscripcionViandasFaltantes(heladera, persona, medioDeContacto, cantidad);
                heladera.getPublisherViandasFaltantes().suscribir(s);
            });
        });
        context.redirect("/heladeras/" + idHeladera);
    }

    public static void cancelarSuscripcionViandasFaltantes(@NotNull Context context) throws Exception {
    }

    public static void postSuscripcionViandasDisponibles(@NotNull Context context) throws Exception {
        Integer idHeladera = context.pathParamAsClass("id", Integer.class).get();
        Integer cantidad = context.formParamAsClass("cantidad", Integer.class).get();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        Persona persona = colaborador.getPersona();
        persona.getMediosDeContacto().stream().findFirst().ifPresent(medioDeContacto -> {
            RepoHeladeras.getInstancia().buscarPorId(idHeladera).ifPresent(heladera -> {
                SubscripcionViandasDisponibles s = new SubscripcionViandasDisponibles(heladera, persona, medioDeContacto, cantidad);
                heladera.getPublisherViandasFaltantes().suscribir(s);
            });
        });
        context.redirect("/heladeras/" + idHeladera);
    }

    public static void cancelarSuscripcionViandasDisponibles(@NotNull Context context) throws Exception {
    }

    public static void cancelarSuscripcion(@NotNull Context context) throws Exception {
        Integer idHeladera = context.pathParamAsClass("heladera", Integer.class).get();
        Integer idSuscripcion = context.pathParamAsClass("suscripcion", Integer.class).get();
        RepoHeladeras.getInstancia().eliminarSuscripcion(idSuscripcion);
        context.redirect("/heladeras/" + idHeladera);
    }
}