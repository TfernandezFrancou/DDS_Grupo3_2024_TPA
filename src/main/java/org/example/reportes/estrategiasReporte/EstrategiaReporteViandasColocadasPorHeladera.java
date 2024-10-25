package org.example.reportes.estrategiasReporte;

import org.example.reportes.itemsReportes.ItemReporte;
import org.example.repositorios.RepoHeladeras;

import java.time.LocalDateTime;
import java.util.List;

public class EstrategiaReporteViandasColocadasPorHeladera implements EstrategiaReporte{

    @Override
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual) {
        return RepoHeladeras.getInstancia().obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual);
    }
}
