package org.example.tarjetas;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Apertura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idApertura;

    @ManyToOne
    private  TarjetaColaborador tarjeta;

    @ManyToOne
    private Heladera heladera;
    private LocalDateTime fechahoraCreacion;

    @Enumerated(EnumType.STRING)
    private TipoDeApertura tipoDeApertura;

    public Apertura(TarjetaColaborador tarjeta, Heladera heladera, LocalDateTime fechahoraCreacion, TipoDeApertura tipoDeApertura) {
        this.tarjeta = tarjeta;
        this.heladera = heladera;
        this.fechahoraCreacion = fechahoraCreacion;
        this.tipoDeApertura = tipoDeApertura;
    }
}
