package com.example.backEnd.Entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "canchas")
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCancha tipo;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activa = true;

    public Cancha() {
    }

    public Cancha(String nombre, TipoCancha tipo, String descripcion, Boolean activa) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.activa = activa != null ? activa : true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoCancha getTipo() {
        return tipo;
    }

    public void setTipo(TipoCancha tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}

