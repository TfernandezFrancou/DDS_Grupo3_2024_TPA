package tests;

import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.contribuciones.DonacionDeDinero;
import org.example.migracion.CSVColaborador;
import org.example.migracion.MigradorContribucion;
import org.example.migracion.TipoColaboracion;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.Registrados;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    private final Usuario usuarioMock = new Usuario("si","1234", LocalDateTime.of(2025,6,10,12,0, 0));

    @Mock
    private MedioDeContacto correoElectronicoMock;

    private PersonaHumana colaboradorMock = new PersonaHumana();

    private final String pathCSVCorto = "src/test/resources/test_data.csv";
    private final String pathCSVGrande = "src/test/resources/test_long_data.csv";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        colaboradorMock.setMediosDeContacto(List.of(correoElectronicoMock));
        usuarioMock.setDocumento(documentoMock);
        usuarioMock.setColaborador(colaboradorMock);
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
    public void testMigrarColaboradores() {

        // Carga los datos desde el CSV
        migradorContribucion.getCsvColaboradores().add(createTestColaborador());

        // Llama al método migrarColaboradores
        migradorContribucion.migrarColaboradores();

        // Verifica que se hayan migrado los datos correctamente
        Assertions.assertEquals(colaboradorMock.getNombre(), "Juan Perez");
        Assertions.assertEquals(colaboradorMock.getApellido(), "Perez");
        Assertions.assertEquals(colaboradorMock.getMediosDeContacto().size(), 1);

        MedioDeContacto medioDeContacto = colaboradorMock.getMediosDeContacto().get(0);
        Assertions.assertInstanceOf(CorreoElectronico.class, medioDeContacto);
        Assertions.assertEquals( ((CorreoElectronico) medioDeContacto).getCorreoElectronico(),"juan.perez@example.com");
        List<Contribucion> contribuciones = colaboradorMock.getFormasContribucion();
        Assertions.assertEquals(contribuciones.size(),1);
        Contribucion contribucion = contribuciones.get(0);
        Assertions.assertInstanceOf(DonacionDeDinero.class,contribucion);

        Assertions.assertEquals(((DonacionDeDinero) contribucion).getMonto(), 100);
    }


    private CSVColaborador createTestColaborador() {
        CSVColaborador csvColaborador = new CSVColaborador();
        csvColaborador.setDocumento(documentoMock);
        csvColaborador.setNombre("Juan");
        csvColaborador.setApellido("Perez");
        csvColaborador.setMail("juan.perez@example.com");
        csvColaborador.setFechaDeColaboracion(LocalDate.of(2022,1,1));
        csvColaborador.setFormaDeColaboarcion(TipoColaboracion.DINERO);
        csvColaborador.setCantidad(100);
        return csvColaborador;
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

}