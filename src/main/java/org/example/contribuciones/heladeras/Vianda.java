package org.example.contribuciones.heladeras;

import org.example.personas.Colaborador;

import java.time.LocalDateTime;

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
