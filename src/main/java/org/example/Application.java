package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;
import io.prometheus.client.exporter.HTTPServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.example.observabilidad.QueuedThreadPoolCollector;
import org.example.observabilidad.StatisticsHandlerCollector;
import org.example.presentacion.*;
import org.example.autenticacion.SessionManager;
import org.example.utils.BDUtils;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {


        StatisticsHandler statisticsHandler = new StatisticsHandler();
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool(200, 8, 60_000);

        Javalin app = Javalin.create(javalinConfig -> {
                    javalinConfig.plugins.enableCors(
                            cors ->
                            cors.add(CorsPluginConfig::anyHost)); // para poder hacer requests de un dominio a otro

                    javalinConfig.staticFiles.add("/public", Location.CLASSPATH); // recursos estaticos (HTML, CSS, JS, IMG)
                    javalinConfig.routing.contextPath = "";

                    javalinConfig.jetty.server(() -> {
                        Server server = new Server(queuedThreadPool);
                        server.setHandler(statisticsHandler);
                        return server;
                    });
                })
                .start(8080);
        app.exception(IllegalArgumentException.class, (e, ctx) -> ctx.status(400));

        initializePrometheus(statisticsHandler, queuedThreadPool);

        app.get("/api/localidades", new GetLocalidadesHandler());
        app.get("/alertas/{nombre}", new AlertasHandler());

        app.routes(() -> {

            path("reportes", () -> {
                get("", ReportesController::mostrarFallas);
                get("fallas", ReportesController::mostrarFallas);
                get("reporteColocadas", ReportesController::mostrarViandasColocadas);
                get("reporteDistribuidas", ReportesController::mostrarViandasDistribuidas);
                get("reporteRetiradas", ReportesController::mostrarViandasRetiradas);
            });

            path("heladeras", () -> {
                get("registrar", HeladerasController::getRegistrar);
                post("registrar", HeladerasController::postRegistrar);
                get("reporte", HeladerasController::getReporte);
                post("reporte", HeladerasController::postReporte);
                get("", HeladerasController::getVisualizar);
                get("{id}", HeladerasController::getCaracteristicas);
                get("{id}/alertas", HeladerasController::getAlertas);
                get("{id}/alertas/{idAlerta}", HeladerasController::getAlerta);
                post("{id}/suscripcion-desperfecto", HeladerasController::postSuscripcionDesperfectos);
                post("{id}/suscripcion-faltantes", HeladerasController::postSuscripcionViandasFaltantes);
                post("{id}/suscripcion-disponibles", HeladerasController::postSuscripcionViandasDisponibles);
                post("{heladera}/suscripciones/{suscripcion}/cancelar", HeladerasController::cancelarSuscripcion);
            });

            path("usuarios", () -> {
                post("inicio-sesion", UsuarioController::postIniciosesion);
                post("registrarse", UsuarioController::postRegistrarse);
                get("registrarse", UsuarioController::getRegistrarUsuario);
                get("InicioSession", UsuarioController::getIniciosesion);
                get("cerrar-sesion", UsuarioController::getCerrarSesion);
            });

            path("carga-csv", () -> {
                post("upload", CargaCSVController::postUploadCSV);
                get("", CargaCSVController::getUploadCSV);
            });

            path("donar-vianda", () -> {
                post("", DonarViandasController::postDonarVianda);
                get("", DonarViandasController::getDonarVianda);
            });

            path("donar-dinero", () -> {
                post("", DonarDineroController::postDonarDinero);
                get("", DonarDineroController::getDonarDinero);
            });

            path("registrar-persona-vulnerable", () -> {
                post("", RegistrarPersonaVulnerableController::postRegistrarPersonaVulnerable);
                get("", RegistrarPersonaVulnerableController::getRegistrarPersonaVulnerable);
            });

            path("colaboraciones", () -> get("", ColaboracionesController::getColaboraciones));

            path("puntos", () -> {
                get("", OfertasController::getOfertas);
                post("",OfertasController::postCanjearOfera);
            });

            path("ofrecer-producto", () -> {
                get("", OfertasController::getOfrecerProducto);
                post("", OfertasController::postOfrecerProducto);
            });
        });

        List<String> rutasSinSesion = List.of("/usuarios", "/api/localidades", "/views/imagenes", "/styles", "/views/js");
        app.before(ctx -> {
            for (String ruta: rutasSinSesion){
                if (ctx.path().startsWith(ruta)) return;
            }
            //comentar esto para trabajar sin loguearse
            SessionManager.getInstancia().validarUsuario(ctx);
        });

        // EntityManager em = BDUtils.getEntityManager();
        // cargarScriptSql(em);
    }

    private static void cargarScriptSql(EntityManager em){
        try{//inserto valores por defualt para mostrar del script defualt_data.sql
            InputStream inputStream = Application.class.getClassLoader().getResourceAsStream("default_data.sql");
            Path tempFile = Files.createTempFile("temp-sql", ".sql");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            String sqlScript = Files.readString(tempFile);
            String[] queries = sqlScript.split(";");

            BDUtils.comenzarTransaccion(em);
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    em.createNativeQuery(query.trim()).executeUpdate();
                }
            }
            em.getTransaction().commit();

        } catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    private static void initializePrometheus(StatisticsHandler statisticsHandler, QueuedThreadPool queuedThreadPool) throws IOException {
        StatisticsHandlerCollector.initialize(statisticsHandler);
        QueuedThreadPoolCollector.initialize(queuedThreadPool);
        HTTPServer prometheusServer = new HTTPServer(7080);
        LoggerFactory.getLogger("Main")
                .info("Prometheus is listening on: http://localhost:7080");
    }
}
