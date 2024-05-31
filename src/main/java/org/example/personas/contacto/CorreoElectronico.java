package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CorreoElectronico implements MedioDeContacto{
    private String correoElectronico;

    @Override
    public void notificar(){
        //TODO notificar por email
    }
}
