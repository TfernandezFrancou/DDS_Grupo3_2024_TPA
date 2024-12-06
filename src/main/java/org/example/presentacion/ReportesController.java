package org.example.presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.reportes.items_reportes.ItemReporteFallasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.items_reportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.reportes.items_reportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.repositorios.RepoReportes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportesController {

    private ReportesController(){}

    private static final String HELADERA_NAME_KEY = "heladera";
    private static final String CANTIDAD_DE_VIANDAS_KEY = "cantidadViandas";

    private static final String CANTIDAD_DE_FALLAS_KEY = "cantidadFallas";
    private static final String COLABORADOR_NAME_KEY = "colaborador";

    public static void mostrarFallas(Context ctx) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(ctx));
        List<ItemReporteFallasPorHeladera> fallas = RepoReportes.getInstancia().obtenerFallasPorHeladera();

        // Prepara el modelo con el tamaño de las fallas
        List<Map<String, String>> fallasModel = fallas.stream()
                .map(falla -> Map.of(
                        HELADERA_NAME_KEY, falla.getHeladera().getNombre(),
                        CANTIDAD_DE_FALLAS_KEY, String.valueOf(fallas.size())
                ))
                .toList();

        model.put("fallas", fallasModel);
        ctx.render("/views/reportes/reportes.mustache", model);
    }

    public static void mostrarViandasColocadas(Context ctx) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(ctx));
        List<ItemReporteViandasColocadasPorHeladera> viandasColocadas = RepoReportes.getInstancia().obtenerViandasColocadasPorHeladera();

        // Prepara el modelo con el tamaño de las viandas colocadas
        List<Map<String, String>> viandasColocadasModel = viandasColocadas.stream()
                .map(vianda -> Map.of(
                        HELADERA_NAME_KEY, vianda.getHeladera().getNombre(),
                        CANTIDAD_DE_VIANDAS_KEY, String.valueOf(viandasColocadas.size())
                ))
                .toList();

        model.put("viandasColocadas", viandasColocadasModel);
        ctx.render("/views/reportes/reporteColocadas.mustache", model);
    }

    public static void mostrarViandasDistribuidas(Context ctx) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(ctx));
        List<ItemReporteViandasDistribuidasPorColaborador> viandasDistribuidas = RepoReportes.getInstancia().obtenerViandasDistribuidasPorColaborador();

        // Prepara el modelo con el tamaño de las viandas distribuidas
        List<Map<String, String>> viandasDistribuidasModel = viandasDistribuidas.stream()
                .map(vianda -> Map.of(
                        COLABORADOR_NAME_KEY, vianda.getColaborador().getNombre(),
                        CANTIDAD_DE_VIANDAS_KEY, String.valueOf(viandasDistribuidas.size())
                ))
                .toList();

        model.put("viandasDistribuidas", viandasDistribuidasModel);
        ctx.render("views/reportes/reporteDistribuidas.mustache", model);
    }

    public static void mostrarViandasRetiradas(Context ctx) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(ctx));
        List<ItemReporteViandasRetiradasPorHeladera> viandasRetiradas = RepoReportes.getInstancia().obtenerViandasRetiradasPorHeladera();

        // Prepara el modelo con el tamaño de las viandas retiradas
        List<Map<String, String>> viandasRetiradasModel = viandasRetiradas.stream()
                .map(vianda -> Map.of(
                        HELADERA_NAME_KEY, vianda.getHeladera().getNombre(),
                        CANTIDAD_DE_VIANDAS_KEY, String.valueOf(viandasRetiradas.size())
                ))
                .toList();

        model.put("viandasRetiradas", viandasRetiradasModel);
        ctx.render("views/reportes/reporteRetiradas.mustache", model);
    }
}