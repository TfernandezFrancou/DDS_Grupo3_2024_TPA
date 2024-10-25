package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.Persona;
import org.example.repositorios.RepoHeladeras;
import org.example.subscripcionesHeladeras.PublisherDesperfecto;
import org.example.subscripcionesHeladeras.PublisherViandasDisponibles;
import org.example.subscripcionesHeladeras.PublisherViandasFaltantes;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Heladera {
    private Ubicacion ubicacion;
    private String direccion;
    private String nombre;

    private Integer capacidadEnViandas;
    private Integer viandasEnHeladera;

    private LocalDate fechaInicioFuncionamiento;

    private EstadoHeladera estadoHeladeraActual;
    private List<EstadoHeladera> historialEstadoHeldera;
    private List<MovimientoViandas> historialMovimientos;
    private TemperaturaHeladera temperaturasDeFuncionamiento;

    private PublisherViandasDisponibles publisherViandasDisponibles;
    private PublisherViandasFaltantes publisherViandasFaltantes;
    private PublisherDesperfecto publisherDesperfecto;

    private List<Persona> colaboradoresAutorizados;

    private int temperaturaActualHeladera;

    public Heladera(){
        this.historialEstadoHeldera = new ArrayList<>();
        this.viandasEnHeladera = 0;
        this.historialMovimientos = new ArrayList<>();
        this.colaboradoresAutorizados = new ArrayList<>();
        this.temperaturasDeFuncionamiento = new TemperaturaHeladera(0, 200);
        this.publisherDesperfecto = new PublisherDesperfecto();
        this.publisherViandasFaltantes = new PublisherViandasFaltantes();
        this.publisherViandasDisponibles = new PublisherViandasDisponibles();
        this.colaboradoresAutorizados = new ArrayList<>();
    }

    public boolean estaActiva() {
        if (this.estadoHeladeraActual == null) {
            return false;
        } else {
            return this.estadoHeladeraActual.getEstaActiva();
        }
    }

    public int obtenerMesesActivos() {
        return this.historialEstadoHeldera.stream()
                .filter(EstadoHeladera::getEstaActiva)
                .map(EstadoHeladera::mesesActivos)
                .reduce(0, Integer::sum);
    }

    public void actualizarEstadoHeladera(boolean nuevoEstado) {
        if (this.estadoHeladeraActual == null) {
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeldera.add(this.estadoHeladeraActual);
        } else if (this.estadoHeladeraActual.getEstaActiva() != nuevoEstado) {
            this.estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeldera.add(this.estadoHeladeraActual);
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

    public void desactivarHeladera() throws MessagingException {
        this.actualizarEstadoHeladera(false);
        this.notificarDesperfecto();
    }

    public void autorizarColaborador(Persona personaColaborador) {
        this.colaboradoresAutorizados.add(personaColaborador);
    }

    public void desautorizarColaborador(Persona personaColaborador) {
        this.colaboradoresAutorizados.remove(personaColaborador);
    }

    public boolean puedeAbrirHeladera(Persona personaColaborador){
        return this.colaboradoresAutorizados.stream()
                .anyMatch(persona -> persona.equals(personaColaborador));
    }
}