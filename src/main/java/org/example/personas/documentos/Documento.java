package org.example.personas.documentos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;

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

    public static Documento fromCsv(String[] columnas) throws ParseException {
        if (columnas.length != 2) {
            throw new ParseException("Documento debe recibir dos columnas", 0);
        }
        TipoDocumento tipo;
        try {
            tipo = TipoDocumento.valueOf(columnas[0]);
        } catch (Exception e) {
            throw new ParseException("Tipo de documento invalido", 0);
        }
        return new Documento(tipo, columnas[1]);
    }
}
