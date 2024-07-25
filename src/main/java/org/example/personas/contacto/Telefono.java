package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Telefono implements MedioDeContacto {
    private String telefono;

    @Override
    public void notificar(Mensaje mensaje){
        //TODO notificar por mensaje al telefono
    }
}
