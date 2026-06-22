package com.example.backEnd.DTO.reserva;

public class ReservaConPagoResponseDTO {
    private ReservaResponseDTO reserva;
    private String pagoRedirectUrl;
    private String pagoEstado;

    public ReservaConPagoResponseDTO() {}

    public ReservaConPagoResponseDTO(ReservaResponseDTO reserva,
                                      String pagoRedirectUrl,
                                      String pagoEstado) {
        this.reserva = reserva;
        this.pagoRedirectUrl = pagoRedirectUrl;
        this.pagoEstado = pagoEstado;
    }

    public ReservaResponseDTO getReserva() { return reserva; }
    public void setReserva(ReservaResponseDTO reserva) { this.reserva = reserva; }
    public String getPagoRedirectUrl() { return pagoRedirectUrl; }
    public void setPagoRedirectUrl(String pagoRedirectUrl) { this.pagoRedirectUrl = pagoRedirectUrl; }
    public String getPagoEstado() { return pagoEstado; }
    public void setPagoEstado(String pagoEstado) { this.pagoEstado = pagoEstado; }
}
