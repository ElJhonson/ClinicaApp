package com.clinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "psicologos")
public class Psicologo {

    @Id
    @Column(name = "idpsicologo")
    private Long idPsicologo;
    private String telefono;
    private String servicios;
    private double comision;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToMany(mappedBy = "psicologo")
    private List<Cita> citas;

    @OneToOne
    @MapsId
    private User user;

}
