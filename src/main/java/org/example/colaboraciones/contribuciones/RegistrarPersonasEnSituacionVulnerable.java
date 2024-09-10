package org.example.colaboraciones.contribuciones;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.config.Configuracion;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.Mensaje;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoTarjetas;
import org.example.tarjetas.TarjetaHeladera;
import org.example.personas.Persona;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RegistrarPersonasEnSituacionVulnerable extends Contribucion {
    private List<Persona> personasRegistradas;
    private List<TarjetaHeladera> tarjetasAEntregar;
    @Setter
    private Integer tarjetasEntregadas;

    public RegistrarPersonasEnSituacionVulnerable(TipoDePersona tipo, LocalDate fecha, Integer cantidad) {
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
        this.setTiposDePersona(tipo);
        this.setFecha(fecha);
        this.tarjetasEntregadas = cantidad;
    }

    public RegistrarPersonasEnSituacionVulnerable(List<TarjetaHeladera> tarjetasAEntregar){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = tarjetasAEntregar;
    }

    public RegistrarPersonasEnSituacionVulnerable(){
        this.personasRegistradas = new ArrayList<>();
        this.tarjetasAEntregar = new ArrayList<>();
    }

    @Override
    public void ejecutarContribucion() throws Exception{
        super.ejecutarContribucion();
        RepoPersona.getInstancia().agregarTodas(personasRegistradas);
        RepoTarjetas.getInstancia().agregarTodas(tarjetasAEntregar);
    }

    @Override
    public boolean puedeRealizarContribucion() {
        return this.getTiposDePersona().equals(TipoDePersona.HUMANA);
    }

    public void enviarTarjetasViaMail(CorreoElectronico mailColaborador, Persona colaborador) throws MessagingException {
        //TODO enviarTarjetasViaMail por ahi tendria que ir en otro lado
        String titulo = Configuracion.obtenerProperties("mensaje.colaboraciones.tarjetas.titulo");
        String contenido = Configuracion.obtenerProperties("mensaje.contribuciones.tarjetas.contenido")
                .replace("{detallesDetarjetas}", this.tarjetasAEntregarToString());
        Mensaje mensaje = new Mensaje(titulo, contenido, colaborador);
        mailColaborador.notificar(mensaje);
    }
    private String tarjetasAEntregarToString(){
        StringBuilder stringBuilderTarjetas = new StringBuilder();

        for(TarjetaHeladera tarjetaHeladera: tarjetasAEntregar){
            stringBuilderTarjetas.append("- Id tarjeta: " + tarjetaHeladera.getId()+ "\n");
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
