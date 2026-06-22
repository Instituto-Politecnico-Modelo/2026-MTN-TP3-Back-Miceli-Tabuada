package com.example.backEnd.Controller;

import com.example.backEnd.DTO.reserva.CancelacionAdminRequestDTO;
import com.example.backEnd.DTO.reserva.ReservaConPagoResponseDTO;
import com.example.backEnd.DTO.reserva.ReservaRequestDTO;
import com.example.backEnd.DTO.reserva.ReservaResponseDTO;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.UsuarioRepository;
import com.example.backEnd.Service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;

    public ReservaController(ReservaService reservaService,
                              UsuarioRepository usuarioRepository) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
    }

    // CU5 — Crear reserva (CLIENTE)
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReservaConPagoResponseDTO> crear(
            @Valid @RequestBody ReservaRequestDTO dto,
            Authentication auth) {
        Long usuarioId = resolveId(auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.crearReserva(dto.getTurnoId(), dto.getFecha(), usuarioId));
    }

    // CU6 — Mis reservas (CLIENTE)
    @GetMapping("/mis-reservas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ReservaResponseDTO>> misReservas(Authentication auth) {
        Long usuarioId = resolveId(auth.getName());
        return ResponseEntity.ok(reservaService.getMisReservas(usuarioId));
    }

    // CU7 — Cancelar propia reserva (CLIENTE)
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, Authentication auth) {
        Long usuarioId = resolveId(auth.getName());
        reservaService.cancelarReserva(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    private Long resolveId(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"))
                .getId();
    }
}
