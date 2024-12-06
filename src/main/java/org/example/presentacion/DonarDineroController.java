package org.example.presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.excepciones.ContribucionNoPermitidaException;
import org.example.personas.roles.Colaborador;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DonarDineroController extends ContribucionController {

    private DonarDineroController(){
        super();
    }

    private static final String VIEW_DONAR_DINERO ="/views/colaboraciones/donar-dinero.mustache";

    public static void postDonarDinero(@NotNull Context context) throws ContribucionNoPermitidaException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        DonacionDeDinero donacion;
        int monto = Integer.parseInt(Objects.requireNonNull(context.formParam("monto")));
        try {
            int frecuencia = Integer.parseInt(Objects.requireNonNull(context.formParam("frecuencia")));
            donacion = new DonacionDeDinero(colaborador, monto, frecuencia);
        } catch (Exception e) {
            donacion = new DonacionDeDinero(colaborador, monto, null);
        }

        try {
            verificarPuedeHacerContribucion(donacion,context);
            donacion.ejecutarContribucion();
            model.put("exito", "La donación fue realizada con éxito");
            context.render(VIEW_DONAR_DINERO, model);
        } catch (Exception e) {
            e.printStackTrace();
            model.put("error", e.getMessage());
            context.render(VIEW_DONAR_DINERO, model);
        }
    }

    public static void getDonarDinero(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        context.render(VIEW_DONAR_DINERO, model);
    }
}