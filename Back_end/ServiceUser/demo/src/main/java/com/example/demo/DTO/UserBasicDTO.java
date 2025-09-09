package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicDTO {
    private Long id;
    private String nombre;
    private String email;
    private String fotoPerfil;
    private Integer puntosTotales;
    private String nivel;
    private String telefono;
}