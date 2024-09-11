package org.example.personas.roles;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.VisitaHeladera;
import org.example.incidentes.Incidente;
import org.example.recomendacion.Zona;
import org.example.repositorios.RepositorioVisitasTecnicos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Tecnico extends Rol {
    private String apellido;
    private String cuil;
    private List<Zona> areasDeCobertura;

    public Tecnico(){
        this.areasDeCobertura = new ArrayList<>();
    }

    public void agregarAreaDeCovertura(Zona areaDeCovertura){
        this.areasDeCobertura.add(areaDeCovertura);
    }


    //Se llama al metodo cuando el tecino avisa al sistema que realizo la visita por un incidente
    public void visitar(Incidente incidente,
                        String trabajoRealizado,
                        String fotoUrl,
                        boolean incidenteSolucionado,
                        boolean trabajoCompletado
    ){
        VisitaHeladera visitaHeladera = new VisitaHeladera(
                LocalDate.now(),
                trabajoRealizado,
                fotoUrl,
                incidenteSolucionado,
                trabajoCompletado,
                incidente
        );

        RepositorioVisitasTecnicos.getInstancia().agregarVisita(visitaHeladera);

        if(incidenteSolucionado  && trabajoCompletado){
            incidente.getHeladera().reactivarHeladera();//si se solucinó se reactiva la heladera
        }
    }
}
