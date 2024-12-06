package org.example.reportes.items_reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.FallaTecnica;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id_item_reporte")
public class ItemReporteFallasPorHeladera extends ItemReporte{

    @OneToMany
    @JoinColumn(name = "id_item_reporte")
    private List<FallaTecnica> fallas;
    @ManyToOne
    private Heladera heladera;

    public ItemReporteFallasPorHeladera() {
        this.fallas = new ArrayList<>();
    }
}
