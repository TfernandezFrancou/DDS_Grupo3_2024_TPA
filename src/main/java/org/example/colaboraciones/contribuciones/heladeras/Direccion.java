package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDireccion;

    private String nombreCalle;
    private String altura;
    private String localidad;

    public Direccion(String nombreCalle, String altura, String localidad) {
        this.nombreCalle = nombreCalle;
        this.altura = altura;
        this.localidad = localidad;
    }
}
