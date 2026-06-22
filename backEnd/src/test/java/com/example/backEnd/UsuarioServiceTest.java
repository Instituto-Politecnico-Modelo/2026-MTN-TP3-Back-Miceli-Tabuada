package com.example.backEnd;

import com.example.backEnd.DTO.UsuarioRequestDTO;
import com.example.backEnd.DTO.UsuarioResponseDTO;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Entidad.Usuario;
import com.example.backEnd.Repository.UsuarioRepository;
import com.example.backEnd.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UsuarioRequestDTO buildDTO() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setDni("12345678");
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEmail("juan@mail.com");
        dto.setPassword("pass123");
        dto.setTelefono("1122334455");
        dto.setRol(Rol.CLIENTE);
        return dto;
    }

    private Usuario buildUsuario() {
        Usuario u = new Usuario("12345678", "Juan", "Pérez", "juan@mail.com", "encodedPass", "1122334455", Rol.CLIENTE);
        u.setId(1L);
        return u;
    }

    @Test
    void testCrearUsuario() {
        UsuarioRequestDTO dto = buildDTO();
        Usuario usuario = buildUsuario();

        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.crearUsuario(dto);

        assertNotNull(result);
        assertEquals("juan@mail.com", result.getEmail());
        assertEquals(Rol.CLIENTE, result.getRol());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearUsuarioEmailDuplicado() {
        UsuarioRequestDTO dto = buildDTO();
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.crearUsuario(dto));
    }

    @Test
    void testObtenerTodos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(buildUsuario()));

        List<UsuarioResponseDTO> result = usuarioService.obtenerTodos();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
    }

    @Test
    void testObtenerPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(buildUsuario()));

        UsuarioResponseDTO result = usuarioService.obtenerPorId(1L);

        assertEquals(1L, result.getId());
        assertEquals("Pérez", result.getApellido());
    }

    @Test
    void testObtenerPorIdNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.obtenerPorId(99L));
    }

    @Test
    void testActualizarUsuario() {
        Usuario usuario = buildUsuario();
        UsuarioRequestDTO dto = buildDTO();
        dto.setNombre("Carlos");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPass");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO result = usuarioService.actualizarUsuario(1L, dto);

        assertNotNull(result);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarUsuarioNoExiste() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> usuarioService.eliminarUsuario(99L));
    }
}
