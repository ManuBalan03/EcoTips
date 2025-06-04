package com.example.demo.Service;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.JTW.JwtUtils;
import com.example.demo.Repository.UserRepository;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(UserDTO userDTO) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        UserModel newUser = new UserModel();
        newUser.setNombre(userDTO.getNombre());
        newUser.setEmail(userDTO.getEmail());
        newUser.setTelefono(userDTO.getTelefono());
        newUser.setFechaRegistro(LocalDateTime.now());
        newUser.setContrasenia(passwordEncoder.encode(userDTO.getContraseña()));
        newUser.setNivel("nivel 0");
        newUser.setPuntosTotales(0);

        userRepository.save(newUser);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    public UserModel obtenerUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }
}
