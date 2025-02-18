package gui;

import dao.ReporteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class VentanaReportes extends JFrame {
    private ReporteDAO reporteDAO;
    private JTable tablaLibros, tablaUsuarios, tablaGeneros, tablaLibrosNoPrestados;

    public VentanaReportes() {
        setTitle("Reportes de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        reporteDAO = new ReporteDAO();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Libros Más Prestados", crearPanelLibrosMasPrestados());
        tabbedPane.add("Usuarios Más Activos", crearPanelUsuariosMasActivos());
        tabbedPane.add("Distribución por Género", crearPanelLibrosPorGenero());
        tabbedPane.add("Libros No Prestados", crearPanelLibrosNoPrestados());
        
        add(tabbedPane);
    }

    private JPanel crearPanelLibrosMasPrestados() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Libro", "Cantidad"}, 0);
        tablaLibros = new JTable(modelo);
        actualizarLibrosMasPrestados();
        panel.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarLibrosMasPrestados());
        panel.add(btnActualizar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelUsuariosMasActivos() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Usuario", "Cantidad"}, 0);
        tablaUsuarios = new JTable(modelo);
        actualizarUsuariosMasActivos();
        panel.add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarUsuariosMasActivos());
        panel.add(btnActualizar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelLibrosPorGenero() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Género", "Porcentaje"}, 0);
        tablaGeneros = new JTable(modelo);
        actualizarLibrosPorGenero();
        panel.add(new JScrollPane(tablaGeneros), BorderLayout.CENTER);
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarLibrosPorGenero());
        panel.add(btnActualizar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelLibrosNoPrestados() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Título"}, 0);
        tablaLibrosNoPrestados = new JTable(modelo);
        actualizarLibrosNoPrestados();
        panel.add(new JScrollPane(tablaLibrosNoPrestados), BorderLayout.CENTER);
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarLibrosNoPrestados());
        panel.add(btnActualizar, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarLibrosMasPrestados() {
        DefaultTableModel modelo = (DefaultTableModel) tablaLibros.getModel();
        modelo.setRowCount(0);
        for (Map.Entry<String, Integer> entry : reporteDAO.obtenerLibrosMasPrestados().entrySet()) {
            modelo.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void actualizarUsuariosMasActivos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaUsuarios.getModel();
        modelo.setRowCount(0);
        for (Map.Entry<String, Integer> entry : reporteDAO.obtenerUsuariosMasActivos().entrySet()) {
            modelo.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private void actualizarLibrosPorGenero() {
        DefaultTableModel modelo = (DefaultTableModel) tablaGeneros.getModel();
        modelo.setRowCount(0);
        for (Map.Entry<String, Double> entry : reporteDAO.obtenerPorcentajeLibrosPorGenero().entrySet()) {
            modelo.addRow(new Object[]{entry.getKey(), String.format("%.2f%%", entry.getValue())});
        }
    }

    private void actualizarLibrosNoPrestados() {
        DefaultTableModel modelo = (DefaultTableModel) tablaLibrosNoPrestados.getModel();
        modelo.setRowCount(0);
        for (Map.Entry<Integer, String> entry : reporteDAO.obtenerLibrosNoPrestados().entrySet()) {
            modelo.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaReportes ventana = new VentanaReportes();
            ventana.setVisible(true);
        });
    }
}
