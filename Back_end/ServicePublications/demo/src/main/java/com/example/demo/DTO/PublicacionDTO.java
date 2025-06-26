package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class PublicacionDTO {
    private Long id;
    private String contenido;
    private String titulo;
    private Long idUsuario;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String nombreAutor;
    private String fotoPerfil;
    private int puntos;


    public PublicacionDTO() {}

    // Constructor parcial si quieres
    public PublicacionDTO(Long id, String contenido, String titulo, Long idUsuario, String descripcion, LocalDateTime fechaCreacion, int puntos) {
        this.id = id;
        this.contenido = contenido;
        this.titulo = titulo;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.puntos= puntos;
    }
}
