package com.clinica.controller;

import com.clinica.dto.secretaria.SecretariaRequest;
import com.clinica.dto.secretaria.SecretariaResponse;
import com.clinica.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/registrar-secretaria")
    public ResponseEntity<SecretariaResponse> registrarSecretaria(@RequestBody SecretariaRequest secretariaDto){
        return ResponseEntity.ok(adminService.registrarSecretaria(secretariaDto));
    }
}
