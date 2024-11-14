package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class TemperaturaHeladera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTemperaturaHeladera;


    private Integer temperaturaMinima;
    private Integer temperaturaMaxima;

    public TemperaturaHeladera(Integer temperaturaMinima, Integer temperaturaMaxima) {
        this.temperaturaMinima = temperaturaMinima;
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public TemperaturaHeladera() {
        temperaturaMinima = 0;
        temperaturaMaxima = 100;
    }
}
