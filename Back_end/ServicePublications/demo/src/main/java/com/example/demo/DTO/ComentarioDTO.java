package com.example.demo.DTO;


import com.example.demo.models.PublicationsModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ComentarioDTO {
    private Long idcomentario;       // Debe ser Long
    private Long idPublicacion;      // Debe ser Long
    private String contenido;        // String para el texto del comentario
    private Long idUsuario;          // Debe ser Long
    private LocalDateTime fechaCreacion;
    private String nombreAutor;

    // Constructores adicionales si los necesitas
    public ComentarioDTO() {}

    public ComentarioDTO(Long idcomentario, Long idPublicacion, String contenido,
                         Long idUsuario, LocalDateTime fechaCreacion) {
        this(idcomentario, idPublicacion, contenido, idUsuario, fechaCreacion, null);
    }

    public ComentarioDTO(Long idcomentario, Long idPublicacion, String contenido,
                         Long idUsuario, LocalDateTime fechaCreacion, String nombreAutor) {
        this.idcomentario = idcomentario;
        this.idPublicacion = idPublicacion;
        this.contenido = contenido;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
        this.nombreAutor = nombreAutor;
    }
}
