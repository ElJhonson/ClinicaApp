package com.clinica.dto.secretaria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretariaRequest {
    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String turno;
}
