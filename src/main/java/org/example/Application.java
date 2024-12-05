package org.example;

import io.javalin.Javalin;
import org.example.Presentacion.*;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.incidentes.Alerta;
import org.example.incidentes.FallaTecnica;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoOfertas;
import org.example.utils.BDUtils;
import static io.javalin.apibuilder.ApiBuilder.*;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Application {

    public static void main(String[] args) {
        Javalin app = Javalin.create(javalinConfig -> {
                    javalinConfig.plugins.enableCors(cors -> {
                        cors.add(it -> it.anyHost());
                    }); // para poder hacer requests de un dominio a otro
                    javalinConfig.staticFiles.add("/"); // recursos estaticos (HTML, CSS, JS, IMG)
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

            path("reportes", () -> {
                get("", ReportesController::getListaReportes);
                //get(":id", ReportesController::getDetalleReporte);  <---   TODO Falta ver
                get("fallas", ReportesController::getReporteFallas);
                get("viandas-colocadas", ReportesController::getReporteViandasColocadas);
                get("viandas-distribuidas", ReportesController::getReporteViandasDistribuidas);
                get("viandas-retiradas", ReportesController::getReporteViandasRetiradas);
            });

            path("heladeras", () -> {
                get("registrar", HeladerasController::getRegistrar);
                post("registrar", HeladerasController::postRegistrar);
                get("reporte", HeladerasController::getReporte);
                post("reporte", HeladerasController::postReporte);
                get("", HeladerasController::getVisualizar);
                get("{id}", HeladerasController::getCaracteristicas);
                get("{id}/alertas", HeladerasController::getAlertas);
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

            path("colaboraciones", () -> {
                get("", ColaboracionesController::getColaboraciones);
            });

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
            // comentar esto para trabajar sin loguearse
            SessionManager.getInstancia().validarUsuario(ctx);
        });

        EntityManager em = BDUtils.getEntityManager();
        cargarScriptSql(em);
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
}
