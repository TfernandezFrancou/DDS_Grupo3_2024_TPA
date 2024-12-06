package org.example.reportes.estrategias_reporte;

import org.example.reportes.items_reportes.ItemReporte;
import org.example.repositorios.RepoHeladeras;

import java.time.LocalDateTime;
import java.util.List;

public class EstrategiaReporteViandasRetiradasPorHeladera implements EstrategiaReporte{
    @Override
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual) {
        return RepoHeladeras.getInstancia().obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);
    }
}
