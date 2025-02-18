package gui;

import dao.PrestamoDAO;
import dao.UsuarioDAO;
import dao.LibroDAO;
import modelo.Prestamo;
import modelo.Usuario;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VentanaPrestamos extends JFrame {
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private PrestamoDAO prestamoDAO;
    private UsuarioDAO usuarioDAO;
    private LibroDAO libroDAO;

    public VentanaPrestamos() {
        setTitle("Gestión de Préstamos");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        prestamoDAO = new PrestamoDAO();
        usuarioDAO = new UsuarioDAO(); // Instanciar usuarioDAO
        libroDAO = new LibroDAO(); // Instanciar libroDAO

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Libro");
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
        ajustarColumnas();
    }

    private void cargarPrestamos() {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar los datos
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Prestamo p : prestamos) {
            String fechaPrestamo = dateFormat.format(p.getFechaPrestamo());
            String fechaDevolucion = (p.getFechaDevolucion() != null) ? dateFormat.format(p.getFechaDevolucion()) : "Pendiente";

            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getUsuarioNombre(),
                    p.getLibroTitulo(),
                    fechaPrestamo,
                    fechaDevolucion
            });
        }

        tablaPrestamos.setDefaultRenderer(Object.class, new PrestamoTableRenderer());
    }

    private void ajustarColumnas() {
        TableColumnModel columnModel = tablaPrestamos.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(120);
        tablaPrestamos.setRowHeight(25);
    }

    private void registrarPrestamo() {
        JComboBox<String> comboUsuarios = new JComboBox<>();
        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();
        for (Usuario u : usuarios) {
            comboUsuarios.addItem(u.getId() + " - " + u.getNombre());
        }

        JComboBox<String> comboLibros = new JComboBox<>();
        List<Libro> libros = libroDAO.obtenerLibros();
        for (Libro l : libros) {
            comboLibros.addItem(l.getId() + " - " + l.getTitulo());
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Seleccione Usuario:"));
        panel.add(comboUsuarios);
        panel.add(new JLabel("Seleccione Libro:"));
        panel.add(comboLibros);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Registrar Préstamo", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();
            int usuarioId = Integer.parseInt(usuarioSeleccionado.split(" - ")[0]);

            String libroSeleccionado = (String) comboLibros.getSelectedItem();
            int libroId = Integer.parseInt(libroSeleccionado.split(" - ")[0]);

            Prestamo nuevoPrestamo = new Prestamo(usuarioId, libroId, new Date());
            if (prestamoDAO.registrarPrestamo(nuevoPrestamo)) {
                JOptionPane.showMessageDialog(this, "✅ Préstamo registrado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ No se pudo registrar el préstamo. Verifique las restricciones.");
            }
            cargarPrestamos();
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
