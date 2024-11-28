package org.example.Presentacion;

import io.javalin.http.Context;
import org.example.autenticacion.IniciarSesion;
import org.example.autenticacion.RegistrarUsuario;
import org.example.autenticacion.Usuario;
import org.example.colaboraciones.contribuciones.heladeras.Direccion;
import org.example.excepciones.UserException;
import org.example.personas.*;
import org.example.personas.contacto.CorreoElectronico;
import org.example.personas.contacto.MedioDeContacto;
import org.example.personas.contacto.Telefono;
import org.example.personas.contacto.Whatsapp;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.RepoUsuario;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.*;

public class UsuarioController  {

    public static void postInicioSeccion(@NotNull Context context){
        String nombreDeUsuario = context.formParam("username");
        String contraseniaUsuario = context.formParam("password");

        IniciarSesion iniciarSesion = new IniciarSesion();
        try{
            if(iniciarSesion.iniciarSesion(nombreDeUsuario, contraseniaUsuario)){
                context.redirect("/heladeras");
            }
        } catch(UserException e){
            Map<String, Object> model = new HashMap<>();
            model.put("error", e.getMessage());
            context.render("/views/usuarios/InicioSession.mustache",model);
        }
    }

    public static void postRegistrarse(@NotNull Context context) {
        String nombreDeUsuario = context.formParam("username");
        String contraseniaUsuario = context.formParam("password");

        RegistrarUsuario registrarUsuario = new RegistrarUsuario();

        try{
            Persona persona = crearPersona(context);
            Usuario usuario = registrarUsuario.registrarUsuario(nombreDeUsuario, contraseniaUsuario);//persiste el usuario
            usuario.setColaborador(persona);
            usuario.setDocumento(persona.getDocumento());

            RepoUsuario.getInstancia().agregarUsuarios(usuario); // guarda tanto al usuario como a la persona
            context.redirect("/usuarios/InicioSession");
        }catch (Exception exception){
            Map<String, Object> model = new HashMap<>();
            model.put("error", exception.getMessage());
            context.render("/views/usuarios/Registrarse.mustache", model);
        }
    }

    private static Persona crearPersona(Context context){
        boolean esJuridica = "on".equals(context.formParam("esJuridica"));
        String direccion = context.formParam("direccion");
        String localidad = context.formParam("localidad");

        Direccion direccion1 ;
        if(!direccion.equals("")){
            direccion1 = new Direccion();
            String[] partes = direccion.split(" ");
            direccion1.setNombreCalle(partes[0]);
            direccion1.setAltura(partes[1]);
            if(localidad.equals("")){
                direccion1.setLocalidad(localidad.toLowerCase());
            }
        } else {
            direccion1 = null;
        }

        String documento = context.formParam("documento");
        String tipoDocumento = context.formParam("tipoDocumento");

        Documento documento1 = new Documento();
        documento1.setNumeroDocumento(documento);
        documento1.setTipoDocumento(TipoDocumento.valueOf(tipoDocumento));

        Persona persona;

        if(!esJuridica){
            persona = crearPersonaHumana(context, direccion1, documento1);
        } else {
            persona = crearPersonaJuridica(context, direccion1, documento1);
        }

        // medios de contacto
        List<MedioDeContacto> mediosDeContacto = obtenerMediosDeContacto(context);

        for (MedioDeContacto mdc: mediosDeContacto) {
            persona.addMedioDeContacto(mdc);
        }

        cargarContrigucionesQuePuedeHacer(persona, context);

        return persona;
    }

    private static PersonaHumana crearPersonaHumana(Context context, Direccion direccion, Documento documento){
        String nombre = context.formParam("nombre");
        String apellido = context.formParam("apellido");
        String fechaNacimiento = context.formParam("fechaNacimiento");//cast to date


        PersonaHumana persona =  new PersonaHumana();

        persona.setNombre(nombre);
        persona.setApellido(apellido);
        if(!fechaNacimiento.equals("")) persona.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
        if(direccion != null) persona.setDireccion(direccion);
        persona.setDocumento(documento);

        return persona;
    }

    private static PersonaJuridica crearPersonaJuridica(Context context, Direccion direccion1, Documento documento1){
        String razonSocial = context.formParam("razonSocial");
        String tipoRazonSocial = context.formParam("tipoRazonSocial");
        String rubro = context.formParam("rubro");

        PersonaJuridica persona =  new PersonaJuridica(razonSocial);
        persona.setTipo(TipoJuridico.valueOf(tipoRazonSocial.toUpperCase()));
        persona.setRubro(rubro);
        if(direccion1 != null) {
            persona.setDireccion(direccion1);
        }
        persona.setDocumento(documento1);

        return persona;
    }

    private static MedioDeContacto crearMedioDeContacto(String tipoContacto, String contacto){
        MedioDeContacto medioDeContacto;

        switch (tipoContacto.toLowerCase()) {
            case "email" -> medioDeContacto = new CorreoElectronico(contacto);//contacto == mail
            case "telefono" -> medioDeContacto = new Telefono(contacto); //contacto == telefono
            case "whatsapp" -> medioDeContacto = new Whatsapp(contacto);
            default -> medioDeContacto = null;
        }
        return medioDeContacto;
    }
    private static List<MedioDeContacto> obtenerMediosDeContacto(Context context){
        List<String> tiposContacto = context.formParams("tipoContacto");
        List<String> valoresContacto = context.formParams("contacto");

        List<MedioDeContacto> contactos = new ArrayList<>();
        for(String tipoContacto: tiposContacto){
            String contacto = valoresContacto.get(tiposContacto.indexOf(tipoContacto));
            contactos.add(crearMedioDeContacto(tipoContacto, contacto));
        }

        return contactos;
    }

    private static void cargarContrigucionesQuePuedeHacer(Persona persona, Context context){
        boolean donarDinero = "on".equals(context.formParam("donarDinero"));
        boolean cargoHeladera = "on".equals(context.formParam("cargoHeladera"));
        boolean donarVianda = "on".equals(context.formParam("donarVianda"));
        boolean distribuirViandas = "on".equals(context.formParam("distribuirViandas"));
        boolean registrarPersonaEnSituacionVulnerable = "on".equals(context.formParam("registrarPersonasSituacionVulnerable"));
        boolean ofrecerProductos = "on".equals(context.formParam("ofrecerProductos"));

        if(donarDinero) persona.addContribucionesQuePuedeHacer(TipoContribucion.DONACION_DINERO);
        if(cargoHeladera) persona.addContribucionesQuePuedeHacer(TipoContribucion.HACERSE_CARGO_HELADERA);
        if(donarVianda) persona.addContribucionesQuePuedeHacer(TipoContribucion.DONACION_VIANDAS);
        if(distribuirViandas) persona.addContribucionesQuePuedeHacer(TipoContribucion.REDISTRIBUCION_VIANDAS);
        if(registrarPersonaEnSituacionVulnerable) persona.addContribucionesQuePuedeHacer(TipoContribucion.ENTREGA_TARJETAS);
        if(ofrecerProductos) persona.addContribucionesQuePuedeHacer(TipoContribucion.OFRECER_PRODUCTO);
    }
    public static void getRegistrarUsuario(@NotNull Context context){
        context.render("/views/usuarios/Registrarse.mustache");
    }

    public static void getInicioSession(@NotNull Context context) {
        context.render("/views/usuarios/InicioSession.mustache");
    }
}
