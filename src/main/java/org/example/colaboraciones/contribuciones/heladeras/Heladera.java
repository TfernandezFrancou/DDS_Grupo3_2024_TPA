package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.Persona;
import org.example.repositorios.RepoHeladeras;
import org.example.subscripciones_heladeras.PublisherDesperfecto;
import org.example.subscripciones_heladeras.PublisherViandasDisponibles;
import org.example.subscripciones_heladeras.PublisherViandasFaltantes;

import javax.mail.MessagingException;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Heladera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHeladera;

    @OneToOne
    private Ubicacion ubicacion;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Direccion direccion;

    private String nombre;

    private Integer capacidadEnViandas;
    private Integer viandasEnHeladera;

    private LocalDate fechaInicioFuncionamiento;

    @OneToOne(cascade = CascadeType.PERSIST)
    private EstadoHeladera estadoHeladeraActual;

    @OneToMany(mappedBy = "heladera", cascade = CascadeType.PERSIST)
    private List<EstadoHeladera> historialEstadoHeladera;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="idHeladera")
    private List<MovimientoViandas> historialMovimientos;

    @OneToOne(cascade = CascadeType.PERSIST)
    private TemperaturaHeladera temperaturasDeFuncionamiento;

    @Transient
    private PublisherViandasDisponibles publisherViandasDisponibles;

    @Transient
    private PublisherViandasFaltantes publisherViandasFaltantes;

    @Transient
    private PublisherDesperfecto publisherDesperfecto;

    @ManyToMany
    @JoinTable(name = "Colaboradores_autorizados")
    private List<Persona> colaboradoresAutorizados;

    private int temperaturaActualHeladera;

    public Heladera(){
        this.viandasEnHeladera = 0;
        this.historialEstadoHeladera = new ArrayList<>();
        this.historialMovimientos = new ArrayList<>();
        this.colaboradoresAutorizados = new ArrayList<>();
        this.temperaturasDeFuncionamiento = new TemperaturaHeladera(0, 200);
        this.publisherDesperfecto = new PublisherDesperfecto();
        this.publisherViandasFaltantes = new PublisherViandasFaltantes();
        this.publisherViandasDisponibles = new PublisherViandasDisponibles();
    }

    public Heladera(String nombre, Ubicacion ubicacion, Direccion direccion, Integer capacidadEnViandas) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.direccion = direccion;
        this.capacidadEnViandas = capacidadEnViandas;
        this.viandasEnHeladera = 0;
        this.historialEstadoHeladera = new ArrayList<>();
        this.historialMovimientos = new ArrayList<>();
        this.colaboradoresAutorizados = new ArrayList<>();
        this.temperaturasDeFuncionamiento = new TemperaturaHeladera(0, 200);
        this.publisherDesperfecto = new PublisherDesperfecto();
        this.publisherViandasFaltantes = new PublisherViandasFaltantes();
        this.publisherViandasDisponibles = new PublisherViandasDisponibles();
        this.colaboradoresAutorizados = new ArrayList<>();
        this.fechaInicioFuncionamiento = LocalDate.now();
        this.actualizarEstadoHeladera(true);
    }

    public boolean estaActiva() {
        if (this.estadoHeladeraActual == null) {
            return false;
        } else {
            return this.estadoHeladeraActual.getEstaActiva();
        }
    }

    public int obtenerMesesActivos() {
        return this.historialEstadoHeladera.stream()
                .filter(EstadoHeladera::getEstaActiva)
                .map(EstadoHeladera::mesesActivos)
                .reduce(0, Integer::sum);
    }

    public void actualizarEstadoHeladera(boolean nuevoEstado) {
        if (this.estadoHeladeraActual == null) {
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeladera.add(this.estadoHeladeraActual);
        } else if (Boolean.TRUE.equals(this.estadoHeladeraActual.getEstaActiva()) != nuevoEstado) {
            this.estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeladera.add(this.estadoHeladeraActual);
        }
    }

    public void notificarCambioViandas(List<Vianda> viandasIntroducidas, List<Vianda> viandasSacadas) throws MessagingException {
        MovimientoViandas m = new MovimientoViandas(viandasIntroducidas, viandasSacadas, LocalDateTime.now());
        this.historialMovimientos.add(m);
        this.viandasEnHeladera += viandasIntroducidas.size() - viandasSacadas.size();
        RepoHeladeras.getInstancia().actualizar(this);
        this.getPublisherViandasFaltantes().notificarATodos(this);
        this.getPublisherViandasDisponibles().notificarATodos(this);
    }

    public void notificarDesperfecto() throws MessagingException {
        this.getPublisherDesperfecto().notificarATodos(this);
    }

    public int faltanteParaLlenar(){
        return this.capacidadEnViandas - this.viandasEnHeladera;
    }

    public void reactivarHeladera() {
        this.actualizarEstadoHeladera(true);
    }

    public void desactivarHeladera() {
        this.actualizarEstadoHeladera(false);
    }

    public void autorizarColaborador(Persona personaColaborador) {
        this.colaboradoresAutorizados.add(personaColaborador);
    }

    public void desautorizarColaborador(Persona personaColaborador) {
        this.colaboradoresAutorizados.remove(personaColaborador);
    }

    public boolean puedeAbrirHeladera(Persona personaColaborador){
        return this.colaboradoresAutorizados.stream()
                .anyMatch(persona -> persona.getIdPersona()==(personaColaborador.getIdPersona()));
    }
}