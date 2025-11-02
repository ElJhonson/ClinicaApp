package com.clinica.dto.pago;

import com.clinica.model.Tipo;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PagoResponseDTO {
    private int idPagos;
    private LocalDateTime fecha;
    private double montoTotal;
    private double comisionClinica;
    private int penalizacion;
    private String motivo;
    private Tipo tipo;
    private String observaciones;
    private boolean aplicado;
    private Integer citaId;
}
