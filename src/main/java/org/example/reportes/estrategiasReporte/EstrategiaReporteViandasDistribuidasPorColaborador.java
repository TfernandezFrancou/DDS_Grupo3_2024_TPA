package org.example.reportes.estrategiasReporte;

import org.example.reportes.itemsReportes.ItemReporte;
import org.example.repositorios.RepoPersona;

import java.time.LocalDateTime;
import java.util.List;

public class EstrategiaReporteViandasDistribuidasPorColaborador implements EstrategiaReporte{
    @Override
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual) {
        return RepoPersona.getInstancia().obtenerCantidadDeViandasDistribuidasPorColaborador(inicioSemanaActual, finSemanaActual);
    }
}