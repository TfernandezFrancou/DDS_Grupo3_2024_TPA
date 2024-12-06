package org.example.validadores;

import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoApertura;

public class VerificadorAperturaHeladera {

    private static VerificadorAperturaHeladera instancia = null;

    private VerificadorAperturaHeladera(){}

    public static VerificadorAperturaHeladera getInstancia(){
        if (instancia == null) {
            instancia = new VerificadorAperturaHeladera();
        }
        return instancia;
    }

    public boolean puedeAbrirHeladera(Heladera heladera, Colaborador colaborador){
        return heladera.estaActiva() &&
                heladera.puedeAbrirHeladera( colaborador.getPersona()) &&
                RepoApertura.getInstancia()
                        .existeSolicitudDeAperturaDeTarjetaParaHeladera(
                                colaborador.getTarjetaColaborador(),
                                heladera
                        );
    }
}
