package com.example.backEnd.Controller;

import com.example.backEnd.DTO.pago.PagoIniciarResponseDTO;
import com.example.backEnd.Service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Iniciar pago para una reserva existente
    @PostMapping("/iniciar/{reservaId}")
    public ResponseEntity<PagoIniciarResponseDTO> iniciar(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.iniciarPago(reservaId));
    }

    // Webhook Mercado Pago (permitAll en SecurityConfig)
    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "x-signature", required = false) String xSignature) {
        pagoService.procesarWebhook(payload, xSignature);
        return ResponseEntity.ok().build();
    }
}
