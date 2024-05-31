package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.MedioDeContacto;
import org.example.colaboraciones.Contribucion;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public abstract class Colaborador {
    private List<MedioDeContacto> mediosDeContacto;
    private String direccion;
    private List<Contribucion> formasContribucion;
    private int puntuaje;

    public Colaborador(){
        this.mediosDeContacto = new ArrayList<>();
        this.formasContribucion = new ArrayList<>();
    }


    public abstract String getNombre();

    public void cambiarContribuciones(Contribucion contribucion)
    {
        this.formasContribucion.add(contribucion);
        //TODO Actualizar en el diagrama
    }

    public void calcularPuntuaje()
    {
        //TODO metodo calcularPuntuaje



    }

    public void canjearPuntuaje(int puntos)
    {
        //TODO metodo canjearPuntuaje
    }
}
