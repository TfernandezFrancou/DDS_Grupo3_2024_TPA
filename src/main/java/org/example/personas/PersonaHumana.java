package org.example.personas;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class PersonaHumana extends Colaborador {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    @Override
    public String getNombre() {
        return nombre + " " + apellido;
    }
}
