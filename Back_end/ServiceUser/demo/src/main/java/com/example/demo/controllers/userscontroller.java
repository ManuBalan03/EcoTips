package com.example.demo.controllers;
import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Service.NotificationsService.NotificacionService;
import com.example.demo.Service.UserService;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class userscontroller {


        private final NotificacionService notificacionService;
        private final UserService service;

        @PostMapping
        public ResponseEntity<UserModel> crearUsuario(@RequestBody UserModel usuario) {
                return ResponseEntity.ok(service.crearUsuario(usuario));
        }

        @GetMapping
        public ResponseEntity<List<UserDTO>> listarUsuarios() {
                return ResponseEntity.ok(service.obtenerTodos());
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserDTO> obtenerUsuario(@PathVariable Long id) {
                return service.obtenerPorId(id)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Long id) {
                service.eliminarUsuario(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<Void> actualizarpuntos(@PathVariable Long id , @RequestBody UpdateUserDTO dto) {
                System.out.println("id es "+id+ dto.getPuntosTotales());

                service.sumarPuntosUsuario(id,dto);
                return ResponseEntity.noContent().build();
        }
        @GetMapping("/puntos/{id}")
        public ResponseEntity<Integer> obtenerpuntos(@PathVariable Long id ) {
                service.obtenerpuntos(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("actualizar/{id}")
        public ResponseEntity<Void> actualizarUsuario(@PathVariable Long id , @RequestBody UserDTO dto) {
                service.actualizarUsuario(id,dto);
                return ResponseEntity.noContent().build();
        }



}
