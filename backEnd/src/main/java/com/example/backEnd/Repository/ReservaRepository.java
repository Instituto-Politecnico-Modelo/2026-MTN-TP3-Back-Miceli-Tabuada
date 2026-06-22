package com.example.backEnd.Repository;

import com.example.backEnd.Entidad.EstadoReserva;
import com.example.backEnd.Entidad.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @EntityGraph(attributePaths = {"turno", "turno.cancha", "pago"})
    List<Reserva> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    Optional<Reserva> findByTurnoIdAndFecha(Long turnoId, LocalDate fecha);

    List<Reserva> findByEstadoAndFechaCreacionBefore(EstadoReserva estado, Timestamp fechaCreacion);

    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.turno t " +
           "JOIN FETCH t.cancha c " +
           "WHERE r.fecha = :fecha " +
           "AND r.estado IN ('PENDIENTE', 'CONFIRMADA')")
    List<Reserva> findReservasActivasByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT r FROM Reserva r " +
           "JOIN FETCH r.turno t " +
           "JOIN FETCH t.cancha c " +
           "LEFT JOIN FETCH r.pago " +
           "WHERE (:canchaId IS NULL OR c.id = :canchaId) " +
           "AND (:usuarioId IS NULL OR r.usuario.id = :usuarioId) " +
           "AND (:estado IS NULL OR r.estado = :estado)")
    Page<Reserva> findWithFilters(
            @Param("canchaId") Long canchaId,
            @Param("usuarioId") Long usuarioId,
            @Param("estado") EstadoReserva estado,
            Pageable pageable);
}
