package org.example.colaboraciones.contribuciones.heladeras;

import java.time.LocalDateTime;

public class MovimientoViandas {
    private int viandasIntroducidas;
    private int viandasSacadas;
    private LocalDateTime fecha;

    public MovimientoViandas(int viandasIntroducidas, int viandasSacadas, LocalDateTime fecha) {
        this.viandasIntroducidas = viandasIntroducidas;
        this.viandasSacadas = viandasSacadas;
        this.fecha = fecha;
    }
}
