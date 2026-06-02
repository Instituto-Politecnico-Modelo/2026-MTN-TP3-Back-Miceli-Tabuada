package com.example.backEnd.Repository;

import com.example.backEnd.Entidad.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}

