package com.example.backEnd.Service;

import com.example.backEnd.DTO.pago.PagoIniciarResponseDTO;
import com.example.backEnd.Entidad.EstadoPago;
import com.example.backEnd.Entidad.Pago;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.PagoRepository;
import com.example.backEnd.Repository.ReservaRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentRefundClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.success-url}")
    private String successUrl;

    @Value("${mercadopago.failure-url}")
    private String failureUrl;

    @Value("${mercadopago.pending-url}")
    private String pendingUrl;

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    @Value("${mercadopago.webhook-secret}")
    private String webhookSecret;

    public PagoService(PagoRepository pagoRepository,
                       ReservaRepository reservaRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
    }

    @PostConstruct
    public void init() {
        if (accessToken != null && !accessToken.isBlank()) {
            MercadoPagoConfig.setAccessToken(accessToken);
        }
    }

    @Transactional
    public PagoIniciarResponseDTO iniciarPago(Long reservaId) {
        Pago pago = pagoRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado para reserva: " + reservaId));

        try {
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .title("Reserva FutYa #" + reservaId)
                    .quantity(1)
                    .unitPrice(pago.getMonto())
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(List.of(item))
                    .backUrls(PreferenceBackUrlsRequest.builder()
                            .success(successUrl)
                            .failure(failureUrl)
                            .pending(pendingUrl)
                            .build())
                    .notificationUrl(notificationUrl)
                    .externalReference(String.valueOf(reservaId))
                    .autoReturn("approved")
                    .build();

            Preference preference = new PreferenceClient().create(preferenceRequest);
            pago.setPreferenceId(preference.getId());
            pagoRepository.save(pago);

            return new PagoIniciarResponseDTO(preference.getInitPoint(), preference.getId());

        } catch (MPException | MPApiException e) {
            return new PagoIniciarResponseDTO(null, null);
        }
    }

    @Transactional
    public void procesarWebhook(Map<String, Object> payload, String xSignature) {
        if (payload == null) return;
        Object dataObj = payload.get("data");
        if (!(dataObj instanceof Map<?, ?> data)) return;
        Object idObj = data.get("id");
        if (idObj == null) return;

        String referenciaExterna = String.valueOf(idObj);
        pagoRepository.findByReferenciaExterna(referenciaExterna).ifPresent(pago -> {
            Object status = payload.get("status");
            if ("approved".equals(status)) {
                pago.setEstado(EstadoPago.APROBADO);
            } else if ("rejected".equals(status)) {
                pago.setEstado(EstadoPago.RECHAZADO);
            } else if ("pending".equals(status) || "in_process".equals(status)) {
                pago.setEstado(EstadoPago.PENDIENTE);
            }
            pago.setFechaTransaccion(new Timestamp(System.currentTimeMillis()));
            pagoRepository.save(pago);
        });
    }

    @Transactional
    public void reembolsar(Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado: " + pagoId));
        try {
            if (pago.getReferenciaExterna() != null) {
                new PaymentRefundClient().refund(Long.parseLong(pago.getReferenciaExterna()));
            }
        } catch (MPException | MPApiException | NumberFormatException e) {
            // Log y continuar: el reembolso físico falló pero marcamos el estado
        }
        pago.setEstado(EstadoPago.REEMBOLSADO);
        pago.setFechaTransaccion(new Timestamp(System.currentTimeMillis()));
        pagoRepository.save(pago);
    }
}
