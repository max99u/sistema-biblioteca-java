package main;

import dao.UsuarioDAO;
import modelo.Usuario;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            FileInputStream input = new FileInputStream("config.properties");
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            System.out.println("Intentando conectar con:");
            System.out.println("URL: " + url);
            System.out.println("Usuario: " + user);

            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a MySQL.");
                conn.close();
            } else {
                System.out.println("❌ Error en la conexión.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error al leer config.properties: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error en la conexión a MySQL: " + e.getMessage());
        }

        // Demostración del DAO
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Insertar un nuevo usuario
        Usuario nuevoUsuario = new Usuario(0, "Max Stolbun", "msolbun@gmail.com", "123456789", "ADMIN", "password123");
        usuarioDAO.registrarUsuario(nuevoUsuario);
        System.out.println("✅ Usuario registrado con éxito.");

        // Obtener y mostrar todos los usuarios
        System.out.println("📋 Lista de usuarios registrados:");
        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();
        for (Usuario u : usuarios) {
            System.out.println("ID: " + u.getId() + " | Nombre: " + u.getNombre() + " | Email: " + u.getEmail());
        }

        // Actualizar un usuario si existe
        if (!usuarios.isEmpty()) {
            Usuario usuarioAActualizar = usuarios.get(0);  // Tomamos el primer usuario
            usuarioAActualizar.setNombre("Juan Pérez Modificado");
            usuarioAActualizar.setEmail("juan_modificado@example.com");
            usuarioDAO.actualizarUsuario(usuarioAActualizar);
            System.out.println("✅ Usuario actualizado con éxito.");
        }

        // Eliminar un usuario si existe
        if (!usuarios.isEmpty()) {
            int usuarioId = usuarios.get(0).getId();
            usuarioDAO.eliminarUsuario(usuarioId);
            System.out.println("✅ Usuario eliminado con éxito.");
        }
    }
}
