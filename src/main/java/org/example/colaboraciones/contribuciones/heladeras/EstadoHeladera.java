package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Getter
@Setter
public class EstadoHeladera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEstadoHeladera;

    @Column(columnDefinition = "INT")
    private  Boolean estaActiva;
    private  LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    @ManyToOne
    private Heladera heladera;

    public EstadoHeladera(boolean activa) {
        this.estaActiva = activa;
        this.fechaHoraInicio = LocalDateTime.now();
        this.fechaHoraFin = null;
    }

    public int mesesActivos() {
        int fechaInicioSegundos = (int) fechaHoraInicio.toEpochSecond(ZoneOffset.UTC);
        int fechaFinSegundos;
        if (this.fechaHoraFin == null) {
            fechaFinSegundos = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        } else {
            fechaFinSegundos = (int) this.fechaHoraFin.toEpochSecond(ZoneOffset.UTC);
        }
        int segundos = fechaFinSegundos - fechaInicioSegundos;
        int segundosEnUnMes = 60 * 60 * 24 * 30;
        return segundos / segundosEnUnMes;
    }
}

