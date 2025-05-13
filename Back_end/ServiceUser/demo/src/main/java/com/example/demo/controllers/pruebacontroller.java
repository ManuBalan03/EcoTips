package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class pruebacontroller {
    @GetMapping("/hola")
    public String hola(){
        return "prueba 1";
    }
}
