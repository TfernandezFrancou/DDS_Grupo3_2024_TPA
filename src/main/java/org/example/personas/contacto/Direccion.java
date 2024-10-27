package org.example.personas.contacto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Direccion {
    private String nombreCalle;
    private String altura;
    private String localidad;
}
