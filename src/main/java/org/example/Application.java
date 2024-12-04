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

//        Heladera origen = new Heladera("Heladera Medrano", new Ubicacion(2222,2222), new Direccion("Medrano", "3322", "Capital Federal"), 3);
//        Heladera destino = new Heladera("Heladera Santa Fe", new Ubicacion(2222,2222), new Direccion("Santa Fe", "2221", "Capital Federal"), 5);
//        RepoHeladeras.getInstancia().agregar(origen);
//        RepoHeladeras.getInstancia().agregar(destino);
//        Colaborador colaborador = new Colaborador();
//        PersonaHumana persona = new PersonaHumana("Juan", "Perez", new CorreoElectronico("juanperez@gmail.com"), new Documento(TipoDocumento.DNI, "33221133"), colaborador);
//        RepoPersona.getInstancia().agregar(persona);
//        colaborador.agregarContribucion(new DonacionDeDinero(colaborador, LocalDate.now(), 2222));
//        colaborador.agregarContribucion(new DonacionDeViandas(colaborador, origen, List.of(), LocalDate.now()));
//        colaborador.agregarContribucion(new DistribucionDeViandas(colaborador, origen, destino, List.of(), "Motivo de distribucion"));
//        colaborador.agregarContribucion(new HacerseCargoDeUnaHeladera(colaborador, List.of(origen)));
//        colaborador.agregarContribucion(new DonacionDeDinero(colaborador, LocalDate.now(), 201));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Bicicleta", 300, "/views/imagenes/bici.png"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Smart TV Marca XJSDJ", 300, "/views/imagenes/tv.ppng.webp"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Bicicleta", 300, "/views/imagenes/bici.png"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Bicicleta", 300, "/views/imagenes/bici.png"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Smart TV Marca XJSDJ", 300, "/views/imagenes/tv.ppng.webp"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Bicicleta", 300, "/views/imagenes/bici.png"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Bicicleta", 300, "/views/imagenes/bici.png"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Smart TV Marca XJSDJ", 300, "/views/imagenes/tv.ppng.webp"));
//        RepoOfertas.getInstancia().agregarOferta(new Oferta("Bicicleta", 300, "/views/imagenes/bici.png"));

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
            //TODO comento esto para que no tengan que iniciar sesion mientras hacen las vistas
            // SessionManager.getInstancia().validarUsuario(ctx);
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

            em.getTransaction().begin();
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    em.createNativeQuery(query.trim()).executeUpdate();
                }
            }
            em.getTransaction().commit();

        } catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }
}
