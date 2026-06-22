package com.example.backEnd.DTO.turno;

import com.example.backEnd.Entidad.DiaSemana;
import java.time.LocalTime;

public class TurnoResponseDTO {
    private Long id;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean disponible;
    private Long canchaId;
    private String canchaNombre;

    public TurnoResponseDTO() {}

    public TurnoResponseDTO(Long id, DiaSemana diaSemana, LocalTime horaInicio,
                             LocalTime horaFin, boolean disponible,
                             Long canchaId, String canchaNombre) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = disponible;
        this.canchaId = canchaId;
        this.canchaNombre = canchaNombre;
    }

    public Long getId() { return id; }
    public DiaSemana getDiaSemana() { return diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public boolean isDisponible() { return disponible; }
    public Long getCanchaId() { return canchaId; }
    public String getCanchaNombre() { return canchaNombre; }
}
