package org.example.reportes.estrategiasReporte;

import org.example.reportes.itemsReportes.ItemReporte;

import java.time.LocalDateTime;
import java.util.List;

public interface EstrategiaReporte {
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual);
}
