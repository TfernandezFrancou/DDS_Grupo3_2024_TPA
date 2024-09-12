package org.example.validadores;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.*;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.PersonaJuridica;

import java.util.Set;

public class VerificadorContribucion {
    private static VerificadorContribucion instancia = null;

    private VerificadorContribucion(){}

    public static VerificadorContribucion getInstancia(){
        if (instancia == null) {
            VerificadorContribucion.instancia = new VerificadorContribucion();
        }
        return instancia;
    }

    public boolean puedeRealizarContribucion(Persona persona, Contribucion contribucion) {
        if (persona instanceof PersonaHumana) {
            return contribucion.getTiposDePersona().contains(TipoDePersona.HUMANA);
        } else if (persona instanceof PersonaJuridica) {
            return contribucion.getTiposDePersona().contains(TipoDePersona.JURIDICA);
        } else {
            return false;
        }
    }
}
