package org.example.subscripcionesHeladeras;

import lombok.Getter;
import lombok.Setter;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoHeladeras;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PublisherDesperfecto extends PublisherHeladera{

    public PublisherDesperfecto() {
    }

    @Override
    public void suscribir(SubscripcionHeladera subscripcion) {
        RepoHeladeras.getInstancia().agregarSubscripcion(subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera) throws MessagingException {
        List<SubscripcionDesperfecto> subs = RepoHeladeras.getInstancia().obtenerSubscripcionesDesperfecto(heladera.getIdHeladera());
        for (SubscripcionHeladera subscripcion : subs) {
            subscripcion.notificar(heladera);
        }
    }
}
