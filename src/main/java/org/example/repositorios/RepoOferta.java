package org.example.repositorios;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;

import java.util.ArrayList;
import java.util.List;

public class RepoOferta {
    private List<Oferta> ofertas;

    private static RepoOferta instancia = null;

    private RepoOferta() {
        this.ofertas = new ArrayList<>();
    }

    public static RepoOferta getInstancia() {
        if (instancia == null) {
            RepoOferta.instancia = new RepoOferta();
        }
        return instancia;
    }

    public void agregar(Oferta oferta) {
        this.ofertas.add(oferta);
    }

    public void agregarTodas(List<Oferta> ofertas) {
        this.ofertas.addAll(ofertas);
    }

    public void eliminar(Oferta oferta) {
        this.ofertas.remove(oferta);
    }
}
