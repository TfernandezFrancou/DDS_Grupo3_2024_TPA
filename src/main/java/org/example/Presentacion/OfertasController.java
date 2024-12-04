package org.example.Presentacion;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.autenticacion.SessionManager;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.OfrecerProductos;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.PuntosInsuficienteParaCanjearOferta;
import org.example.personas.Persona;
import org.example.personas.PersonaJuridica;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoContribucion;
import org.example.repositorios.RepoHeladeras;
import org.example.repositorios.RepoOfertas;
import org.example.repositorios.RepoPersona;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfertasController extends ContribucionController {

    public static void postCanjearOfera(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();

        String nombreProducto = context.formParam("nombre");

        Oferta resultadoBusqueda = RepoOfertas.getInstancia().buscarPorNombre(nombreProducto);
        String errorMessage = null;
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        try{
            colaborador.canjearOferta(resultadoBusqueda); // tambien modifica el puntaje
        } catch (Exception ex){
            ex.printStackTrace();
            errorMessage = "puntos insuficientes";
        }

        int puntos = (int) colaborador.getPuntuaje();

        List<Oferta> ofertas = RepoOfertas.getInstancia().obtenerTodas();//se puede canjear mas de una vez una misma oferta
        List<Map<String,String>> ofertas_view = new ArrayList<>();
        //agrego mensaje de error si corresponde
        for (Oferta o: ofertas) {
            if(o.getNombre().equals(nombreProducto) && errorMessage!= null){
                ofertas_view.add(Map.of(
                        "nombre",o.getNombre(),
                        "imagenURL",o.getImagenURL(),
                        "puntosNecesarios", o.getPuntosNecesarios().toString(),
                        "error", errorMessage
                ));
            } else if(o.getNombre().equals(nombreProducto)){
                ofertas_view.add(Map.of(
                        "nombre",o.getNombre(),
                        "imagenURL",o.getImagenURL(),
                        "puntosNecesarios", o.getPuntosNecesarios().toString(),
                        "exito", "canjeado con exito"
                ));
            } else {
                ofertas_view.add(Map.of(
                        "nombre",o.getNombre(),
                        "imagenURL",o.getImagenURL(),
                        "puntosNecesarios", o.getPuntosNecesarios().toString()
                ));
            }
        }

        model.put("puntos", puntos);
        model.put("ofertas", ofertas_view);
        context.render("views/colaboraciones/puntos.mustache", model);
    }

    public static void getOfertas(@NotNull Context context) throws Exception {
        Map<String, Object> model = new HashMap<>();
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        int puntos = (int) colaborador.getPuntuaje();

        model.put("puntos", puntos);
        List<Oferta> ofertas = RepoOfertas.getInstancia().obtenerTodas();
        model.put("ofertas", ofertas);
        context.render("views/colaboraciones/puntos.mustache", model);
    }

    public static void getOfrecerProducto(@NotNull Context context) throws Exception {
        context.render("views/colaboraciones/ofrecer-producto.mustache");
    }

    public static void postOfrecerProducto(@NotNull Context context){
        try{
            verificarEsPersonaJuridica(context);
            OfrecerProductos contribucion = almacernarOferta(context);
            contribucion.ejecutarContribucion();
            Map<String, Object> model = new HashMap<>();
            model.put("exito", "El registro fue exitoso");
            context.render("views/colaboraciones/ofrecer-producto.mustache", model);
        }catch (Exception exception){
            Map<String, Object> model = new HashMap<>();
            model.put("error", exception.getMessage());
            context.render("views/colaboraciones/ofrecer-producto.mustache", model);
        }

    }

    private static void verificarEsPersonaJuridica(Context context) throws RuntimeException{
        Usuario user = context.attribute("usuario");
        Persona personaUser = user.getColaborador();
        if (!(personaUser instanceof PersonaJuridica)){
            throw new RuntimeException("Esta funcionalidad está disponible solo para personas jurídicas.");
        }
    }

    private static OfrecerProductos almacernarOferta(@NotNull Context context) throws RuntimeException{
        String nombre = context.formParam("nombre");
        String puntos = context.formParam("puntos");
        int intPuntos = Integer.parseInt(puntos);

        String fotoUrl = "";
        //TODO comento esto apra no tener que subir una foto
//        UploadedFile uploadedFile = context.uploadedFile("imagen");
//
//
//        if(uploadedFile != null){
//            String uploadDir = "/uploads/imagen/";
//            String fileName = uploadedFile.filename();
//            Path filePath = Paths.get(uploadDir, fileName);
//
//            try{
//                //crea el directorio si no existe
//                Files.createDirectories(filePath.getParent());
//
//                //guarda el archivo
//                Files.copy(uploadedFile.content(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                //guardo la url
//                fotoUrl = uploadDir + fileName;
//            } catch (Exception exception){
//                throw new RuntimeException("Error al guardar la imagen");
//            }
//        }

        OfrecerProductos ofrecerProductos = new OfrecerProductos();
        Oferta oferta = new Oferta(nombre,intPuntos,fotoUrl);

        ofrecerProductos.agregarOferta(oferta);
        Usuario usuario = context.attribute("usuario");
        Colaborador colaborador = asignarRol(usuario);
        ofrecerProductos.setColaborador(colaborador);
        return ofrecerProductos;

    }

    private static Colaborador asignarRol(Usuario usuario){
        Persona personaUser = usuario.getColaborador();
        if(personaUser.getRol() == null) {
            Colaborador colaboradorRol = new Colaborador();
            colaboradorRol.setEstaActivo(true);
            personaUser.setRol(colaboradorRol);
            // Actualiza persona para obtener id del rol
            personaUser = RepoPersona.getInstancia().actualizarPersona(personaUser);
        }
        return (Colaborador) personaUser.getRol();
    }
}
