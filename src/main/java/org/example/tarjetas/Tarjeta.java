package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.personas.roles.Rol;
import org.example.repositorios.RepoTarjetas;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminador")
public abstract class Tarjeta {
    @Id
    private String idTarjeta;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idTarjeta")
    private List<Uso> usos;

    public abstract void usar(Rol duenio, Heladera heladera) throws Exception;

    @PrePersist
    public void prePersist() {//genera un id string nuevo cuando se guarda en la db
        if (idTarjeta == null) {
            idTarjeta = UUID.randomUUID().toString();
        }
    }
}
