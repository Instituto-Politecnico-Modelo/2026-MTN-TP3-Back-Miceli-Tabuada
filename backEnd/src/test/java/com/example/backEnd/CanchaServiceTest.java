package com.example.backEnd;

import com.example.backEnd.DTO.cancha.CanchaRequestDTO;
import com.example.backEnd.DTO.cancha.CanchaResponseDTO;
import com.example.backEnd.Entidad.Cancha;
import com.example.backEnd.Entidad.TipoCancha;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.CanchaRepository;
import com.example.backEnd.Repository.ReservaRepository;
import com.example.backEnd.Service.CanchaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CanchaServiceTest {

    @Mock
    private CanchaRepository canchaRepository;

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private CanchaService canchaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Cancha buildCancha() {
        Cancha c = new Cancha("Cancha A", TipoCancha.FUTBOL_5, "Desc", true);
        c.setId(1L);
        return c;
    }

    private CanchaRequestDTO buildDTO() {
        CanchaRequestDTO dto = new CanchaRequestDTO();
        dto.setNombre("Cancha A");
        dto.setTipo(TipoCancha.FUTBOL_5);
        dto.setDescripcion("Desc");
        dto.setActiva(true);
        return dto;
    }

    @Test
    void testCrear() {
        when(canchaRepository.save(any())).thenReturn(buildCancha());

        CanchaResponseDTO result = canchaService.crear(buildDTO());

        assertNotNull(result);
        assertEquals("Cancha A", result.getNombre());
        verify(canchaRepository).save(any(Cancha.class));
    }

    @Test
    void testBuscarTodas() {
        when(canchaRepository.findAll()).thenReturn(List.of(buildCancha()));

        List<CanchaResponseDTO> result = canchaService.buscarTodas();

        assertEquals(1, result.size());
        assertEquals(TipoCancha.FUTBOL_5, result.get(0).getTipo());
    }

    @Test
    void testBuscarPorIdExiste() {
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(buildCancha()));

        CanchaResponseDTO result = canchaService.buscarPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testBuscarPorIdNoExiste() {
        when(canchaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> canchaService.buscarPorId(99L));
    }

    @Test
    void testActualizar() {
        Cancha cancha = buildCancha();
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(cancha));
        when(canchaRepository.save(any())).thenReturn(cancha);

        CanchaRequestDTO dto = buildDTO();
        dto.setNombre("Cancha B");
        CanchaResponseDTO result = canchaService.actualizar(1L, dto);

        assertNotNull(result);
        verify(canchaRepository).save(cancha);
    }

    @Test
    void testCambiarEstado() {
        Cancha cancha = buildCancha();
        cancha.setActiva(true);
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(cancha));
        when(canchaRepository.save(any())).thenReturn(cancha);

        CanchaResponseDTO result = canchaService.cambiarEstado(1L, false);

        assertNotNull(result);
        verify(canchaRepository).save(cancha);
    }
}
