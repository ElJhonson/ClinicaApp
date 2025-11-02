package com.clinica.dto.cita;

import com.clinica.model.Estado;
import com.clinica.model.TipoCita;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequestDTO {
    private LocalDate fecha;
    private LocalTime hora;
    private String consultorio;
    private TipoCita tipo;
    private String observaciones;
    private Long psicologoId;
    private String pacienteId;

    private boolean aplicarPenalizacionesPendientes = true;
    private Double pagoInicialEsperado;
}
