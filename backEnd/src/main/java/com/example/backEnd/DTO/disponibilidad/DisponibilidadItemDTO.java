package com.example.backEnd.DTO.disponibilidad;

import java.time.LocalTime;

public class DisponibilidadItemDTO {
    private Long turnoId;
    private Long canchaId;
    private String canchaNombre;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean reservado;
    private Long reservaId;

    public DisponibilidadItemDTO() {}

    public Long getTurnoId() { return turnoId; }
    public void setTurnoId(Long turnoId) { this.turnoId = turnoId; }
    public Long getCanchaId() { return canchaId; }
    public void setCanchaId(Long canchaId) { this.canchaId = canchaId; }
    public String getCanchaNombre() { return canchaNombre; }
    public void setCanchaNombre(String canchaNombre) { this.canchaNombre = canchaNombre; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public boolean isReservado() { return reservado; }
    public void setReservado(boolean reservado) { this.reservado = reservado; }
    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }
}
