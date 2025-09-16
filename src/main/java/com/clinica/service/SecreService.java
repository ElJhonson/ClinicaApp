package com.clinica.service;

import com.clinica.dto.psicologo.PsicologoRequest;
import com.clinica.dto.psicologo.PsicologoResponse;
import com.clinica.mapper.PsicologoMapper;
import com.clinica.model.Psicologo;
import com.clinica.model.Rol;
import com.clinica.model.User;
import com.clinica.repository.PsicologoRepository;
import com.clinica.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.clinica.mapper.PsicologoMapper.toEntity;
import static com.clinica.mapper.PsicologoMapper.toResponse;

@Service
public class SecreService {

    private final UserRepository userRepo;
    private final PsicologoRepository psicologoRepo;
    private final BCryptPasswordEncoder encoder;
    private static PsicologoMapper psicologoMapper;

    public SecreService(UserRepository userRepo,
                        PsicologoRepository psicologoRepo) {
        this.userRepo = userRepo;
        this.psicologoRepo = psicologoRepo;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public PsicologoResponse registrarPsicologo(PsicologoRequest psicologoDto) {
        User user = new User();
        user.setEmail(psicologoDto.getEmail());
        user.setPassword(encoder.encode(psicologoDto.getPassword()));
        user.setFullName(psicologoDto.getNombre());
        user.setRol(Rol.PSICOLOGO);
        userRepo.save(user);

        Psicologo psicologo = toEntity(psicologoDto, user);
        psicologoRepo.save(psicologo);

        return toResponse(psicologo);
    }

}
