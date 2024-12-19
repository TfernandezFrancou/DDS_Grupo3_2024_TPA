package org.example.autenticacion;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import io.javalin.http.Context;
import org.example.config.Configuracion;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.repositorios.RepoUsuario;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GoogleAuthController {

    private static GoogleAuthorizationCodeFlow flow;

    private static List<String> scopes = Arrays.asList(
        "https://www.googleapis.com/auth/userinfo.email",
        "https://www.googleapis.com/auth/userinfo.profile"
    );

    static {
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    Configuracion.obtenerProperties("google.client-id"),
                    Configuracion.obtenerProperties("google.client-secret"),
                    scopes
            ).build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static void login(Context ctx) {
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(Configuracion.obtenerProperties("google.redirect-uri"));
        ctx.redirect(authorizationUrl.build());
    }

    public static void oauth2callback(Context ctx) {
        String code = ctx.queryParam("code");

        System.out.println("google code: " + code);

        if (code != null) {
            try {
                TokenResponse tokenResponse = flow.newTokenRequest(code)
                        .setRedirectUri(Configuracion.obtenerProperties("google.redirect-uri"))
                        .execute();
                Oauth2 oauth2 = new Oauth2.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance(),
                        flow.createAndStoreCredential(tokenResponse, null)
                ).setApplicationName("SMAACVS").build();
                Userinfo googleUserinfo = oauth2.userinfo().get().execute();

                System.out.println("google id: " + googleUserinfo.getId());

                Usuario usuario;
                try {
                    usuario = RepoUsuario.getInstancia().obtenerUsuarioPorGoogleId(googleUserinfo.getId());
                } catch (Exception e) {
                    PersonaHumana persona = new PersonaHumana();
                    persona.setNombre(googleUserinfo.getGivenName());
                    persona.setApellido(googleUserinfo.getFamilyName());
                    if (googleUserinfo.getEmail() != null) {
                        persona.addMedioDeContacto(new CorreoElectronico(googleUserinfo.getEmail()));
                    }
                    usuario = new Usuario();
                    usuario.setGoogleId(googleUserinfo.getId());
                    usuario.setColaborador(persona);
                    usuario.setNombreDeUsuario(googleUserinfo.getName());
                    usuario.setFoto(googleUserinfo.getPicture());
                    usuario.setEstanDatosCompletos(false);
                    RepoUsuario.getInstancia().agregarUsuarios(usuario);
                }

                String token = SessionManager.getInstancia().iniciarSesionConGoogle(usuario);
                ctx.cookie("token", token);
                ctx.redirect("/heladeras");
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                ctx.redirect("/login");
            }
        } else {
            ctx.redirect("/login");
        }
    }
}
