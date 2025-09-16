package com.clinica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "psicologos")
public class Psicologo {

    @Id
    @Column(name = "idespecialistas")
    private Long idPsicologo;
    @Column(name = "telefonoe")
    private String telefono;
//    private String servicios;
//    @Column(name = "nombre_usr")
//    private String nombreUsr;
//    @Column(name = "clave_acceso")
//    private String claveAcceso;
//    @Column(name = "estadoe")
//    private String estado;

//    @OneToMany(mappedBy = "especialista")
//    private List<Cita> citas;

    @OneToOne
    @MapsId
    private User user;

}
