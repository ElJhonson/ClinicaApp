package com.clinica.repository;

import com.clinica.model.Cita;
import com.clinica.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
    Boolean existsByCita(Cita cita);
}
