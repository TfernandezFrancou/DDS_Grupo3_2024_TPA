package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;
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

    public Heladera(){
        this.historialEstadoHeldera = new ArrayList<>();
        this.colaboradoresAutorizados = new ArrayList<>();
    }

    public boolean estaActiva() {
        return this.estadoHeladeraActual.getEstaActiva();
    }

    public int obtenerMesesActivos() {
        return this.historialEstadoHeldera.stream()
                .filter(EstadoHeladera::getEstaActiva)
                .map(EstadoHeladera::mesesActivos)
                .reduce(0, Integer::sum);
    }

    public void actualizarEstadoHeladera(Sensor sensor) throws MessagingException {
        boolean nuevoEstado = sensor.getEstadoHeladera();
        if (this.estadoHeladeraActual.getEstaActiva() != nuevoEstado) {
            this.estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeldera.add(this.estadoHeladeraActual);
        }
    }

    public void agregarEstadoHeladeraAlHistorial(EstadoHeladera nuevoEstadoHeladera){
        this.historialEstadoHeldera.add(nuevoEstadoHeladera);
    }

    public void notificarCambioViandas(int viandasIntroducidas, int viandasSacadas) throws MessagingException {
        MovimientoViandas m = new MovimientoViandas(viandasIntroducidas, viandasSacadas, LocalDateTime.now());
        this.historialMovimientos.add(m);
        this.viandasEnHeladera += viandasIntroducidas - viandasSacadas;
        RepoHeladeras.getInstancia().actualizar(this);
        publisherViandasFaltantes.notificarATodos(this);
        publisherViandasDisponibles.notificarATodos(this);
    }

    public void notificarDesperfecto() throws MessagingException {
        publisherDesperfecto.notificarATodos(this);
    }

    public int faltanteParaLlenar(){
        return this.capacidadEnViandas - this.viandasEnHeladera;
    }

    public void reactivarHeladera() {
        //finalizo estado anterior
        estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());

        EstadoHeladera estadoHeladera = new EstadoHeladera(true);

        this.setEstadoHeladeraActual(estadoHeladera);
        this.agregarEstadoHeladeraAlHistorial(estadoHeladera);
    }

    public void desactivarHeladera() {
        //finalizo estado anterior
        estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());

        EstadoHeladera estadoHeladera = new EstadoHeladera(false);

        this.setEstadoHeladeraActual(estadoHeladera);
        this.agregarEstadoHeladeraAlHistorial(estadoHeladera);
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