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
}
