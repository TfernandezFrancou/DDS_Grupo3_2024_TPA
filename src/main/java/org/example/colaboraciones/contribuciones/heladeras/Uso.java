package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Uso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUso;

    private LocalDateTime fechaHoraDeUso;

    @ManyToOne
    private Heladera heladera;

    public Uso(LocalDateTime fechaHoraDeUso, Heladera heladera) {
        this.fechaHoraDeUso = fechaHoraDeUso;
        this.heladera = heladera;
    }

}
