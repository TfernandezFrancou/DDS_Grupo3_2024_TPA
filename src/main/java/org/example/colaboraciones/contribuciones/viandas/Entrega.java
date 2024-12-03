package org.example.colaboraciones.contribuciones.viandas;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEntrega;

    @Enumerated(EnumType.STRING)
    private EstadoEntrega estadoEntrega;
    private LocalDate fechaEntrega;

    public Entrega() {
    }

    public Entrega(EstadoEntrega estadoEntrega, LocalDate fechaEntrega) {
        this.estadoEntrega = estadoEntrega;
        this.fechaEntrega = fechaEntrega;
    }
}
