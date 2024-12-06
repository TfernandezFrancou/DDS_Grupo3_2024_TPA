package org.example.reportes.items_reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.Persona;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id_item_reporte")
public class ItemReporteViandasDistribuidasPorColaborador extends ItemReporte{

    @ManyToMany
    @JoinTable(name="ItemReporteViandasDistribuidasPorColaboradorXVianda")
    private List<Vianda> viandasDistribuidas;

    @ManyToOne
    private Persona colaborador;

    public ItemReporteViandasDistribuidasPorColaborador() {
        this.viandasDistribuidas = new ArrayList<>();
    }
}
