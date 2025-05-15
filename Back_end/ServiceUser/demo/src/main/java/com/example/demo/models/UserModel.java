package com.example.demo.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String telefono;

    private String contraseña; // Cifrada

    private String fotoPerfil;

    private Integer nivel; // 0, 1, 2

    private Integer puntosTotales;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime fechaRegistro;
}
