package com.example.demo.controllers;


import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.EvaluacionDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Service.ComentariosService.ComentarioService;
import com.example.demo.Service.EvaluacionService.EvaluacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Evaluacion")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<EvaluacionDTO> crearComentario(@RequestBody EvaluacionDTO dto) {
        EvaluacionDTO creado = evaluacionService.crearEvaluacion(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDTO>> listarTodasEvaluacion(HttpServletRequest request) {
        return ResponseEntity.ok(evaluacionService.listarTodas("PENDIENTE", null));
    }

}
