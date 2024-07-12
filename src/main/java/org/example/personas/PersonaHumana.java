package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Colaborador;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter

public class PersonaHumana extends Persona {
    //el nomobre ya esta en Persona
    private String apellido;
    private LocalDate fechaNacimiento;

    public PersonaHumana() {
    }

    public static PersonaHumana fromCsv(String[] columnas) {
        PersonaHumana persona = new PersonaHumana();
        persona.setDocumento(Documento.fromCsv(Arrays.copyOfRange(columnas, 0, 2)));
        persona.setNombre(columnas[2]);
        persona.setApellido(columnas[3]);
        persona.addMedioDeContacto(new CorreoElectronico(columnas[4]));
        persona.setRol(new Colaborador());
        return persona;
    }

    @Override
    public String getNombre() {
        return super.getNombre() + " " + apellido;
    }
}
