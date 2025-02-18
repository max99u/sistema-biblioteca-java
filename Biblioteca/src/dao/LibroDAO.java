package dao;

import modelo.Libro;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public boolean registrarLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, genero, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getGenero());
            stmt.setString(4, libro.getEstado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar libro: " + e.getMessage());
        }
        return false;
    }

    public List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                libros.add(new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener libros: " + e.getMessage());
        }
        return libros;
    }

    public boolean actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, genero = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getGenero());
            stmt.setString(4, libro.getEstado());
            stmt.setInt(5, libro.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar libro: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarLibro(int libroId) {
        String sql = "DELETE FROM libros WHERE id = ? AND id NOT IN (SELECT libro_id FROM prestamos WHERE fecha_devolucion IS NULL)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, libroId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar libro: " + e.getMessage());
        }
        return false;
    }
}
