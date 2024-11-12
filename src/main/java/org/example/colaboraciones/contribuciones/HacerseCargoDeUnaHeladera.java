package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoHeladeras;

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
public class HacerseCargoDeUnaHeladera extends Contribucion {

    @OneToMany
    @JoinColumn(name = "id_contribucion_hacerce_cargo_heladera")
    private List<Heladera> heladerasColocadas;

    public HacerseCargoDeUnaHeladera(){
        this.tiposDePersona = Set.of(TipoDePersona.JURIDICA, TipoDePersona.HUMANA);
        this.heladerasColocadas = new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion() throws Exception{
        super.ejecutarContribucion();
        RepoHeladeras.getInstancia().agregarTodas(heladerasColocadas);
    }

    public void agregarHeladera(Heladera heladera){
        this.heladerasColocadas.add(heladera);
    }

    @Override
    public float getCoeficientePuntaje() {
        return 5;
    }

    @Override
    public float obtenerPuntaje(){
        List<Heladera> heladerasActivas = heladerasColocadas.stream().filter(Heladera::estaActiva).toList();
        int mesesActiva = 0;
        for (Heladera heladera: heladerasActivas) {
            mesesActiva += heladera.obtenerMesesActivos();
        }
        return heladerasActivas.size() * mesesActiva  * this.getCoeficientePuntaje();
    }
}
