package com.example.backEnd.Service;

import com.example.backEnd.DTO.cancha.CanchaRequestDTO;
import com.example.backEnd.DTO.cancha.CanchaResponseDTO;
import com.example.backEnd.Entidad.Cancha;
import com.example.backEnd.Exception.ResourceNotFoundException;
import com.example.backEnd.Repository.CanchaRepository;
import com.example.backEnd.Repository.ReservaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanchaService {

    private final CanchaRepository canchaRepository;
    private final ReservaRepository reservaRepository;

    public CanchaService(CanchaRepository canchaRepository,
                         ReservaRepository reservaRepository) {
        this.canchaRepository = canchaRepository;
        this.reservaRepository = reservaRepository;
    }

    public CanchaResponseDTO crear(CanchaRequestDTO dto) {
        Cancha cancha = new Cancha(dto.getNombre(), dto.getTipo(),
                dto.getDescripcion(), dto.getActiva() != null ? dto.getActiva() : true);
        return toDTO(canchaRepository.save(cancha));
    }

    public List<CanchaResponseDTO> buscarTodas() {
        return canchaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CanchaResponseDTO buscarPorId(Long id) {
        return toDTO(canchaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada: " + id)));
    }

    public CanchaResponseDTO actualizar(Long id, CanchaRequestDTO dto) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada: " + id));
        cancha.setNombre(dto.getNombre());
        cancha.setTipo(dto.getTipo());
        cancha.setDescripcion(dto.getDescripcion());
        if (dto.getActiva() != null) {
            cancha.setActiva(dto.getActiva());
        }
        return toDTO(canchaRepository.save(cancha));
    }

    public void eliminar(Long id) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada: " + id));
        cancha.getTurnos().forEach(t -> {
            if (t.isDisponible()) {
                throw new DataIntegrityViolationException(
                        "La cancha tiene turnos activos. Deshabilite los turnos antes de eliminar.");
            }
        });
        canchaRepository.delete(cancha);
    }

    public CanchaResponseDTO cambiarEstado(Long id, boolean activa) {
        Cancha cancha = canchaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancha no encontrada: " + id));
        cancha.setActiva(activa);
        return toDTO(canchaRepository.save(cancha));
    }

    private CanchaResponseDTO toDTO(Cancha c) {
        return new CanchaResponseDTO(c.getId(), c.getNombre(), c.getTipo(),
                c.getDescripcion(), c.isActiva());
    }
}
