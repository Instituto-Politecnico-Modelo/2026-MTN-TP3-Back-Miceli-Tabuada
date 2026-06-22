package com.example.backEnd;

import com.example.backEnd.DTO.turno.TurnoRequestDTO;
import com.example.backEnd.DTO.turno.TurnoResponseDTO;
import com.example.backEnd.Entidad.Cancha;
import com.example.backEnd.Entidad.DiaSemana;
import com.example.backEnd.Entidad.TipoCancha;
import com.example.backEnd.Entidad.Turno;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.CanchaRepository;
import com.example.backEnd.Repository.TurnoRepository;
import com.example.backEnd.Service.TurnoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TurnoServiceTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private CanchaRepository canchaRepository;

    @InjectMocks
    private TurnoService turnoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Cancha buildCancha() {
        Cancha c = new Cancha("Cancha A", TipoCancha.FUTBOL_5, "Desc", true);
        c.setId(1L);
        return c;
    }

    private Turno buildTurno() {
        Cancha c = buildCancha();
        Turno t = new Turno(DiaSemana.LUNES, LocalTime.of(18, 0), LocalTime.of(19, 0), true, c);
        t.setId(10L);
        return t;
    }

    private TurnoRequestDTO buildDTO() {
        TurnoRequestDTO dto = new TurnoRequestDTO();
        dto.setDiaSemana(DiaSemana.LUNES);
        dto.setHoraInicio(LocalTime.of(18, 0));
        dto.setHoraFin(LocalTime.of(19, 0));
        dto.setDisponible(true);
        return dto;
    }

    @Test
    void testCrear() {
        when(canchaRepository.findById(1L)).thenReturn(Optional.of(buildCancha()));
        when(turnoRepository.save(any())).thenReturn(buildTurno());

        TurnoResponseDTO result = turnoService.crear(1L, buildDTO());

        assertNotNull(result);
        assertEquals(DiaSemana.LUNES, result.getDiaSemana());
    }

    @Test
    void testCrearCanchaNoExiste() {
        when(canchaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> turnoService.crear(99L, buildDTO()));
    }

    @Test
    void testBuscarPorCancha() {
        when(turnoRepository.findByCanchaId(1L)).thenReturn(List.of(buildTurno()));

        List<TurnoResponseDTO> result = turnoService.buscarPorCancha(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testEliminarConReservasActivas() {
        when(turnoRepository.existsById(10L)).thenReturn(true);
        when(turnoRepository.existsReservaActivaByTurnoId(10L)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> turnoService.eliminar(10L));
    }

    @Test
    void testEliminarOk() {
        when(turnoRepository.existsById(10L)).thenReturn(true);
        when(turnoRepository.existsReservaActivaByTurnoId(10L)).thenReturn(false);
        doNothing().when(turnoRepository).deleteById(10L);

        assertDoesNotThrow(() -> turnoService.eliminar(10L));
        verify(turnoRepository).deleteById(10L);
    }

    @Test
    void testCambiarDisponibilidad() {
        Turno turno = buildTurno();
        when(turnoRepository.findById(10L)).thenReturn(Optional.of(turno));
        when(turnoRepository.save(any())).thenReturn(turno);

        TurnoResponseDTO result = turnoService.cambiarDisponibilidad(10L, false);

        assertNotNull(result);
        verify(turnoRepository).save(turno);
    }
}
