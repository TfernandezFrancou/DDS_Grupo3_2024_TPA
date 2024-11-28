package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.colaboraciones.Contribucion;
import org.example.repositorios.RepoContribucion;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColaboracionesController {
    public static void getColaboraciones(@NotNull Context context) {
        int idPersona = 1; // TODO: agarrarlo del contexto cuando tengamos auth
        List<Contribucion> contribuciones = RepoContribucion.getInstancia()
                .obtenerContribucionesPorPersona(idPersona);
        Map<String, Object> model = new HashMap<>();
        model.put("colaboraciones", contribuciones.stream().map((contribucion -> {
            Map<String, Object> m = new HashMap<>();
            m.put(contribucion.getClass().getSimpleName(), contribucion);
            if (contribucion.getFecha() != null) {
                String fecha = contribucion.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                m.put("fechaFormateada", fecha);
            } else {
                m.put("fechaFormateada", "-");
            }
            return m;
        })).toList());
        context.render("/views/colaboraciones/mis-colaboraciones.mustache", model);
    }
}