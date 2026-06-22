package com.example.backEnd.DTO.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CancelacionAdminRequestDTO {
    @NotBlank @Size(max = 500)
    private String motivo;

    public CancelacionAdminRequestDTO() {}
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
