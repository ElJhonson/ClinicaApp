package com.clinica.repository;

import com.clinica.model.Secretaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecreRepository extends JpaRepository<Secretaria, Long> {
}
