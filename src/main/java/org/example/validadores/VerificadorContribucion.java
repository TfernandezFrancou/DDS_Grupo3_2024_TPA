package org.example.validadores;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.PersonaJuridica;


public class VerificadorContribucion {
    private static VerificadorContribucion instancia = null;

    private VerificadorContribucion(){}

    public static VerificadorContribucion getInstancia(){
        if (instancia == null) {
            instancia = new VerificadorContribucion();
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
