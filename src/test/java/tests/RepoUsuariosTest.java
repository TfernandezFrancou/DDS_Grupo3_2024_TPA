package tests;

import org.example.autenticacion.Usuario;
import org.example.personas.PersonaHumana;
import org.example.personas.documentos.Documento;
import org.example.personas.documentos.TipoDocumento;
import org.example.repositorios.RepoUsuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepoUsuariosTest {

    RepoUsuario repoUsuario;

    Usuario usuario;

    Documento documento;

    @BeforeEach
    public void setUp(){
         this.repoUsuario = RepoUsuario.getInstancia();
        this.repoUsuario.clean();

        this. documento = new Documento(TipoDocumento.DNI, "43244599");


        PersonaHumana personaHumana = new PersonaHumana();
        personaHumana.setNombre("franco");
        personaHumana.setDocumento(documento);
        this.usuario = new Usuario("franco", documento, personaHumana);
        this.usuario.setContrasenia("1234");
    }

    @Test
    public void testObtenerUsuarioPorDocumento(){

        repoUsuario.agregarUsuarios(usuario);

        Usuario user = repoUsuario.obtenerUsuarioPorDocumento(documento);
        Assertions.assertEquals(usuario.getIdUsuario(),user.getIdUsuario());
    }

    @Test
    public void testObtenerUsuarioPorNombreDeUsuarioYContrasenia(){

        repoUsuario.agregarUsuarios(usuario);

        Usuario user = repoUsuario.obtenerUsuarioPorNombreDeUsuario("franco");
        Assertions.assertEquals(usuario.getIdUsuario(),user.getIdUsuario());
    }

    @Test
    public void testExisteNombreUsuarioRegistrado(){
        repoUsuario.agregarUsuarios(usuario);

        boolean existeUsuario = repoUsuario.existeNombreUsuarioRegistrado("franco");
        Assertions.assertTrue(existeUsuario);
    }
}
