package com.clinica.service;

import com.clinica.dto.paciente.PacienteRequest;
import com.clinica.dto.paciente.PacienteResponse;
import com.clinica.exceptions.PacienteNotFoundException;
import com.clinica.mapper.PacienteMapper;
import com.clinica.model.Estado;
import com.clinica.model.Paciente;
import com.clinica.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.clinica.mapper.PacienteMapper.toEntity;
import static com.clinica.mapper.PacienteMapper.toResponse;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepo;

    public PacienteService(PacienteRepository pacienteRepo) {
        this.pacienteRepo = pacienteRepo;
    }

    public PacienteResponse registrarPaciente(PacienteRequest pacienteDto) {
        Paciente entity = toEntity(pacienteDto);
        entity.setEstado(Estado.ACTIVO);
        Paciente saved = pacienteRepo.save(entity);
        return toResponse(saved);
    }

    @Transactional
    public PacienteResponse actPaciente(String clave, PacienteRequest dto) {
        Paciente paciente = pacienteRepo.findById(clave)
                .orElseThrow(()-> new PacienteNotFoundException("Paciente no encontrado"));

        paciente.setNombre(dto.getNombre());
        paciente.setFechaNac(dto.getFechaNac());
        paciente.setSexo(dto.getSexo());
        paciente.setTelefono(dto.getTelefono());
        paciente.setContacto(dto.getContacto());
        paciente.setParentesco(dto.getParentesco());
        paciente.setTelefonoCp(dto.getTelefonoCp());

        pacienteRepo.save(paciente);
        return toResponse(paciente);
    }

    public List<PacienteResponse> obtenerPacientes() {
        List<Paciente> pacientes = pacienteRepo.findAll();
        return pacientes.stream()
                .map(PacienteMapper::toResponse)
                .toList();
    }

}
