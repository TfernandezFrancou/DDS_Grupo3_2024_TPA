package org.example.contribuciones.heladeras;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Heladera {
    private String direccion;
    private String nombre;
    private Integer capacidadEnViandas;
    private LocalDate fechaInicioFuncionamiento;
    private EstadoHeladera estadoHeladeraActual;
    private List<EstadoHeladera> historialEstadoHeldera;
    private List<Vianda> viandasEnHeladera;

    public void agregarVianda(Vianda vianda) {
        this.viandasEnHeladera.add(vianda);
    }

    public boolean estaActiva() {
        return this.estadoHeladeraActual.getEstaActiva();
    }

    public int obtenerMesesActivos() {
        return this.historialEstadoHeldera.stream()
                .filter(EstadoHeladera::getEstaActiva)
                .map(EstadoHeladera::mesesActivos)
                .reduce(0, Integer::sum);
    }

    public void actualizarEstadoHeladera(boolean nuevoEstado) {
        if (this.estadoHeladeraActual.getEstaActiva() != nuevoEstado) {
            this.estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeldera.add(this.estadoHeladeraActual);
        }
    }
}
