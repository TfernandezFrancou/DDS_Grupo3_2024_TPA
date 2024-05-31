package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.contacto.MedioDeContacto;
import org.example.colaboraciones.Contribucion;
import org.example.puntaje.Coeficiente;
import org.example.puntaje.RegistroContribucion;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public abstract class Colaborador {
    private List<MedioDeContacto> mediosDeContacto;
    private String direccion;
    private List<Contribucion> formasContribucion;
    private float puntuaje;

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
        Coeficiente coeficiente = new Coeficiente();
        coeficiente.setCoeficienteDineroDonado(0.5F);
        coeficiente.setCoeficienteViandasDistribuidas(1);
        coeficiente.setCoeficienteViandasDonadas(1.5F);
        coeficiente.setCoeficienteTarjetasRepartidas(2);
        coeficiente.setCoeficienteHeladeras(5);

        RegistroContribucion registroContribucion = new RegistroContribucion();

        registroContribucion.setCoeficientes(coeficiente);

        //TODO obtener atributos de las contribuciones para los puntos

        this.puntuaje = registroContribucion.calcularPuntos();
    }

    public void canjearPuntuaje(int puntos)
    {
        //TODO metodo canjearPuntuaje
    }
}
