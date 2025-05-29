package com.example.demo.DTO;


import com.example.demo.models.PublicationsModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ComentarioDTO {
    private String contenido;
    private Long idcomentario;
    private Long idUsuario;
    private Long idPublicacion;
    private LocalDateTime fechaCreacion;
    private String nombreAutor;

    public ComentarioDTO(){}

    public ComentarioDTO(Long idcomentario, long idPublicacion, String contenido, Long idUsuario, LocalDateTime fechaCreacion) {
        this.idcomentario = idcomentario;
        this.idPublicacion= idPublicacion;
        this.contenido = contenido;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
    }
}
