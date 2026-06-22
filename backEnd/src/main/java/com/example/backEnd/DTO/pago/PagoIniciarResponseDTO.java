package com.example.backEnd.DTO.pago;

public class PagoIniciarResponseDTO {
    private String redirectUrl;
    private String preferenceId;

    public PagoIniciarResponseDTO() {}

    public PagoIniciarResponseDTO(String redirectUrl, String preferenceId) {
        this.redirectUrl = redirectUrl;
        this.preferenceId = preferenceId;
    }

    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }
    public String getPreferenceId() { return preferenceId; }
    public void setPreferenceId(String preferenceId) { this.preferenceId = preferenceId; }
}
