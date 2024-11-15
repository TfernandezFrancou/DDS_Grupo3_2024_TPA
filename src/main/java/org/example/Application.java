package org.example;

import io.javalin.Javalin;
import org.example.Presentacion.*;
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
                        // TODO vistas faltantes: registro-persona-vulnerable, reporte, reportes, puntos, ofrecer-producto, mis-colaboraciones
                        // y arreglar la navegacion entre vistas
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
                get("reporte", HeladerasController::getReporte);
                post("reporte", HeladerasController::postReporte);
                get("", HeladerasController::getVisualizar);
                get("{id}", HeladerasController::getCaracteristicas);
                get("{id}/alertas", HeladerasController::getAlertas);
            });

            path("usuarios", () -> {
                post("inicio-session", UsuarioController::postInicioSeccion);
                post("registrarse", UsuarioController::postRegistrarse);
                get("registrarse", UsuarioController::getRegistrarUsuario);
            });

            path("carga-csv", ()->{
                post("upload", CargaCSVController::postUploadCSV);
                get("", CargaCSVController::getUploadCSV);
            });

            path("donar-vianda",()->{
                post("", DonarViandasController::postDonarVianda);
                get("", DonarViandasController::getDonarVianda);
            });

        });

        EntityManager em = BDUtils.getEntityManager();
    }
}
