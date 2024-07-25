package org.example.repositorios;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.incidentes.FallaTecnica;
import org.example.reportes.ItemReporteHeladera;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class RepoHeladeras {
    private List<Heladera> heladeras;

    private static RepoHeladeras instancia = null;
    private RepoHeladeras() {
        this.heladeras = new ArrayList<>();
    }

    public static RepoHeladeras getInstancia() {
        if (instancia == null) {
            RepoHeladeras.instancia = new RepoHeladeras();
        }
        return instancia;
    }

    public void clean(){
        this.heladeras.clear();
    }

    public void agregarTodas(List<Heladera> heladeras) {
        this.heladeras.addAll(heladeras);
    }

    public void agregar(Heladera heladera) {
        this.heladeras.add(heladera);
    }

    public void actualizar(Heladera heladeraActualizada) {
        Heladera heladeraGuardada = this.buscarHeladera(heladeraActualizada);

        heladeraGuardada.setCapacidadEnViandas(heladeraActualizada.getCapacidadEnViandas());
        heladeraGuardada.setViandasEnHeladera(heladeraActualizada.getViandasEnHeladera());
        heladeraGuardada.setFechaInicioFuncionamiento(heladeraActualizada.getFechaInicioFuncionamiento());
        heladeraGuardada.setEstadoHeladeraActual(heladeraActualizada.getEstadoHeladeraActual());
        heladeraGuardada.setHistorialEstadoHeldera(heladeraActualizada.getHistorialEstadoHeldera());
        heladeraGuardada.setHistorialMovimientos(heladeraActualizada.getHistorialMovimientos());
        heladeraGuardada.setTemperaturasDeFuncionamiento(heladeraActualizada.getTemperaturasDeFuncionamiento());
    }

    public void eliminar(Heladera heladera) {
        this.heladeras.remove(heladera);
    }

    public List<Heladera> buscarHeladerasActivas() {
        return heladeras.stream().filter(Heladera::estaActiva).toList();
    }

    public List<Heladera> buscarHeladerasCercanasA(Heladera heladeraDada, double distanciaMaximaKm) {

        List<Heladera> heladerasCercanas = new ArrayList<>();
        Ubicacion ubicacionDada = heladeraDada.getUbicacion();

        for (Heladera heladera : heladeras) {
            if (!heladera.equals(heladeraDada)) {
                double distancia = ubicacionDada.calcularDistanciaA(heladera.getUbicacion());
                if (distancia <= distanciaMaximaKm) {
                    heladerasCercanas.add(heladera);
                }
            }
        }

        return heladerasCercanas;
    }



    public Heladera buscarHeladera(Heladera heladeraAEncontrar){
        return this.heladeras.stream().filter(
                heladeraN -> heladeraN.getNombre().equals(heladeraAEncontrar.getNombre())
        ).findFirst().get();
    }
    public List<ItemReporteHeladera> obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerCantidadDeViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, MovimientoViandas::getCantidadDeViandasIntroducidas);
    }

    public List<ItemReporteHeladera> obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerCantidadDeViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, MovimientoViandas::getCantidadDeViandasSacadas);
    }

    private List<ItemReporteHeladera> obtenerCantidadDeViandasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual, ToIntFunction<MovimientoViandas> extractorDeCantidad) {
        List<ItemReporteHeladera> reporte = new ArrayList<>();

        for (Heladera heladera : heladeras) {
            List<MovimientoViandas> movimientosDeLaSemana = heladera.getHistorialMovimientos().stream()
                    .filter(movimiento ->
                            (movimiento.getFechaMovimiento().isAfter(inicioSemanaActual) &&
                                    movimiento.getFechaMovimiento().isBefore(finSemanaActual)) ||
                                    (movimiento.getFechaMovimiento().equals(inicioSemanaActual) ||
                                            movimiento.getFechaMovimiento().equals(finSemanaActual)
                                    )
                    ).toList();

            int cantidadViandasEnLaSemana = movimientosDeLaSemana.stream().mapToInt(extractorDeCantidad).sum();
            reporte.add(new ItemReporteHeladera(cantidadViandasEnLaSemana, heladera));
        }

        return reporte;
    }
}