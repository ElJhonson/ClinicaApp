package com.clinica.repository;

import com.clinica.model.Secretaria;
import com.clinica.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecreRepository extends JpaRepository<Secretaria, Long> {
    Optional<Secretaria> findByUser(User user);

}
