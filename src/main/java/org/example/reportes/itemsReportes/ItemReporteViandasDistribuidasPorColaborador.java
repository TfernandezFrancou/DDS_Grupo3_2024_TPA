package org.example.reportes.itemsReportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.Persona;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemReporteViandasDistribuidasPorColaborador extends ItemReporte{
    private List<Vianda> viandasDistribuidas;
    private Persona colaborador;

    public ItemReporteViandasDistribuidasPorColaborador() {
        this.viandasDistribuidas = new ArrayList<>();
    }
}
