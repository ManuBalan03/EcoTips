package com.example.demo.DTO;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ReactionsDTO {
    private String Tipo;
    private Long idReaccion;
    private Long idUsuario;
    private Long idPublicacion;
    private LocalDateTime fechaCreacion;
    private String nombreAutor;

    public ReactionsDTO(){}

    public ReactionsDTO(Long idReaccion, long idPublicacion, String Tipo, Long idUsuario, LocalDateTime fechaCreacion) {
        this.idReaccion = idReaccion;
        this.idPublicacion= idPublicacion;
        this.Tipo = Tipo;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
    }
}
