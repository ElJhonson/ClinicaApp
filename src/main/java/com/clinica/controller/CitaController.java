package com.clinica.controller;

import com.clinica.dto.cita.CitaRequestDTO;
import com.clinica.dto.cita.CitaResponseDTO;
import com.clinica.model.Cita;
import com.clinica.model.Estado;
import com.clinica.service.CitaService;
import com.clinica.service.FiltrosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secretaria/citas")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('SECRETARIA')")
public class CitaController {

    private final CitaService citaService;
    private final FiltrosService filtrosService;

    public CitaController(CitaService citaService, FiltrosService filtrosService) {
        this.citaService = citaService;
        this.filtrosService = filtrosService;
    }

    // === Registrar nueva cita ===
    @PostMapping("/registrar")
    public ResponseEntity<CitaResponseDTO> registrarCita(@RequestBody CitaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(citaService.registrarCita(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable int id) {
        return ResponseEntity.ok(citaService.findByIdWithPagos(id));
    }


    // === Actualizar cita existente ===
    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> actualizarCita(
            @PathVariable int id,
            @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.ok(citaService.actCita(id, dto));
    }

    // === Obtener todas las citas ===
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitas() {
        return ResponseEntity.ok(citaService.obtenerCitas());
    }

    // === Cambiar estado de cita (CANCELADA, ATENDIDA, NO_ASISTIO...) ===
    @PutMapping("/{id}/estado")
    public ResponseEntity<CitaResponseDTO> cambiarEstadoCita(
            @PathVariable int id,
            @RequestParam Estado estado) {
        return ResponseEntity.ok(citaService.cambiarEstadoCita(id, estado));
    }

    // === Filtros ===
    @GetMapping("/dia")
    public List<CitaResponseDTO> obtenerCitasPorDia(@RequestParam String fecha) {
        return filtrosService.obtenerCitasPorDia(fecha);
    }

    @GetMapping("/semana")
    public List<CitaResponseDTO> obtenerCitasPorSemana(
            @RequestParam String inicio,
            @RequestParam String fin) {
        return filtrosService.obtenerCitasPorSemana(inicio, fin);
    }

    @GetMapping("/mes")
    public List<CitaResponseDTO> obtenerCitasPorMes(
            @RequestParam int anio,
            @RequestParam int mes) {
        return filtrosService.obtenerCitasPorMes(anio, mes);
    }
}
