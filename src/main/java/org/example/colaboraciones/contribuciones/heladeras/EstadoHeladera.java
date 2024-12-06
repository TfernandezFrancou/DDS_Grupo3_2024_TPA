package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

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

    public EstadoHeladera() {
        estaActiva = true;
        fechaHoraInicio = LocalDateTime.now();
        fechaHoraFin = null;
    }

    public EstadoHeladera(boolean activa) {
        this.estaActiva = activa;
        this.fechaHoraInicio = LocalDateTime.now();
        this.fechaHoraFin = null;
    }

    public int mesesActivos() {
        int fechaInicioSegundos = (int) fechaHoraInicio.toEpochSecond(ZoneOffset.UTC);
        int fechaFinSegundos;
        fechaFinSegundos = (int) Objects.requireNonNullElseGet(this.fechaHoraFin, LocalDateTime::now).toEpochSecond(ZoneOffset.UTC);
        int segundos = fechaFinSegundos - fechaInicioSegundos;
        int segundosEnUnMes = 60 * 60 * 24 * 30;
        return segundos / segundosEnUnMes;
    }
}

