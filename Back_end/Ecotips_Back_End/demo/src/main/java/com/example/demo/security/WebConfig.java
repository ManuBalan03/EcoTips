package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir solicitudes de cualquier origen
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Aquí defines el origen del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Los métodos que deseas permitir
                .allowedHeaders("*") // Permitir todos los encabezados
                .allowCredentials(true);
    }
}
