package org.example.colaboraciones.contribuciones.heladeras;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.Ubicacion;
import org.example.config.Configuracion;
import org.example.excepciones.SolicitudVencida;
import org.example.repositorios.RepositorioAperturasHeladera;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepositorioSolicitudesApertura;
import org.example.subscripcionesHeladeras.PublisherDesperfecto;
import org.example.subscripcionesHeladeras.PublisherViandasDisponibles;
import org.example.subscripcionesHeladeras.PublisherViandasFaltantes;
import org.example.tarjetas.SolicitudDeApertura;
import org.example.tarjetas.TarjetaColaborador;

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

    public void solicitarApertura(TarjetaColaborador tarjeta) {
        LocalDateTime fecha = LocalDateTime.now();
        SolicitudDeApertura solicitud = new SolicitudDeApertura(this, fecha, tarjeta);
        RepositorioSolicitudesApertura.getInstancia().agregarSolicitudDeApertura(solicitud);
    }

    public void registrarApertura(TarjetaColaborador tarjetaColaborador) throws SolicitudVencida {
        SolicitudDeApertura solicitud = RepositorioSolicitudesApertura.getInstancia().buscarSolicitudDeApertura(this, tarjetaColaborador);
        LocalDateTime horarioApertura = LocalDateTime.now();
        boolean vencida = solicitud.getFechaCreacion().plusMinutes(tarjetaColaborador.getLimiteDeTiempoEnMinutos()).isBefore(horarioApertura);
        if (vencida) throw new SolicitudVencida(Configuracion.obtenerProperties("mensaje.apertura-heladera.solicitud-vencida"));
        RepositorioAperturasHeladera.getInstancia().agregarApertura(new AperturaHeladera(this, tarjetaColaborador, horarioApertura));
    }
}