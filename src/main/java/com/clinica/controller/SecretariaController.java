package com.clinica.controller;

import com.clinica.dto.cita.CitaRequestDTO;
import com.clinica.dto.cita.CitaResponseDTO;
import com.clinica.dto.paciente.PacienteRequest;
import com.clinica.dto.paciente.PacienteResponse;
import com.clinica.dto.psicologo.PsicologoRequest;
import com.clinica.dto.psicologo.PsicologoResponse;
import com.clinica.model.Estado;
import com.clinica.service.SecreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secretaria")
public class SecretariaController {

    private final SecreService secreService;

    public SecretariaController(SecreService secreService) {
        this.secreService = secreService;
    }

    @PreAuthorize("hasRole('SECRETARIA')")
    @PostMapping("/registrar-psicologo")
    public ResponseEntity<PsicologoResponse> registrarPsicologo(@RequestBody PsicologoRequest psicologoDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(secreService.registrarPsicologo(psicologoDto));
    }
    @PreAuthorize("hasRole('SECRETARIA')")
    @PostMapping("/registrar-paciente")
    public ResponseEntity<PacienteResponse> registrarPaciente(@RequestBody PacienteRequest pacienteDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(secreService.registrarPaciente(pacienteDto));
    }

    @PreAuthorize("hasRole('SECRETARIA')")
    @PostMapping("/registrar-cita")
    public ResponseEntity<CitaResponseDTO> registrarCita(@RequestBody CitaRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(secreService.registrarCita(dto));
    }

    @PreAuthorize("hasRole('SECRETARIA')")
    @PostMapping("/cambiar-estado-cita")
    public ResponseEntity<CitaResponseDTO> cambiarEstadoCita(@RequestParam int id, @RequestParam Estado estado){
        return ResponseEntity.status(HttpStatus.OK).body(secreService.cambiarEstadoCita(id, estado));
    }

}
