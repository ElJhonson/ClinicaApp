package com.clinica.mapper;

import com.clinica.dto.secretaria.SecretariaRequest;
import com.clinica.dto.secretaria.SecretariaResponse;
import com.clinica.model.Secretaria;
import com.clinica.model.User;

import java.time.LocalDate;
import java.util.Date;

public class SecretariaMapper {

    public static Secretaria toEntity(SecretariaRequest dto, User user) {
        Secretaria secretaria = new Secretaria();
        secretaria.setTelefono(dto.getTelefono());
        secretaria.setEstado("ACTIVO");
        secretaria.setUser(user);
        secretaria.setFechaContratacion(LocalDate.now());
        secretaria.setTurno(dto.getTurno());
        return secretaria;
    }

    public static SecretariaResponse toResponse(Secretaria entity) {
        SecretariaResponse response = new SecretariaResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getUser().getFullName());
        response.setEmail(entity.getUser().getEmail());
        response.setTelefono(entity.getTelefono());
        response.setEstado(entity.getEstado());
        response.setTurno(entity.getTurno());
        return response;
    }
}
