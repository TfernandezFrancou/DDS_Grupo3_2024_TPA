package org.example.Presentacion;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.repositorios.RepoHeladeras;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CaracteristicasHandler implements Handler {
    private final RepoHeladeras repoHeladeras;

    public CaracteristicasHandler(){
        this.repoHeladeras = RepoHeladeras.getInstancia();
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        prueba();
        String nombreHeladera = context.pathParamAsClass("nombre", String.class).get();
        final Optional<Heladera> resultadoBusqueda = this.repoHeladeras.buscarHeladeraPorNombre(nombreHeladera);

        if (resultadoBusqueda.isPresent()){
            var heladera = resultadoBusqueda.get();
            Map<String, Object> model = new HashMap<>();
            model.put("heladera", heladera);
            model.put("tiempoActiva", heladera.obtenerMesesActivos());
            context.render("views/heladeras/caracteristicas.mustache",model);
            //context.json(model);
        }else{
            context.status(404);
        }
    }

    //TODO sacar prueba porque hardcodeado
    private void prueba(){
        Heladera heladera1 = new Heladera();
        heladera1.setNombre("MedranoUTN");
        Direccion direccionHeladera = new Direccion();
        direccionHeladera.setAltura("951");
        direccionHeladera.setNombreCalle("Medrano");
        heladera1.setDireccion(direccionHeladera);
        heladera1.setCapacidadEnViandas(25);

        heladera1.actualizarEstadoHeladera(true);
        LocalDate fechaInicio = LocalDate.of(2023,5,23);
        heladera1.setFechaInicioFuncionamiento(fechaInicio);

        repoHeladeras.agregar(heladera1);
    }
}
