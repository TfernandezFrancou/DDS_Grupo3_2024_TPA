package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoOferta;

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
        super.ejecutarContribucion();
        RepoOferta.getInstancia().agregarTodas(ofertas);
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.JURIDICA);
    }

    public void agregarOferta(Oferta oferta){
        this.ofertas.add(oferta);
    }

    @Override
    public float getCoeficientePuntaje() {
        return 0;
    }

    @Override
    public float obtenerPuntaje(){
        return 0 * this.getCoeficientePuntaje();
    }
}
