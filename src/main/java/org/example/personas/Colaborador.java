package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
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
    private List<Oferta> ofertasCanjeadas;

    public Colaborador(){
        this.mediosDeContacto = new ArrayList<>();
        this.formasContribucion = new ArrayList<>();
        this.ofertasCanjeadas = new ArrayList<>();
    }

    public Colaborador(List<MedioDeContacto> mediosDeContacto){
        this.mediosDeContacto = mediosDeContacto;
        this.formasContribucion = new ArrayList<>();
        this.ofertasCanjeadas = new ArrayList<>();
    }


    public abstract String getNombre();

    public void agregarContribucion(Contribucion contribucion)
    {
        this.formasContribucion.add(contribucion);
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

        int puntosCanjeados = 0;

        for(Oferta oferta: this.ofertasCanjeadas){
            puntosCanjeados = oferta.getPuntosNecesarios();
        }

        this.puntuaje = registroContribucion.calcularPuntos() - puntosCanjeados;
    }

    public void canjearOferta(Oferta oferta)
    {
        this.ofertasCanjeadas.add(oferta);
        this.puntuaje -= oferta.getPuntosNecesarios();
    }
}
