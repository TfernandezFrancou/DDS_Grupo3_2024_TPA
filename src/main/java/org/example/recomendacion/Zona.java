package org.example.recomendacion;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;

@Getter
@Setter

public class Zona {
    private String nombreZona;
    private Ubicacion ubicacion;
    private int radio;
}
