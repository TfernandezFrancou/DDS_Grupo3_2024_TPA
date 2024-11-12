package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.repositorios.RepoOfertas;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "id_contribucion")
public class OfrecerProductos extends Contribucion {
    @OneToMany
    @JoinColumn(name ="id_contribucion")
    private List<Oferta> ofertas;

    public OfrecerProductos(){
        this.tiposDePersona = Set.of(TipoDePersona.JURIDICA);
        this.ofertas = new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion() throws Exception{
        super.ejecutarContribucion();
        RepoOfertas.getInstancia().agregarTodas(ofertas);
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
