package org.example.personas.documentos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Documento {
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;

    public Documento(TipoDocumento tipo, String numero) {
        this.numeroDocumento = numero;
        this.tipoDocumento = tipo;
    }

    public static Documento fromCsv(String[] columnas) {
        return new Documento(TipoDocumento.valueOf(columnas[0]), columnas[1]);
    }
}
