package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.excepciones.EmailNoRegistradoException;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.personas.PersonaHumana;
import org.example.repositorios.RepoPersona;

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

    public void cargarCSVDesdeMemoria(InputStream csvFile) throws IOException, ParseException {
        this.cargarCSV(new BufferedReader(new InputStreamReader(csvFile)));
    }

    public void cargarCSVDesdeArchivo(FileInputStream archivoCSV) throws IOException, ParseException {
        this.cargarCSV(new BufferedReader(new FileReader(archivoCSV.getFD())));
    }

    private void cargarCSV(BufferedReader bufferedReader) throws IOException, ParseException {
        String linea = "";
        for (int i = 0;(linea = bufferedReader.readLine()) != null; i++) {
            if (i == 0) continue; // ignoro el nombre de las columnas
            String[] columnas = linea.split(",");
            colaboradores.add(PersonaHumana.fromCsv(Arrays.copyOfRange(columnas, 0, 5)));
            contribuciones.add(Contribucion.fromCsv(Arrays.copyOfRange(columnas, 5, 8)));
        }
    }

    public void migrarColaboradores() throws MessagingException, EmailNoRegistradoException {
        for (int i = 0; i < colaboradores.size(); i++) {
            this.migrarContribucion(colaboradores.get(i), contribuciones.get(i));
        }
    }

    private void migrarContribucion(PersonaHumana colaborador, Contribucion contribucion) throws MessagingException, EmailNoRegistradoException {
        Persona personaExistente = RepoPersona.getInstancia().buscarPorNombre(colaborador.getNombre());
        if(personaExistente==null){
            Usuario usuario = colaborador.buscarOCrearUsuario();//ya lo guarda en el repo
            ((Colaborador) colaborador.getRol()).agregarContribucion(contribucion);
            ((Colaborador) colaborador.getRol()).calcularPuntuaje();
            Persona colaboradorActualizado = RepoPersona.getInstancia().actualizarPersona(colaborador);
            usuario.setColaborador(colaboradorActualizado);
        } else {
            Usuario usuario = personaExistente.buscarOCrearUsuario();//ya lo guarda en el repo
            if (personaExistente.getRol() == null) {
                personaExistente.setRol(colaborador.getRol());//asigno colaborador ya creado
                personaExistente = RepoPersona.getInstancia().actualizarPersona(personaExistente);
            }
            Colaborador rol = ((Colaborador) personaExistente.getRol());
            Colaborador rolColaborador = RepoPersona.getInstancia().getRolColaboradorById(rol.getIdrol());
            rolColaborador.agregarContribucion(contribucion);//y guarda la contribucion
            rolColaborador.calcularPuntuaje();
            personaExistente.setRol(rolColaborador);
            personaExistente = RepoPersona.getInstancia().actualizarPersona(personaExistente);
            usuario.setColaborador(personaExistente);
        }
    }
}
