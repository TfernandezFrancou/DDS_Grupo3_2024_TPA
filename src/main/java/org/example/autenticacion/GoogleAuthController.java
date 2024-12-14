package org.example.autenticacion;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import io.javalin.http.Context;
import org.example.config.GoogleConfig;
import org.example.repositorios.RepoUsuario;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuthController {

    private static GoogleAuthorizationCodeFlow flow;

    static {
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    GoogleConfig.CLIENT_ID,
                    GoogleConfig.CLIENT_SECRET,
                    Collections.singleton("https://www.googleapis.com/auth/userinfo.profile")
            ).build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static void login(Context ctx) {
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(GoogleConfig.REDIRECT_URI);
        ctx.redirect(authorizationUrl.build());
    }

    public static void oauth2callback(Context ctx) {
        String code = ctx.queryParam("code");
        if (code != null) {
            try {
                TokenResponse tokenResponse = flow.newTokenRequest(code)
                        .setRedirectUri(GoogleConfig.REDIRECT_URI)
                        .execute();
                Oauth2 oauth2 = new Oauth2.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JacksonFactory.getDefaultInstance(),
                        flow.createAndStoreCredential(tokenResponse, null)
                ).setApplicationName("SMAACVS").build();
                Userinfo googleUserinfo = oauth2.userinfo().get().execute();

                Usuario usuario = new Usuario();
                usuario.setNombreDeUsuario(googleUserinfo.getName());
                usuario.setEmail(googleUserinfo.getEmail());
                usuario.setFoto(googleUserinfo.getPicture());
                RepoUsuario.getInstancia().agregarUsuarios(usuario);

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