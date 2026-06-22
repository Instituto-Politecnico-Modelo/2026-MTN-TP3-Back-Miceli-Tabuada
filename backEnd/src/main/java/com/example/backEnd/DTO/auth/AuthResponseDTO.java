package com.example.backEnd.DTO.auth;

public class AuthResponseDTO {
    private String token;
    private String rol;
    private Long usuarioId;
    private String email;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String rol, Long usuarioId, String email) {
        this.token = token;
        this.rol = rol;
        this.usuarioId = usuarioId;
        this.email = email;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
