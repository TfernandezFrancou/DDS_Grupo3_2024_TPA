package org.example.personas.contacto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Whatsapp implements MedioDeContacto{
    private String telefono;

    @Override
    public void notificar(String contenidoANotificar){
        //TODO notificar por mensaje via whatsapp
    }
}
