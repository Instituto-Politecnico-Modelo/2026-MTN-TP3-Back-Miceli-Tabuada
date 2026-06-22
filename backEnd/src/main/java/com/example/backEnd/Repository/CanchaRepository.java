package com.example.backEnd.Repository;

import com.example.backEnd.Entidad.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanchaRepository extends JpaRepository<Cancha, Long> {
    List<Cancha> findByActivaTrue();
    List<Cancha> findByActiva(boolean activa);
    boolean existsByNombreIgnoreCase(String nombre);
}

