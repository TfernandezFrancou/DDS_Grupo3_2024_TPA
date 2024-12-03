package org.example.Presentacion;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.apache.commons.io.IOUtils;
import org.example.migracion.MigradorContribucion;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;


public class CargaCSVController {

    public static void postUploadCSV(@NotNull Context context) {
        UploadedFile csvFile = context.uploadedFile("csv");

        if (csvFile != null){
            InputStream inputStream = csvFile.content();
            File fileCSV = new File("src/main/resources/targetCSV.tmp");
            try{
                java.nio.file.Files.copy(
                        inputStream,
                        fileCSV.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                IOUtils.closeQuietly(inputStream);

                MigradorContribucion migradorContribucion = new MigradorContribucion();

                FileInputStream fileInputStream = new FileInputStream(fileCSV);

                migradorContribucion.cargarCSV(fileInputStream);

                migradorContribucion.migrarColaboradores();

                fileInputStream.close();

                fileCSV.delete();//elimino archivo temporal luego de usarlo

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
