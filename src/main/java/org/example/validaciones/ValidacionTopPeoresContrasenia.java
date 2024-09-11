package org.example.validaciones;

import org.example.config.Configuracion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ValidacionTopPeoresContrasenia extends ValidacionContrasenia {

    private List<String> contrasenias;

    public ValidacionTopPeoresContrasenia() {
        super(Configuracion.obtenerProperties("mensaje.validacion.contrasenia-debil"));

        this.contrasenias = new ArrayList<>();
        this.cargarPeoresContraseniasDesdeArchivo();
    }

    // Verifica mediante un archivo txt de 10k peores contrasenias
    @Override
    public boolean condition(String password) {
        return contrasenias.stream().anyMatch(contrasenia -> contrasenia.equals(password));
    }

    private void cargarPeoresContraseniasDesdeArchivo() {
        try {
            String nombreDeArchivo = Configuracion.obtenerProperties("archivo.path.top-peores-contrasenias");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nombreDeArchivo);
            BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            while ((linea = lector.readLine()) != null) {
                this.contrasenias.add(linea.trim());
            }
            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}