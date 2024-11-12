package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.viandas.Vianda;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class MovimientoViandas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMovimientoViandas;

    @ManyToMany
    @JoinTable(name = "MovimientoViandasXVianda_introducidas")
    private List<Vianda> viandasIntroducidas;

    @ManyToMany
    @JoinTable(name = "MovimientoViandasXVianda_sacadas")
    private List<Vianda> viandasSacadas;

    private LocalDateTime fechaMovimiento;

    @ManyToOne
    private Heladera heladera;

    public MovimientoViandas(List<Vianda> viandasIntroducidas, List<Vianda> viandasSacadas, LocalDateTime fechaMovimiento) {
        this.viandasIntroducidas = viandasIntroducidas;
        this.viandasSacadas = viandasSacadas;
        this.fechaMovimiento = fechaMovimiento;
    }
}
