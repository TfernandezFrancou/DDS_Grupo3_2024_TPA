package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.heladeras.Heladera;
import org.example.colaboraciones.contribuciones.viandas.Entrega;
import org.example.colaboraciones.contribuciones.viandas.EstadoEntrega;
import org.example.colaboraciones.contribuciones.viandas.Vianda;
import org.example.incidentes.FallaTecnica;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoIncidente;
import org.example.repositorios.RepoPersona;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DonarViandasController extends ContribucionController {

    public static void postDonarVianda(@NotNull Context context) {
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
            vianda.setHeladera(heladeraOptional.get());

            DonacionDeViandas donacionDeViandas = new DonacionDeViandas();
            donacionDeViandas.setViandas(List.of(vianda));
            donacionDeViandas.setFecha(LocalDate.now());
            donacionDeViandas.setHeladera(heladeraOptional.get());
            donacionDeViandas.setCantidadDeViandas(1);


            actualizarPuntajeUsuarioActual(context, donacionDeViandas);

            Colaborador colaborador = obtenerRolColaboradorActual(context);
            vianda.setColaborador(colaborador);

            List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
            Map<String, Object> model = new HashMap<>();
            model.put("heladeras", heladeras);
            model.put("exito", "Contribucion registrada con exito");
            context.render("/views/colaboraciones/donar-viandas.mustache", model);
        } else {
            //error
            List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
            Map<String, Object> model = new HashMap<>();
            model.put("heladeras", heladeras);
            model.put("error", "No existe la heladera seleccionada");
            context.render("/views/colaboraciones/donar-viandas.mustache", model);
        }

    }

    public static void getDonarVianda(@NotNull Context context) {
        List<Heladera> heladeras = RepoHeladeras.getInstancia().obtenerTodas();
        Map<String, Object> model = new HashMap<>();
        model.put("heladeras", heladeras);
        model.put("error", "");
        context.render("/views/colaboraciones/donar-viandas.mustache", model);
    }

}
