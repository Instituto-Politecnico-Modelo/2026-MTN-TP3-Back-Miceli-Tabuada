package com.example.backEnd.Controller;

import com.example.backEnd.DTO.UsuarioRequestDTO;
import com.example.backEnd.DTO.UsuarioResponseDTO;
import com.example.backEnd.DTO.reserva.CancelacionAdminRequestDTO;
import com.example.backEnd.DTO.reserva.ReservaResponseDTO;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.ReservaRepository;
import com.example.backEnd.Repository.UsuarioRepository;
import com.example.backEnd.Service.ReservaService;
import com.example.backEnd.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ReservaRepository reservaRepository;
    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService,
                              ReservaRepository reservaRepository,
                              ReservaService reservaService,
                              UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.reservaRepository = reservaRepository;
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
    }

    // CU16 — Listar clientes (ADMINISTRADOR)
    @GetMapping("/admin/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<UsuarioResponseDTO>> listarClientes(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAllClientes(pageable));
    }

    @GetMapping("/admin/usuarios/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // CU17 — Historial de reservas de un cliente (ADMINISTRADOR)
    @GetMapping("/admin/usuarios/{id}/reservas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ReservaResponseDTO>> historialReservas(@PathVariable Long id) {
        return ResponseEntity.ok(
                reservaRepository.findByUsuarioIdOrderByFechaDesc(id).stream()
                        .map(reservaService::toDTO).toList());
    }

    // Cancelar reserva desde admin (CU17 extension)
    @PutMapping("/admin/reservas/{reservaId}/cancelar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> cancelarReservaAdmin(
            @PathVariable Long reservaId,
            @Valid @RequestBody CancelacionAdminRequestDTO dto,
            Authentication auth) {
        Long adminId = resolveUsuarioId(auth.getName());
        reservaService.cancelarReservaAdmin(reservaId, dto.getMotivo(), adminId);
        return ResponseEntity.noContent().build();
    }

    // Perfil propio (autenticado)
    @GetMapping("/usuarios/me")
    public ResponseEntity<UsuarioResponseDTO> miPerfil(Authentication auth) {
        Long id = resolveUsuarioId(auth.getName());
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @DeleteMapping("/admin/usuarios/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private Long resolveUsuarioId(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + email))
                .getId();
    }
}
