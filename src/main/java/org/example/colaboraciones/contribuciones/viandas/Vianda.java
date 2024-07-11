package org.example.colaboraciones.contribuciones.viandas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.roles.Colaborador;

import java.time.LocalDateTime;

@Getter
@Setter
public class Vianda {
    private String descripcion;
    private LocalDateTime fechaCaducidad;
    private LocalDateTime fechaDonacion;
    private Colaborador colaborador;
    private Heladera heladera;
    private int calorias;
    private float peso;
    private Entrega entrega;
}
