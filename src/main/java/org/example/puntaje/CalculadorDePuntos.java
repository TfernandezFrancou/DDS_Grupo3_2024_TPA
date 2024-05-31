package org.example.puntaje;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CalculadorDePuntos {
    private Coeficiente coeficientes;


    public float calcularPuntos(){
        return getDineroDonado() * coeficientes.getCoeficienteDineroDonado() +
                getViandasDistribuidas() * coeficientes.getCoeficienteViandasDistribuidas() +
                getViandasDonadas() * coeficientes.getCoeficienteViandasDonadas() +
                getTarjetasRepartidas() * coeficientes.getCoeficienteTarjetasRepartidas() +
                getCantidadDeHeladerasActivas() * getMesesHeladeraActiva() * coeficientes.getCoeficienteHeladeras();
    }

    public abstract float getMesesHeladeraActiva();

    public abstract float getCantidadDeHeladerasActivas();

    public abstract float getTarjetasRepartidas();

    public abstract float getViandasDonadas();

    public abstract float getViandasDistribuidas();

    public abstract float getDineroDonado();
}
