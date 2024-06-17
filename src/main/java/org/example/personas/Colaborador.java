package org.example.personas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
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
    private float puntuaje;
    private List<Oferta> ofertasCanjeadas;

    private boolean estaActivo;

    public Colaborador(){
        this.mediosDeContacto = new ArrayList<>();
        this.formasContribucion = new ArrayList<>();
        this.ofertasCanjeadas = new ArrayList<>();
        this.estaActivo = true;
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
        //TODO obtener atributos de las contribuciones para los puntos

        for (Contribucion contribucion:this.formasContribucion) {
            this.puntuaje += contribucion.obtenerPuntaje();
        }

        int puntosCanjeados = 0;

        for(Oferta oferta: this.ofertasCanjeadas){
            puntosCanjeados = oferta.getPuntosNecesarios();
        }

        this.puntuaje  -= puntosCanjeados;
    }

    public void canjearOferta(Oferta oferta)
    {
        this.ofertasCanjeadas.add(oferta);
        this.puntuaje -= oferta.getPuntosNecesarios();
    }

    public void darDeBaja()
    {
        this.estaActivo = false;
    }
}
