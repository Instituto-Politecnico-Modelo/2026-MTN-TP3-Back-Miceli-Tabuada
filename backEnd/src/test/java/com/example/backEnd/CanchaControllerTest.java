package com.example.backEnd;

import com.example.backEnd.Controller.CanchaController;
import com.example.backEnd.DTO.cancha.CanchaRequestDTO;
import com.example.backEnd.DTO.cancha.CanchaResponseDTO;
import com.example.backEnd.Entidad.TipoCancha;
import com.example.backEnd.Security.JwtAuthFilter;
import com.example.backEnd.Security.JwtUtil;
import com.example.backEnd.Service.CanchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CanchaController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
class CanchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CanchaService canchaService;

    @MockBean
    private JwtUtil jwtUtil;

    private CanchaResponseDTO buildResponse() {
        return new CanchaResponseDTO(1L, "Cancha A", TipoCancha.FUTBOL_5, "Desc", true);
    }

    @Test
    void testCrear() throws Exception {
        CanchaRequestDTO dto = new CanchaRequestDTO();
        dto.setNombre("Cancha A");
        dto.setTipo(TipoCancha.FUTBOL_5);
        dto.setDescripcion("Desc");
        dto.setActiva(true);

        when(canchaService.crear(any())).thenReturn(buildResponse());

        mockMvc.perform(post("/api/admin/canchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Cancha A"));
    }

    @Test
    void testListar() throws Exception {
        when(canchaService.buscarTodas()).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/api/admin/canchas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("FUTBOL_5"));
    }

    @Test
    void testCambiarEstado() throws Exception {
        CanchaResponseDTO resp = buildResponse();
        when(canchaService.cambiarEstado(eq(1L), eq(false))).thenReturn(resp);

        mockMvc.perform(patch("/api/admin/canchas/1/estado")
                        .param("activa", "false"))
                .andExpect(status().isOk());
    }
}
