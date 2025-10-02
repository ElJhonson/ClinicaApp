package com.clinica.service;

import com.clinica.dto.cita.CitaRequestDTO;
import com.clinica.dto.cita.CitaResponseDTO;
import com.clinica.dto.paciente.PacienteRequest;
import com.clinica.dto.paciente.PacienteResponse;
import com.clinica.dto.psicologo.PsicologoRequest;
import com.clinica.dto.psicologo.PsicologoResponse;
import com.clinica.mapper.CitaMapper;
import com.clinica.mapper.PacienteMapper;
import com.clinica.mapper.PsicologoMapper;
import com.clinica.model.*;
import com.clinica.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.clinica.mapper.PsicologoMapper.toEntity;
import static com.clinica.mapper.PsicologoMapper.toResponse;
import static com.clinica.mapper.PacienteMapper.toEntity;
import static com.clinica.mapper.PacienteMapper.toResponse;

@Service
public class SecreService {

    private final UserRepository userRepo;
    private final PsicologoRepository psicologoRepo;
    private final PacienteRepository pacienteRepo;
    private final SecreRepository secreRepository;
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;

    private final BCryptPasswordEncoder encoder;

    public SecreService(UserRepository userRepo,
                        PsicologoRepository psicologoRepo,
                        PacienteRepository pacienteRepo, SecreRepository secreRepository, CitaRepository citaRepository, PagoRepository pagoRepository) {
        this.userRepo = userRepo;
        this.psicologoRepo = psicologoRepo;
        this.pacienteRepo = pacienteRepo;
        this.secreRepository = secreRepository;
        this.citaRepository = citaRepository;
        this.pagoRepository = pagoRepository;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    //PSICOLOGO
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

    //PACIENTE
    public PacienteResponse registrarPaciente(PacienteRequest pacienteDto) {

        Paciente entity = toEntity(pacienteDto);
        entity.setEstado(Estado.ACTIVO);
        Paciente saved = pacienteRepo.save(entity);


        return toResponse(saved);
    }

    //CITA
    public CitaResponseDTO registrarCita(CitaRequestDTO dto) {
        Psicologo psicologo = psicologoRepo.findById(dto.getIdPsicologo())
                .orElseThrow(() -> new RuntimeException("Psicólogo no encontrado"));

        Paciente paciente = pacienteRepo.findById(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Secretaria secretaria = secreRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Secretaria no encontrada"));

        Cita cita = CitaMapper.toEntity(dto, psicologo, paciente, secretaria);

        Cita citaGuardada = citaRepository.save(cita);

        return CitaMapper.toResponseDTO(citaGuardada);

    }

    public CitaResponseDTO cambiarEstadoCita(int citaId, Estado nuevoEstado) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstado(nuevoEstado);
        citaRepository.save(cita);

        // Si pasa a ATENDIDA, generar pago automáticamente
        if (nuevoEstado == Estado.ATENDIDA) {
            registrarPago(cita, 500);
        }


        return CitaMapper.toResponseDTO(cita);
    }


    private Pago registrarPago(Cita cita, int montoBase) {
        if (pagoRepository.existsByCita(cita)) {
            throw new IllegalStateException("Ya existe un pago registrado para esta cita");
        }

        int penalizacionPendiente = calcularPenalizacionPendiente(cita.getPaciente());

        Psicologo psicologo = cita.getPsicologo();
        int comision = (int) ((montoBase + penalizacionPendiente) * psicologo.getComision() / 100);

        Pago pago = new Pago();
        pago.setFecha(LocalDateTime.now());
        pago.setCantidad(montoBase);
        pago.setPenalizacion(penalizacionPendiente);
        pago.setMotivo("Consulta psicológica");
        pago.setTipo("EFECTIVO");
        pago.setObservaciones(penalizacionPendiente > 0 ? "Incluye penalización" : "");
        pago.setComision(comision);
        pago.setCita(cita);

        Pago pagoGuardado = pagoRepository.save(pago);

        // Actualizar la lista de pagos de la cita
        if (cita.getPagos() == null) {
            cita.setPagos(new ArrayList<>());
        }
        cita.getPagos().add(pagoGuardado);

        return pagoGuardado;
    }

    public int calcularPenalizacionPendiente(Paciente paciente) {
        int penalizacionTotal = 0;

        List<Cita> citas = paciente.getCitas();

        for (Cita cita : citas) {
            if (cita.getEstado() == Estado.NO_ASISTIO) {
                boolean yaPagado = cita.getPagos() != null && cita.getPagos().stream()
                        .anyMatch(p -> p.getPenalizacion() > 0);

                if (!yaPagado) {
                    penalizacionTotal += 50;
                }
            }
        }
        return penalizacionTotal;
    }

}
