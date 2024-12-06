package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.config.Configuracion;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoTarjetas;
import org.example.tarjetas.TarjetaHeladera;
import org.example.personas.Persona;

import javax.mail.MessagingException;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id_contribucion")
public class RegistrarPersonasEnSituacionVulnerable extends Contribucion {
    @OneToMany
    @JoinColumn(name = "id_contribucion_registro")
    private List<Persona> personasRegistradas;
    @OneToMany
    @JoinColumn(name = "id_contribucion_registro")
    private List<TarjetaHeladera> tarjetasAEntregar;
    private Integer tarjetasEntregadas;

    public RegistrarPersonasEnSituacionVulnerable(LocalDate fecha, Integer cantidad) {
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(fecha);
        this.tarjetasEntregadas = cantidad;
    }

    public RegistrarPersonasEnSituacionVulnerable(List<TarjetaHeladera> tarjetasAEntregar) throws MessagingException, EmailNoRegistradoException {
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = tarjetasAEntregar;
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(LocalDate.now());
        this.enviarTarjetasViaMail();
        this.tarjetasEntregadas = 0;
    }

    public RegistrarPersonasEnSituacionVulnerable(){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
        this.tiposDePersona = Set.of(TipoDePersona.HUMANA);
        this.setFecha(LocalDate.now());
        this.tarjetasEntregadas = 0;
    }

    @Override
    public void ejecutarContribucion() throws Exception{
        RepoPersona.getInstancia().agregarTodas(personasRegistradas);
        RepoTarjetas.getInstancia().agregarTodas(tarjetasAEntregar);
        this.setTarjetasEntregadas(personasRegistradas.size());
        super.ejecutarContribucion();
    }

    //cuando se tengan las tarjetas, se las envia al colaborador
    public void setTarjetasAEntregar(List<TarjetaHeladera> tarjetasAEntregar) throws MessagingException, EmailNoRegistradoException {
        this.tarjetasAEntregar = tarjetasAEntregar;
        this.enviarTarjetasViaMail();
    }

    public void enviarTarjetasViaMail() throws MessagingException, EmailNoRegistradoException {
        String titulo = Configuracion.obtenerProperties("mensaje.colaboraciones.tarjetas.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.contribuciones.tarjetas.contenido")
                .replace("{detallesDetarjetas}", this.tarjetasAEntregarToString());
        Persona personaColaborador = colaborador.getPersona();
        Mensaje mensaje = new Mensaje(titulo, contenido, personaColaborador);
        personaColaborador.getEmail().notificar(mensaje);
    }
    private String tarjetasAEntregarToString(){
        StringBuilder stringBuilderTarjetas = new StringBuilder();

        for(TarjetaHeladera tarjetaHeladera: tarjetasAEntregar){
            stringBuilderTarjetas.append("- Id tarjeta: ").append(tarjetaHeladera.getIdTarjeta()).append("\n");
        }
        return stringBuilderTarjetas.toString();
    }

    public void agregarPersona(Persona persona){
        this.personasRegistradas.add(persona);
        tarjetasEntregadas++;
    }

    @Override
    public float getCoeficientePuntaje() {
        return 2;
    }

    @Override
    public float obtenerPuntaje(){
        return tarjetasEntregadas  * this.getCoeficientePuntaje();
    }
}
