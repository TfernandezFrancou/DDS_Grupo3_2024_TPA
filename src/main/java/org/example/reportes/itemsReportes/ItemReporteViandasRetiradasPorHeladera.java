package org.example.reportes.itemsReportes;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemReporteViandasRetiradasPorHeladera extends ItemReporte {

    private List<Vianda> viandasRetiradas;
    private Heladera heladera;

    public ItemReporteViandasRetiradasPorHeladera() {
        this.viandasRetiradas = new ArrayList<>();
    }
}
