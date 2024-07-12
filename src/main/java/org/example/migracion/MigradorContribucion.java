package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.personas.roles.Colaborador;
import org.example.personas.PersonaHumana;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class MigradorContribucion {
    private Map<PersonaHumana, Contribucion> colaboradores;

    public MigradorContribucion(){
        this.colaboradores = new HashMap<>();
    }

    public void cargarCSV(FileInputStream archivoCSV) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoCSV.getFD()));
        String linea = "";
        for (int i = 0;(linea = bufferedReader.readLine()) != null; i++) {
            if (i == 0) continue; // ignoro el nombre de las columnas
            String[] columnas = linea.split(";");
            PersonaHumana persona = PersonaHumana.fromCsv(Arrays.copyOfRange(columnas, 0, 5));
            Contribucion contribucion = Contribucion.fromCsv(Arrays.copyOfRange(columnas, 5, 8));
            colaboradores.put(persona, contribucion);
        }
    }

    public void migrarColaboradores() throws MessagingException {
        for (PersonaHumana persona: colaboradores.keySet()) {
            this.migrarContribucion(persona, colaboradores.get(persona));
        }
    }

    private void migrarContribucion(PersonaHumana colaborador, Contribucion contribucion) throws MessagingException {
        Usuario usuario = colaborador.buscarOCrearUsuario();
        ((Colaborador) colaborador.getRol()).agregarContribucion(contribucion);
        // TODO: guardar Colaborador y contribuciones en DB
    }
}
