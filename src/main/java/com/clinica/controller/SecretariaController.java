package com.clinica.controller;

import com.clinica.dto.PsicologoRequest;
import com.clinica.model.Psicologo;
import com.clinica.service.SecreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secretaria")
public class SecretariaController {

    private final SecreService secreService;

    public SecretariaController(SecreService secreService) {
        this.secreService = secreService;
    }

    @PreAuthorize("hasRole('SECRETARIA')")
    @PostMapping("/registrar-psicologo")
    public ResponseEntity<Psicologo> registrarPsicologo(@RequestBody PsicologoRequest psicologoDto){
        return ResponseEntity.ok(secreService.registrarPsicologo(psicologoDto));
    }
}
