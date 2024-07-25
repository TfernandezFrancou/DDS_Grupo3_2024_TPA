package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoHeladera;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HacerseCargoDeUnaHeladera extends Contribucion {

    private List<Heladera> heladerasColocadas;

    public HacerseCargoDeUnaHeladera(){
        this.heladerasColocadas = new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion(){
        super.ejecutarContribucion();
        RepoHeladera.getInstancia().agregarTodas(heladerasColocadas);
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.JURIDICA);
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
