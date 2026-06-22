package com.example.backEnd.DTO.reserva;

import com.example.backEnd.Entidad.EstadoReserva;
import java.time.LocalDate;

public class ReservaResponseDTO {
    private Long id;
    private LocalDate fecha;
    private EstadoReserva estado;
    private String motivoCancelacion;
    private Long turnoId;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private Long canchaId;
    private String canchaNombre;
    private Long usuarioId;

    public ReservaResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }
    public Long getTurnoId() { return turnoId; }
    public void setTurnoId(Long turnoId) { this.turnoId = turnoId; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public Long getCanchaId() { return canchaId; }
    public void setCanchaId(Long canchaId) { this.canchaId = canchaId; }
    public String getCanchaNombre() { return canchaNombre; }
    public void setCanchaNombre(String canchaNombre) { this.canchaNombre = canchaNombre; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
