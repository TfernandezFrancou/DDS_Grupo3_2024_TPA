package org.example.reportes;

import lombok.Getter;
import lombok.Setter;
import org.example.reportes.itemsReportes.ItemReporte;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReportesDeLaSemana {

    private int idReportesDeLaSemana;
    private List<ItemReporte> reporteCantidadDeFallasPorHeladera;
    private List<ItemReporte> reporteCantidadDeViandasColocadasPorHeladera;
    private List<ItemReporte> reporteCantidadDeViandasRetiradasPorHeladera;
    private List<ItemReporte> reporteCantidadDeviandasDistribuidasPorColaborador;
    private LocalDate fechaInicioSemana;
    private LocalDate fechaFinSemana;

}
