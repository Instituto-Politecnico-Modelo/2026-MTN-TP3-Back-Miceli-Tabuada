package com.example.backEnd;

import com.example.backEnd.Controller.AuthController;
import com.example.backEnd.DTO.UsuarioResponseDTO;
import com.example.backEnd.DTO.auth.AuthResponseDTO;
import com.example.backEnd.DTO.auth.LoginRequestDTO;
import com.example.backEnd.DTO.auth.RegistroRequestDTO;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Security.JwtUtil;
import com.example.backEnd.Service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testRegistro() throws Exception {
        RegistroRequestDTO dto = new RegistroRequestDTO();
        dto.setDni("12345678");
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEmail("juan@mail.com");
        dto.setPassword("pass123");
        dto.setTelefono("1122334455");

        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "12345678", "Juan", "Pérez",
                "juan@mail.com", "1122334455", Rol.CLIENTE, true, null);
        when(usuarioService.registrar(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("juan@mail.com");
        dto.setPassword("pass123");

        AuthResponseDTO response = new AuthResponseDTO("token-abc", "CLIENTE", 1L, "juan@mail.com");
        when(usuarioService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-abc"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent());
    }
}
