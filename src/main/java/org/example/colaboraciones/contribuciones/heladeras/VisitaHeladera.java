package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.incidentes.Incidente;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class VisitaHeladera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idVisitaHeladera;

    private LocalDate fechaVisita;
    private String trabajoRealizado;

    @Column(columnDefinition = "TEXT")//la url puede ser muy larga y no basta con un varchar
    private String foto;

    @Column(columnDefinition = "INT")
    private Boolean trabajoCompletado;

    @ManyToOne
    private Incidente incidenteASolucionar;

    public VisitaHeladera(LocalDate fechaVisita, String trabajoRealizado, String foto, Boolean trabajoCompletado, Incidente incidenteASolucionar) {
        this.fechaVisita = fechaVisita;
        this.trabajoRealizado = trabajoRealizado;
        this.foto = foto;
        this.trabajoCompletado = trabajoCompletado;
        this.incidenteASolucionar = incidenteASolucionar;
    }

}