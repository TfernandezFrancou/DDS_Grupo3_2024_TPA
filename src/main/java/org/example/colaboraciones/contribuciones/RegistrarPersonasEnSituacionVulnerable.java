package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.tarjetas.TarjetaHeladera;
import org.example.personas.Persona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RegistrarPersonasEnSituacionVulnerable extends Contribucion {

    private List<Persona> personasRegistradas;
    private List<TarjetaHeladera> tarjetasAEntregar;
    @Setter
    private Integer tarjetasEntregadas;

    public RegistrarPersonasEnSituacionVulnerable(TipoDePersona tipo, LocalDate fecha, Integer cantidad) {
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
        this.setTiposDePersona(tipo);
        this.setFecha(fecha);
        this.tarjetasEntregadas = cantidad;
    }

    public RegistrarPersonasEnSituacionVulnerable(List<TarjetaHeladera> tarjetasAEntregar){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = tarjetasAEntregar;
    }

    public RegistrarPersonasEnSituacionVulnerable(){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
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

    public void agregarPersona(Persona persona){
        this.personasRegistradas.add(persona);
        tarjetasEntregadas++;
    }

    @Override
    public float obtenerPuntaje(){
        return tarjetasEntregadas  * this.getCoeficientePuntaje();
    }
}
