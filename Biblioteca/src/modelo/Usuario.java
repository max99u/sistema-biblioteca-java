package modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String rol;
    private String password;

    public Usuario(int id, String nombre, String email, String telefono, String rol, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.password = password;
    }

 // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPassword() { return password; }
    public void setPassword(String conytraseña) { this.password = password; }
}

