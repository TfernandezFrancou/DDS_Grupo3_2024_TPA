package org.example.puntaje;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RegistroContribucion extends CalculadorDePuntos{
    private int mesesHeladeraActiva;
    private int cantidadHeladerasActivas;
    private int tarjetasRepartidas;
    private int viandasDonadas;
    private int viandasDistribuidas;
    private int dineroDonado;
}
