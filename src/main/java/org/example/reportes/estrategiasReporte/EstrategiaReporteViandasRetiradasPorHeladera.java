package org.example.reportes.estrategiasReporte;

import org.example.reportes.itemsReportes.ItemReporte;
import org.example.repositorios.RepoHeladeras;

import java.time.LocalDateTime;
import java.util.List;

public class EstrategiaReporteViandasRetiradasPorHeladera implements EstrategiaReporte{
    @Override
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual) {
        return RepoHeladeras.getInstancia().obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);
    }
}
