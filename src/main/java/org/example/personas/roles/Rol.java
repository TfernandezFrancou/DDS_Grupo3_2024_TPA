package org.example.personas.roles;


public abstract class Rol {

    private boolean estaActivo;

    public Rol(){
        this.estaActivo = true;
    }

    public void darDeBaja(){
        this.estaActivo = false;
    }
    public void tienePermisoParaHacerAccion(){
        //TODO implementar esto
    }
}
