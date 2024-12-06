package org.example.subscripciones_heladeras;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoHeladeras;

import javax.mail.MessagingException;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PublisherViandasDisponibles extends PublisherHeladera {

    @Override
    public void suscribir(SubscripcionHeladera subscripcion) {
        RepoHeladeras.getInstancia().agregarSubscripcion(subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera) throws MessagingException {
        List<SubscripcionViandasDisponibles> subscripciones = RepoHeladeras.getInstancia().obtenerSubscripcionesViandasDisponibles(heladera.getIdHeladera());
        for (SubscripcionViandasDisponibles subscripcion : subscripciones){
            if (heladera.getViandasEnHeladera() == subscripcion.getCantidadDeViandas()) {
                subscripcion.notificar(heladera);
            }
        }
    }
}