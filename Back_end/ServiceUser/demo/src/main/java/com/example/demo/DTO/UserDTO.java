package com.example.demo.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private String fotoPerfil;
    private String nivel;
    private Integer puntosTotales;
    private String contraseña;
}
//json para registrar