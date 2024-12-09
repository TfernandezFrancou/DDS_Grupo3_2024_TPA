package org.example.presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.SessionManager;
import org.example.colaboraciones.contribuciones.OfrecerProductos;
import org.example.colaboraciones.contribuciones.ofertas.Oferta;
import org.example.excepciones.ContribucionNoPermitidaException;
import org.example.excepciones.ImagenURLException;
import org.example.excepciones.OfertaException;
import org.example.personas.roles.Colaborador;
import org.example.repositorios.RepoOfertas;
import org.example.validadores.VerificadorImagenURL;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OfertasController extends ContribucionController {

    private static final String FIELD_NOMBRE = "nombre";
    private static final String FIELD_IMAGEN_URL= "imagenURL";
    private static final String FIELD_PUNTOS_NECESARIOS= "puntosNecesarios";

    private static final String FIELD_PUNTOS= "puntos";

    private static final String VIEW_OFRECER_PRODUCTO = "views/colaboraciones/ofrecer-producto.mustache";
    private static final String VIEW_PUNTOS = "views/colaboraciones/puntos.mustache";


    public static void postCanjearOfera(@NotNull Context context) throws ContribucionNoPermitidaException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));

        String nombreProducto = context.formParam(FIELD_NOMBRE);

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
        List<Oferta> ofertas = RepoOfertas.getInstancia().obtenerTodas();
        List<Map<String, String>> ofertasView = new ArrayList<>();
        for (Oferta o : ofertas) {
            if (o.getNombre().equals(nombreProducto) && errorMessage != null) {
                ofertasView.add(Map.of(
                        FIELD_NOMBRE, o.getNombre(),
                        FIELD_IMAGEN_URL, o.getImagenURL(),
                        FIELD_PUNTOS_NECESARIOS, o.getPuntosNecesarios().toString(),
                        "error", errorMessage
                ));
            } else if (o.getNombre().equals(nombreProducto)) {
                ofertasView.add(Map.of(
                        FIELD_NOMBRE, o.getNombre(),
                        FIELD_IMAGEN_URL, o.getImagenURL(),
                        FIELD_PUNTOS_NECESARIOS, o.getPuntosNecesarios().toString(),
                        "exito", "canjeado con éxito"
                ));
            } else {
                ofertasView.add(Map.of(
                        FIELD_NOMBRE, o.getNombre(),
                        FIELD_IMAGEN_URL, o.getImagenURL(),
                        FIELD_PUNTOS_NECESARIOS, o.getPuntosNecesarios().toString()
                ));
            }
        }

        List<Map<String, String>>  ofertasCanjeadasView = generarVistaProductosCanjeados(colaborador.getOfertasCanjeadas());
        model.put("productosCanjeados",ofertasCanjeadasView);
        model.put(FIELD_PUNTOS, puntos);
        model.put("ofertas", ofertasView);
        context.render(VIEW_PUNTOS, model);
    }

    public static void getOfertas(@NotNull Context context) throws ContribucionNoPermitidaException {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        int puntos = (int) colaborador.getPuntuaje();


        List<Map<String, String>>  ofertasCanjeadasView = generarVistaProductosCanjeados(colaborador.getOfertasCanjeadas());

        model.put("productosCanjeados",ofertasCanjeadasView);
        model.put(FIELD_PUNTOS, puntos);
        List<Oferta> ofertas = RepoOfertas.getInstancia().obtenerTodas();
        model.put("ofertas", ofertas);
        context.render(VIEW_PUNTOS, model);
    }
    private static List<Map<String, String>> generarVistaProductosCanjeados(List<Oferta> ofertasCanjeadas) {
        Set<String> nombresVistos = new HashSet<>();
        List<Oferta> ofertasList = ofertasCanjeadas.stream()
                .filter(oferta -> nombresVistos.add(oferta.getNombre())) // Solo agregar si es nuevo
                .toList();

        return ofertasList.stream()
                .map(oferta -> Map.of(
                        FIELD_NOMBRE, oferta.getNombre(),
                        "cantidad", String.valueOf(
                                ofertasCanjeadas.stream()
                                        .filter(o -> o.getNombre().equalsIgnoreCase(oferta.getNombre()) )
                                        .toList().size()
                        )
                )).toList();
    }


    public static void getOfrecerProducto(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        context.render(VIEW_OFRECER_PRODUCTO, model);
    }

    public static void postOfrecerProducto(@NotNull Context context){
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        try{
            OfrecerProductos contribucion = almacernarOferta(context);
            verificarPuedeHacerContribucion(contribucion,context);

            contribucion.ejecutarContribucion();
            model.put("exito", "El registro fue exitoso");
            context.render(VIEW_OFRECER_PRODUCTO, model);
        }catch (Exception exception){
            model.put("error", exception.getMessage());
            exception.printStackTrace();
            context.render(VIEW_OFRECER_PRODUCTO, model);
        }

    }

    private static OfrecerProductos almacernarOferta(@NotNull Context context) throws ContribucionNoPermitidaException, ImagenURLException, OfertaException {
        String nombre = context.formParam(FIELD_NOMBRE);
        String puntos = context.formParam(FIELD_PUNTOS);
        assert puntos != null;
        int intPuntos = Integer.parseInt(puntos);
        String fotoUrl = context.formParam("imagen");

        Oferta ofertaExistente = RepoOfertas.getInstancia().buscarPorNombre(nombre);

        if(ofertaExistente != null){
            throw new OfertaException("Ya existe una oferta con el mismo nombre");
        }

        VerificadorImagenURL verificadorImagenURL = VerificadorImagenURL.getInstancia();
        verificadorImagenURL.verifyImagen(fotoUrl);

        OfrecerProductos ofrecerProductos = new OfrecerProductos();
        Oferta oferta = new Oferta(nombre,intPuntos,fotoUrl);

        ofrecerProductos.agregarOferta(oferta);
        Colaborador colaborador = obtenerRolColaboradorActual(context);
        ofrecerProductos.setColaborador(colaborador);
        return ofrecerProductos;
    }


}
