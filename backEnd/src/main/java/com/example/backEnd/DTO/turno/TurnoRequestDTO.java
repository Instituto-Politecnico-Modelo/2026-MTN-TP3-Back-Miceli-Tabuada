package com.example.backEnd.DTO.turno;

import com.example.backEnd.Entidad.DiaSemana;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class TurnoRequestDTO {
    @NotNull
    private DiaSemana diaSemana;
    @NotNull
    private LocalTime horaInicio;
    @NotNull
    private LocalTime horaFin;
    private boolean disponible = true;

    public TurnoRequestDTO() {}
    public DiaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaSemana diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
