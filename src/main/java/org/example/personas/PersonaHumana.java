package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.MedioDeContacto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter

public class PersonaHumana extends Colaborador {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;

    public PersonaHumana() {
    }
    public PersonaHumana(List<MedioDeContacto> mediosDeContacto) {
        super(mediosDeContacto);
    }


    @Override
    public String getNombre() {
        return nombre + " " + apellido;
    }
}
