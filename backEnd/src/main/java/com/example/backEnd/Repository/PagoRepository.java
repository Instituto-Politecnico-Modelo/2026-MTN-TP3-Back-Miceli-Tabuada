package com.example.backEnd.Repository;

import com.example.backEnd.Entidad.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByReservaId(Long reservaId);

    Optional<Pago> findByReferenciaExterna(String referenciaExterna);

    Optional<Pago> findByPreferenceId(String preferenceId);
}
