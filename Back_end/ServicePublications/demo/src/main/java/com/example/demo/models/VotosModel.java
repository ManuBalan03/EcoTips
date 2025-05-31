package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "Votos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVotos;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String Comentario;

    @Column(nullable = false)
    private LocalDateTime fechaVoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private PublicationsModel publicacion;

    private Long idUsuario;
}
