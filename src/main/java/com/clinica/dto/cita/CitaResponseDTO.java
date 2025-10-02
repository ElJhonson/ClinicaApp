package com.clinica.dto.cita;

import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CitaResponseDTO {
    private int idCitas;
    private LocalDate fecha;
    private Time hora;
    private String consultorio;
    private String tipo;
    private String observaciones;
    private String estado;

    private String psicologoNombre;
    private String pacienteNombre;
    private String secretariaNombre;
}
