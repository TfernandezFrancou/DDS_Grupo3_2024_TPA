package org.example.presentacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.example.repositorios.RepoIncidente;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AlertasHandler implements Handler {
    private final RepoIncidente repoIncidente;

    public AlertasHandler(){
        this.repoIncidente = RepoIncidente.getInstancia();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String nombreHeladera = context.pathParamAsClass("nombre",String.class).get();
        var resultadoBusqueda = repoIncidente.obtenerTodasLasAlertas().stream()
                .filter(incidente -> incidente.getHeladera().getNombre().equals(nombreHeladera)).toList();

        Map<String, Object> model = new HashMap<>();
        model.put("listaAlertas", resultadoBusqueda);
        model.put("nombre",nombreHeladera);
        context.render("views/heladeras/alertas.mustache",model);
    }
}
