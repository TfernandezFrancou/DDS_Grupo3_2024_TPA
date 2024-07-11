package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.MedioDeContacto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

public class PersonaHumana extends Persona {
    //el nomobre ya esta en Persona
    private String apellido;
    private LocalDate fechaNacimiento;

    public PersonaHumana() {
    }
    public PersonaHumana(String nombre) {
        super.setNombre(nombre);
    }


    @Override
    public String getNombre() {
        return super.getNombre() + " " + apellido;
    }
}
