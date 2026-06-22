package com.example.backEnd;

import com.example.backEnd.Controller.ReservaController;
import com.example.backEnd.DTO.reserva.ReservaConPagoResponseDTO;
import com.example.backEnd.DTO.reserva.ReservaRequestDTO;
import com.example.backEnd.DTO.reserva.ReservaResponseDTO;
import com.example.backEnd.Entidad.Usuario;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Entidad.EstadoPago;
import com.example.backEnd.Entidad.EstadoReserva;
import com.example.backEnd.Repository.UsuarioRepository;
import com.example.backEnd.Security.JwtUtil;
import com.example.backEnd.Service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservaController.class)
@Import(TestSecurityConfig.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private ReservaResponseDTO buildReservaResponse() {
        ReservaResponseDTO r = new ReservaResponseDTO();
        r.setId(100L);
        r.setFecha(LocalDate.now().plusDays(1));
        r.setEstado(EstadoReserva.PENDIENTE);
        r.setTurnoId(10L);
        r.setUsuarioId(1L);
        return r;
    }

    @Test
    @WithMockUser(username = "juan@mail.com", roles = "CLIENTE")
    void testCrearReserva() throws Exception {
        Usuario usuario = new Usuario("12345678", "Juan", "Pérez", "juan@mail.com", "pass", "111", Rol.CLIENTE);
        usuario.setId(1L);
        when(usuarioRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(usuario));

        ReservaConPagoResponseDTO resp = new ReservaConPagoResponseDTO(
                buildReservaResponse(), "http://mp.com/pay", EstadoPago.PENDIENTE.name());

        when(reservaService.crearReserva(eq(10L), any(), eq(1L))).thenReturn(resp);

        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setTurnoId(10L);
        dto.setFecha(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "juan@mail.com", roles = "CLIENTE")
    void testMisReservas() throws Exception {
        Usuario usuario = new Usuario("12345678", "Juan", "Pérez", "juan@mail.com", "pass", "111", Rol.CLIENTE);
        usuario.setId(1L);
        when(usuarioRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(usuario));

        ReservaResponseDTO reservaResp = buildReservaResponse();
        reservaResp.setEstado(EstadoReserva.CONFIRMADA);
        when(reservaService.getMisReservas(1L)).thenReturn(List.of(reservaResp));

        mockMvc.perform(get("/api/reservas/mis-reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("CONFIRMADA"));
    }
}
