package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.incidentes.Incidente;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class VisitaHeladera {
    private LocalDate fechaVisita;
    private String trabajoRealizado;
    private String foto;
    private Boolean trabajoCompletado;
    private Incidente incidenteASolucionar;


}