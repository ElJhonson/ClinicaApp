package com.clinica.dto.cita;

import com.clinica.model.Estado;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CitaRequestDTO {
    private LocalDate fecha;
    private Time hora;
    private String consultorio;
    private String tipo;
    private String observaciones;
    private Estado estado;
    private Long idPsicologo;
    private String pacienteId;
}
