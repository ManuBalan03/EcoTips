package com.example.demo.controllers;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.Service.NotificationsService.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<NotificationsDTO> nuevaNotificacion(@RequestBody NotificationsDTO dto) {
            System.out.println("RECIBIDO: " + dto); // Para depurar
            notificacionService.crear(dto); // lógica para guardar o procesar la notificación
            return ResponseEntity.ok().build();
        }


    @GetMapping("/{id}")
    public ResponseEntity<List<NotificationsDTO>> obtenerNotificacionsPorUsuario(@PathVariable Long id) {
        List<NotificationsDTO> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(id);
        return ResponseEntity.ok(notificaciones);
    }


    @PatchMapping("/{idNotificacion}/leida")
    public ResponseEntity<Void> marcarComoLeida(
            @PathVariable Long idNotificacion) {

        notificacionService.marcarComoLeida(idNotificacion);
        return ResponseEntity.ok().build();
    }


}
