package org.example.autenticacion;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import org.example.config.Configuracion;
import org.example.excepciones.PasswordException;
import org.example.excepciones.UserException;
import org.example.repositorios.RepoUsuario;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionManager {
    private static SessionManager instancia;

    private Map<String, List<LocalDateTime>> intentos;

    private SessionManager(){
        this.intentos = new HashMap<>();
    }

    public static SessionManager getInstancia() {
        if (instancia == null) {
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
        if (usuarioQueQuiereIngresar.getFechaExpiracionContrasenia().isAfter(LocalDateTime.now())) { // valido si la fecha de expiración es mayor a hoy
            //throw new UserException(Configuracion.obtenerProperties("mensaje.inicio-sesion.contrasenia-expirada"));
            //TODO lo ignoro por ahora, porque sino hay que crear otra vista para cambiar la contraseña expirada
        } else {
            System.out.println(Configuracion.obtenerProperties("mensaje.inicio-sesion.inicio-sesion-correcto"));
        }
        // creo y devuelvo el JWT para ser guardado en la cookie
        return this.crearJWT(usuarioQueQuiereIngresar.getIdUsuario());
    }

    // borra los intentos que tienen mas de 10 segundos, y las listas vacias
    private void limpiarIntentosViejos() {
        for (String usuario : intentos.keySet()) {
            List<LocalDateTime> i = intentos.get(usuario);
            i.removeIf((intento) -> intento.isBefore(LocalDateTime.now().minusSeconds(10)));
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
            System.err.println(exception.getMessage());
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
        } catch (JWTVerificationException e) {
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
}