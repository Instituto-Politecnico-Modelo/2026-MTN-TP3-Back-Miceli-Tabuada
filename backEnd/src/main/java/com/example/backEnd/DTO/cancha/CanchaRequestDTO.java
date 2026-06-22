package com.example.backEnd.DTO.cancha;

import com.example.backEnd.Entidad.TipoCancha;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CanchaRequestDTO {
    @NotBlank @Size(max = 100)
    private String nombre;
    @NotNull
    private TipoCancha tipo;
    @Size(max = 500)
    private String descripcion;
    private Boolean activa = true;

    public CanchaRequestDTO() {}
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoCancha getTipo() { return tipo; }
    public void setTipo(TipoCancha tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
