package org.example.Presentacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.incidentes.Alerta;
import org.example.repositorios.RepoIncidente;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AlertasHandler implements Handler {
    private final RepoIncidente repoIncidente;

    public AlertasHandler(){
        this.repoIncidente = RepoIncidente.getInstancia();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        // prueba();
        String nombreHeladera = context.pathParamAsClass("nombre",String.class).get();
        var resultadoBusqueda = repoIncidente.obtenerTodasLasAlertas().stream()
                .filter(incidente -> incidente.getHeladera().getNombre().equals(nombreHeladera)).collect(Collectors.toList());

        Map<String, Object> model = new HashMap<>();
        model.put("listaAlertas", resultadoBusqueda);
        model.put("nombre",nombreHeladera);
        context.render("views/heladeras/alertas.mustache",model);
    }

    //TODO codigo hardcodeado para mostrar algo
    private void prueba() throws Exception{
        this.repoIncidente.clean();
        Heladera heladera = new Heladera();
        heladera.setNombre("MedranoUTN");
        Alerta alerta = new Alerta("algunTipoDeAlerta",heladera,"algunTipoIncidente", LocalDateTime.now());
        repoIncidente.agregarAlerta(alerta);
    }
}
