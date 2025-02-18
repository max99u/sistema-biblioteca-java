package modelo;

import java.util.Date;

public class Prestamo {
    private int id;
    private int usuarioId; // ID del usuario
    private int libroId;   // ID del libro
    private String usuarioNombre; // Nombre del usuario
    private String libroTitulo;   // Título del libro
    private Date fechaPrestamo;
    private Date fechaDevolucion;

    // 🔹 Constructor para obtener préstamos con nombres de usuario y libro (usado en la GUI)
    public Prestamo(int id, String usuarioNombre, String libroTitulo, Date fechaPrestamo, Date fechaDevolucion) {
        this.id = id;
        this.usuarioNombre = usuarioNombre;
        this.libroTitulo = libroTitulo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    // 🔹 Constructor para DAO con IDs (usado en la base de datos)
    public Prestamo(int id, int usuarioId, int libroId, Date fechaPrestamo, Date fechaDevolucion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    // 🔹 Constructor adicional para registrar préstamos (sin ID de préstamo ni fecha de devolución)
    public Prestamo(int usuarioId, int libroId, Date fechaPrestamo) {
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = null; // No tiene fecha de devolución aún
    }

    // 🔹 Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getLibroId() { return libroId; }
    public void setLibroId(int libroId) { this.libroId = libroId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getLibroTitulo() { return libroTitulo; }
    public void setLibroTitulo(String libroTitulo) { this.libroTitulo = libroTitulo; }

    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
}
