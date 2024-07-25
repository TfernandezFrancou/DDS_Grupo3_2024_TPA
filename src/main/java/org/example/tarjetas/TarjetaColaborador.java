package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.excepciones.LimiteDeTiempoSuperado;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Rol;
import org.example.repositorios.RepositorioSolicitudesApertura;

import java.time.LocalDateTime;

@Getter
@Setter
public class TarjetaColaborador extends Tarjeta  {
    private int limiteDeTiempoEnMinutos;

    @Override
    public void usar(Rol duenio, Heladera heladera) throws LimiteDeTiempoSuperado
    {
        RepositorioSolicitudesApertura repositorioSolicitudesApertura = RepositorioSolicitudesApertura.getInstancia();

        SolicitudDeApertura solicitudDeApertura =
                repositorioSolicitudesApertura.buscarSolicitudDeApertura(heladera,((Colaborador) duenio).getTarjetaColaborador());
        LocalDateTime fechaActual = LocalDateTime.now();
        if(solicitudDeApertura.getFechaCreacion().plusHours(limiteDeTiempoEnMinutos).isBefore(fechaActual)){
            throw new LimiteDeTiempoSuperado();
        }
        Uso nuevoUso = new Uso(fechaActual, heladera);
        this.getUsos().add(nuevoUso);
    }
}
