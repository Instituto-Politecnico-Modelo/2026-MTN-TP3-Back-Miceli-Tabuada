package com.example.backEnd.config;

import com.example.backEnd.Entidad.Cancha;
import com.example.backEnd.Entidad.DiaSemana;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Entidad.TipoCancha;
import com.example.backEnd.Entidad.Turno;
import com.example.backEnd.Entidad.Usuario;
import com.example.backEnd.Repository.CanchaRepository;
import com.example.backEnd.Repository.TurnoRepository;
import com.example.backEnd.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CanchaRepository canchaRepository;
    private final TurnoRepository turnoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, CanchaRepository canchaRepository,
                      TurnoRepository turnoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.canchaRepository = canchaRepository;
        this.turnoRepository = turnoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@futya.com";
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Usuario admin = new Usuario(
                    "00000000",
                    "Admin",
                    "FutYa",
                    adminEmail,
                    passwordEncoder.encode("Admin1234!"),
                    null,
                    Rol.ADMINISTRADOR
            );
            usuarioRepository.save(admin);
            System.out.println(">> Admin por defecto creado: " + adminEmail);
        }

        List<Cancha> canchas;
        if (canchaRepository.count() == 0) {
            canchas = canchaRepository.saveAll(List.of(
                    new Cancha("Cancha 1", TipoCancha.FUTBOL_5, "Cancha principal techada", true),
                    new Cancha("Cancha 2", TipoCancha.FUTBOL_5, "Cancha auxiliar al aire libre", true),
                    new Cancha("Cancha 3", TipoCancha.FUTBOL_7, "Cancha grande con iluminacion", true)
            ));
            System.out.println(">> Canchas demo creadas: " + canchas.size());
        } else {
            canchas = canchaRepository.findAll();
        }

        if (turnoRepository.count() == 0) {
            List<DiaSemana> diasLaborables = List.of(
                    DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES,
                    DiaSemana.JUEVES, DiaSemana.VIERNES);
            List<LocalTime[]> bloques = List.of(
                    new LocalTime[]{LocalTime.of(18, 0), LocalTime.of(19, 0)},
                    new LocalTime[]{LocalTime.of(19, 0), LocalTime.of(20, 0)},
                    new LocalTime[]{LocalTime.of(20, 0), LocalTime.of(21, 0)}
            );
            List<Turno> turnos = new ArrayList<>();
            for (Cancha cancha : canchas) {
                for (DiaSemana dia : diasLaborables) {
                    for (LocalTime[] bloque : bloques) {
                        turnos.add(new Turno(dia, bloque[0], bloque[1], true, cancha));
                    }
                }
            }
            turnoRepository.saveAll(turnos);
            System.out.println(">> Turnos demo creados: " + turnos.size());
        }
    }
}
