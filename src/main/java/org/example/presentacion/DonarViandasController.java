package org.example.presentacion;

import io.javalin.http.Context;

import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Entrega;
import org.example.colaboraciones.contribuciones.viandas.EstadoEntrega;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.excepciones.ContribucionNoPermitidaException;
import org.example.excepciones.FormatoFechaInvalidaException;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoHeladeras;
import org.jetbrains.annotations.NotNull;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DonarViandasController extends ContribucionController {

    private static final String VIEW_DONAR_VIANDAS ="/views/colaboraciones/donar-viandas.mustache";

    private static final String ERROR_FIELD ="error";


    public static void postDonarVianda(@NotNull Context context) throws MessagingException, ContribucionNoPermitidaException, FormatoFechaInvalidaException {
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
        assert calorias != null;
        if(!Objects.equals(calorias, "")){
            vianda.setCalorias(Integer.parseInt(calorias));
        }

        assert peso != null;
        if(!peso.equals("")){
            vianda.setPeso(Float.parseFloat(peso));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        vianda.setFechaCaducidad(LocalDateTime.parse(fechaCaducidad+" 00:00",formatter));
        vianda.setFechaDonacion(LocalDateTime.now());
        vianda.setColaborador(colaborador);

        assert entregado != null;
        if(entregado.equals("si")){
            assert fechaEntregado != null;
            if(fechaEntregado.equals("")){
                throw new FormatoFechaInvalidaException("No se introdujo una fecha de entrega válida");
            }else {
                DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                vianda.setEntrega(new Entrega(EstadoEntrega.ENTREGADO,LocalDate.parse(fechaEntregado,formatterDate)));
            }
        } else if(entregado.equals("no")){
            vianda.setEntrega(new Entrega(EstadoEntrega.PENDIENTE,null));
        }

        assert idHeladera != null;
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
                model.put(ERROR_FIELD, e.getMessage());
                context.render(VIEW_DONAR_VIANDAS, model);
            }

            if (heladera.getCapacidadEnViandas() - heladera.getViandasEnHeladera() < donacionDeViandas.getCantidadDeViandas()) {
                model.put(ERROR_FIELD, "La heladera esta llena");
                context.render(VIEW_DONAR_VIANDAS, model);
                return;
            }

            // no usamos ejecutarContribucion porque eso involucra lo de las tarjetas
            colaborador.agregarContribucion(donacionDeViandas);
            colaborador.calcularPuntuaje();
            heladera.notificarCambioViandas(List.of(vianda), List.of());

            model.put("exito", "Contribucion registrada con exito");
            context.render(VIEW_DONAR_VIANDAS, model);
        } else {
            model.put(ERROR_FIELD, "No existe la heladera seleccionada");
            context.render(VIEW_DONAR_VIANDAS, model);
        }
    }

    public static void getDonarVianda(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        model.put("heladeras", heladeras);
        context.render(VIEW_DONAR_VIANDAS, model);
    }
}
