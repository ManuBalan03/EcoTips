package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${usuarios.service.url}")
    private String usuariosServiceUrl;

    // Necesitamos acceder al contexto de seguridad para obtener el token
    @Autowired
    private HttpServletRequest request;

    public String[] obtenerNombrePorId(Long idUsuario) {
        String[] Datos = new String[3];
        try {
            // Crear HttpHeaders y añadir el token JWT del contexto actual
            HttpHeaders headers = new HttpHeaders();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                headers.set("Authorization", authHeader);
            }

            // Crear HttpEntity con los headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Usar exchange en lugar de getForObject para incluir los headers
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    usuariosServiceUrl + "/" + idUsuario,
                    HttpMethod.GET,
                    entity,
                    UserDTO.class
            );

            UserDTO usuario = response.getBody();

            Datos[0]=usuario != null ? usuario.getNombre() : "Desconocido";
            Datos[1]=usuario != null ? usuario.getFotoPerfil() : "";
            return Datos;

        } catch (Exception e) {
            e.printStackTrace(); // para ver detalle en consola
            Datos[0]="Desconocido";
            Datos[1]="";
            return Datos;
        }
    }
}