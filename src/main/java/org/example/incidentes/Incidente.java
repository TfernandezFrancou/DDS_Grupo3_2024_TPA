package org.example.incidentes;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoPersona;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public abstract class Incidente {
    private Heladera heladera;
    private String tipoDeIncidente;
    private LocalDateTime fechaDeEmision;
    private boolean solucionado;

    public Incidente(Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        this.heladera = heladera;
        this.tipoDeIncidente = tipoDeIncidente;
        this.fechaDeEmision = fechaDeEmision;
    }

    public void reportarIncidente() throws MessagingException {
        if(heladera.estaActiva()){//si ya esta desactivada no hago nada
            heladera.desactivarHeladera();
        }

        this.avisarATecnico();
    }

    private void avisarATecnico() throws MessagingException {
        Optional<Persona> tecnicoCercanoOp = RepoPersona.getInstancia().tecnicoMasCercanoAHeladera(heladera);

        if(tecnicoCercanoOp.isPresent()){
            Persona tecnicoCercano = tecnicoCercanoOp.get();

            String asunto = Configuracion.obtenerProperties("mensaje.incidentes.heladera.titulo");
            String contenido = Configuracion.obtenerProperties("mensaje.incidentes.heladera.contenido")
                    .replace("{nombreHeladera}", heladera.getNombre())
                    .replace("{direccionHeladera}", heladera.getDireccion())
                    .replace("{tipoIncidente}", this.tipoDeIncidente);
            Mensaje mensaje = new Mensaje(asunto, contenido, tecnicoCercano);
            tecnicoCercano.getMediosDeContacto().get(0).notificar(mensaje);//aviso por el primer medio de contacto que registró

        } //si no hay tecnico cerca, no se avisa a nadie
    }
}
