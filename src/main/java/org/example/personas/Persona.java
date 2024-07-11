package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.roles.Rol;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public abstract class Persona {
    private String nombre;
    private String direccion;
    private Rol rol;
    private Documento documento;
    private List<MedioDeContacto> mediosDeContacto;

    public Persona(){
        this.mediosDeContacto = new ArrayList<>();
    }

    public void addMedioDeContacto(MedioDeContacto medioDeContacto){
        this.mediosDeContacto.add(medioDeContacto);
    }

}
