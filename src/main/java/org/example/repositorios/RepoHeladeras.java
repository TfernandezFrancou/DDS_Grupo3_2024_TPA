package org.example.repositorios;

import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.MovimientoViandas;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.reportes.itemsReportes.ItemReporte;
import org.example.reportes.itemsReportes.ItemReporteViandasColocadasPorHeladera;
import org.example.reportes.itemsReportes.ItemReporteViandasRetiradasPorHeladera;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<ItemReporte> obtenerCantidadDeViandasColocadasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, true);
    }

    public List<ItemReporte> obtenerCantidadDeViandasRetiradasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual) {
        return obtenerViandasPorHeladeraDeLaSemana(inicioSemanaActual, finSemanaActual, false);
    }

    private List<ItemReporte> obtenerViandasPorHeladeraDeLaSemana(LocalDateTime inicioSemanaActual, LocalDateTime finSemanaActual, boolean viandasColocadas) {
        List<ItemReporte> reporte = new ArrayList<>();


        for (Heladera heladera : heladeras) {
            List<MovimientoViandas> movimientosDeLaSemana = heladera.getHistorialMovimientos().stream()
                    .filter(movimiento ->
                            (movimiento.getFechaMovimiento().isAfter(inicioSemanaActual) &&
                                    movimiento.getFechaMovimiento().isBefore(finSemanaActual)) ||
                                    (movimiento.getFechaMovimiento().equals(inicioSemanaActual) ||
                                            movimiento.getFechaMovimiento().equals(finSemanaActual)
                                    )
                    ).toList();

            Function<MovimientoViandas, Stream<Vianda>> extractorFuncion;
            if(viandasColocadas){//si necesito viandas colocadas
                extractorFuncion = (movimientoViandas -> movimientoViandas.getViandasIntroducidas().stream());
            } else {//si necesito viandas retiradas
                extractorFuncion = (movimientoViandas -> movimientoViandas.getViandasSacadas().stream());
            }

            List<Vianda> viandas =  movimientosDeLaSemana.stream()
                    .flatMap(extractorFuncion)
                    .collect(Collectors.toList());

            if(viandasColocadas){//si necesito viandas colocadas
                ItemReporteViandasColocadasPorHeladera item = new ItemReporteViandasColocadasPorHeladera();
                item.setHeladera(heladera);
                item.setViandasColocadas(viandas);
                reporte.add(item);
            } else {//si necesito viandas retiradas
                ItemReporteViandasRetiradasPorHeladera item = new ItemReporteViandasRetiradasPorHeladera();
                item.setHeladera(heladera);
                item.setViandasRetiradas(viandas);
                reporte.add(item);
            }
        }

        return reporte;
    }
}