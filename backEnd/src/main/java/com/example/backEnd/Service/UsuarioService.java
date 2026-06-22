package com.example.backEnd.Service;

import com.example.backEnd.DTO.UsuarioRequestDTO;
import com.example.backEnd.DTO.UsuarioResponseDTO;
import com.example.backEnd.DTO.auth.AuthResponseDTO;
import com.example.backEnd.DTO.auth.LoginRequestDTO;
import com.example.backEnd.DTO.auth.RegistroRequestDTO;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Entidad.Usuario;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.UsuarioRepository;
import com.example.backEnd.Security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // ── UserDetailsService ──────────────────────────────────────────────────
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }

    // ── Auth ─────────────────────────────────────────────────────────────────
    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI: " + dto.getDni());
        }
        Usuario usuario = new Usuario(
                dto.getDni(), dto.getNombre(), dto.getApellido(),
                dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                dto.getTelefono(), Rol.CLIENTE
        );
        return toDTO(usuarioRepository.save(usuario));
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name());
        return new AuthResponseDTO(token, usuario.getRol().name(), usuario.getId(), usuario.getEmail());
    }

    // ── CRUD usuario ─────────────────────────────────────────────────────────
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI: " + dto.getDni());
        }
        Rol rol = dto.getRol() != null ? dto.getRol() : Rol.CLIENTE;
        Usuario usuario = new Usuario(
                dto.getDni(), dto.getNombre(), dto.getApellido(),
                dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                dto.getTelefono(), rol
        );
        return toDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        return toDTO(usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id)));
    }

    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        }
        return toDTO(usuarioRepository.save(usuario));
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // ── Admin ─────────────────────────────────────────────────────────────────
    public Page<UsuarioResponseDTO> findAllClientes(Pageable pageable) {
        return usuarioRepository.findByRol(Rol.CLIENTE, pageable).map(this::toDTO);
    }

    public List<com.example.backEnd.DTO.reserva.ReservaResponseDTO> findReservasByUsuarioId(
            Long usuarioId, com.example.backEnd.Repository.ReservaRepository reservaRepository,
            com.example.backEnd.Service.ReservaService reservaService) {
        return reservaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId)
                .stream().map(reservaService::toDTO).collect(Collectors.toList());
    }

    // ── Mapeo ─────────────────────────────────────────────────────────────────
    public UsuarioResponseDTO toDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(), u.getDni(), u.getNombre(), u.getApellido(),
                u.getEmail(), u.getTelefono(), u.getRol(),
                u.isActivo(), u.getFechaRegistro()
        );
    }
}
