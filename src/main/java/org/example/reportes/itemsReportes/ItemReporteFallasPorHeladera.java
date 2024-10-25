package org.example.reportes.itemsReportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.FallaTecnica;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemReporteFallasPorHeladera extends ItemReporte{

    private List<FallaTecnica> fallas;
    private Heladera heladera;

    public ItemReporteFallasPorHeladera() {
        this.fallas = new ArrayList<>();
    }
}
