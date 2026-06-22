package com.example.backEnd.DTO;

import com.example.backEnd.Entidad.Rol;
import java.sql.Timestamp;

public class UsuarioResponseDTO {
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Rol rol;
    private boolean activo;
    private Timestamp fechaRegistro;

    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Long id, String dni, String nombre, String apellido,
                               String email, String telefono, Rol rol,
                               boolean activo, Timestamp fechaRegistro) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public Rol getRol() { return rol; }
    public boolean isActivo() { return activo; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
}
