package org.example.validadores;

import org.example.excepciones.ImagenURLException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class VerificadorImagenURL {


    private static VerificadorImagenURL instancia = null;

    private VerificadorImagenURL(){}

    public static VerificadorImagenURL getInstancia(){
        if (instancia == null) {
            instancia = new VerificadorImagenURL();
        }
        return instancia;
    }

    public void verifyImagen(String imagenURL) throws ImagenURLException {//llevarlo a una clase verify
        if(imagenURL!=null && !imagenURL.equals("") && !this.validarImagen(imagenURL)){
            throw new ImagenURLException("La url de la imagen ingresada no es válida o no existe");
        }
    }

    private boolean validarImagen(String fotoURL){
        try {

            // Validar que la URL esté bien formada
            URL url = new URI(fotoURL).toURL();

            // Abrir conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD"); // Solo obtener encabezados
            connection.setConnectTimeout(5000); // Timeout de 5 segundos
            connection.setReadTimeout(5000);
            connection.connect();

            // Verificar el código de estado
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false; // No existe o no es accesible
            }

            // Verificar el tipo de contenido
            String contentType = connection.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                return true; // Es una imagen válida
            }
        } catch (Exception e) {
            // En caso de error (URL inválida o conexión fallida)
            e.printStackTrace();
        }

        return false; // No es una imagen válida o no se puede acceder
    }

}
