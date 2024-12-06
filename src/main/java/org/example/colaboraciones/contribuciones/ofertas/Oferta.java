package org.example.colaboraciones.contribuciones.ofertas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOferta;

    private String nombre;
    private Integer puntosNecesarios;

    @Column(columnDefinition = "TEXT")//ya que puede ser valores muy largos que en un varchar no entra
    private String imagenURL;


    public Oferta(String nombre, Integer puntosNecesarios, String imagenURL) {
        this.nombre = nombre;
        this.puntosNecesarios = puntosNecesarios;
        this.imagenURL = imagenURL;
    }
}
