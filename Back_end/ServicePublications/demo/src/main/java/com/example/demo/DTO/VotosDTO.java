package com.example.demo.DTO;

import com.example.demo.models.Enum.TipoVoto;
import com.example.demo.models.PublicationsModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotosDTO {

    @JsonProperty("idVotos")
    private Long idVotos;

    @JsonProperty("Comentario")
    private String Comentario;

    @JsonProperty("Voto")
    private String voto;

    @JsonProperty("FechaVoto")
    private LocalDateTime fechaVoto;

    @JsonProperty("idUsuario")
    private Long idUsuario;

    @JsonProperty("idPublicacion")
    private Long idPublicacion;

}
