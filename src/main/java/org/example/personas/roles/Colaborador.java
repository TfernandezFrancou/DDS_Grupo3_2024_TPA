package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.tarjetas.TarjetaColaborador;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.colaboraciones.Contribucion;
import org.example.repositorios.RepoContribucion;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class Colaborador extends Rol {
    private List<Contribucion> formasContribucion;
    private float puntuaje;
    private List<Oferta> ofertasCanjeadas;
    private TarjetaColaborador tarjetaColaborador;

    public Colaborador(){
        this.formasContribucion = new ArrayList<>();
        this.ofertasCanjeadas = new ArrayList<>();
    }

    public void agregarContribucion(Contribucion contribucion)
    {
        RepoContribucion.getInstancia().agregar(contribucion);
        this.formasContribucion.add(contribucion);
    }

    public void calcularPuntuaje()
    {

        for (Contribucion contribucion:this.formasContribucion) {
            this.puntuaje += contribucion.obtenerPuntaje();
        }

        int puntosCanjeados = 0;

        for(Oferta oferta: this.ofertasCanjeadas){
            puntosCanjeados = oferta.getPuntosNecesarios();
        }

        this.puntuaje  -= puntosCanjeados;
    }

    public void canjearOferta(Oferta oferta) throws PuntosInsuficienteParaCanjearOferta {
        if(this.puedeCanjearOferta(oferta)){
            this.ofertasCanjeadas.add(oferta);
            this.puntuaje -= oferta.getPuntosNecesarios();
        } else {
            throw new PuntosInsuficienteParaCanjearOferta();
        }
    }

    private boolean puedeCanjearOferta(Oferta oferta){
        return this.puntuaje >= oferta.getPuntosNecesarios();
    }

    public void asignarTarjeta(TarjetaColaborador tarjeta) {
        this.tarjetaColaborador = tarjeta;
    }
}
