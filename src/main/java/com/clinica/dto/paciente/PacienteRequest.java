package com.clinica.dto.paciente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {
    private String clave;
    private String nombre;
    private LocalDate fechaNac;
    private String sexo;
    private String telefono;
    private String contacto;
    private String parentesco;
    private String telefonoCp;
}

