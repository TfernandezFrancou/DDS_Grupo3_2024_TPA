package org.example;

import io.javalin.Javalin;
import org.example.Presentacion.AlertasHandler;
import org.example.Presentacion.CaracteristicasHandler;
import org.example.Presentacion.GetLocalidadesHandler;
import org.example.Presentacion.VisualizarHandler;
import org.example.utils.BDUtils;

import javax.persistence.EntityManager;

public class Application {

    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
                    javalinConfig.plugins.enableCors(cors -> {
                        cors.add(it -> it.anyHost());
                    }); // para poder hacer requests de un dominio a otro
                    //TODO algunas vistas hay que pasarlas a mustache o similar para mostrar datos reales
                    javalinConfig.staticFiles.add("/"); //recursos estaticos (HTML, CSS, JS, IMG)
                })
                .get("/hello", ctx -> ctx.result("Hello World"))
                .start(8080);

        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
        });

        app.get("/api/localidades", new GetLocalidadesHandler());
        app.get("/alertas/{nombre}", new AlertasHandler());
        app.get("/caracteristicas/{nombre}", new CaracteristicasHandler());
        app.get("/visualizar", new VisualizarHandler());

        EntityManager em = BDUtils.getEntityManager();
    }
}
