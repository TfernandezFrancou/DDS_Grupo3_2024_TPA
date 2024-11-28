package org.example;

import io.javalin.Javalin;
import org.example.Presentacion.*;
import org.example.colaboraciones.Ubicacion;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.HacerseCargoDeUnaHeladera;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.personas.roles.Colaborador;
import org.example.personas.roles.Rol;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoPersona;
import org.example.utils.BDUtils;
import static io.javalin.apibuilder.ApiBuilder.*;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        });

        EntityManager em = BDUtils.getEntityManager();
    }
}
