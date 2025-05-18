package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class pruebacontroller {
    @GetMapping("/hola1")
    public String hola(){
        return "prueba  2 3q234q 321";
    }
}
