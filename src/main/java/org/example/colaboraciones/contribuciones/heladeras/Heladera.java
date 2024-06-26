package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Heladera {
    private Ubicacion ubicacion;
    private String direccion;
    private String nombre;
    private Integer capacidadEnViandas;
    private LocalDate fechaInicioFuncionamiento;
    private EstadoHeladera estadoHeladeraActual;
    private List<EstadoHeladera> historialEstadoHeldera;
    private TemperaturaHeladera temperaturasDeFuncionamiento;

    public Heladera(){
        this.historialEstadoHeldera = new ArrayList<>();
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

    public void actualizarEstadoHeladera(Sensor sensor) {
        boolean nuevoEstado = sensor.getEstadoHeladera();
        if (this.estadoHeladeraActual.getEstaActiva() != nuevoEstado) {
            this.estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeldera.add(this.estadoHeladeraActual);
        }
    }
}
