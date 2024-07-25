package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.documentos.Documento;
import org.example.recomendacion.Zona;
import org.example.personas.contacto.MedioDeContacto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Tecnico extends Rol {
    private String apellido;
    private String cuil;
    private List<Zona> areasDeCobertura;

    public Tecnico(){
        this.areasDeCobertura = new ArrayList<>();
    }

    public void agregarAreaDeCovertura(Zona areaDeCovertura){
        this.areasDeCobertura.add(areaDeCovertura);
    }
}
