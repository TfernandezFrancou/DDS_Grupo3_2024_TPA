package org.example.personas.documentos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Documento {
    private String numeroDocumento;
    private TipoDocumento tipoDocumento;
}
