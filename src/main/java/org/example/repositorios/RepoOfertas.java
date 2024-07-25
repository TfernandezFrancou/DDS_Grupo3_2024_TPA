package org.example.repositorios;

import org.example.colaboraciones.contribuciones.ofertas.Oferta;

import java.util.ArrayList;
import java.util.List;

public class RepoOfertas {
    private List<Oferta> ofertas;

    private static RepoOfertas instancia = null;

    private RepoOfertas() {
        this.ofertas = new ArrayList<>();
    }

    public static RepoOfertas getInstancia() {
        if (instancia == null) {
            RepoOfertas.instancia = new RepoOfertas();
        }
        return instancia;
    }

    public void agregarOferta(Oferta oferta) {
        this.ofertas.add(oferta);
    }

    public void agregarTodas(List<Oferta> ofertas) {
        this.ofertas.addAll(ofertas);
    }

    public void eliminarOferta(Oferta oferta) {
        this.ofertas.remove(oferta);
    }
}
