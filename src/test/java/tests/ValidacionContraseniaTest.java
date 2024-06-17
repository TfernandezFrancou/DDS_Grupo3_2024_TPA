package tests;

import org.example.excepciones.PasswordException;
import org.example.validaciones.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public class ValidacionContraseniaTest {

    private final String contraseniaCorta = "1234";
    private final String contraseniaLarga = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    private final String contraseniaConRepes = "1111234678";
    private final String contraseniaSinRepes = "12345678";
    private final String contraseniaConEspecial = "aA1#";
    private final String contraseniaSinEspecial = "aA1";
    private final String contraseniaSinDigito = "aA#";
    private final String contraseniaComun = "12345678";
    private final String contraseniaValida = "Abcd12345_";

    private final VerificadorContrasenia validador = VerificadorContrasenia.getInstancia();
    private final LongitudMinimaContrasenia longitudMinima = new LongitudMinimaContrasenia();
    private final LongitudMaximaContrasenia longitudMaxima = new LongitudMaximaContrasenia();
    private final CaracterRepetido repeticiones = new CaracterRepetido();
    private final ValidacionTopPeoresContrasenia topPeoresContrasenia = new ValidacionTopPeoresContrasenia();
    private final FormatoContrasenia formatoContrasenia = new FormatoContrasenia();

    @Test //Verificando que las contraseñas cortas lancen una excepcion
    public void testClaveMuyCorta()
    {
        Assertions.assertTrue(longitudMinima.condition(contraseniaCorta));
        Assertions.assertFalse(longitudMaxima.condition(contraseniaCorta));
    }

    @Test
    public void testClaveMuyLarga()
    {
        Assertions.assertFalse(longitudMinima.condition(contraseniaLarga));
        Assertions.assertTrue(longitudMaxima.condition(contraseniaLarga));
    }

    @Test
    public void testContraseniaEsValida(){

        Assertions.assertDoesNotThrow(() -> {
            validador.validarContrasenia(contraseniaValida);
        });
    }

    @Test
    public void testContraseniaNoEsValida(){
        Assertions.assertThrows(PasswordException.class,() -> {
            validador.validarContrasenia(contraseniaCorta);
        });
    }

    @Test
    public void testClaveConRepeticiones()
    {
        Assertions.assertTrue(repeticiones.condition(contraseniaConRepes));
    }

    @Test
    public void testClaveConRepeticionesException()
    {
        Assertions.assertThrows(PasswordException.class, () -> {
            repeticiones.validate(contraseniaConRepes);
        }, "Se espera que se lance una excepcion");
    }

    @Test
    public void testClaveSinRepeticionesException()
    {
        Assertions.assertTrue(repeticiones.condition(contraseniaSinRepes));
    }

    @Test
    public void testClaveDebeTenerAlMenosUnCaracEspecial()
    {
        Assertions.assertFalse(formatoContrasenia.condition(contraseniaConEspecial));
    }

    @Test
    public void testNoTieneCaracEspecial()
    {
        Assertions.assertTrue(formatoContrasenia.condition(contraseniaSinEspecial));
    }


    @Test
    public void testDebeTenerAlMenosUnDigito()
    {
        Assertions.assertFalse(formatoContrasenia.condition(contraseniaValida));
    }

    @Test
    public void testNoTieneDigito()
    {
        Assertions.assertTrue(formatoContrasenia.condition(contraseniaSinDigito));
    }


    @Test
    public void estaEnTopPeores()
    {
        Assertions.assertTrue(topPeoresContrasenia.condition(contraseniaComun));
    }
}