package gui;

import dao.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaUsuarios extends JFrame {
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private UsuarioDAO usuarioDAO;

    public VentanaUsuarios() {
        setTitle("Gestión de Usuarios");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarioDAO = new UsuarioDAO();

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Rol");

        // Tabla de usuarios
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnActualizar = new JButton("Actualizar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnActualizar.addActionListener(e -> actualizarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnCerrar.addActionListener(e -> dispose());

        add(panel);
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{u.getId(), u.getNombre(), u.getEmail(), u.getTelefono(), u.getRol()});
        }
    }

    private void agregarUsuario() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre:");
        String email = JOptionPane.showInputDialog(this, "Ingrese el email:");
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el teléfono:");
        String rol = JOptionPane.showInputDialog(this, "Ingrese el rol (ADMIN o USUARIO):");
        String password = JOptionPane.showInputDialog(this, "Ingrese la contraseña:");

        if (nombre != null && email != null && telefono != null && rol != null && password != null) {
            Usuario nuevoUsuario = new Usuario(0, nombre, email, telefono, rol.toUpperCase(), password);
            usuarioDAO.registrarUsuario(nuevoUsuario);
            cargarUsuarios();
        }
    }

    private void actualizarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para actualizar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre:", modeloTabla.getValueAt(filaSeleccionada, 1));
        String email = JOptionPane.showInputDialog(this, "Ingrese el nuevo email:", modeloTabla.getValueAt(filaSeleccionada, 2));
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el nuevo teléfono:", modeloTabla.getValueAt(filaSeleccionada, 3));
        String rol = JOptionPane.showInputDialog(this, "Ingrese el nuevo rol (ADMIN o USUARIO):", modeloTabla.getValueAt(filaSeleccionada, 4));

        if (nombre != null && email != null && telefono != null && rol != null) {
            Usuario usuarioActualizado = new Usuario(id, nombre, email, telefono, rol.toUpperCase(), "password");
            usuarioDAO.actualizarUsuario(usuarioActualizado);
            cargarUsuarios();
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            usuarioDAO.eliminarUsuario(id);
            cargarUsuarios();
        }
    }
}
