package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.personas.roles.Colaborador;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class DonarDineroController extends ContribucionController {

    public static void postDonarDinero(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        DonacionDeDinero donacion;
        int monto = Integer.parseInt(context.formParam("monto"));
        try {
            int frecuencia = Integer.parseInt(context.formParam("frecuencia"));
            donacion = new DonacionDeDinero(colaborador, monto, frecuencia);
        } catch (Exception e) {
            donacion = new DonacionDeDinero(colaborador, monto, null);
        }

        try {
            verificarPuedeHacerContribucion(donacion,context);
            donacion.ejecutarContribucion();
            model.put("exito", "La donación fue realizada con éxito");
            context.render("/views/colaboraciones/donar-dinero.mustache", model);
        } catch (Exception e) {
            e.printStackTrace();
            model.put("error", e.getMessage());
            context.render("/views/colaboraciones/donar-dinero.mustache", model);
        }
    }

    public static void getDonarDinero(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        context.render("/views/colaboraciones/donar-dinero.mustache", model);
    }
}