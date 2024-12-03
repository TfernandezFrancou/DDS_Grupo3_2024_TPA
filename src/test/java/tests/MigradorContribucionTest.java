package tests;

import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.RegistrarPersonasEnSituacionVulnerable;
import org.example.excepciones.UserException;
import org.example.migracion.MigradorContribucion;
import org.example.personas.Persona;
import org.example.personas.roles.Colaborador;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.RepoPersona;
import org.example.repositorios.RepoUsuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

public class MigradorContribucionTest {
    @InjectMocks
    private MigradorContribucion migradorContribucion;
    private final String pathCSVCorto = "src/test/resources/test_data.csv";
    private final String pathCSVGrande = "src/test/resources/test_long_data.csv";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        RepoPersona.getInstancia().clean();
        RepoUsuario.getInstancia().clean();

        System.setProperty("env", "test");// para evitar que se notifique en ambiente de pruebas
    }

    private void verificarContribucion(Contribucion contribucion, String fechaColaboracion, Class clase) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertEquals(LocalDate.parse(fechaColaboracion,formatter), contribucion.getFecha());
        Assertions.assertInstanceOf(clase, contribucion);
    }

    private void verificarColaborador(PersonaHumana colaborador, String nombre, String apellido, String numeroDeDocumento, String tipoDeDocumento, String mail) {
        Assertions.assertEquals(nombre + " " + apellido, colaborador.getNombre());
        Assertions.assertEquals(numeroDeDocumento, colaborador.getDocumento().getNumeroDocumento());
        Assertions.assertEquals(TipoDocumento.valueOf(tipoDeDocumento), colaborador.getDocumento().getTipoDocumento());
        Assertions.assertEquals(mail, colaborador.getEmail().getMail());
    }

    @Test
    public void testCargarCSV() throws IOException, ParseException {
        // Prepara el archivo CSV de prueba
        FileInputStream fileInputStream = new FileInputStream(pathCSVCorto);
        // Llama al método cargarCSV
        migradorContribucion.cargarCSV(fileInputStream);
        // Verifica que los datos se cargaron correctamente
        List<PersonaHumana> colaboradores = migradorContribucion.getColaboradores();
        List<Contribucion> contribuciones = migradorContribucion.getContribuciones();
        Assertions.assertEquals(4, colaboradores.size());
        Assertions.assertEquals(4, contribuciones.size());
        // Juan Perez
        verificarColaborador(colaboradores.get(0), "Juan", "Perez", "12345678", "DNI", "juan.perez@example.com");
        verificarContribucion(contribuciones.get(0), "01/01/2022", DonacionDeDinero.class);
        Assertions.assertEquals(((DonacionDeDinero) contribuciones.get(0)).getMonto(), 100);
        // Ana Gomez
        verificarColaborador(colaboradores.get(1), "Ana", "Gomez", "87654321", "LC", "ana.gomez@example.com");
        verificarContribucion(contribuciones.get(1), "15/02/2023", DonacionDeViandas.class);
        Assertions.assertEquals(((DonacionDeViandas) contribuciones.get(1)).getCantidadDeViandas(), 50);
        // Maria Lopez
        verificarColaborador(colaboradores.get(2), "Maria", "Lopez", "11223344", "LE", "maria.lopez@example.com");
        verificarContribucion(contribuciones.get(2), "23/03/2021", DistribucionDeViandas.class);
        Assertions.assertEquals(((DistribucionDeViandas) contribuciones.get(2)).getCantidad(), 75);
        // Carlos Martinez
        verificarColaborador(colaboradores.get(3), "Carlos", "Martinez", "55667788", "DNI", "carlos.martinez@example.com");
        verificarContribucion(contribuciones.get(3), "12/04/2020", RegistrarPersonasEnSituacionVulnerable.class);
        Assertions.assertEquals(((RegistrarPersonasEnSituacionVulnerable) contribuciones.get(3)).getTarjetasEntregadas(), 30);
    }

    @Test
    public void testCargarCSVGrande() throws IOException, ParseException {
        // Prepara el archivo CSV de prueba
        FileInputStream fileInputStream = new FileInputStream(pathCSVGrande);
        // Llama al método cargarCSV
        migradorContribucion.cargarCSV(fileInputStream);
        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(1000, migradorContribucion.getContribuciones().size());
        Assertions.assertEquals(1000, migradorContribucion.getColaboradores().size());
    }

    @Test
    public void testCargarCSVGrandeParalelo() throws InterruptedException, ParseException {
        // Define el número de archivos CSV a cargar en paralelo
        int numCSVs = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numCSVs);

        for (int i = 0; i < numCSVs; i++) {
            executorService.submit(() -> {
                try {
                    // Prepara el archivo CSV de prueba
                    FileInputStream fileInputStream = new FileInputStream(pathCSVGrande);

                    // Llama al método cargarCSV
                    synchronized (migradorContribucion){ // evito condicion de carrera
                        migradorContribucion.cargarCSV(fileInputStream);
                    }

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);//le doy el tiempo suficiente para que ejecuten todos los hilos

        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(numCSVs * 1000, migradorContribucion.getContribuciones().size()); // Asumiendo 1000 registros por archivo CSV
        Assertions.assertEquals(numCSVs * 1000, migradorContribucion.getColaboradores().size()); // Asumiendo 1000 registros por archivo CSV
    }

    @Test
    public void testCargarCSVParalelo() throws InterruptedException {
        // Define el número de archivos CSV a cargar en paralelo
        int numCSVs = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(numCSVs);

        for (int i = 0; i < numCSVs; i++) {
            executorService.submit(() -> {
                try {
                    // Prepara el archivo CSV de prueba
                    FileInputStream fileInputStream = new FileInputStream(pathCSVCorto);

                    // Llama al método cargarCSV
                    synchronized (migradorContribucion){ // evito condicion de carrera
                        migradorContribucion.cargarCSV(fileInputStream);
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(4, TimeUnit.HOURS);//le doy el tiempo suficiente para que ejecuten todos los hilos

        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(numCSVs * 4, migradorContribucion.getColaboradores().size()); // Asumiendo 4 registros por archivo CSV
        Assertions.assertEquals(numCSVs * 4, migradorContribucion.getContribuciones().size()); // Asumiendo 4 registros por archivo CSV
    }

    @Test
    public void testMigrarUsuarioExistente() throws MessagingException {
        CorreoElectronico correo = Mockito.mock(CorreoElectronico.class);
        Mockito.doNothing().when(correo).notificar(any());
        Colaborador colaborador = new Colaborador();
        PersonaHumana persona1 = new PersonaHumana("Juan", "Perez", correo, new Documento(TipoDocumento.DNI, "33333333"), colaborador);
        RepoPersona.getInstancia().agregar(persona1);
        colaborador.agregarContribucion(new DonacionDeViandas());
        colaborador.calcularPuntuaje();
        RepoPersona.getInstancia().actualizarPersona(persona1);
        Usuario usuario1 = new Usuario("Juan Perez", persona1.getDocumento(), persona1);
        try (MockedStatic<RepoUsuario> mockedSingleton = Mockito.mockStatic(RepoUsuario.class)) {
            RepoUsuario repoUsuario = Mockito.mock(RepoUsuario.class);
            Mockito.when(RepoUsuario.getInstancia()).thenReturn(repoUsuario);
            Mockito.when(repoUsuario.obtenerUsuarioPorDocumento(any(Documento.class))).thenReturn(usuario1);

            migradorContribucion.getColaboradores().add(persona1);
            migradorContribucion.getContribuciones().add(new DonacionDeDinero(LocalDate.of(2022, 1, 1), 100));
            migradorContribucion.migrarColaboradores();

            // se hace una busqueda
            Mockito.verify(repoUsuario, Mockito.times(1)).obtenerUsuarioPorDocumento(any());
            // no se agrega ningun usuario
            Mockito.verify(repoUsuario, Mockito.times(0)).agregarUsuarios(any());

            Persona personaMigrada =RepoPersona.getInstancia().buscarPorId(persona1.getIdPersona());
            // se agrega la forma de contribucion
            Assertions.assertEquals(((Colaborador) personaMigrada.getRol()).getFormasContribucion().size(), 2);
            // no se notifica al usuario pues no se creo
            Mockito.verify(correo, Mockito.times(0)).notificar(any());
        }
    }

    @Test
    public void testMigrarUsuarioNoExistente() throws MessagingException {
        CorreoElectronico correo = new CorreoElectronico("example@gmail.com");
        PersonaHumana persona1 = new PersonaHumana("Juan", "Perez", correo, new Documento(TipoDocumento.DNI, "33333333"), new Colaborador());
        RepoPersona.getInstancia().agregar(persona1);

        try (MockedStatic<RepoUsuario> mockedSingleton = Mockito.mockStatic(RepoUsuario.class)) {
            RepoUsuario repoUsuario = Mockito.mock(RepoUsuario.class);
            Mockito.when(RepoUsuario.getInstancia()).thenReturn(repoUsuario);
            Mockito.when(repoUsuario.obtenerUsuarioPorDocumento(any(Documento.class))).thenThrow(UserException.class);
            Mockito.doNothing().when(repoUsuario).agregarUsuarios(any());

            migradorContribucion.getColaboradores().add(persona1);
            migradorContribucion.getContribuciones().add(new DonacionDeDinero(LocalDate.of(2022, 1, 1), 100));
            migradorContribucion.migrarColaboradores();

            // se hace una busqueda
            Mockito.verify(repoUsuario, Mockito.times(1)).obtenerUsuarioPorDocumento(any());
            // se agrega un usuario
            Mockito.verify(repoUsuario, Mockito.times(1)).agregarUsuarios(any());
            Persona personaMigrada = RepoPersona.getInstancia().buscarPorId(persona1.getIdPersona());
            // termina con una sola forma de colaboracion
            Assertions.assertEquals(((Colaborador) personaMigrada.getRol()).getFormasContribucion().size(), 1);
            verificarContribucion(((Colaborador) personaMigrada.getRol()).getFormasContribucion().get(0), "01/01/2022", DonacionDeDinero.class);
        }
    }

    @Test
    public void testParsearDocumentoValido() throws ParseException {
        String[] columnas = { "DNI", "4334234" };
        Assertions.assertDoesNotThrow(() -> { Documento.fromCsv(columnas); });
        Documento documento = Documento.fromCsv(columnas);
        Assertions.assertEquals(documento.getTipoDocumento(), TipoDocumento.DNI);
        Assertions.assertEquals(documento.getNumeroDocumento(), "4334234");
    }

    @Test
    public void testParsearDocumentoInvalido() {
        String[] columnas1 = { "AAAAA", "4334234" };
        Assertions.assertThrows(ParseException.class, () -> { Documento.fromCsv(columnas1); });
        String[] columnas2 = { "4334234" };
        Assertions.assertThrows(ParseException.class, () -> { Documento.fromCsv(columnas2); });
    }

    @Test
    public void testParsearContribucionValida() throws ParseException {
        String[] columnas = { "02/02/2022", "DONACION_VIANDAS", "30" };
        Assertions.assertDoesNotThrow(() -> { Contribucion.fromCsv(columnas); });
        Contribucion contribucion = Contribucion.fromCsv(columnas);
        Assertions.assertInstanceOf(DonacionDeViandas.class, contribucion);
        Assertions.assertEquals(contribucion.getFecha().getDayOfMonth(), 2);
        Assertions.assertEquals(contribucion.getFecha().getMonth().getValue(), 2);
        Assertions.assertEquals(contribucion.getFecha().getYear(), 2022);
        Assertions.assertEquals(((DonacionDeViandas) contribucion).getCantidadDeViandas(), 30);
    }

    @Test
    public void testParsearContribucionInvalida() {
        String[] columnas1 = { "02/2022", "DONACION_VIANDAS", "30" };
        Assertions.assertThrows(ParseException.class, () -> { Contribucion.fromCsv(columnas1); });
        String[] columnas2 = { "02/02/2022", "HOLA", "30" };
        Assertions.assertThrows(ParseException.class, () -> { Contribucion.fromCsv(columnas2); });
        String[] columnas3 = { "02/2022", "DONACION_VIANDAS", "bla" };
        Assertions.assertThrows(ParseException.class, () -> { Contribucion.fromCsv(columnas3); });
    }

    @Test
    public void testParsearPersonaValida() throws ParseException {
        String[] columnas = { "DNI", "4334234", "Juan", "Perez", "jperez@gmail.com" };
        Assertions.assertDoesNotThrow(() -> { PersonaHumana.fromCsv(columnas); });
        PersonaHumana persona = PersonaHumana.fromCsv(columnas);
        Assertions.assertEquals(persona.getDocumento().getTipoDocumento(), TipoDocumento.DNI);
        Assertions.assertEquals(persona.getDocumento().getNumeroDocumento(), "4334234");
        Assertions.assertEquals(persona.getNombre(), "Juan Perez");
        Assertions.assertEquals(persona.getEmail().getMail(), "jperez@gmail.com");
    }

    @Test
    public void testParsearPersonaInvalida() {
        String[] columnas1 = { "DNI", "4334234", "Perez", "jperez@gmail.com" };
        Assertions.assertThrows(ParseException.class, () -> { PersonaHumana.fromCsv(columnas1); });
        String[] columnas2 = { "ASDAS", "4334234", "Juan", "Perez", "jperez@gmail.com" };
        Assertions.assertThrows(ParseException.class, () -> { PersonaHumana.fromCsv(columnas2); });
    }
}