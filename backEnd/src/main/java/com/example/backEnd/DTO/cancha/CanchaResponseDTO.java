package com.example.backEnd.DTO.cancha;

import com.example.backEnd.Entidad.TipoCancha;

public class CanchaResponseDTO {
    private Long id;
    private String nombre;
    private TipoCancha tipo;
    private String descripcion;
    private boolean activa;

    public CanchaResponseDTO() {}

    public CanchaResponseDTO(Long id, String nombre, TipoCancha tipo,
                              String descripcion, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.activa = activa;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoCancha getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public boolean isActiva() { return activa; }
}
