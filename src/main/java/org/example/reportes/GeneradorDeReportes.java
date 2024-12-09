package org.example.reportes;

import lombok.Getter;
import org.example.reportes.estrategias_reporte.*;
import org.example.reportes.items_reportes.ItemReporte;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class GeneradorDeReportes {

    private ReportesDeLaSemana reportesSemanaActual ;
    private final List<EstrategiaReporte> estrategiasGenerarReportes ;
    private ScheduledExecutorService scheduler ;

    public GeneradorDeReportes() {
        this.estrategiasGenerarReportes = new ArrayList<>();

        this.estrategiasGenerarReportes.add(new EstrategiaReporteFallasPorHeladera());
        this.estrategiasGenerarReportes.add(new EstrategiaReporteViandasColocadasPorHeladera());
        this.estrategiasGenerarReportes.add(new EstrategiaReporteViandasDistribuidasPorColaborador());
        this.estrategiasGenerarReportes.add(new EstrategiaReporteViandasRetiradasPorHeladera());

    }

    public void generarReportesDeLaSemana(){
        // calculo el inicio y fin de la semana actual
         LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicioSemanaActual = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
        LocalDateTime  finSemanaActual = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);

         this.reportesSemanaActual = new ReportesDeLaSemana();

         List<ItemReporte> items;
        for (EstrategiaReporte estrategia: this.estrategiasGenerarReportes) {
            items= estrategia.generarReporte(inicioSemanaActual, finSemanaActual);

            if(estrategia instanceof EstrategiaReporteFallasPorHeladera){
                reportesSemanaActual.setReporteCantidadDeFallasPorHeladera(items);
            } else if (estrategia instanceof EstrategiaReporteViandasColocadasPorHeladera) {
                reportesSemanaActual.setReporteCantidadDeViandasColocadasPorHeladera(items);
            }else if (estrategia instanceof EstrategiaReporteViandasDistribuidasPorColaborador){
                reportesSemanaActual.setReporteCantidadDeviandasDistribuidasPorColaborador(items);
            }else if(estrategia instanceof EstrategiaReporteViandasRetiradasPorHeladera){
                reportesSemanaActual.setReporteCantidadDeViandasRetiradasPorHeladera(items);
            }
        }

        try {
            scheduler.close(); // cierro el scheduler cuando no se use
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReportesSemanalmente(){
        scheduler = Executors.newScheduledThreadPool(1);
        // Programar para que se ejecute una vez por semana
        scheduler.scheduleAtFixedRate(this::generarReportesDeLaSemana, 0, 7, TimeUnit.DAYS);
    }
}
