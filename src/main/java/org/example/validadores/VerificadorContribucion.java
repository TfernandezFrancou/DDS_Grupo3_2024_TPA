package org.example.validadores;

import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.*;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.PersonaJuridica;

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

        boolean puedeHacerContribucion= false;

        if(contribucion instanceof OfrecerProductos){
            puedeHacerContribucion =persona instanceof PersonaJuridica;
        } if(contribucion instanceof DonacionDeDinero
                || contribucion instanceof HacerseCargoDeUnaHeladera
        ){
            puedeHacerContribucion =persona instanceof PersonaJuridica
                    || persona instanceof PersonaHumana ;
        }else if(contribucion instanceof DistribucionDeViandas
                || contribucion instanceof DonacionDeViandas
            || contribucion instanceof RegistrarPersonasEnSituacionVulnerable
        ){
            puedeHacerContribucion =persona instanceof PersonaHumana ;
        }

        return puedeHacerContribucion;
    }
}
