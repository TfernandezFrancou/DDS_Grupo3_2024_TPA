package org.example.Presentacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoHeladeras;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VisualizarHandler implements Handler {
    private final RepoHeladeras repoHeladeras;

    public VisualizarHandler(){
        this.repoHeladeras = RepoHeladeras.getInstancia();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        prueba();
        var heladeras = this.repoHeladeras.obtenerTodas();

        Map<String, Object> model = new HashMap<>();
        model.put("listaheladeras",heladeras);
        context.render("views/heladeras/visualizar.mustache",model);
        //context.json(model);
    }

    //TODO sacar prueba porque hardcodeado
    public void prueba(){
        Heladera heladera = new Heladera();
        heladera.setNombre("MedranoUTN");
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-34.5985827f);
        ubicacion.setLongitud(-58.4201726f);
        heladera.setUbicacion(ubicacion);
        heladera.actualizarEstadoHeladera(true);

        repoHeladeras.agregar(heladera);
    }
}
