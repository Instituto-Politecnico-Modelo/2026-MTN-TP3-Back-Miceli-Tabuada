package com.example.backEnd.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequestDTO {
    @NotBlank @Size(max = 20)
    private String dni;
    @NotBlank @Size(max = 100)
    private String nombre;
    @NotBlank @Size(max = 100)
    private String apellido;
    @NotBlank @Email
    private String email;
    @NotBlank @Size(min = 6)
    private String password;
    @Size(max = 20)
    private String telefono;

    public RegistroRequestDTO() {}
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
