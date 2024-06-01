package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.puntaje.RegistroContribucion;

import java.time.LocalDate;

@Getter
@Setter
public class CSVColaborador {

    private Documento documento;
    private String nombre;
    private String apellido;
    private String mail;
    private LocalDate fechaDeColaboracion;
    private TipoColaboracion formaDeColaboarcion;
    private Integer cantidad;



    public void cargarCSV(String[] datosCsv){
        this.documento = new Documento();
        documento.setTipoDocumento(TipoDocumento.valueOf(datosCsv[0]));
        documento.setNumeroDocumento(datosCsv[1]);
        this.nombre = datosCsv[2];
        this.apellido = datosCsv[3];
        this.mail = datosCsv[4];
        this.fechaDeColaboracion = LocalDate.parse(datosCsv[5]);

        this.formaDeColaboarcion = TipoColaboracion.valueOf(datosCsv[6]);
        this.cantidad = Integer.parseInt(datosCsv[7]);
    }
}
