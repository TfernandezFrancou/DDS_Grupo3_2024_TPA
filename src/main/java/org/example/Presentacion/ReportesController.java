package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.reportes.itemsReportes.ItemReporteFallasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasDistribuidasPorColaborador;
import org.example.reportes.itemsReportes.ItemReporteViandasRetiradasPorHeladera;
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
/*
    public static void getDetalleReporte(@NotNull Context context) {
        int idReporte = Integer.parseInt(context.pathParam("id"));
        ReportesDeLaSemana reporte = RepoReportes.getInstancia().obtenerReportes()
                .stream()
                .filter(r -> r.getIdReportesDeLaSemana() == idReporte)
                .findFirst()
                .orElse(null);

        if (reporte != null) {
            Map<String, Object> model = new HashMap<>();
            model.put("reporte", reporte);
            context.render("/views/reportes/reporte_detalle.mustache", model);
        } else {
            context.status(404).result("Reporte no encontrado");
        }
    }*/

        public static void getReporteFallas(Context context) {
            List<ItemReporteFallasPorHeladera> reportes = RepoReportes.getInstancia().obtenerReporteFallas();
            Map<String, Object> model = new HashMap<>();
            model.put("reportes", reportes);
            context.json(model);
        }

        public static void getReporteViandasColocadas(Context context) {
            List<ItemReporteViandasColocadasPorHeladera> reportes = RepoReportes.getInstancia().obtenerReporteViandasColocadas();
            Map<String, Object> model = new HashMap<>();
            model.put("reportes", reportes);
            context.json(model);
        }

        public static void getReporteViandasDistribuidas(Context context) {
            List<ItemReporteViandasDistribuidasPorColaborador> reportes = RepoReportes.getInstancia().obtenerReporteViandasDistribuidas();
            Map<String, Object> model = new HashMap<>();
            model.put("reportes", reportes);
            context.json(model);
        }

        public static void getReporteViandasRetiradas(Context context) {
            List<ItemReporteViandasRetiradasPorHeladera> reportes = RepoReportes.getInstancia().obtenerReporteViandasRetiradas();
            Map<String, Object> model = new HashMap<>();
            model.put("reportes", reportes);
            context.json(model);
        }
}