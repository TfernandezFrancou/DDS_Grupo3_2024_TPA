package org.example.autenticacion;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.javalin.http.Context;
import org.example.config.GoogleConfig;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuthController {

    private static AuthorizationCodeFlow flow;

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
        // Implementación del callback
    }
}