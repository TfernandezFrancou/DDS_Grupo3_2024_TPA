package tests;

import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.migracion.CSVColaborador;
import org.example.migracion.MigradorContribucion;
import org.example.migracion.TipoColaboracion;
import org.example.personas.roles.Colaborador;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.Registrados;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MigradorContribucionTest {

    @InjectMocks
    private MigradorContribucion migradorContribucion;

    private final Registrados registrados = Registrados.getInstancia();


    private final Documento documentoMock = new Documento("12345678",TipoDocumento.DNI);

    private final Documento documentoMockUsuarioNoExistente = new Documento("87654321",TipoDocumento.DNI);

    private final Usuario usuarioMock = new Usuario("si","1234", LocalDateTime.of(2025,6,10,12,0, 0));

    @Mock
    private MedioDeContacto correoElectronicoMock;

    private PersonaHumana personaColaboradorMock = new PersonaHumana();
    private Colaborador colaboradorMock = new Colaborador();

    private final String pathCSVCorto = "src/test/resources/test_data.csv";
    private final String pathCSVGrande = "src/test/resources/test_long_data.csv";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        personaColaboradorMock.setRol(colaboradorMock);
        personaColaboradorMock.setMediosDeContacto(List.of(correoElectronicoMock));
        usuarioMock.setDocumento(documentoMock);
        usuarioMock.setColaborador(personaColaboradorMock);
        registrados.agregarUsuarios(usuarioMock);
    }

    @Test
    public void testCargarCSV() throws IOException {
        // Prepara el archivo CSV de prueba
        FileInputStream fileInputStream = new FileInputStream(pathCSVCorto);

        // Llama al método cargarCSV
        migradorContribucion.cargarCSV(fileInputStream);

        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(4, migradorContribucion.getCsvColaboradores().size());
        List<CSVColaborador> colaboradores = migradorContribucion.getCsvColaboradores();
        verificarCsvColaborador(
                "Juan",
                "Perez",
                "12345678",
                "DNI",
                "juan.perez@example.com",
                "01/01/2022",
                "DINERO",
                100,
                colaboradores.get(0)
        );
        verificarCsvColaborador(
                "Ana",
                "Gomez",
                "87654321",
                "LC",
                "ana.gomez@example.com",
                "15/02/2023",
                "DONACION_VIANDAS",
                50,
                colaboradores.get(1)
        );
        verificarCsvColaborador(
                "Maria",
                "Lopez",
                "11223344",
                "LE",
                "maria.lopez@example.com",
                "23/03/2021",
                "REDISTRIBUCION_VIANDAS",
                75,
                colaboradores.get(2)
        );
        verificarCsvColaborador(
                "Carlos",
                "Martinez",
                "55667788",
                "DNI",
                "carlos.martinez@example.com",
                "12/04/2020",
                "ENTREGA_TARJETAS",
                30,
                colaboradores.get(3)
        );
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(4, TimeUnit.HOURS);//le doy el tiempo suficiente para que ejecuten todos los hilos

        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(numCSVs * 4, migradorContribucion.getCsvColaboradores().size()); // Asumiendo 4 registros por archivo CSV
    }

    @Test
    public void testCargarCSVGrande() throws IOException {
        // Prepara el archivo CSV de prueba
        FileInputStream fileInputStream = new FileInputStream(pathCSVGrande);

        // Llama al método cargarCSV
        migradorContribucion.cargarCSV(fileInputStream);

        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(1000, migradorContribucion.getCsvColaboradores().size());
    }

    @Test
    public void testCargarCSVGrandeParalelo() throws InterruptedException {
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);//le doy el tiempo suficiente para que ejecuten todos los hilos

        // Verifica que los datos se cargaron correctamente
        Assertions.assertEquals(numCSVs * 1000, migradorContribucion.getCsvColaboradores().size()); // Asumiendo 1000 registros por archivo CSV
    }

    @Test
    public void testMigrarColaboradores() throws MessagingException {

        // Carga los datos desde el CSV
        migradorContribucion.getCsvColaboradores().add(createCSVColaborador(
                documentoMock,//documento de usuario existente en el sistema
                "Juan", "Perez",
                "juan.perez@example.com",
                LocalDate.of(2022,1,1),
                TipoColaboracion.DINERO,
                100
        ));

        // Llama al método migrarColaboradores
        migradorContribucion.migrarColaboradores();

        // Verifica que se hayan migrado los datos correctamente
        Assertions.assertEquals(personaColaboradorMock.getNombre(), "Juan Perez");
        Assertions.assertEquals(personaColaboradorMock.getApellido(), "Perez");
        Assertions.assertEquals(personaColaboradorMock.getMediosDeContacto().size(), 1);

        MedioDeContacto medioDeContacto = personaColaboradorMock.getMediosDeContacto().get(0);
        Assertions.assertInstanceOf(CorreoElectronico.class, medioDeContacto);
        Assertions.assertEquals(((CorreoElectronico) medioDeContacto).getMail(),"juan.perez@example.com");
        List<Contribucion> contribuciones = colaboradorMock.getFormasContribucion();
        Assertions.assertEquals(contribuciones.size(),1);
        Contribucion contribucion = contribuciones.get(0);
        Assertions.assertInstanceOf(DonacionDeDinero.class,contribucion);

        Assertions.assertEquals(((DonacionDeDinero) contribucion).getMonto(), 100);
    }



    private void verificarCsvColaborador(String nombre,
                                         String apellido,
                                         String numeroDeDocumento,
                                         String tipoDeDocumento,
                                         String mail,
                                         String fechaColaboracion,
                                         String formaColaboracion,
                                         Integer cantidad,
                                         CSVColaborador colaboradorObtenido
    ){
        Assertions.assertEquals(nombre, colaboradorObtenido.getNombre());
        Assertions.assertEquals(apellido, colaboradorObtenido.getApellido());
        Assertions.assertEquals(numeroDeDocumento, colaboradorObtenido.getDocumento().getNumeroDocumento());
        Assertions.assertEquals(TipoDocumento.valueOf(tipoDeDocumento), colaboradorObtenido.getDocumento().getTipoDocumento());
        Assertions.assertEquals(mail, colaboradorObtenido.getMail());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertEquals(LocalDate.parse(fechaColaboracion,formatter), colaboradorObtenido.getFechaDeColaboracion());

        Assertions.assertEquals(TipoColaboracion.valueOf(formaColaboracion), colaboradorObtenido.getFormaDeColaboarcion());
        Assertions.assertEquals(cantidad, colaboradorObtenido.getCantidad());
    }

    private CSVColaborador createCSVColaborador(
                Documento documento,
                String nombre,
                String apellido,
                String mail,
                LocalDate fechaDeColaboracion,
                TipoColaboracion formaDeColaboarcion,
                Integer cantidad
    ){
        CSVColaborador csvColaborador = new CSVColaborador();
        csvColaborador.setDocumento(documento);
        csvColaborador.setNombre(nombre);
        csvColaborador.setApellido(apellido);
        csvColaborador.setMail(mail);
        csvColaborador.setFechaDeColaboracion(fechaDeColaboracion);
        csvColaborador.setFormaDeColaboarcion(formaDeColaboarcion);
        csvColaborador.setCantidad(cantidad);
        return csvColaborador;
    }

    @Test
    public void testMigrarCSVUsuarioNoExistente() throws Exception {

        // Carga los datos desde el CSV
        migradorContribucion.getCsvColaboradores().add(
                createCSVColaborador(
                    documentoMockUsuarioNoExistente,
                    "Francisco", "Mendez",
                    "fran.mendez@example.com",
                    LocalDate.of(2022,1,1),
                    TipoColaboracion.DINERO,
                    100
                )
        );

        //para que cuando se haga new CorreoElectonico(); en realidad devuelva un mock
        try(MockedConstruction<CorreoElectronico> mocked = Mockito.mockConstruction(CorreoElectronico.class)) {

            // Llama al método migrarColaboradores
            migradorContribucion.migrarColaboradores();

            // Se busca al colaborador
            Usuario usuario = registrados.obtenerUsuarioPorDocumento(documentoMockUsuarioNoExistente);

            PersonaHumana personaColaboradorATestear = (PersonaHumana) usuario.getColaborador();
            Colaborador rolColaboradorATestear = (Colaborador) personaColaboradorATestear.getRol();

            // Verifica que se hayan migrado los datos correctamente
            Assertions.assertEquals(personaColaboradorATestear.getNombre(), "Francisco Mendez");
            Assertions.assertEquals(personaColaboradorATestear.getApellido(), "Mendez");
            Assertions.assertEquals(personaColaboradorATestear.getMediosDeContacto().size(), 1);

            MedioDeContacto medioDeContacto = personaColaboradorATestear.getMediosDeContacto().get(0);
            Assertions.assertInstanceOf(CorreoElectronico.class, medioDeContacto);
            List<Contribucion> contribuciones = rolColaboradorATestear.getFormasContribucion();
            Assertions.assertEquals(contribuciones.size(), 1);
            Contribucion contribucion = contribuciones.get(0);
            Assertions.assertInstanceOf(DonacionDeDinero.class, contribucion);

            Assertions.assertEquals(((DonacionDeDinero) contribucion).getMonto(), 100);
        }
    }

}