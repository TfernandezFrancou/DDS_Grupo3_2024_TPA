package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MovimientoViandas {
    private int cantidadDeViandasIntroducidas;
    private int cantidadDeViandasSacadas;
    private LocalDateTime fechaMovimiento;

}
