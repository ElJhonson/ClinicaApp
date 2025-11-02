package com.clinica.service;

import com.clinica.dto.cita.CitaRequestDTO;
import com.clinica.dto.cita.CitaResponseDTO;
import com.clinica.exceptions.*;
import com.clinica.mapper.CitaMapper;
import com.clinica.model.*;
import com.clinica.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.clinica.mapper.CitaMapper.toResponse;

@Service
public class CitaService {

    private final PsicologoRepository psicologoRepo;
    private final PacienteRepository pacienteRepo;
    private final UserRepository userRepo;
    private final SecreRepository secreRepository;
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;

    public CitaService(PsicologoRepository psicologoRepo, PacienteRepository pacienteRepo, UserRepository userRepo,
                       SecreRepository secreRepository, CitaRepository citaRepository, PagoRepository pagoRepository) {
        this.psicologoRepo = psicologoRepo;
        this.pacienteRepo = pacienteRepo;
        this.userRepo = userRepo;
        this.secreRepository = secreRepository;
        this.citaRepository = citaRepository;
        this.pagoRepository = pagoRepository;
    }

    public CitaResponseDTO registrarCita(CitaRequestDTO dto) {
        Psicologo psicologo = psicologoRepo.findById(dto.getPsicologoId())
                .orElseThrow(() -> new PsicologoNotFoundException("Psicólogo no encontrado"));

        Paciente paciente = pacienteRepo.findById(dto.getPacienteId())
                .orElseThrow(() -> new PacienteNotFoundException("Paciente no encontrado"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        Secretaria secretaria = secreRepository.findByUser(user)
                .orElseThrow(() -> new SecretariaNotFoundException("Secretaria no encontrada"));

        Cita cita = CitaMapper.toEntity(dto, psicologo, paciente, secretaria);

        boolean citaExiste = citaRepository.existsByPacienteAndFecha(cita.getPaciente(), cita.getFecha());
        if (citaExiste) {
            throw new CitaDuplicadaException("El paciente ya tiene una cita registrada en esta fecha");
        }

        Cita citaGuardada = citaRepository.save(cita);
        return CitaMapper.toResponse(citaGuardada);
    }

    public List<CitaResponseDTO> obtenerCitas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(CitaMapper::toResponse).toList();
    }

    public CitaResponseDTO actCita(int id, CitaRequestDTO dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException("Cita no encontrada"));

        // 🚫 No se puede modificar una cita atendida o cancelada
        if (cita.getEstado() == Estado.ATENDIDA || cita.getEstado() == Estado.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede modificar una cita que ya fue atendida o cancelada");
        }

        Psicologo psicologo = psicologoRepo.findById(dto.getPsicologoId())
                .orElseThrow(() -> new PsicologoNotFoundException("Psicólogo no encontrado"));

        Paciente paciente = pacienteRepo.findById(dto.getPacienteId())
                .orElseThrow(() -> new PacienteNotFoundException("Paciente no encontrado"));

        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setConsultorio(dto.getConsultorio());
        cita.setTipo(dto.getTipo());
        cita.setObservaciones(dto.getObservaciones());
        cita.setPsicologo(psicologo);
        cita.setPaciente(paciente);

        citaRepository.save(cita);
        return toResponse(cita);
    }

    public CitaResponseDTO cambiarEstadoCita(int citaId, Estado nuevoEstado) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new CitaNotFoundException("Cita no encontrada"));

        // 🚫 Si ya fue atendida o cancelada, no se puede cambiar nuevamente
        if (cita.getEstado() == Estado.ATENDIDA || cita.getEstado() == Estado.CANCELADA || cita.getEstado() == Estado.NO_ASISTIO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede cambiar el estado de una cita ya atendida o cancelada");
        }

        // Guardamos el estado nuevo
        cita.setEstado(nuevoEstado);
        citaRepository.save(cita);

        // Generar pago solo si la cita fue marcada como ATENDIDA o NO_ASISTIO
        if (nuevoEstado == Estado.ATENDIDA || nuevoEstado == Estado.NO_ASISTIO) {
            Pago pago = new Pago();
            pago.setCita(cita);
            pago.setFecha(LocalDateTime.now());
            pago.setAplicado(false);

            if (nuevoEstado == Estado.ATENDIDA) {
                // Monto base de la cita
                double montoBase = calcularMonto(cita);

                // Revisar penalización pendiente
                Optional<Pago> penalizacionOpt = pagoRepository.findFirstPenalizacionNoAplicada(
                        cita.getPaciente().getClave(),
                        TipoPago.PENALIZACION
                );

                int penalizacion = 0;
                if (penalizacionOpt.isPresent()) {
                    Pago penal = penalizacionOpt.get();
                    penalizacion = penal.getPenalizacion();
                    penal.setAplicado(true);
                    pagoRepository.save(penal);
                }

                pago.setMontoTotal(montoBase + penalizacion);
                pago.setComisionClinica(cita.getPsicologo().getComision() * montoBase / 100); // solo sobre monto de cita
                pago.setTipoPago(TipoPago.PAGO_NORMAL);
                pago.setMotivo("Cita atendida");
                pago.setObservaciones("Pago normal generado para cita atendida");
            }
            else if (nuevoEstado == Estado.NO_ASISTIO) {
                // Penalización
                int penal = 200; // ejemplo de penalización fija
                pago.setPenalizacion(penal);
                pago.setMontoTotal(penal);
                pago.setComisionClinica(0);
                pago.setTipoPago(TipoPago.PENALIZACION);
                pago.setMotivo("No asistencia");
                pago.setObservaciones("Penalización por no asistencia");
            }

            pagoRepository.save(pago);
        }

        return CitaMapper.toResponse(cita);
    }

    private double calcularMonto(Cita cita) {
        switch (cita.getTipo()) {
            case TipoCita.PRIMERA_VEZ:
                return 500;
            case TipoCita.SEGUIMIENTO:
                return 300;
            default:
                return 0;
        }
    }


}
