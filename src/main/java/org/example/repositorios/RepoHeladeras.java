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

    private double calcularDistancia(Ubicacion ubicacion1, Ubicacion ubicacion2) {
        final int RADIO_TIERRA_KM = 6371; // Radio de la tierra en kilómetros
        double lat1 = ubicacion1.getLatitud();
        double lon1 = ubicacion1.getLongitud();
        double lat2 = ubicacion2.getLatitud();
        double lon2 = ubicacion2.getLongitud();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIO_TIERRA_KM * c;
    }
    public List<Heladera> buscarHeladerasCercanasA(Heladera heladeraDada, double distanciaMaximaKm) {

        List<Heladera> heladerasCercanas = new ArrayList<>();
        Ubicacion ubicacionDada = heladeraDada.getUbicacion();

        for (Heladera heladera : heladeras) {
            if (!heladera.equals(heladeraDada)) {
                double distancia = calcularDistancia(ubicacionDada, heladera.getUbicacion());
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