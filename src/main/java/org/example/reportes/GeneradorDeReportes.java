package org.example.reportes;

import lombok.Getter;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public class GeneradorDeReportes {

    private List<ItemReporteHeladera> reporteCantidadDeFallasPorHeladera;
    private List<ItemReporteHeladera> reporteCantidadDeViandasColocadasPorHeladera;
    private List<ItemReporteHeladera> reporteCantidadDeViandasRetiradasPorHeladera;
    private  List<ItemReporteColaborador> reporteCantidadDeviandasDistribuidasPorColaborador;

    private LocalDateTime inicioSemanaActual;
    private LocalDateTime finSemanaActual;

    public void  generarReporteDeCantidadDeFallasPorHeladera() {
        reporteCantidadDeFallasPorHeladera = RepoIncidente.getInstancia().obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);
    }
    public void generarReporteCantidadDeViandasColocadasPorHeladera() {
        reporteCantidadDeViandasColocadasPorHeladera = RepoHeladeras.getInstancia().obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);

    }
    public void generarReporteCantidadDeViandasRetiradasPorHeladera(){
        reporteCantidadDeViandasRetiradasPorHeladera = RepoHeladeras.getInstancia().obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);
    }
    public void generarReporteCantidadDeViandasDistribuidasPorColaborador(){
        reporteCantidadDeviandasDistribuidasPorColaborador = RepoPersona.getInstancia().obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemanaActual, finSemanaActual);
    }

    public void generarReportesDeLaSemana(){
        // calculo el inicio y fin de la semana actual
         LocalDateTime now = LocalDateTime.now();
         inicioSemanaActual = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
         finSemanaActual = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).toLocalDate().atTime(23, 59, 59);


        // Generar todos los reportes
        generarReporteDeCantidadDeFallasPorHeladera();
        generarReporteCantidadDeViandasColocadasPorHeladera();
        generarReporteCantidadDeViandasRetiradasPorHeladera();
        generarReporteCantidadDeViandasDistribuidasPorColaborador();
    }

    public void generarReportesSemanalmente(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Programar para que se ejecute una vez por semana
        scheduler.scheduleAtFixedRate(this::generarReportesDeLaSemana, 0, 7, TimeUnit.DAYS);
    }


}
