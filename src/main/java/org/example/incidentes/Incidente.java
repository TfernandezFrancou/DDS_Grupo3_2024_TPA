package org.example.incidentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.EstadoHeladera;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public abstract class Incidente {
    private Heladera heladera;
    private String tipoDeIncidente;
    private LocalDateTime fechaDeEmision;

    public Incidente(Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        //TODO esto comentado en realidad no va aca, pero tiene que ir en algun lado
        // desactivo la heladera
        /*EstadoHeladera estadoHeladera = new EstadoHeladera(false);
        heladera.getEstadoHeladeraActual().setFechaHoraFin(LocalDateTime.now());//finalizo estado anterior
        heladera.setEstadoHeladeraActual(estadoHeladera);
        heladera.agregarEstadoHeladeraAlHistorial(estadoHeladera);
        */
        this.heladera = heladera;
        this.tipoDeIncidente = tipoDeIncidente;
        this.fechaDeEmision = fechaDeEmision;
    }

    public void avisarATecnico() throws MessagingException {
        Persona tecnicoCercano = RepoPersona.getInstancia().tecnicoMasCercanoAHeladera(heladera);

        String asunto = Configuracion.obtenerProperties("mensaje.incidentes.heladera.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.incidentes.heladera.contenido")
                .replace("{nombreHeladera}", heladera.getNombre())
                .replace("{direccionHeladera}", heladera.getDireccion())
                .replace("{tipoIncidente}", this.tipoDeIncidente);
        Mensaje mensaje = new Mensaje(asunto, contenido, tecnicoCercano);
        tecnicoCercano.getMediosDeContacto().get(0).notificar(mensaje);
    }
}
