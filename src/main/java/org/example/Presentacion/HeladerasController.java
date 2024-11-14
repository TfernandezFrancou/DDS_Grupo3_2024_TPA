package org.example.Presentacion;


import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*;
import io.javalin.http.Context;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoHeladeras;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HeladerasController {

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

        Heladera heladera = new Heladera(nombre, ubicacion, direccion, capacidad);

        try {
            RepoHeladeras.getInstancia().agregar(heladera);
        } catch (Exception e) {
            System.err.println("Error al agregar la heladera: " + e);
        }

        context.redirect("/heladeras/visualizar");
    }
}