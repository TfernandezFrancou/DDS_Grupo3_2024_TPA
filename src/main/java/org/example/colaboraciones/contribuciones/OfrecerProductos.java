package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OfrecerProductos extends Contribucion {
    private List<Oferta> ofertas;

    public OfrecerProductos(){
        this.ofertas = new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion(){
        //TODO guardar en la DB las ofertas
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.JURIDICA);
    }

    public void agregarOferta(Oferta oferta){
        this.ofertas.add(oferta);
    }

    @Override
    public float obtenerPuntaje(){
        return 0  * this.getCoeficientePuntaje();
    }
}
