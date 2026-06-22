package com.example.backEnd.Controller;

import com.example.backEnd.DTO.cancha.CanchaRequestDTO;
import com.example.backEnd.DTO.cancha.CanchaResponseDTO;
import com.example.backEnd.Service.CanchaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/canchas")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class CanchaController {

    private final CanchaService canchaService;

    public CanchaController(CanchaService canchaService) {
        this.canchaService = canchaService;
    }

    // CU8 — Crear cancha
    @PostMapping
    public ResponseEntity<CanchaResponseDTO> crear(@Valid @RequestBody CanchaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(canchaService.crear(dto));
    }

    // CU9 — Listar canchas
    @GetMapping
    public ResponseEntity<List<CanchaResponseDTO>> listar() {
        return ResponseEntity.ok(canchaService.buscarTodas());
    }

    // CU10 — Actualizar cancha
    @PutMapping("/{id}")
    public ResponseEntity<CanchaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CanchaRequestDTO dto) {
        return ResponseEntity.ok(canchaService.actualizar(id, dto));
    }

    // CU11 — Cambiar estado (activar/desactivar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CanchaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activa) {
        return ResponseEntity.ok(canchaService.cambiarEstado(id, activa));
    }

    // Eliminar cancha
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        canchaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
