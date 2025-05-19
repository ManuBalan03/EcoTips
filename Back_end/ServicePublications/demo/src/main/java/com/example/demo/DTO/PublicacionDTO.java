package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PublicacionDTO {
    private Long id;
    private String contenido;
    private String titulo;
    private Long idUsuario;
    private String descripcion;
    private LocalDateTime fechaCreacion;
}
