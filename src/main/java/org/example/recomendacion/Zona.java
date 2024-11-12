package org.example.recomendacion;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idZona;
    private String nombreZona;

    @OneToOne
    private Ubicacion ubicacion;
    private int radio;
}
