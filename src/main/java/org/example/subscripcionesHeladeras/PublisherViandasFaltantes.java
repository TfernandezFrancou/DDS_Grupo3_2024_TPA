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
public class PublisherViandasFaltantes extends PublisherHeladera{
    // private List<SubscripcionViandasFaltantes> subscripciones;

    public PublisherViandasFaltantes() {
        // this.subscripciones = new ArrayList<>();
    }

    @Override
    public void suscribir(SubscripcionHeladera subscripcion) {
        // subscripciones.add((SubscripcionViandasFaltantes) subscripcion);
        RepoHeladeras.getInstancia().agregarSubscripcion(subscripcion);
    }

    @Override
    public void notificarATodos(Heladera heladera) throws MessagingException {
        List<SubscripcionViandasFaltantes> subs = RepoHeladeras.getInstancia().obtenerSubscripcionesViandasFaltantes(heladera.getIdHeladera());
        for (SubscripcionViandasFaltantes subscripcion : subs) {
            if (heladera.faltanteParaLlenar() == subscripcion.getCantidadDeViandas()) {
                subscripcion.notificar(heladera);
            }
        }
    }
}
