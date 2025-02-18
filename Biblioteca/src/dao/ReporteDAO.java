package dao;

import util.ConexionDB;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReporteDAO {
    
    // Reporte 1: Libros más prestados en los últimos 6 meses
    public Map<String, Integer> obtenerLibrosMasPrestados() {
        Map<String, Integer> reporte = new HashMap<>();
        String sql = "SELECT l.titulo, COUNT(p.id) AS cantidad FROM prestamos p " +
                     "JOIN libros l ON p.libro_id = l.id " +
                     "WHERE p.fecha_prestamo >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
                     "GROUP BY l.titulo ORDER BY cantidad DESC LIMIT 10";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reporte.put(rs.getString("titulo"), rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener el reporte de libros más prestados: " + e.getMessage());
        }
        return reporte;
    }
    
    // Reporte 2: Usuarios con más préstamos en los últimos 3 meses
    public Map<String, Integer> obtenerUsuariosMasActivos() {
        Map<String, Integer> reporte = new HashMap<>();
        String sql = "SELECT u.nombre, COUNT(p.id) AS cantidad FROM prestamos p " +
                     "JOIN usuarios u ON p.usuario_id = u.id " +
                     "WHERE p.fecha_prestamo >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) " +
                     "GROUP BY u.nombre ORDER BY cantidad DESC LIMIT 10";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reporte.put(rs.getString("nombre"), rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener el reporte de usuarios más activos: " + e.getMessage());
        }
        return reporte;
    }
    
    // Reporte 3: Porcentaje de libros por género
    public Map<String, Double> obtenerPorcentajeLibrosPorGenero() {
        Map<String, Double> reporte = new HashMap<>();
        String sql = "SELECT genero, COUNT(*) * 100.0 / (SELECT COUNT(*) FROM libros) AS porcentaje " +
                     "FROM libros GROUP BY genero";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reporte.put(rs.getString("genero"), rs.getDouble("porcentaje"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener el reporte de libros por género: " + e.getMessage());
        }
        return reporte;
    }
    
    // Reporte 4: Libros que no se han prestado en el último año
    public Map<Integer, String> obtenerLibrosNoPrestados() {
        Map<Integer, String> reporte = new HashMap<>();
        String sql = "SELECT id, titulo FROM libros " +
                     "WHERE id NOT IN (SELECT libro_id FROM prestamos WHERE fecha_prestamo >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR))";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reporte.put(rs.getInt("id"), rs.getString("titulo"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener el reporte de libros no prestados: " + e.getMessage());
        }
        return reporte;
    }
}
