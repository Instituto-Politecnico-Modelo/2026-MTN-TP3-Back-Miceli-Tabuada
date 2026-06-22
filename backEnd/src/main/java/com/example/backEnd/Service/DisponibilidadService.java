package com.example.backEnd.Service;

import com.example.backEnd.DTO.disponibilidad.DisponibilidadItemDTO;
import com.example.backEnd.Entidad.DiaSemana;
import com.example.backEnd.Entidad.Reserva;
import com.example.backEnd.Entidad.Turno;
import com.example.backEnd.Repository.ReservaRepository;
import com.example.backEnd.Repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    private final TurnoRepository turnoRepository;
    private final ReservaRepository reservaRepository;

    public DisponibilidadService(TurnoRepository turnoRepository,
                                  ReservaRepository reservaRepository) {
        this.turnoRepository = turnoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<DisponibilidadItemDTO> getGrilla(LocalDate fecha, Long canchaId) {
        DiaSemana diaSemana = toDiaSemana(fecha.getDayOfWeek());

        List<Reserva> reservasActivas = reservaRepository.findReservasActivasByFecha(fecha);
        Map<Long, Long> reservasByTurnoId = reservasActivas.stream()
                .collect(Collectors.toMap(r -> r.getTurno().getId(), Reserva::getId));

        List<Turno> turnos = canchaId != null
                ? turnoRepository.findByCanchaId(canchaId)
                : turnoRepository.findAll();

        return turnos.stream()
                .filter(t -> t.getDiaSemana() == diaSemana && t.isDisponible())
                .map(t -> {
                    DisponibilidadItemDTO dto = new DisponibilidadItemDTO();
                    dto.setTurnoId(t.getId());
                    dto.setCanchaId(t.getCancha().getId());
                    dto.setCanchaNombre(t.getCancha().getNombre());
                    dto.setDiaSemana(t.getDiaSemana().name());
                    dto.setHoraInicio(t.getHoraInicio());
                    dto.setHoraFin(t.getHoraFin());
                    boolean reservado = reservasByTurnoId.containsKey(t.getId());
                    dto.setReservado(reservado);
                    if (reservado) dto.setReservaId(reservasByTurnoId.get(t.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private DiaSemana toDiaSemana(DayOfWeek dow) {
        return switch (dow) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }
}
