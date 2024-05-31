package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
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
    //TODO
    }
}
