package gui;

import dao.LibroDAO;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaLibros extends JFrame {
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private LibroDAO libroDAO;

    public VentanaLibros() {
        setTitle("Gestión de Libros");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        libroDAO = new LibroDAO();

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Autor");
        modeloTabla.addColumn("Género");
        modeloTabla.addColumn("Estado");

        // Tabla de libros
        tablaLibros = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Libro");
        JButton btnActualizar = new JButton("Actualizar Libro");
        JButton btnEliminar = new JButton("Eliminar Libro");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnCerrar.addActionListener(e -> dispose());

        add(panel);
        cargarLibros();
    }

    private void cargarLibros() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Libro> libros = libroDAO.obtenerLibros();
        for (Libro l : libros) {
            modeloTabla.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getGenero(), l.getEstado()});
        }
    }

    private void agregarLibro() {
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el título:");
        String autor = JOptionPane.showInputDialog(this, "Ingrese el autor:");
        String genero = JOptionPane.showInputDialog(this, "Ingrese el género:");
        String estado = "DISPONIBLE"; // Los libros nuevos están disponibles

        if (titulo != null && autor != null && genero != null) {
            Libro nuevoLibro = new Libro(0, titulo, autor, genero, estado);
            libroDAO.registrarLibro(nuevoLibro);
            cargarLibros();
        }
    }

    private void actualizarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para actualizar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String titulo = JOptionPane.showInputDialog(this, "Ingrese el nuevo título:", modeloTabla.getValueAt(filaSeleccionada, 1));
        String autor = JOptionPane.showInputDialog(this, "Ingrese el nuevo autor:", modeloTabla.getValueAt(filaSeleccionada, 2));
        String genero = JOptionPane.showInputDialog(this, "Ingrese el nuevo género:", modeloTabla.getValueAt(filaSeleccionada, 3));
        String estado = JOptionPane.showInputDialog(this, "Ingrese el nuevo estado (DISPONIBLE o NO DISPONIBLE):", modeloTabla.getValueAt(filaSeleccionada, 4));

        if (titulo != null && autor != null && genero != null && estado != null) {
            Libro libroActualizado = new Libro(id, titulo, autor, genero, estado.toUpperCase());
            libroDAO.actualizarLibro(libroActualizado);
            cargarLibros();
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este libro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            libroDAO.eliminarLibro(id);
            cargarLibros();
        }
    }
}
