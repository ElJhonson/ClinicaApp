package com.clinica.config;

import com.clinica.model.Rol;
import com.clinica.model.User;
import com.clinica.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    private final BCryptPasswordEncoder encoder;

    public DataInitializer() {
        this.encoder = new BCryptPasswordEncoder(12);
    }

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User admin = new User();
                admin.setFullName("Administrador");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(encoder.encode("admin"));
                admin.setRol(Rol.ADMIN);
                userRepository.save(admin);
                System.out.println("✅ Usuario ADMIN creado");
            } else {
                System.out.println("ℹ️ Usuario ADMIN ya existe");
            }
        };
    }

}
