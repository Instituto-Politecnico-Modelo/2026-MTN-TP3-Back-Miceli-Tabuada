package com.example.backEnd;

import com.example.backEnd.Controller.TurnoController;
import com.example.backEnd.DTO.turno.TurnoRequestDTO;
import com.example.backEnd.DTO.turno.TurnoResponseDTO;
import com.example.backEnd.Entidad.DiaSemana;
import com.example.backEnd.Security.JwtUtil;
import com.example.backEnd.Service.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TurnoController.class)
@Import(TestSecurityConfig.class)
class TurnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TurnoService turnoService;

    @MockBean
    private JwtUtil jwtUtil;

    private TurnoResponseDTO buildResponse() {
        return new TurnoResponseDTO(10L, DiaSemana.LUNES, LocalTime.of(18, 0),
                LocalTime.of(19, 0), true, 1L, "Cancha A");
    }

    @Test
    void testCrear() throws Exception {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setDiaSemana(DiaSemana.LUNES);
        dto.setHoraInicio(LocalTime.of(18, 0));
        dto.setHoraFin(LocalTime.of(19, 0));

        when(turnoService.crear(eq(1L), any())).thenReturn(buildResponse());

        mockMvc.perform(post("/api/admin/canchas/1/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.diaSemana").value("LUNES"));
    }

    @Test
    void testListar() throws Exception {
        when(turnoService.buscarPorCancha(1L)).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/api/admin/canchas/1/turnos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].disponible").value(true));
    }

    @Test
    void testCambiarDisponibilidad() throws Exception {
        TurnoResponseDTO resp = buildResponse();
        when(turnoService.cambiarDisponibilidad(eq(10L), eq(false))).thenReturn(resp);

        mockMvc.perform(patch("/api/admin/canchas/1/turnos/10/disponibilidad")
                        .param("disponible", "false"))
                .andExpect(status().isOk());
    }
}
