package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoHeladeras;

import javax.mail.MessagingException;
import java.util.List;

@Getter
@Setter
public class SubscripcionDesperfecto extends SubscripcionHeladera {

    public SubscripcionDesperfecto(Persona suscriptor, MedioDeContacto medioDeContacto) {
        super.subscriptor = suscriptor;
        super.medioDeContactoElegido = medioDeContacto;
    }

    @Override
    public void notificar(Heladera heladera) throws MessagingException {
        String asunto = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.desperfecto.titulo");
        List<Heladera> heladerasSugueridas = RepoHeladeras.getInstancia().buscarHeladerasCercanasA(heladera, 10.0);
        String contenido = Configuracion.obtenerProperties("mensaje.subscripciones.heladera.desperfecto.contenido")
            .replace("{heladera}", heladera.getNombre())
                .replace("{heladerasSugueridas}", this.getStringHeladerasSugueridas(heladerasSugueridas));
        Mensaje mensaje = new Mensaje(asunto, contenido, subscriptor);
        medioDeContactoElegido.notificar(mensaje);
    }
    private String getStringHeladerasSugueridas(List<Heladera> heladeras){
        StringBuilder heladerasToTextoBuilder = new StringBuilder();

        for(Heladera heladera: heladeras){
            heladerasToTextoBuilder.append("- Nombre: " + heladera.getNombre() +" Direccion: " + heladera.getDireccion() + ", \n") ;
        }
        return heladerasToTextoBuilder.toString();
    }
}
