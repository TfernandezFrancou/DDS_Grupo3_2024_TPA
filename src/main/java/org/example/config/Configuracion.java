package org.example.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {
    private Configuracion(){}

    public static String obtenerProperties(String nombreProperty) {
        Properties appProps = new Properties();

        InputStream iputStream = Configuracion.class.getClassLoader().getResourceAsStream("application.properties");

        try {
            appProps.load(iputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appProps.getProperty(nombreProperty);
    }
}
