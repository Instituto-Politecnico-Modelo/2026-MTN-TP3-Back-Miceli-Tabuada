package com.example.backEnd.Service;

import com.example.backEnd.DTO.reserva.ReservaConPagoResponseDTO;
import com.example.backEnd.DTO.reserva.ReservaResponseDTO;
import com.example.backEnd.Entidad.*;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.PagoRepository;
import com.example.backEnd.Repository.ReservaRepository;
import com.example.backEnd.Repository.TurnoRepository;
import com.example.backEnd.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoRepository pagoRepository;
    private final PagoService pagoService;

    @Value("${reserva.pago.timeout-minutos}")
    private int timeoutMinutos;

    public ReservaService(ReservaRepository reservaRepository,
                          TurnoRepository turnoRepository,
                          UsuarioRepository usuarioRepository,
                          PagoRepository pagoRepository,
                          PagoService pagoService) {
        this.reservaRepository = reservaRepository;
        this.turnoRepository = turnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pagoRepository = pagoRepository;
        this.pagoService = pagoService;
    }

    @Transactional
    public ReservaConPagoResponseDTO crearReserva(Long turnoId, LocalDate fecha, Long usuarioId) {
        if (reservaRepository.findByTurnoIdAndFecha(turnoId, fecha).isPresent()) {
            throw new IllegalArgumentException("El turno ya está reservado para esa fecha.");
        }
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado: " + turnoId));
        if (!turno.isDisponible()) {
            throw new IllegalArgumentException("El turno no está disponible.");
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usuarioId));

        Reserva reserva = new Reserva();
        reserva.setFecha(fecha);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setTurno(turno);
        reserva.setUsuario(usuario);
        reserva = reservaRepository.save(reserva);

        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(new BigDecimal("2000.00"));
        pago.setMoneda("ARS");
        pago.setEstado(EstadoPago.PENDIENTE);
        pagoRepository.save(pago);

        var pagoResp = pagoService.iniciarPago(reserva.getId());
        return new ReservaConPagoResponseDTO(
                toDTO(reserva),
                pagoResp.getRedirectUrl(),
                EstadoPago.PENDIENTE.name()
        );
    }

    @Transactional
    public void cancelarReserva(Long reservaId, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada: " + reservaId));
        if (!reserva.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("No tenés permiso para cancelar esta reserva.");
        }
        if (reserva.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede cancelar una reserva pasada.");
        }
        boolean anticipacion24h = reserva.getFecha().atStartOfDay()
                .isAfter(LocalDateTime.now().plusHours(24));
        if (anticipacion24h) {
            pagoRepository.findByReservaId(reservaId).ifPresent(p -> {
                if (p.getEstado() == EstadoPago.APROBADO) {
                    pagoService.reembolsar(p.getId());
                }
            });
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void cancelarReservaAdmin(Long reservaId, String motivo, Long adminId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada: " + reservaId));
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin no encontrado: " + adminId));
        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setMotivoCancelacion(motivo);
        reserva.setCanceladoPorAdmin(true);
        reserva.setCanceladoPorUsuario(admin);
        reservaRepository.save(reserva);
    }

    public List<ReservaResponseDTO> getMisReservas(Long usuarioId) {
        return reservaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void liberarReservasPendientes() {
        Timestamp limite = Timestamp.valueOf(
                LocalDateTime.now().minusMinutes(timeoutMinutos));
        List<Reserva> expiradas = reservaRepository
                .findByEstadoAndFechaCreacionBefore(EstadoReserva.PENDIENTE, limite);
        expiradas.forEach(r -> {
            r.setEstado(EstadoReserva.CANCELADA);
            r.setMotivoCancelacion("Cancelada automáticamente por timeout de pago.");
        });
        if (!expiradas.isEmpty()) {
            reservaRepository.saveAll(expiradas);
        }
    }

    public ReservaResponseDTO toDTO(Reserva r) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(r.getId());
        dto.setFecha(r.getFecha());
        dto.setEstado(r.getEstado());
        dto.setMotivoCancelacion(r.getMotivoCancelacion());
        dto.setTurnoId(r.getTurno().getId());
        dto.setDiaSemana(r.getTurno().getDiaSemana().name());
        dto.setHoraInicio(r.getTurno().getHoraInicio().toString());
        dto.setHoraFin(r.getTurno().getHoraFin().toString());
        dto.setCanchaId(r.getTurno().getCancha().getId());
        dto.setCanchaNombre(r.getTurno().getCancha().getNombre());
        dto.setUsuarioId(r.getUsuario().getId());
        return dto;
    }
}
