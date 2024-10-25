package org.example.repositorios;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.Alerta;
import org.example.incidentes.FallaTecnica;
import org.example.incidentes.Incidente;
import org.example.reportes.itemsReportes.ItemReporte;
import org.example.reportes.itemsReportes.ItemReporteFallasPorHeladera;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private List<FallaTecnica> obtenerTodasLasFallasTecnicasDeHeladera(Heladera heladera, LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){
        return this.obtenerTodasLasFallasTecnicas().stream()
                .filter(falla -> (falla.getHeladera().equals(heladera)) &&
                        ((falla.getFechaDeEmision().isAfter(inicioSemanaActual) && falla.getFechaDeEmision().isBefore(finSemanaActual)
                        ) || (falla.getFechaDeEmision().equals(inicioSemanaActual) || falla.getFechaDeEmision().equals(finSemanaActual)))
                )
                .map(FallaTecnica.class::cast)           // Hago el cast a la clase FallaTecnica
                .collect(Collectors.toList());
    }

    private List<Heladera> obtenerHeladerasConFallasTecnicasEnLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){
        return this.obtenerTodasLasFallasTecnicas().stream()
                .filter(falla ->
                        (falla.getFechaDeEmision().isAfter(inicioSemanaActual) && falla.getFechaDeEmision().isBefore(finSemanaActual)
                        ) || (falla.getFechaDeEmision().equals(inicioSemanaActual) || falla.getFechaDeEmision().equals(finSemanaActual))
                )
                .map(Incidente::getHeladera)
                .distinct() //elimino duplicados
                .toList();
    }

    public List<ItemReporte> obtenerCantidadDeFallasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual){

        List<Heladera> heladerasConFallas = this.obtenerHeladerasConFallasTecnicasEnLaSemana(inicioSemanaActual, finSemanaActual);


        List<ItemReporte> reporte = new ArrayList<>();
        for (Heladera heladera : heladerasConFallas) {

            ItemReporteFallasPorHeladera itemReporte = new ItemReporteFallasPorHeladera();
            itemReporte.setHeladera(heladera);
            itemReporte.setFallas(obtenerTodasLasFallasTecnicasDeHeladera(heladera, inicioSemanaActual, finSemanaActual));
            reporte.add(itemReporte);
        }

        return reporte;
    }

    public void clean() {
        this.incidentes.clear();
    }
}
