package org.example.Presentacion;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.example.migracion.MigradorContribucion;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CargaCSVController {

    public static void postUploadCSV(@NotNull Context context) {
        UploadedFile csvFile = context.uploadedFile("csv");

        if (csvFile != null) {
            try {
                InputStream inputStream = csvFile.content();
                MigradorContribucion migradorContribucion = new MigradorContribucion();
                migradorContribucion.cargarCSVDesdeMemoria(inputStream);
                migradorContribucion.migrarColaboradores();
                inputStream.close();
                context.redirect("/views/colaboraciones/carga-csv-correcta.html");
            } catch (Exception ex){
                Map<String, Object> model = new HashMap<>();
                model.put("error", ex.getMessage());
                context.render("/views/colaboraciones/carga-csv-incorrecta.mustache", model);
                ex.printStackTrace();
            }
        }else {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "No subiste un archivo");
            context.render("/views/colaboraciones/carga-csv-incorrecta.mustache", model);
        }
    }

    public static void getUploadCSV(@NotNull Context context){
        context.render("/views/colaboraciones/carga-csv.mustache");
    }

}
