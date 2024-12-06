package org.example.presentacion.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LocalidadDTO {

    private String nombreLocalidad;
    private int cantidadDePersonas;
    private List<String> nombresYApellidosDePersonas;

    public LocalidadDTO(){
        this.nombresYApellidosDePersonas = new ArrayList<>();
    }

    public void agregarPersona(String nombreYApellido){
        this.nombresYApellidosDePersonas.add(nombreYApellido);
    }



}
