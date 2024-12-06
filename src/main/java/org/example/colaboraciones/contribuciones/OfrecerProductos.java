package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "id_contribucion")
public class OfrecerProductos extends Contribucion {
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name ="id_contribucion")
    private final List<Oferta> ofertas;

    public OfrecerProductos(){
        this.tiposDePersona = Set.of(TipoDePersona.JURIDICA);
        this.setFecha(LocalDate.now());
        this.ofertas = new ArrayList<>();
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
