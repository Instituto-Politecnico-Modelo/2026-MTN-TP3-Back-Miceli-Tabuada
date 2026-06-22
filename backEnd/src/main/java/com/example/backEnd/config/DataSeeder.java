package com.example.backEnd.config;

import com.example.backEnd.Entidad.Cancha;
import com.example.backEnd.Entidad.Rol;
import com.example.backEnd.Entidad.TipoCancha;
import com.example.backEnd.Entidad.Usuario;
import com.example.backEnd.Repository.CanchaRepository;
import com.example.backEnd.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CanchaRepository canchaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, CanchaRepository canchaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.canchaRepository = canchaRepository;
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

        if (canchaRepository.count() == 0) {
            List<Cancha> canchasDemo = List.of(
                    new Cancha("Cancha 1", TipoCancha.FUTBOL_5, "Cancha principal techada", true),
                    new Cancha("Cancha 2", TipoCancha.FUTBOL_5, "Cancha auxiliar al aire libre", true),
                    new Cancha("Cancha 3", TipoCancha.FUTBOL_7, "Cancha grande con iluminacion", true)
            );
            canchaRepository.saveAll(canchasDemo);
            System.out.println(">> Canchas demo creadas: " + canchasDemo.size());
        }

        // El seed de turnos de ejemplo se habilita en T013 cuando exista la entidad Turno.
    }
}
