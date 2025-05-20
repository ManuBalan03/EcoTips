package com.example.demo.controllers;

import com.example.demo.DTO.AuthResponse;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserBasicDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.JTW.JtwResponse;
import com.example.demo.Service.AuthService;
import com.example.demo.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class authcontroller {

    private final AuthService authService; // Servicio que maneja lógica de registro y login

    public authcontroller(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        authService.register(userDTO);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        UserModel user = authService.obtenerUsuarioPorEmail(loginDTO.getEmail());

        UserBasicDTO userDTO = new UserBasicDTO(
                user.getIdUsuario(),
                user.getNombre(),
                user.getEmail(),
                user.getFotoPerfil(),
                user.getPuntosTotales(),
                user.getNivel()
        );

        return ResponseEntity.ok(new AuthResponse(token, userDTO));
    }
}
