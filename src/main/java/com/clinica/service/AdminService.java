package com.clinica.service;

import com.clinica.dto.secretaria.SecretariaRequest;
import com.clinica.dto.secretaria.SecretariaResponse;
import com.clinica.mapper.SecretariaMapper;
import com.clinica.model.Rol;
import com.clinica.model.Secretaria;
import com.clinica.model.User;
import com.clinica.repository.SecreRepository;
import com.clinica.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.clinica.mapper.SecretariaMapper.*;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final SecreRepository secreRepository;
    private  final BCryptPasswordEncoder encoder;

    public AdminService(UserRepository userRepository,
                        SecreRepository secreRepository) {
        this.userRepository = userRepository;
        this.secreRepository = secreRepository;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public SecretariaResponse registrarSecretaria(SecretariaRequest secretariaDto) {
        User user = new User();
        user.setFullName(secretariaDto.getNombre());
        user.setPassword(encoder.encode(secretariaDto.getPassword()));
        user.setRol(Rol.SECRETARIA);
        user.setEmail(secretariaDto.getEmail());
        userRepository.save(user);

        Secretaria secretaria = toEntity(secretariaDto, user);
        secreRepository.save(secretaria);

        return toResponse(secretaria);
    }
}
