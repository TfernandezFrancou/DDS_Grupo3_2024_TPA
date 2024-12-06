package org.example.presentacion;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.autenticacion.SessionManager;
import org.example.migracion.MigradorContribucion;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CargaCSVController {
    private CargaCSVController(){}

    public static void postUploadCSV(@NotNull Context context) {
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        UploadedFile csvFile = context.uploadedFile("csv");

        if (csvFile != null) {
            try {
                InputStream inputStream = csvFile.content();
                MigradorContribucion migradorContribucion = new MigradorContribucion();
                migradorContribucion.cargarCSVDesdeMemoria(inputStream);
                migradorContribucion.migrarColaboradores();
                inputStream.close();
                context.render("/views/colaboraciones/carga-csv-correcta.mustache",model);
            } catch (Exception ex){
                model.put("error", ex.getMessage());
                context.render("/views/colaboraciones/carga-csv-incorrecta.mustache", model);
                ex.printStackTrace();
            }
        }else {
            model.put("error", "No subiste un archivo");
            context.render("/views/colaboraciones/carga-csv-incorrecta.mustache", model);
        }
    }

    public static void getUploadCSV(@NotNull Context context){
        Map<String, Object> model = new HashMap<>(SessionManager.getInstancia().atributosDeSesion(context));
        context.render("/views/colaboraciones/carga-csv.mustache", model);
    }

}
