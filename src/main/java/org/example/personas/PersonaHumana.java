package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Rol;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter

public class PersonaHumana extends Persona {
    //el nomobre ya esta en Persona
    private String apellido;
    private LocalDate fechaNacimiento;

    public PersonaHumana() {}

    public PersonaHumana(String nombre, String apellido, MedioDeContacto medioDeContacto, Documento documento, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mediosDeContacto.add(medioDeContacto);
        this.documento = documento;
        this.rol = rol;
    }

    public static PersonaHumana fromCsv(String[] columnas) throws ParseException {
        if (columnas.length != 5) {
            throw new ParseException("Deberian ser 5 columnas", 0);
        }
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
