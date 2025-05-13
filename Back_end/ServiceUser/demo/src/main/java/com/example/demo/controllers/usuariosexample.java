package com.example.demo.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class usuariosexample {

        private Long id;
        private String nombre;
        private String email;
        private String rol;

         public usuariosexample(Long id ,String nombre, String email, String rol) {
             this.id = id;
             this.nombre = nombre;
             this.email = email;
             this.rol=rol;
    }
}
