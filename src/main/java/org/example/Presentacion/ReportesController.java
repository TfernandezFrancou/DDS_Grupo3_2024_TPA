package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.incidentes.FallaTecnica;
import org.example.reportes.itemsReportes.ItemReporteFallasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.reportes.itemsReportes.ItemReporteViandasRetiradasPorHeladera;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoReportes;
import org.example.reportes.ReportesDeLaSemana;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportesController {

    public static void getListaReportes(@NotNull Context context) {
        List<ReportesDeLaSemana> reportes = RepoReportes.getInstancia().obtenerReportes();
        Map<String, Object> model = new HashMap<>();
        model.put("reportes", reportes);
        context.render("/views/reportes/reportes.mustache", model);
    }

    public static void getDetalleFalla(Context context) {
    int idFalla = Integer.parseInt(context.pathParam("id"));
    FallaTecnica falla = RepoIncidente.getInstancia().obtenerFallaPorId(idFalla);

        if (falla != null) {
            Map<String, Object> model = new HashMap<>();
            model.put("falla", falla);
            context.render("/views/fallas/detalle_falla.mustache", model);
        } else {
            context.status(404).result("Falla no encontrada");//TODO esto no es cliente liviano
        }
    }

    public static void getReporteFallas(Context context) {
        List<ItemReporteFallasPorHeladera> reportes = RepoReportes.getInstancia().obtenerReporteFallas();
        Map<String, Object> model = new HashMap<>();
        model.put("reportes", reportes);
        context.json(model);//TODO esto no es cliente liviano
    }

    public static void getReporteViandasColocadas(Context context) {
        List<ItemReporteViandasColocadasPorHeladera> reportes = RepoReportes.getInstancia().obtenerReporteViandasColocadas();
        Map<String, Object> model = new HashMap<>();
        model.put("reportes", reportes);
        context.json(model);//TODO esto no es cliente liviano
    }

    public static void getReporteViandasDistribuidas(Context context) {
        List<ItemReporteViandasDistribuidasPorColaborador> reportes = RepoReportes.getInstancia().obtenerReporteViandasDistribuidas();
        Map<String, Object> model = new HashMap<>();
        model.put("reportes", reportes);
        context.json(model);//TODO esto no es cliente liviano
    }

    public static void getReporteViandasRetiradas(Context context) {
        List<ItemReporteViandasRetiradasPorHeladera> reportes = RepoReportes.getInstancia().obtenerReporteViandasRetiradas();
        Map<String, Object> model = new HashMap<>();
        model.put("reportes", reportes);
        context.json(model);//TODO esto no es cliente liviano
    }
}