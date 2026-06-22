package com.example.backEnd.Repository;

import com.example.backEnd.Entidad.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByCanchaId(Long canchaId);

    List<Turno> findByCanchaIdAndDisponibleTrue(Long canchaId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Reserva r " +
           "WHERE r.turno.id = :turnoId " +
           "AND r.estado IN ('PENDIENTE', 'CONFIRMADA') " +
           "AND r.fecha >= CURRENT_DATE")
    boolean existsReservaActivaByTurnoId(@Param("turnoId") Long turnoId);
}
