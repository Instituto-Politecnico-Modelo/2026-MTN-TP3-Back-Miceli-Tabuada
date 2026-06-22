package com.example.backEnd.Entidad;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(
    name = "reservas",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_reservas_turno_fecha", columnNames = {"turno_id", "fecha"})
    }
)
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @Column(name = "motivo_cancelacion", length = 500)
    private String motivoCancelacion;

    @Column(name = "cancelado_por_admin", nullable = false)
    private boolean canceladoPorAdmin = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelado_por_usuario_id")
    private Usuario canceladoPorUsuario;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp fechaCreacion;

    @Column(name = "fecha_modificacion")
    private Timestamp fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    private Pago pago;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = new Timestamp(System.currentTimeMillis());
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = new Timestamp(System.currentTimeMillis());
    }

    public Reserva() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }

    public boolean isCanceladoPorAdmin() { return canceladoPorAdmin; }
    public void setCanceladoPorAdmin(boolean canceladoPorAdmin) { this.canceladoPorAdmin = canceladoPorAdmin; }

    public Usuario getCanceladoPorUsuario() { return canceladoPorUsuario; }
    public void setCanceladoPorUsuario(Usuario canceladoPorUsuario) { this.canceladoPorUsuario = canceladoPorUsuario; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Timestamp getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(Timestamp fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
}
