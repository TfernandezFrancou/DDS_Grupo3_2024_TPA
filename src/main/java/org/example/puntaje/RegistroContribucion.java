package org.example.puntaje;

import lombok.Getter;
import lombok.Setter;


@Setter
public class RegistroContribucion extends CalculadorDePuntos{
    private int mesesHeladeraActiva;
    private int cantidadHeladerasActivas;
    private int tarjetasRepartidas;
    private int viandasDonadas;
    private int viandasDistribuidas;
    private int dineroDonado;

    public RegistroContribucion(){
        Coeficiente coeficiente = new Coeficiente();
        coeficiente.setCoeficienteDineroDonado(0.5F);
        coeficiente.setCoeficienteViandasDistribuidas(1);
        coeficiente.setCoeficienteViandasDonadas(1.5F);
        coeficiente.setCoeficienteTarjetasRepartidas(2);
        coeficiente.setCoeficienteHeladeras(5);

        this.setCoeficientes(coeficiente);//setear coeficientes a la clase padre
    }
    @Override
    public float getMesesHeladeraActiva(){
        return mesesHeladeraActiva;
    }
    @Override
    public float getCantidadDeHeladerasActivas() {
        return cantidadHeladerasActivas;
    }

    @Override
    public  float getTarjetasRepartidas(){
        return tarjetasRepartidas;
    }

    @Override
    public  float getViandasDonadas(){
        return viandasDonadas;
    }

    @Override
    public float getViandasDistribuidas(){
        return viandasDistribuidas;
    }

    @Override
    public float getDineroDonado(){
        return dineroDonado;
    }

}
