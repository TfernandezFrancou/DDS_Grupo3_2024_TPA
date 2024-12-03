package org.example.colaboraciones.contribuciones.viandas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.roles.Colaborador;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Vianda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idVianda;

    private String descripcion;
    private LocalDateTime fechaCaducidad;
    private LocalDateTime fechaDonacion;

    @ManyToOne
    private Colaborador colaborador;

    @ManyToOne
    private Heladera heladera;

    private int calorias;
    private float peso;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Entrega entrega;

    public Vianda(String descripcion, int calorias, float peso) {
        this.descripcion = descripcion;
        this.calorias = calorias;
        this.peso = peso;
    }
}
