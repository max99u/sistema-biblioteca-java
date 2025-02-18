package modelo;

import java.util.Date;

public class Prestamo {
    private int id;
    private int usuarioId;
    private int libroId;
    private Date fechaPrestamo;
    private Date fechaDevolucion;

    // Constructor con todos los atributos
    public Prestamo(int id, int usuarioId, int libroId, Date fechaPrestamo, Date fechaDevolucion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    // Constructor sin ID y sin fecha de devolución (cuando se crea un préstamo nuevo)
    public Prestamo(int usuarioId, int libroId, Date fechaPrestamo) {
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = null; // Se actualizará al devolver el libro
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getLibroId() { return libroId; }
    public void setLibroId(int libroId) { this.libroId = libroId; }

    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
}
