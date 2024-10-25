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
public class ItemReporteViandasColocadasPorHeladera extends ItemReporte{

    private List<Vianda> viandasColocadas;
    private Heladera heladera;

    public ItemReporteViandasColocadasPorHeladera() {
        this.viandasColocadas = new ArrayList<>();
    }
}
