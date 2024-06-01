package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

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
        //TODO guarda en la DB las nuevas heladeras
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.JURIDICA);
    }

    public  void agregarHeladera(Heladera heladera){
        this.heladerasColocadas.add(heladera);
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
