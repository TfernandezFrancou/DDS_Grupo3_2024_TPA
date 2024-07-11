package org.example.migracion;

import lombok.Getter;
import lombok.Setter;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.Contribucion;
import org.example.colaboraciones.TipoDePersona;
import org.example.colaboraciones.contribuciones.DistribucionDeViandas;
import org.example.colaboraciones.contribuciones.DonacionDeViandas;
import org.example.colaboraciones.contribuciones.RegistrarPersonasEnSituacionVulnerable;
import org.example.config.Configuracion;
import org.example.excepciones.UserException;
import org.example.migracion.estrategiasColaboracion.*;
import org.example.personas.PersonaHumana;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.documentos.Documento;
import org.example.repositorios.Registrados;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MigradorContribucion {
    private List<CSVColaborador> csvColaboradores;
    private EstrategiaColaboracion estrategiaColaboracion;

    private final String CUERPO_MENSAJE_BIENVENIDA = Configuracion.obtenerProperties("mail.mensajeDeBienvenida.cuerpo");
    private final String ASUNTO_MENSAJE_BIENVENIDA = Configuracion.obtenerProperties("mail.mensajeDeBienvenida.asunto");

    public MigradorContribucion(){
        this.csvColaboradores = new ArrayList<>();
    }

    public void cargarCSV(FileInputStream archivoCSV) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(archivoCSV.getFD()));
        String linea = "";
        for (int i = 0;(linea = bufferedReader.readLine()) != null; i++) {
            if(i == 0) continue; // ignoro el nombre de las columnas
            CSVColaborador csvColaborador = new CSVColaborador();
            csvColaborador.cargarCSV(linea.split(","));

            this.csvColaboradores.add(csvColaborador);
        }
    }

    public void migrarColaboradores() throws MessagingException {
        for (CSVColaborador colaborador: this.csvColaboradores) {
            this.migrarContribucion(colaborador);
        }
    }

    private void migrarContribucion(CSVColaborador csvColaborador) throws MessagingException {


        CorreoElectronico email = new CorreoElectronico();
        email.setMail(csvColaborador.getMail());

        String colaboradorNombre = csvColaborador.getNombre();
        Usuario usuarioColaborador = this.buscarOCrearUsuario(csvColaborador.getDocumento(), email, colaboradorNombre);

        PersonaHumana colab = (PersonaHumana) usuarioColaborador.getColaborador();

        colab.setNombre(colaboradorNombre);
        colab.setApellido(csvColaborador.getApellido());
        colab.setMediosDeContacto(List.of(email));

        TipoColaboracion tipoColaboracion = csvColaborador.getFormaDeColaboarcion();
        Integer cantidad = csvColaborador.getCantidad();

        switch (tipoColaboracion){
            case DINERO -> estrategiaColaboracion = new EstrategiaDinero();
            case DONACION_VIANDAS ->  estrategiaColaboracion = new EstrategiaDonacionViandas();
            case REDISTRIBUCION_VIANDAS -> estrategiaColaboracion = new EstrategiaDistribucionDeViandas();
            case ENTREGA_TARJETAS -> estrategiaColaboracion = new EstrategiaEntregarTarjetas();
        }

        Contribucion contribucion = estrategiaColaboracion.cargarColaboracion(cantidad);

        colab.agregarContribucion(contribucion);
        //todo guardar Colaborador y contribuciones en la DB
    }

    private Usuario buscarOCrearUsuario(Documento documento, MedioDeContacto email, String nombreUsuario) throws MessagingException {
        Usuario usuarioColaborador = null;
        try {
            usuarioColaborador = Registrados.getInstancia()
                    .obtenerUsuarioPorDocumento(documento);
        } catch (UserException userException) {
            usuarioColaborador = new Usuario();
            usuarioColaborador.setNombreDeUsuario(nombreUsuario);
            usuarioColaborador.setDocumento(documento);
            usuarioColaborador.setContrasenia(this.generarCredenciales());

            PersonaHumana colaborador = new PersonaHumana();
            colaborador.setMediosDeContacto(List.of(email));
            usuarioColaborador.setColaborador(colaborador);

            Registrados.getInstancia().agregarUsuarios(usuarioColaborador);
            this.enviarCredenciales(usuarioColaborador);
        }
        return usuarioColaborador;
    }
    private void enviarCredenciales(Usuario usuario) throws MessagingException {
        //enviarCredenciales via Mail
        MedioDeContacto medioDeContacto= usuario.getColaborador().getMediosDeContacto().get(0);
        String mensajeDeBienvenida = CUERPO_MENSAJE_BIENVENIDA
                    .replace("{username}", usuario.getNombreDeUsuario())
                    .replace("{password}", usuario.getContrasenia());

        medioDeContacto.notificar(ASUNTO_MENSAJE_BIENVENIDA, mensajeDeBienvenida);
    }
    private String generarCredenciales(){
        //TODO generar credenciales
        return UUID.randomUUID().toString();
    }
}
