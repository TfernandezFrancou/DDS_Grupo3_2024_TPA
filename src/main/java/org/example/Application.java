package org.example;

import io.javalin.Javalin;
import org.example.Presentacion.AlertasHandler;
import org.example.Presentacion.HeladerasController;
import org.example.Presentacion.GetLocalidadesHandler;
import org.example.Presentacion.UsuarioController;
import org.example.utils.BDUtils;
import static io.javalin.apibuilder.ApiBuilder.*;

import javax.persistence.EntityManager;

public class Application {

    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
                    javalinConfig.plugins.enableCors(cors -> {
                        cors.add(it -> it.anyHost());
                    }); // para poder hacer requests de un dominio a otro
                    //TODO algunas vistas hay que pasarlas a mustache o similar para mostrar datos reales
                    // javalinConfig.staticFiles.add("/"); //recursos estaticos (HTML, CSS, JS, IMG)
                    javalinConfig.staticFiles.add("/");
                    javalinConfig.routing.contextPath = "";
                })
                .get("/hello", ctx -> ctx.result("Hello World"))
                .start(8080);

        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
        });

        app.get("/api/localidades", new GetLocalidadesHandler());
        app.get("/alertas/{nombre}", new AlertasHandler());

        app.routes(() -> {
            path("heladeras", () -> {
                get("registrar", HeladerasController::getRegistrar);
                post("registrar", HeladerasController::postRegistrar);
                get("", HeladerasController::getVisualizar);
                get("{id}", HeladerasController::getCaracteristicas);
            });

            path("usuarios", () -> {
                post("inicio-session", UsuarioController::postInicioSeccion);
                post("registrarse", UsuarioController::postRegistrarse);
                get("Registrarse", UsuarioController::getRegistrarUsuario);
            });

        });

        EntityManager em = BDUtils.getEntityManager();
    }
}
