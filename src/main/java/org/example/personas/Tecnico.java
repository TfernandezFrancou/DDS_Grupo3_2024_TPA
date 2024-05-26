package org.example.personas;

import org.example.personas.documentos.Documento;
import org.example.recomendacion.Zona;
import org.example.personas.contacto.MedioDeContacto;

import java.util.List;

public class Tecnico {
    private String nombre;
    private String apellido;
    private Documento documento;
    private String cuil;
    private List<MedioDeContacto> mediosDeContacto;
    private List<Zona> areasDeCobertura;
}
