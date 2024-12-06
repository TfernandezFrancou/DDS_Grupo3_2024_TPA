package org.example.tarjetas;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.heladeras.Uso;
import org.example.excepciones.LimiteDeTiempoSuperado;
import org.example.excepciones.LimiteDeUsosDiariosSuperados;
import org.example.personas.roles.Rol;
import org.example.repositorios.RepoApertura;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@Entity
public class TarjetaColaborador extends Tarjeta  {
    private int limiteDeTiempoDeUsoEnHoras;

    public TarjetaColaborador(){
        this.setUsos(new ArrayList<>());
        this.limiteDeTiempoDeUsoEnHoras = 3;
    }

    @Override
    public void usar(Rol duenio, Heladera heladera) throws LimiteDeTiempoSuperado
    {
        RepoApertura repoApertura = RepoApertura.getInstancia();

        Apertura solicitudDeApertura = repoApertura.buscarSolicitudDeApertura(heladera,this);
        LocalDateTime fechaActual = LocalDateTime.now();
        if(solicitudDeApertura.getFechahoraCreacion().plusHours(limiteDeTiempoDeUsoEnHoras).isBefore(fechaActual)){
            throw new LimiteDeTiempoSuperado();
        }
        Uso nuevoUso = new Uso(fechaActual, heladera);
        this.getUsos().add(nuevoUso);
    }
}
