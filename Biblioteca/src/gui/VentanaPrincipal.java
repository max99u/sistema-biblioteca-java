package gui;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Biblioteca");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // Se agregó un espacio más para el botón de reportes

        // Botones para funcionalidades
        JButton btnUsuarios = new JButton("Gestión de Usuarios");
        JButton btnLibros = new JButton("Gestión de Libros");
        JButton btnPrestamos = new JButton("Gestión de Préstamos");
        JButton btnReportes = new JButton("Generar Reportes");
        JButton btnSalir = new JButton("Salir");

        // Agregar botones al panel
        panel.add(btnUsuarios);
        panel.add(btnLibros);
        panel.add(btnPrestamos);
        panel.add(btnReportes);
        panel.add(btnSalir);

        // Acción para abrir la ventana de gestión de usuarios
        btnUsuarios.addActionListener(e -> {
            VentanaUsuarios ventanaUsuarios = new VentanaUsuarios();
            ventanaUsuarios.setVisible(true);
        });

        // Acción para abrir la ventana de gestión de libros
        btnLibros.addActionListener(e -> {
            VentanaLibros ventanaLibros = new VentanaLibros();
            ventanaLibros.setVisible(true);
        });

        // Acción para abrir la ventana de gestión de préstamos
        btnPrestamos.addActionListener(e -> {
            VentanaPrestamos ventanaPrestamos = new VentanaPrestamos();
            ventanaPrestamos.setVisible(true);
        });

        // Acción para abrir la ventana de reportes
        btnReportes.addActionListener(e -> {
            VentanaReportes ventanaReportes = new VentanaReportes();
            ventanaReportes.setVisible(true);
        });

        // Acción para cerrar la aplicación
        btnSalir.addActionListener(e -> System.exit(0));

        // Agregar el panel a la ventana
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
