package org.example.autenticacion;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import org.example.config.Configuracion;
import org.example.excepciones.UserException;
import org.example.personas.Persona;
import org.example.personas.PersonaHumana;
import org.example.personas.PersonaJuridica;
import org.example.repositorios.RepoUsuario;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;

public class SessionManager {

    private final Map<String, List<LocalDateTime>> intentos ;

    private static SessionManager instancia = null;
    private SessionManager(){
        this.intentos = new HashMap<>();
    }

    public static SessionManager getInstancia(){
        if(instancia == null){
            instancia = new SessionManager();
        }
        return instancia;
    }


    // devuelve el token para la cookie
    public String iniciarSesion(String username, String password) throws UserException {
        this.limpiarIntentosViejos();
        // si no tiene intentos, creo la lista
        if (!this.intentos.containsKey(username)) {
            this.intentos.put(username, new ArrayList<>());
        }
        List<LocalDateTime> intentosRealizados = this.intentos.get(username);
        // agrego el intento actual
        intentosRealizados.add(LocalDateTime.now());
        // si tiene mas de 3 intentos en 10 segundos falla
        if (intentosRealizados.size() > 3) {
            throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-sesion.volver-a-intentar"));
        }
        // inicio chequeando que exista y validando la contraseña
        Usuario usuarioQueQuiereIngresar = ValidarUsuario.getInstancia().validarUsuarioIngresado(username, password); //válido que el usuario esté en una lista de usuarios registrados
        if (usuarioQueQuiereIngresar == null) {

            throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-sesion.contrasenia-incorrecta"));

        }

        // creo y devuelvo el JWT para ser guardado en la cookie
        return this.crearJWT(Objects.requireNonNull(usuarioQueQuiereIngresar).getIdUsuario());
    }

    // borra los intentos que tienen mas de 10 segundos, y las listas vacias
    private void limpiarIntentosViejos() {
        for (Map.Entry<String,List<LocalDateTime>> entry  : intentos.entrySet()) {
            String usuario = entry.getKey();
            List<LocalDateTime> i = entry.getValue();
            i.removeIf(intento -> intento.isBefore(LocalDateTime.now().minusSeconds(10)));
            if (i.isEmpty()) intentos.remove(usuario);
        }
    }

    private String crearJWT(int idUsuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Configuracion.obtenerProperties("jwt.hmac-secret"));
            return JWT.create()
                    .withIssuer("auth0")
                    .withClaim("idUsuario", idUsuario)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
           exception.printStackTrace();
            return "";
        }
    }

    // devuelve el idUsuario
    private int parsearJWT(String token) throws UserException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(Configuracion.obtenerProperties("jwt.hmac-secret"));
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token);
            return decoded.getClaim("idUsuario").asInt();
        } catch (Exception e) {
            throw new UserException("La sesion no es valida");
        }
    }

    public void validarUsuario(@NotNull Context context) {
        String token = context.cookie("token");
        if (token == null) {
            context.redirect("/usuarios/InicioSession");
            return;
        }
        try {
            int idUsuario = parsearJWT(token);
            Usuario usuario = RepoUsuario.getInstancia().obtenerUsuarioPorId(idUsuario);
            context.attribute("usuario", usuario);
        } catch (Exception e) {
            context.redirect("/usuarios/InicioSession");
        }
    }

    public Map<String, Object> atributosDeSesion(@NotNull Context context) {
        Map<String, Object> atributos = new HashMap<>();
        Usuario usuario = context.attribute("usuario");
        Persona persona = Objects.requireNonNull(usuario).getColaborador();
        if (persona instanceof PersonaHumana) {
            atributos.put("DonacionDeDinero?", "true");
            atributos.put("DonacionDeViandas?", "true");
            atributos.put("DistribucionDeViandas?", "true");
            atributos.put("RegistrarPersonasEnSituacionVulnerable?", "true");
        } else if (persona instanceof PersonaJuridica) {
            atributos.put("DonacionDeDinero?", "true");
            atributos.put("HacerseCargoDeUnaHeladera?", "true");
            atributos.put("OfrecerProductos?", "true");
        }
        return atributos;
    }
}