package com.example.backEnd.Controller;

import com.example.backEnd.DTO.disponibilidad.DisponibilidadItemDTO;
import com.example.backEnd.Service.DisponibilidadService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    // CU4 — Grilla de disponibilidad por fecha
    @GetMapping
    public ResponseEntity<List<DisponibilidadItemDTO>> getGrilla(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) Long canchaId) {
        return ResponseEntity.ok(disponibilidadService.getGrilla(fecha, canchaId));
    }
}
