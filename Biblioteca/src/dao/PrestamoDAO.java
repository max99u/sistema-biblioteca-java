package dao;

import modelo.Prestamo;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrestamoDAO {

    // Verificar si el usuario tiene menos de 3 préstamos activos
    private boolean usuarioPuedePedirPrestamo(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE usuario_id = ? AND fecha_devolucion IS NULL";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) < 3; // Permite el préstamo solo si tiene menos de 3 activos
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al verificar los préstamos activos del usuario: " + e.getMessage());
        }
        return false;
    }

    // Verificar si el libro ya está prestado y no ha sido devuelto
    private boolean libroDisponible(int libroId) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE libro_id = ? AND fecha_devolucion IS NULL";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, libroId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // El libro está disponible si no hay registros sin devolver
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al verificar la disponibilidad del libro: " + e.getMessage());
        }
        return false;
    }

    public boolean registrarPrestamo(Prestamo prestamo) {
        if (!usuarioPuedePedirPrestamo(prestamo.getUsuarioId())) {
            System.out.println("❌ El usuario ya tiene 3 préstamos activos. No puede pedir más.");
            return false;
        }

        if (!libroDisponible(prestamo.getLibroId())) {
            System.out.println("❌ El libro ya está prestado y no ha sido devuelto.");
            return false;
        }

        String sql = "INSERT INTO prestamos (usuario_id, libro_id, fecha_prestamo) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, prestamo.getUsuarioId());
            stmt.setInt(2, prestamo.getLibroId());
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));

            int filasInsertadas = stmt.executeUpdate();
            if (filasInsertadas > 0) {
                // Obtener el ID del préstamo insertado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int prestamoId = rs.getInt(1);
                    registrarPrestamoExpress(prestamoId, prestamo.getFechaPrestamo());
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar préstamo: " + e.getMessage());
        }
        return false;
    }

    // Registrar préstamo express si se devuelve en menos de 7 días
    private void registrarPrestamoExpress(int prestamoId, Date fechaPrestamo) {
        long sieteDias = 7L * 24 * 60 * 60 * 1000; // 7 días en milisegundos
        Date fechaLimite = new Date(fechaPrestamo.getTime() + sieteDias);

        String sql = "INSERT INTO prestamos_express (prestamo_id, fecha_prestamo, fecha_limite) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prestamoId);
            stmt.setDate(2, new java.sql.Date(fechaPrestamo.getTime()));
            stmt.setDate(3, new java.sql.Date(fechaLimite.getTime()));

            stmt.executeUpdate();
            System.out.println("✅ Préstamo express registrado correctamente.");
        } catch (SQLException e) {
            System.out.println("❌ Error al registrar préstamo express: " + e.getMessage());
        }
    }


    public boolean devolverPrestamo(int prestamoId) {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(new Date().getTime()));
            stmt.setInt(2, prestamoId);

            int filasActualizadas = stmt.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error al devolver préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPrestamo(int prestamoId) {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prestamoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar préstamo: " + e.getMessage());
        }
        return false;
    }

    public List<Prestamo> obtenerPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT p.id, u.nombre AS usuario, l.titulo AS libro, p.fecha_prestamo, p.fecha_devolucion " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.usuario_id = u.id " +
                     "JOIN libros l ON p.libro_id = l.id";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("libro"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion")
                );
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener préstamos: " + e.getMessage());
        }
        return prestamos;
    }

}
