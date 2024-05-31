package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.RegistroDePersonaEnSituacionVulnerable;
import org.example.colaboraciones.contribuciones.heladeras.TarjetaHeladera;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RegistrarPersonasEnSituacionVulnerable extends Contribucion {

    private List<RegistroDePersonaEnSituacionVulnerable> personasRegistradas ;
    private List<TarjetaHeladera> tarjetasAEntregar;
    @Setter
    private Integer tarjetasEntregadas;

    public RegistrarPersonasEnSituacionVulnerable(){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion() {

    }

    @Override
    public boolean puedeRealizarContribucion() {
        //TODO
        return false;
    }
}
