package org.example.reportes.estrategiasReporte;

import org.example.reportes.itemsReportes.ItemReporte;
import org.example.repositorios.RepoIncidente;

import java.time.LocalDateTime;
import java.util.List;

public class EstrategiaReporteFallasPorHeladera implements EstrategiaReporte{
    @Override
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual) {
        return  RepoIncidente.getInstancia().obtenerCantidadDeFallasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);
    }
}
