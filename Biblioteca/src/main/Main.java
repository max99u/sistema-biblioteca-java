package main;

import dao.UsuarioDAO;
import dao.PrestamoDAO;
import modelo.Usuario;
import modelo.Prestamo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
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

        // 🔹 DEMOSTRACIÓN DE USUARIOS
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        PrestamoDAO prestamoDAO = new PrestamoDAO();

        // Insertar un nuevo usuario
        Usuario nuevoUsuario = new Usuario(0, "Max Stolbun", "msolbun@gmail.com", "123456789", "ADMIN", "password123");
        usuarioDAO.registrarUsuario(nuevoUsuario);
        System.out.println("✅ Usuario registrado con éxito.");

        // Obtener el usuario registrado
        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();
        int usuarioId = (usuarios.isEmpty()) ? -1 : usuarios.get(0).getId(); // Tomar el ID del primer usuario encontrado

        if (usuarioId != -1) {
            // 🔹 DEMOSTRACIÓN DE PRÉSTAMOS
            System.out.println("📚 Registrando un nuevo préstamo...");
            Prestamo nuevoPrestamo = new Prestamo(usuarioId, 3, new Date()); // Usuario registrado toma el libro 3
            boolean exitoRegistro = prestamoDAO.registrarPrestamo(nuevoPrestamo);
            System.out.println(exitoRegistro ? "✅ Préstamo registrado con éxito." : "❌ Error al registrar el préstamo.");

            // Obtener y listar los préstamos existentes
            System.out.println("📋 Listando todos los préstamos...");
            List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();
            for (Prestamo p : prestamos) {
                System.out.println("ID: " + p.getId() + " | Usuario: " + p.getUsuarioId() + " | Libro: " + p.getLibroId()
                        + " | Fecha Préstamo: " + p.getFechaPrestamo() + " | Fecha Devolución: " + p.getFechaDevolucion());
            }

            // Devolver un préstamo (suponiendo que el ID del préstamo es 1)
            if (!prestamos.isEmpty()) {
                int idPrestamoADevolver = prestamos.get(0).getId(); // Tomamos el primer préstamo
                System.out.println("📦 Devolviendo el préstamo con ID " + idPrestamoADevolver + "...");
                boolean exitoDevolucion = prestamoDAO.devolverPrestamo(idPrestamoADevolver);
                System.out.println(exitoDevolucion ? "✅ Préstamo devuelto con éxito." : "❌ Error al devolver el préstamo.");
            }

            // Ahora, eliminar al usuario (después de realizar las operaciones de préstamo)
            usuarioDAO.eliminarUsuario(usuarioId);
            System.out.println("✅ Usuario eliminado con éxito.");
        } else {
            System.out.println("❌ No se encontró un usuario válido para registrar el préstamo.");
        }
    }
}
