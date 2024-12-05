package org.example.Presentacion;

import io.javalin.http.Context;

import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Entrega;
import org.example.colaboraciones.contribuciones.viandas.EstadoEntrega;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoHeladeras;
import org.jetbrains.annotations.NotNull;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DonarViandasController extends ContribucionController {

    public static void postDonarVianda(@NotNull Context context) throws MessagingException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);

        Colaborador colaborador = obtenerRolColaboradorActual(context);

        String tipoComida = context.formParam("tipo-comida");
        String fechaCaducidad = context.formParam("fecha-caducidad");
        String idHeladera = context.formParam("heladera");
        String calorias = context.formParam("calorias");
        String peso = context.formParam("peso");
        String entregado = context.formParam("entregado");
        String fechaEntregado = context.formParam("fecha-entregado");

        Vianda vianda = new Vianda();
        vianda.setDescripcion(tipoComida);
        vianda.setCalorias(Integer.parseInt(calorias));
        vianda.setPeso(Float.parseFloat(peso));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        vianda.setFechaCaducidad(LocalDateTime.parse(fechaCaducidad+" 00:00",formatter));
        vianda.setFechaDonacion(LocalDateTime.now());
        vianda.setColaborador(colaborador);

        if(entregado.equals("si")){
            if(fechaEntregado.equals("")){
                throw new RuntimeException("No se introdujo una fecha de entrega válida");
            }else {
                DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                vianda.setEntrega(new Entrega(EstadoEntrega.ENTREGADO,LocalDate.parse(fechaEntregado,formatter_date)));
            }
        } else if(entregado.equals("no")){
            vianda.setEntrega(new Entrega(EstadoEntrega.PENDIENTE,null));
        }

        Integer idHeladeraInt = Integer.parseInt(idHeladera);
        Optional<Heladera> heladeraOptional = RepoHeladeras.getInstancia().buscarPorId(idHeladeraInt);

        if(heladeraOptional.isPresent()){
            Heladera heladera = heladeraOptional.get();
            vianda.setHeladera(heladera);

            DonacionDeViandas donacionDeViandas = new DonacionDeViandas();
            donacionDeViandas.setViandas(List.of(vianda));
            donacionDeViandas.setFecha(LocalDate.now());
            donacionDeViandas.setHeladera(heladeraOptional.get());
            donacionDeViandas.setCantidadDeViandas(1);
            donacionDeViandas.setColaborador(colaborador);

            try{
                verificarPuedeHacerContribucion(donacionDeViandas,context);
            } catch (Exception e){
                e.printStackTrace();
                model.put("error", e.getMessage());
                context.render("/views/colaboraciones/donar-viandas.mustache", model);
            }


            // TODO: esto no deberia ir en ejecutarContribucion junto con lo otro ?
            if (heladera.getCapacidadEnViandas() - heladera.getViandasEnHeladera() < donacionDeViandas.getCantidadDeViandas()) {
                model.put("error", "La heladera esta llena");
                context.render("/views/colaboraciones/donar-viandas.mustache", model);
                return;
            }

            // no usamos ejecutarContribucion porque eso involucra lo de las tarjetas
            colaborador.agregarContribucion(donacionDeViandas);
            colaborador.calcularPuntuaje();
            heladera.notificarCambioViandas(List.of(vianda), List.of());

            model.put("exito", "Contribucion registrada con exito");
            context.render("/views/colaboraciones/donar-viandas.mustache", model);
        } else {
            model.put("error", "No existe la heladera seleccionada");
            context.render("/views/colaboraciones/donar-viandas.mustache", model);
        }
    }

    public static void getDonarVianda(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);
        context.render("/views/colaboraciones/donar-viandas.mustache", model);
    }
}
