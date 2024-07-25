package org.example.reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.roles.Colaborador;

@Getter
@Setter
@AllArgsConstructor
public class ItemReporteHeladera {
   private int cantidad;
    private Heladera heladera;
}
