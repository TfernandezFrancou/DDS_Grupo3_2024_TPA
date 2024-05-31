package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.RegistroDePersonaEnSituacionVulnerable;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.heladeras.TarjetaHeladera;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RegistrarPersonasEnSituacionVulnerable extends Contribucion {

    private List<RegistroDePersonaEnSituacionVulnerable> personasRegistradas ;
    private List<TarjetaHeladera> tarjetasAEntregar;
    @Setter
    private Integer tarjetasEntregadas;

    public RegistrarPersonasEnSituacionVulnerable(List<TarjetaHeladera> tarjetasAEntregar){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = tarjetasAEntregar;
    }

    @Override
    public void ejecutarContribucion() {
        //TODO guarda en la DB
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA);
    }

    public void enviarTarjetasViaMail(){
        //TODO enviar tarjetas via mail
    }

    public void agregarRegistro(RegistroDePersonaEnSituacionVulnerable registro){
     this.personasRegistradas.add(registro);
    }
}
