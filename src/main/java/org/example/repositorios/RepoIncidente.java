package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.Alerta;
import org.example.incidentes.FallaTecnica;
import org.example.incidentes.Incidente;
import org.example.reportes.ItemReporteHeladera;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepoIncidente {
    private List<Incidente> incidentes;

    private static RepoIncidente instancia = null;

    private RepoIncidente() {
        this.incidentes = new ArrayList<>();
    }

    public static RepoIncidente getInstancia() {
        if (instancia == null) {
            RepoIncidente.instancia = new RepoIncidente();
        }
        return instancia;
    }

    public void agregarFalla(FallaTecnica fallaTecnica) {
        this.incidentes.add(fallaTecnica);
    }

    public void agregarAlerta(Alerta alerta) {
        this.incidentes.add(alerta);
    }

    public void eliminarIncidente(Incidente incidente) {
        this.incidentes.remove(incidente);
    }

    public List<Incidente> obtenerTodasLasFallasTecnicas(){
        return this.incidentes.stream().filter(incidente -> incidente instanceof FallaTecnica).toList();
    }

    public List<Incidente> obtenerTodasLasAlertas(){
        return this.incidentes.stream().filter(incidente -> incidente instanceof Alerta).toList();
    }

    public List<ItemReporteHeladera> obtenerCantidadDeFallasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){

        Map<Heladera, Long> conteoFallasPorHeladera = this.obtenerTodasLasFallasTecnicas().stream()
                .filter(falla ->
                        (falla.getFechaDeEmision().isAfter(inicioSemanaActual) && falla.getFechaDeEmision().isBefore(finSemanaActual)
                        ) || (falla.getFechaDeEmision().equals(inicioSemanaActual) || falla.getFechaDeEmision().equals(finSemanaActual))
                )
                .collect(Collectors.groupingBy(Incidente::getHeladera, Collectors.counting()));

        List<ItemReporteHeladera> reporte = new ArrayList<>();
        for (Map.Entry<Heladera, Long> entry : conteoFallasPorHeladera.entrySet()) {
            reporte.add(new ItemReporteHeladera(Math.toIntExact(entry.getValue()), entry.getKey()));
        }
        return reporte;
    }

    public void clean() {
        this.incidentes.clear();
    }
}
