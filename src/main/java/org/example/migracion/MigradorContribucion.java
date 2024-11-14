package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.personas.roles.Colaborador;
import org.example.personas.PersonaHumana;

import javax.mail.MessagingException;
import java.io.*;
import java.text.ParseException;
import java.util.*;

@Getter
@Setter
public class MigradorContribucion {
    private List<PersonaHumana> colaboradores;
    private List<Contribucion> contribuciones;

    public MigradorContribucion(){
        this.colaboradores = new ArrayList<>();
        this.contribuciones = new ArrayList<>();
    }

    public void cargarCSV(FileInputStream archivoCSV) throws IOException, ParseException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoCSV.getFD()));
        String linea = "";
        for (int i = 0;(linea = bufferedReader.readLine()) != null; i++) {
            if (i == 0) continue; // ignoro el nombre de las columnas
            String[] columnas = linea.split(",");
            colaboradores.add(PersonaHumana.fromCsv(Arrays.copyOfRange(columnas, 0, 5)));
            contribuciones.add(Contribucion.fromCsv(Arrays.copyOfRange(columnas, 5, 8)));
        }
    }

    public void migrarColaboradores() throws MessagingException {
        for (int i = 0; i < colaboradores.size(); i++) {
            this.migrarContribucion(colaboradores.get(i), contribuciones.get(i));
        }
    }

    private void migrarContribucion(PersonaHumana colaborador, Contribucion contribucion) throws MessagingException {
        Usuario usuario = colaborador.buscarOCrearUsuario();//ya lo guarda en el repo
        ((Colaborador) colaborador.getRol()).agregarContribucion(contribucion);
    }
}
