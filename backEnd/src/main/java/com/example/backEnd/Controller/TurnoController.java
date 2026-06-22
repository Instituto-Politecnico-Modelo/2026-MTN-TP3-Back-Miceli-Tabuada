package com.example.backEnd.Controller;

import com.example.backEnd.DTO.turno.TurnoRequestDTO;
import com.example.backEnd.DTO.turno.TurnoResponseDTO;
import com.example.backEnd.Service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/canchas/{canchaId}/turnos")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // CU12 — Crear turno
    @PostMapping
    public ResponseEntity<TurnoResponseDTO> crear(
            @PathVariable Long canchaId,
            @Valid @RequestBody TurnoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoService.crear(canchaId, dto));
    }

    // CU13 — Listar turnos de una cancha
    @GetMapping
    public ResponseEntity<List<TurnoResponseDTO>> listar(@PathVariable Long canchaId) {
        return ResponseEntity.ok(turnoService.buscarPorCancha(canchaId));
    }

    // CU14 — Actualizar turno
    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponseDTO> actualizar(
            @PathVariable Long canchaId,
            @PathVariable Long id,
            @Valid @RequestBody TurnoRequestDTO dto) {
        return ResponseEntity.ok(turnoService.actualizar(id, dto));
    }

    // CU15 — Cambiar disponibilidad
    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<TurnoResponseDTO> cambiarDisponibilidad(
            @PathVariable Long canchaId,
            @PathVariable Long id,
            @RequestParam boolean disponible) {
        return ResponseEntity.ok(turnoService.cambiarDisponibilidad(id, disponible));
    }

    // Eliminar turno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long canchaId,
            @PathVariable Long id) {
        turnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
