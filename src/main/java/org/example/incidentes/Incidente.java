package org.example.incidentes;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.config.Configuracion;
import org.example.personas.Persona;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoPersona;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator")
public abstract class Incidente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idIncidente;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Heladera heladera;

    private String tipoDeIncidente;
    private LocalDateTime fechaDeEmision;
    @Column(columnDefinition = "INT")
    private boolean solucionado;

    public Incidente(Heladera heladera, String tipoDeIncidente, LocalDateTime fechaDeEmision) {
        this.heladera = heladera;
        this.tipoDeIncidente = tipoDeIncidente;
        this.fechaDeEmision = fechaDeEmision;
        this.solucionado = false;
    }

    public Incidente() { }

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

            Direccion direccion = heladera.getDireccion();
            String asunto = Configuracion.obtenerProperties("mensaje.incidentes.heladera.titulo");
            String contenido = Configuracion.obtenerProperties("mensaje.incidentes.heladera.contenido")
                    .replace("{nombreHeladera}", heladera.getNombre())
                    .replace("{direccionHeladera}", direccion.getNombreCalle() +' '+direccion.getAltura() )
                    .replace("{tipoIncidente}", this.tipoDeIncidente);
            Mensaje mensaje = new Mensaje(asunto, contenido, tecnicoCercano);
            tecnicoCercano.getMediosDeContacto().get(0).notificar(mensaje);//aviso por el primer medio de contacto que registró

        } //si no hay tecnico cerca, no se avisa a nadie
    }
}
