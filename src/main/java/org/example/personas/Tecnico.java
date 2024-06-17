package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.documentos.Documento;
import org.example.recomendacion.Zona;
import org.example.personas.contacto.MedioDeContacto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Tecnico {
    private String nombre;
    private String apellido;
    private Documento documento;
    private String cuil;
    private List<MedioDeContacto> mediosDeContacto;
    private List<Zona> areasDeCobertura;
    private boolean estaActivo;

    public Tecnico(){
        this.mediosDeContacto = new ArrayList<>();
        this.areasDeCobertura = new ArrayList<>();
        this.estaActivo = true;
    }

    public void darDeBaja()
    {
        this.estaActivo = false;
    }
}
