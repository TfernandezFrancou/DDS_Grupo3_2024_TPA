package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.MedioDeContacto;
import org.example.contribuciones.Contribucion;

import java.util.List;

@Getter
@Setter

public abstract class Colaborador {
    private List<MedioDeContacto> mediosDeContacto;
    private String direccion;
    private List<Contribucion> formasContribucion;
    private int puntuaje;

    public abstract String getNombre();

    public void cambiarContribuciones(Contribucion contribucion)
    {
        this.formasContribucion.add(contribucion);
        //TODO Actualizar en el diagrama
    }

    public void actuar()
    {
        //TODO metodo actuar
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
