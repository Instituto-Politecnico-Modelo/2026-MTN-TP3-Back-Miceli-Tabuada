package com.example.backEnd.DTO.clima;

public class ClimaResponseDTO {
    private boolean disponible;
    private String descripcion;
    private Double temperatura;
    private String icono;

    public ClimaResponseDTO() {}

    public ClimaResponseDTO(boolean disponible, String descripcion,
                             Double temperatura, String icono) {
        this.disponible = disponible;
        this.descripcion = descripcion;
        this.temperatura = temperatura;
        this.icono = icono;
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
}
