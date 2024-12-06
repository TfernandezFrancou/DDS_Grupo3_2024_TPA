package tests;

import org.example.excepciones.PasswordException;
import org.example.validaciones.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


 class ValidacionContraseniaTest {

    private final String contraseniaCorta = "1234";
    private final String contraseniaLarga = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    private final String contraseniaConRepes = "1111234678";
    private final String contraseniaSinRepes = "12345678";
    private final String contraseniaConEspecial = "a1A1343#";
    private final String contraseniaSinEspecial = "aA1";
    private final String contraseniaSinDigito = "aA#";
    private final String contraseniaConEspacio = "123 456";
    private final String contraseniaComun = "12345678";
    private final String contraseniaValida = "Abcd1232_";

    private final VerificadorContrasenia validador = VerificadorContrasenia.getInstancia();
    private final LongitudMinimaContrasenia longitudMinima = new LongitudMinimaContrasenia();
    private final LongitudMaximaContrasenia longitudMaxima = new LongitudMaximaContrasenia();
    private final CaracterRepetido repeticiones = new CaracterRepetido();
    private final ValidacionTopPeoresContrasenia topPeoresContrasenia = new ValidacionTopPeoresContrasenia();
    private final FormatoContrasenia formatoContrasenia = new FormatoContrasenia();

    @Test //Verificando que las contraseñas cortas lancen una excepcion
    void testClaveMuyCorta()
    {
        Assertions.assertTrue(longitudMinima.condition(contraseniaCorta));
        Assertions.assertFalse(longitudMaxima.condition(contraseniaCorta));
    }

    @Test
    void testClaveMuyLarga()
    {
        Assertions.assertFalse(longitudMinima.condition(contraseniaLarga));
        Assertions.assertTrue(longitudMaxima.condition(contraseniaLarga));
    }

    @Test
    void testContraseniaEsValida(){

        Assertions.assertDoesNotThrow(() -> {
            validador.validarContrasenia(contraseniaValida);
        });
    }

    @Test
    void testContraseniaNoEsValida(){
        Assertions.assertThrows(PasswordException.class,() -> {
            validador.validarContrasenia(contraseniaCorta);
        });
    }

    @Test
     void testClaveConRepeticiones()
    {
        Assertions.assertTrue(repeticiones.condition(contraseniaConRepes));
    } 

    @Test
     void testClaveSinRepeticionesException()
    {
        Assertions.assertFalse(repeticiones.condition(contraseniaSinRepes));
    }

    @Test
     void testClaveDebeTenerAlMenosUnCaracEspecial()
    {
        Assertions.assertFalse(formatoContrasenia.condition(contraseniaConEspecial));
    }

    @Test
     void testNoTieneCaracEspecial()
    {
        Assertions.assertTrue(formatoContrasenia.condition(contraseniaSinEspecial));
    }


    @Test
     void testDebeTenerAlMenosUnDigito()
    {
        Assertions.assertFalse(formatoContrasenia.condition(contraseniaValida));
    }

    @Test
     void testNoTieneDigito()
    {
        Assertions.assertTrue(formatoContrasenia.condition(contraseniaSinDigito));
    }

    @Test
     void testContieneUnEspacio()
    {
        Assertions.assertTrue(formatoContrasenia.condition(contraseniaConEspacio));
    }


    @Test
     void estaEnTopPeores()
    {
        Assertions.assertTrue(topPeoresContrasenia.condition(contraseniaComun));
    }
}