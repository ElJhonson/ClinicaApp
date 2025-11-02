package com.clinica.service;

import com.clinica.dto.cita.CitaResponseDTO;
import com.clinica.mapper.CitaMapper;
import com.clinica.model.Cita;
import com.clinica.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FiltrosService {

    private final CitaRepository citaRepository;

    public FiltrosService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<CitaResponseDTO> obtenerCitasPorDia(String fechaStr) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        List<Cita> citas = citaRepository.findByFecha(fecha);
        return citas.stream().map(CitaMapper::toResponse).toList();
    }

    // 🔹 Citas de una semana (inicio y fin)
    public List<CitaResponseDTO> obtenerCitasPorSemana(String inicioStr, String finStr) {
        LocalDate inicio = LocalDate.parse(inicioStr);
        LocalDate fin = LocalDate.parse(finStr);
        List<Cita> citas = citaRepository.findByFechaBetween(inicio, fin);
        return citas.stream().map(CitaMapper::toResponse).toList();
    }

    // 🔹 Citas de un mes
    public List<CitaResponseDTO> obtenerCitasPorMes(int anio, int mes) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.plusMonths(1).minusDays(1); // último día del mes
        List<Cita> citas = citaRepository.findByFechaBetween(inicio, fin);
        return citas.stream().map(CitaMapper::toResponse).toList();
    }

}
