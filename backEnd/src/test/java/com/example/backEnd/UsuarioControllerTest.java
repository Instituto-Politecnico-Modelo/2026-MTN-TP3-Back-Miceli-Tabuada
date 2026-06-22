package com.example.backEnd;

import com.example.backEnd.Controller.UsuarioController;
import com.example.backEnd.DTO.UsuarioResponseDTO;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Entidad.Usuario;
import com.example.backEnd.Repository.ReservaRepository;
import com.example.backEnd.Repository.UsuarioRepository;
import com.example.backEnd.Security.JwtUtil;
import com.example.backEnd.Service.ReservaService;
import com.example.backEnd.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@Import(TestSecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private ReservaRepository reservaRepository;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private JwtUtil jwtUtil;

    private UsuarioResponseDTO buildDTO() {
        return new UsuarioResponseDTO(1L, "12345678", "Juan", "Pérez",
                "juan@mail.com", "1122334455", Rol.CLIENTE, true,
                new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testListarClientes() throws Exception {
        when(usuarioService.findAllClientes(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(buildDTO())));

        mockMvc.perform(get("/api/admin/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("juan@mail.com"));
    }

    @Test
    @WithMockUser(username = "juan@mail.com", roles = "CLIENTE")
    void testMiPerfil() throws Exception {
        Usuario usuario = new Usuario("12345678", "Juan", "Pérez", "juan@mail.com",
                "pass", "111", Rol.CLIENTE);
        usuario.setId(1L);
        when(usuarioRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(usuario));
        when(usuarioService.obtenerPorId(1L)).thenReturn(buildDTO());

        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@mail.com"));
    }
}
