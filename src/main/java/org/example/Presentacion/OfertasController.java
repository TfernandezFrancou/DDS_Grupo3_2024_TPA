package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoOfertas;
import org.example.repositorios.RepoPersona;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfertasController {
    public static void getOfertas(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        Usuario user =(Usuario) SessionManager.getInstancia().obtenerAtributo("usuario");
        int idPersona = user.getColaborador().getIdPersona();
        Persona persona = RepoPersona.getInstancia().buscarPorId(idPersona);
        int puntos = 0;
        if (persona.getRol() instanceof Colaborador) {
            puntos = (int) ((Colaborador) persona.getRol()).getPuntuaje();
        }
        model.put("puntos", puntos);
        List<Oferta> ofertas = RepoOfertas.getInstancia().obtenerTodas();
        model.put("ofertas", ofertas);
        context.render("views/colaboraciones/puntos.mustache", model);
    }

    public static void getOfrecerProducto(@NotNull Context context) throws Exception {
        context.render("views/colaboraciones/ofrecer-producto.mustache");
    }
}
