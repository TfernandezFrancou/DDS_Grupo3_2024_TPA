package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.documentos.Documento;
import org.example.puntaje.RegistroContribucion;

import java.io.FileInputStream;

@Getter
@Setter
public class MigradorContribucion {

    private RegistroContribucion registroContribucion;
    private Documento identidadColaborador;

    public void cargarCSV(FileInputStream archivoCSV){
        //TODO cargarCSV
    }
    public void migrarContribucion(){
        //TODO migrarContribucion
    }
    private void enviarCredenciales(String credencial){
        //TODO enviarCredenciales
    }
}
