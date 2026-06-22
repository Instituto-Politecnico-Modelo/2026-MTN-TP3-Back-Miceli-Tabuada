package com.example.backEnd;

import com.example.backEnd.DTO.reserva.ReservaConPagoResponseDTO;
import com.example.backEnd.DTO.pago.PagoIniciarResponseDTO;
import com.example.backEnd.Entidad.*;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.*;
import com.example.backEnd.Service.PagoService;
import com.example.backEnd.Service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(reservaService, "timeoutMinutos", 30);
    }

    private Cancha buildCancha() {
        Cancha c = new Cancha("Cancha A", TipoCancha.FUTBOL_5, "Desc", true);
        c.setId(1L);
        return c;
    }

    private Turno buildTurno() {
        Turno t = new Turno(DiaSemana.LUNES, java.time.LocalTime.of(18, 0),
                java.time.LocalTime.of(19, 0), true, buildCancha());
        t.setId(10L);
        return t;
    }

    private Usuario buildUsuario() {
        Usuario u = new Usuario("12345678", "Juan", "Pérez", "juan@mail.com",
                "pass", "1122334455", Rol.CLIENTE);
        u.setId(1L);
        return u;
    }

    private Reserva buildReserva() {
        Reserva r = new Reserva();
        r.setId(100L);
        r.setFecha(LocalDate.now().plusDays(2));
        r.setEstado(EstadoReserva.PENDIENTE);
        r.setTurno(buildTurno());
        r.setUsuario(buildUsuario());
        return r;
    }

    @Test
    void testCrearReservaOk() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        when(reservaRepository.findByTurnoIdAndFecha(eq(10L), eq(fecha))).thenReturn(Optional.empty());
        when(turnoRepository.findById(10L)).thenReturn(Optional.of(buildTurno()));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(buildUsuario()));
        Reserva saved = buildReserva();
        saved.setFecha(fecha);
        when(reservaRepository.save(any())).thenReturn(saved);
        when(pagoRepository.save(any())).thenReturn(new Pago());
        when(pagoService.iniciarPago(any())).thenReturn(
                new PagoIniciarResponseDTO("http://mp.com/pay", "pref-1"));

        ReservaConPagoResponseDTO result = reservaService.crearReserva(10L, fecha, 1L);

        assertNotNull(result);
        verify(reservaRepository).save(any());
        verify(pagoService).iniciarPago(any());
    }

    @Test
    void testCrearReservaTurnoOcupado() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        when(reservaRepository.findByTurnoIdAndFecha(eq(10L), eq(fecha)))
                .thenReturn(Optional.of(buildReserva()));

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.crearReserva(10L, fecha, 1L));
    }

    @Test
    void testCrearReservaTurnoNoDisponible() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        Turno turno = buildTurno();
        turno.setDisponible(false);
        when(reservaRepository.findByTurnoIdAndFecha(any(), any())).thenReturn(Optional.empty());
        when(turnoRepository.findById(10L)).thenReturn(Optional.of(turno));

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.crearReserva(10L, fecha, 1L));
    }

    @Test
    void testCrearReservaTurnoNoEncontrado() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        when(reservaRepository.findByTurnoIdAndFecha(any(), any())).thenReturn(Optional.empty());
        when(turnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reservaService.crearReserva(99L, fecha, 1L));
    }

    @Test
    void testCancelarReservaPermisoDenegado() {
        Reserva r = buildReserva();
        when(reservaRepository.findById(100L)).thenReturn(Optional.of(r));

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.cancelarReserva(100L, 99L));
    }
}
