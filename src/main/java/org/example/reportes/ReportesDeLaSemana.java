package org.example.reportes;

import lombok.Getter;
import lombok.Setter;
import org.example.reportes.items_reportes.ItemReporte;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class ReportesDeLaSemana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReportesDeLaSemana;

    @OneToMany
    @JoinColumn(name = "id_reporte_de_la_semana")
    private List<ItemReporte> reporteCantidadDeFallasPorHeladera;

    @OneToMany
    @JoinColumn(name = "id_reporte_de_la_semana")
    private List<ItemReporte> reporteCantidadDeViandasColocadasPorHeladera;

    @OneToMany
    @JoinColumn(name = "id_reporte_de_la_semana")
    private List<ItemReporte> reporteCantidadDeViandasRetiradasPorHeladera;

    @OneToMany
    @JoinColumn(name = "id_reporte_de_la_semana")
    private List<ItemReporte> reporteCantidadDeviandasDistribuidasPorColaborador;

    private LocalDate fechaInicioSemana;
    private LocalDate fechaFinSemana;
}
