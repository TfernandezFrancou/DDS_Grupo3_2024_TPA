package org.example.presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.repositorios.RepoContribucion;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ColaboracionesController {
    private ColaboracionesController(){}

    public static void getColaboraciones(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        Usuario user = context.attribute("usuario");
        int idPersona = Objects.requireNonNull(user).getColaborador().getIdPersona();
        List<Contribucion> contribuciones = RepoContribucion.getInstancia()
                .obtenerContribucionesPorPersona(idPersona);
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