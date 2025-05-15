package com.example.demo.Service;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.JTW.JwtUtils;
import com.example.demo.Repository.UserRepository;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public void register(UserDTO userDTO) {
        UserModel newUser = new UserModel();
        newUser.setNombre(userDTO.getNombre());
        newUser.setEmail(userDTO.getEmail());
        newUser.setTelefono(userDTO.getTelefono());
        newUser.setContraseña(passwordEncoder.encode(userDTO.getContraseña()));
        newUser.setNivel("nivel 0");
        newUser.setPuntosTotales(0);

        userRepository.save(newUser);
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
}
