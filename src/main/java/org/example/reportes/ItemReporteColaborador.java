package org.example.reportes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;

@Getter
@Setter
@AllArgsConstructor
public class ItemReporteColaborador {

    private int cantidad;
    private Persona colaborador;
}
