package org.example.reportes.items_reportes;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id_item_reporte")
public class ItemReporteViandasRetiradasPorHeladera extends ItemReporte {

    @ManyToMany
    @JoinTable(name="ItemReporteViandasRetiradasPorHeladeraXVianda")
    private List<Vianda> viandasRetiradas;
    @ManyToOne
    private Heladera heladera;

    public ItemReporteViandasRetiradasPorHeladera() {
        this.viandasRetiradas = new ArrayList<>();
    }
}
