package com.example.backEnd.DTO.reserva;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservaRequestDTO {
    @NotNull
    private Long turnoId;
    @NotNull @Future
    private LocalDate fecha;

    public ReservaRequestDTO() {}
    public Long getTurnoId() { return turnoId; }
    public void setTurnoId(Long turnoId) { this.turnoId = turnoId; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
