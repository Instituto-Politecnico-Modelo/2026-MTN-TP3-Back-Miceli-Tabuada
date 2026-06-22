package com.example.backEnd.Service;

import com.example.backEnd.DTO.turno.TurnoRequestDTO;
import com.example.backEnd.DTO.turno.TurnoResponseDTO;
import com.example.backEnd.Entidad.Cancha;
import com.example.backEnd.Entidad.Turno;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.CanchaRepository;
import com.example.backEnd.Repository.TurnoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final CanchaRepository canchaRepository;

    public TurnoService(TurnoRepository turnoRepository,
                        CanchaRepository canchaRepository) {
        this.turnoRepository = turnoRepository;
        this.canchaRepository = canchaRepository;
    }

    public TurnoResponseDTO crear(Long canchaId, TurnoRequestDTO dto) {
        Cancha cancha = canchaRepository.findById(canchaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada: " + canchaId));
        Turno turno = new Turno(dto.getDiaSemana(), dto.getHoraInicio(),
                dto.getHoraFin(), dto.isDisponible(), cancha);
        return toDTO(turnoRepository.save(turno));
    }

    public List<TurnoResponseDTO> buscarPorCancha(Long canchaId) {
        return turnoRepository.findByCanchaId(canchaId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public TurnoResponseDTO buscarPorId(Long id) {
        return toDTO(turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado: " + id)));
    }

    public TurnoResponseDTO actualizar(Long id, TurnoRequestDTO dto) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado: " + id));
        if (turnoRepository.existsReservaActivaByTurnoId(id)) {
            throw new DataIntegrityViolationException(
                    "El turno tiene reservas activas y no puede modificarse.");
        }
        turno.setDiaSemana(dto.getDiaSemana());
        turno.setHoraInicio(dto.getHoraInicio());
        turno.setHoraFin(dto.getHoraFin());
        return toDTO(turnoRepository.save(turno));
    }

    public void eliminar(Long id) {
        if (!turnoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Turno no encontrado: " + id);
        }
        if (turnoRepository.existsReservaActivaByTurnoId(id)) {
            throw new DataIntegrityViolationException(
                    "El turno tiene reservas activas y no puede eliminarse.");
        }
        turnoRepository.deleteById(id);
    }

    public TurnoResponseDTO cambiarDisponibilidad(Long id, boolean disponible) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turno no encontrado: " + id));
        turno.setDisponible(disponible);
        return toDTO(turnoRepository.save(turno));
    }

    private TurnoResponseDTO toDTO(Turno t) {
        return new TurnoResponseDTO(
                t.getId(), t.getDiaSemana(), t.getHoraInicio(), t.getHoraFin(),
                t.isDisponible(), t.getCancha().getId(), t.getCancha().getNombre());
    }
}
