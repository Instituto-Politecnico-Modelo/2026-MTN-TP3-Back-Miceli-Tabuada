package com.example.backEnd.Controller;

import com.example.backEnd.DTO.clima.ClimaResponseDTO;
import com.example.backEnd.Service.ClimaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clima")
public class ClimaController {

    private final ClimaService climaService;

    public ClimaController(ClimaService climaService) {
        this.climaService = climaService;
    }

    @GetMapping
    public ResponseEntity<ClimaResponseDTO> getClima(
            @RequestParam String fecha,
            @RequestParam(defaultValue = "18:00:00") String hora) {
        return ResponseEntity.ok(climaService.getClima(fecha, hora));
    }
}
