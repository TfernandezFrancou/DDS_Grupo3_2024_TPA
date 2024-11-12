package org.example.personas.roles;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idrol;

    @Column(columnDefinition = "INT")
    private boolean estaActivo;

    public Rol(){
        this.estaActivo = true;
    }

    public void darDeBaja(){
        this.estaActivo = false;
    }

}
