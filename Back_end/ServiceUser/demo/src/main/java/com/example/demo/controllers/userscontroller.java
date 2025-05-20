package com.example.demo.controllers;
import com.example.demo.DTO.UserDTO;
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
}
