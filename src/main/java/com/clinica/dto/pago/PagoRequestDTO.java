package com.clinica.dto.pago;

import com.clinica.model.Tipo;
import lombok.Data;

@Data
public class PagoRequestDTO {
    private double montoTotal;
    private int penalizacion;
    private String motivo;
    private String observaciones;
    private Tipo tipo;
    private Integer citaId;
}
