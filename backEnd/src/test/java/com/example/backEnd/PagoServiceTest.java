package com.example.backEnd;

import com.example.backEnd.Entidad.EstadoPago;
import com.example.backEnd.Entidad.Pago;
import com.example.backEnd.Repository.PagoRepository;
import com.example.backEnd.Repository.ReservaRepository;
import com.example.backEnd.Service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private PagoService pagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(pagoService, "accessToken", "TEST-TOKEN");
        ReflectionTestUtils.setField(pagoService, "successUrl", "http://localhost/success");
        ReflectionTestUtils.setField(pagoService, "failureUrl", "http://localhost/failure");
        ReflectionTestUtils.setField(pagoService, "pendingUrl", "http://localhost/pending");
        ReflectionTestUtils.setField(pagoService, "notificationUrl", "http://localhost/webhook");
        ReflectionTestUtils.setField(pagoService, "webhookSecret", "secret");
    }

    @Test
    void testProcesarWebhookAprobado() {
        Pago pago = new Pago();
        pago.setId(1L);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setReferenciaExterna("payment-id-123");
        when(pagoRepository.findByReferenciaExterna("payment-id-123"))
                .thenReturn(Optional.of(pago));
        when(pagoRepository.save(any())).thenReturn(pago);

        Map<String, Object> payload = Map.of(
                "status", "approved",
                "data", Map.of("id", "payment-id-123")
        );

        pagoService.procesarWebhook(payload, null);

        verify(pagoRepository).findByReferenciaExterna("payment-id-123");
        verify(pagoRepository).save(pago);
    }

    @Test
    void testProcesarWebhookRechazado() {
        Pago pago = new Pago();
        pago.setId(2L);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setReferenciaExterna("payment-id-456");
        when(pagoRepository.findByReferenciaExterna("payment-id-456"))
                .thenReturn(Optional.of(pago));
        when(pagoRepository.save(any())).thenReturn(pago);

        Map<String, Object> payload = Map.of(
                "status", "rejected",
                "data", Map.of("id", "payment-id-456")
        );

        pagoService.procesarWebhook(payload, null);

        verify(pagoRepository).save(pago);
    }

    @Test
    void testProcesarWebhookNullPayload() {
        pagoService.procesarWebhook(null, null);
        verifyNoInteractions(pagoRepository);
    }

    @Test
    void testProcesarWebhookSinData() {
        Map<String, Object> payload = Map.of("status", "approved");
        pagoService.procesarWebhook(payload, null);
        verifyNoInteractions(pagoRepository);
    }
}
