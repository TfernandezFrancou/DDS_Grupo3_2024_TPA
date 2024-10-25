package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MovimientoViandas {
    private List<Vianda> viandasIntroducidas;
    private List<Vianda> viandasSacadas;
    private LocalDateTime fechaMovimiento;

}
