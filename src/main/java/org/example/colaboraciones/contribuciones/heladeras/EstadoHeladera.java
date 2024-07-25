package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class EstadoHeladera {
    @Getter
    private final Boolean estaActiva;
    private final LocalDateTime fechaHoraInicio;
    @Setter
    private LocalDateTime fechaHoraFin;
    
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
