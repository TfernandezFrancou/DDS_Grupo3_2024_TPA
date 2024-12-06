package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.reportes.itemsReportes.*;
import org.example.autenticacion.SessionManager;
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

    public static void mostrarFallas(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        List<ItemReporteFallasPorHeladera> fallas = RepoReportes.getInstancia().obtenerFallasPorHeladera();

        // Prepara el modelo con el tamaño de las fallas
        List<Map<String, String>> fallasModel = fallas.stream()
                .map(falla -> Map.of(
                        "heladera", falla.getHeladera().getNombre(),
                        "cantidadFallas", String.valueOf(fallas.size())
                ))
                .toList();

        model.put("fallas", fallasModel);
        ctx.render("/views/reportes/reportes.mustache", model);
    }

    public static void mostrarViandasColocadas(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        List<ItemReporteViandasColocadasPorHeladera> viandasColocadas = RepoReportes.getInstancia().obtenerViandasColocadasPorHeladera();

        // Prepara el modelo con el tamaño de las viandas colocadas
        List<Map<String, String>> viandasColocadasModel = viandasColocadas.stream()
                .map(vianda -> Map.of(
                        "heladera", vianda.getHeladera().getNombre(),
                        "cantidadViandas", String.valueOf(viandasColocadas.size())
                ))
                .toList();

        model.put("viandasColocadas", viandasColocadasModel);
        ctx.render("/views/reportes/reporteColocadas.mustache", model);
    }

    public static void mostrarViandasDistribuidas(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        List<ItemReporteViandasDistribuidasPorColaborador> viandasDistribuidas = RepoReportes.getInstancia().obtenerViandasDistribuidasPorColaborador();

        // Prepara el modelo con el tamaño de las viandas distribuidas
        List<Map<String, String>> viandasDistribuidasModel = viandasDistribuidas.stream()
                .map(vianda -> Map.of(
                        "colaborador", vianda.getColaborador().getNombre(),
                        "cantidadViandas", String.valueOf(viandasDistribuidas.size())
                ))
                .toList();

        model.put("viandasDistribuidas", viandasDistribuidasModel);
        ctx.render("views/reportes/reporteDistribuidas.mustache", model);
    }

    public static void mostrarViandasRetiradas(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        List<ItemReporteViandasRetiradasPorHeladera> viandasRetiradas = RepoReportes.getInstancia().obtenerViandasRetiradasPorHeladera();

        // Prepara el modelo con el tamaño de las viandas retiradas
        List<Map<String, String>> viandasRetiradasModel = viandasRetiradas.stream()
                .map(vianda -> Map.of(
                        "heladera", vianda.getHeladera().getNombre(),
                        "cantidadViandas", String.valueOf(viandasRetiradas.size())
                ))
                .toList();

        model.put("viandasRetiradas", viandasRetiradasModel);
        ctx.render("views/reportes/reporteRetiradas.mustache", model);
    }
}