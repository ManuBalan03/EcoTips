package com.example.demo.controllers;

import com.example.demo.Service.PublicationsService;
import com.example.demo.JWT.JwtUltis;
import com.example.demo.DTO.PublicacionDTO;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationsService service;
    private final JwtUltis jwtUtils; // Necesario para extraer userId del token

    @PostMapping
    public ResponseEntity<PublicacionDTO> crear(@RequestHeader("Authorization") String authHeader,
                                                @RequestBody PublicacionDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        Long idUsuario = jwtUtils.getUserIdFromToken(token); // Método que debes implementar en JwtUtils
        dto.setIdUsuario(idUsuario);

        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }
    @GetMapping("/usuario")
    public ResponseEntity<List<PublicacionDTO>> listarPropias(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long idUsuario = jwtUtils.getUserIdFromToken(token);

        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

