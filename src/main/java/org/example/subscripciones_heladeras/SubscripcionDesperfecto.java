package org.example.subscripciones_heladeras;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoMensajes;

import javax.mail.MessagingException;
import javax.persistence.Entity;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SubscripcionDesperfecto extends SubscripcionHeladera {


    public SubscripcionDesperfecto(Heladera heladera, Persona suscriptor, MedioDeContacto medioDeContacto) {
        this.setHeladera(heladera);
        this.subscriptor = suscriptor;
        this.medioDeContactoElegido = medioDeContacto;
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
        RepoMensajes.getInstancia().agregarMensaje(mensaje);
    }
    private String getStringHeladerasSugueridas(List<Heladera> heladeras){
        StringBuilder heladerasToTextoBuilder = new StringBuilder();

        for(Heladera heladera: heladeras){
            heladerasToTextoBuilder.append("- Nombre: " + heladera.getNombre() +" Direccion: " + heladera.getDireccion() + ", \n") ;
        }
        return heladerasToTextoBuilder.toString();
    }
}
