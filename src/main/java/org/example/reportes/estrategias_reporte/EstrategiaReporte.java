package org.example.reportes.estrategias_reporte;

import org.example.reportes.items_reportes.ItemReporte;

import java.time.LocalDateTime;
import java.util.List;

public interface EstrategiaReporte {
    public List<ItemReporte> generarReporte(LocalDateTime inicioSemanaActual, LocalDateTime  finSemanaActual);
}
