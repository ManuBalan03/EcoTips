package com.example.demo.controllers;

import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.JWT.JwtUltis;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Service.VotosService.VotesService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationsService service;
    private final JwtUltis jwtUtils; // Necesario para extraer userId del token
    private final VotesService VotesService;

    @PostMapping
    public ResponseEntity<PublicacionDTO> crear(@RequestHeader("Authorization") String authHeader,
                                                @RequestBody PublicacionDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        Long idUsuario = jwtUtils.getUserIdFromToken(token);
        dto.setIdUsuario(idUsuario);


        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDTO>> listarTodas() {
        Long dato= null;
        return ResponseEntity.ok(service.listarTodas("APROBADA",null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> obtenerPublicacion(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPublicacionPorId(id));
    }


    @GetMapping("/pendiente")
    public ResponseEntity<List<PublicacionDTO>> listarTodaspendiente( HttpServletRequest request) {
        Long idUsuarioActual = obtenerIdUsuarioDeJWT(request);
        return ResponseEntity.ok(service.listarTodas("PENDIENTE", idUsuarioActual));
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

    private Long obtenerIdUsuarioDeJWT(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                // Usar una clave secreta válida (sin caracteres especiales)
                String secretKey = "ecoTipsClaveSuperSeguraYlarga20030409"; // Al menos 32 caracteres

                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey.getBytes()) // Convertir a bytes
                        .parseClaimsJws(token)
                        .getBody();

                return claims.get("userId", Long.class);

            } catch (Exception e) {
                throw new RuntimeException("Error al decodificar el token: " + e.getMessage());
            }
        }
        throw new RuntimeException("Token no encontrado");
    }
}

