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
    // private List<SubscripcionDesperfecto> subscripciones;

    public PublisherDesperfecto() {
        // this.subscripciones = new ArrayList<>();
    }

    @Override
    public void suscribir(SubscripcionHeladera subscripcion) {
        // subscripciones.add((SubscripcionDesperfecto) subscripcion);
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
