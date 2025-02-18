package modelo;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private String estado;

    public Libro(int id, String titulo, String autor, String genero, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
