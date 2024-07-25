package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.FallaTecnica;
import org.example.reportes.ItemReporteHeladera;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepoFallasTecnicas {
    private List<FallaTecnica> fallasTecnicas;

    private static RepoFallasTecnicas instancia = null;

    private RepoFallasTecnicas() {
        this.fallasTecnicas = new ArrayList<>();
    }

    public static RepoFallasTecnicas getInstancia() {
        if (instancia == null) {
            RepoFallasTecnicas.instancia = new RepoFallasTecnicas();
        }
        return instancia;
    }

    public void agregarFalla(FallaTecnica fallaTecnica) {
        this.fallasTecnicas.add(fallaTecnica);
    }

    public void eliminarFalla(FallaTecnica fallaTecnica) {
        this.fallasTecnicas.remove(fallaTecnica);
    }

    public List<ItemReporteHeladera> obtenerCantidadDeFallasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){

        Map<Heladera, Long> conteoFallasPorHeladera = fallasTecnicas.stream()
                .filter(falla ->
                        (falla.getFechaDeEmision().isAfter(inicioSemanaActual) && falla.getFechaDeEmision().isBefore(finSemanaActual)
                        ) || (falla.getFechaDeEmision().equals(inicioSemanaActual) || falla.getFechaDeEmision().equals(finSemanaActual))
                )
                .collect(Collectors.groupingBy(FallaTecnica::getHeladera, Collectors.counting()));

        List<ItemReporteHeladera> reporte = new ArrayList<>();
        for (Map.Entry<Heladera, Long> entry : conteoFallasPorHeladera.entrySet()) {
            reporte.add(new ItemReporteHeladera(Math.toIntExact(entry.getValue()), entry.getKey()));
        }
        return reporte;
    }

    public void clean() {
        this.fallasTecnicas.clear();
    }
}
