package gui;

import dao.PrestamoDAO;
import modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class VentanaPrestamos extends JFrame {
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private PrestamoDAO prestamoDAO;

    public VentanaPrestamos() {
        setTitle("Gestión de Préstamos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        prestamoDAO = new PrestamoDAO();

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Usuario ID");
        modeloTabla.addColumn("Libro ID");
        modeloTabla.addColumn("Fecha Préstamo");
        modeloTabla.addColumn("Fecha Devolución");

        // Tabla de préstamos
        tablaPrestamos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPrestamos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar Préstamo");
        JButton btnDevolver = new JButton("Devolver Préstamo");
        JButton btnEliminar = new JButton("Eliminar Préstamo");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> devolverPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());
        btnCerrar.addActionListener(e -> dispose());

        add(panel);
        cargarPrestamos();
    }

    private void cargarPrestamos() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();
        for (Prestamo p : prestamos) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getUsuarioId(), p.getLibroId(), p.getFechaPrestamo(), p.getFechaDevolucion()});
        }
    }

    private void registrarPrestamo() {
        String usuarioIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID del usuario:");
        String libroIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID del libro:");

        if (usuarioIdStr != null && libroIdStr != null) {
            try {
                int usuarioId = Integer.parseInt(usuarioIdStr);
                int libroId = Integer.parseInt(libroIdStr);
                Prestamo nuevoPrestamo = new Prestamo(0, usuarioId, libroId, new Date(), null);

                if (prestamoDAO.registrarPrestamo(nuevoPrestamo)) {
                    JOptionPane.showMessageDialog(this, "✅ Préstamo registrado con éxito.");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ No se pudo registrar el préstamo. Verifique las restricciones.");
                }
                cargarPrestamos();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "❌ ID inválido. Debe ser un número.");
            }
        }
    }

    private void devolverPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para devolver.");
            return;
        }

        int prestamoId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de devolver este préstamo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (prestamoDAO.devolverPrestamo(prestamoId)) {
                JOptionPane.showMessageDialog(this, "✅ Préstamo devuelto con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al devolver el préstamo.");
            }
            cargarPrestamos();
        }
    }

    private void eliminarPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para eliminar.");
            return;
        }

        int prestamoId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este préstamo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (prestamoDAO.eliminarPrestamo(prestamoId)) {
                JOptionPane.showMessageDialog(this, "✅ Préstamo eliminado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar el préstamo.");
            }
            cargarPrestamos();
        }
    }
}
