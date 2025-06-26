package com.example.demo.controllers;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.Service.NotificationsService.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
