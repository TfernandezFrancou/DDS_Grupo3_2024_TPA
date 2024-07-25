package org.example.colaboraciones.contribuciones.heladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;
import org.example.excepciones.SolicitudInexistente;
import org.example.excepciones.SolicitudVencida;
import org.example.repositorios.RepoAperturaHeladera;
import org.example.repositorios.RepoHeladera;
import org.example.repositorios.RepoSolicitudApertura;
import org.example.subscripcionesHeladeras.PublisherDesperfecto;
import org.example.subscripcionesHeladeras.PublisherViandasDisponibles;
import org.example.subscripcionesHeladeras.PublisherViandasFaltantes;
import org.example.tarjetas.AperturaHeladera;
import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.TarjetaColaborador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
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

    public Heladera(){
        this.historialEstadoHeldera = new ArrayList<>();
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

    public void actualizarEstadoHeladera(Sensor sensor) {
        boolean nuevoEstado = sensor.getEstadoHeladera();
        if (this.estadoHeladeraActual.getEstaActiva() != nuevoEstado) {
            this.estadoHeladeraActual.setFechaHoraFin(LocalDateTime.now());
            this.estadoHeladeraActual = new EstadoHeladera(nuevoEstado);
            this.historialEstadoHeldera.add(this.estadoHeladeraActual);
        }
    }

    public void actualizarCantidadViandas(int viandasIntroducidas, int viandasSacadas) {
        MovimientoViandas m = new MovimientoViandas(viandasIntroducidas, viandasSacadas, LocalDateTime.now());
        this.historialMovimientos.add(m);
        this.viandasEnHeladera += viandasIntroducidas - viandasSacadas;
        RepoHeladera.getInstancia().actualizar(this);
        // TODO: notificar a los suscriptores
    }

    public int faltanteParaLlenar(){
        return this.capacidadEnViandas - this.viandasEnHeladera;
    }

    public void solicitarApertura(TarjetaColaborador tarjeta) {
        LocalDateTime fecha = LocalDateTime.now();
        SolicitudDeApertura solicitud = new SolicitudDeApertura(this, fecha, tarjeta);
        RepoSolicitudApertura.getInstancia().agregar(solicitud);
    }

    public void registrarApertura(TarjetaColaborador tarjeta) throws SolicitudInexistente, SolicitudVencida {
        // TODO: esta validacion va en una clase aparte segun el diagrama y las secuencias
        Optional<SolicitudDeApertura> s = RepoSolicitudApertura.getInstancia().buscarSolicitud(this, tarjeta);
        SolicitudDeApertura solicitud = s.orElseThrow(() -> new SolicitudInexistente("No se encontro solicitud para esa tarjeta"));
        LocalDateTime horarioApertura = LocalDateTime.now();
        boolean vencida = solicitud.getFechaCreacion().plusMinutes(tarjeta.getLimiteDeTiempoEnMinutos()).isBefore(horarioApertura);
        if (vencida) throw new SolicitudVencida("Ya paso su limite de tiempo");
        RepoAperturaHeladera.getInstancia().agregar(new AperturaHeladera(this, tarjeta, horarioApertura));
    }
}