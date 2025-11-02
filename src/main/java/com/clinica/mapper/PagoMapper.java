package com.clinica.mapper;

import com.clinica.dto.pago.PagoResponseDTO;
import com.clinica.model.Pago;

public class PagoMapper {
    public static PagoResponseDTO toResponse(Pago pago) {
        if (pago == null) {
            return null;
        }

        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPagos(pago.getIdPagos());
        dto.setFecha(pago.getFecha());
        dto.setMontoTotal(pago.getMontoTotal());
        dto.setComisionClinica(pago.getComisionClinica());
        dto.setPenalizacion(pago.getPenalizacion());
        dto.setMotivo(pago.getMotivo());
        dto.setTipoPago(pago.getTipoPago());
        dto.setObservaciones(pago.getObservaciones());
        dto.setAplicado(pago.isAplicado());
        dto.setCitaId(
                pago.getCita() != null ? pago.getCita().getIdCitas() : null
        );

        return dto;
    }
}
