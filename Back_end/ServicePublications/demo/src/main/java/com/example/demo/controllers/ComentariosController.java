package com.example.demo.controllers;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.Service.ComentariosService.ComentarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones/comentarios")
@RequiredArgsConstructor
public class ComentariosController {
    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioDTO> crearComentario(@RequestBody ComentarioDTO dto) {
        ComentarioDTO creado = comentarioService.crearComentario(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentarios(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorPublicacion(id));
    }
}
