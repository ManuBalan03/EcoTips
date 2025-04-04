package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class userscontroller {

        private List<usuariosexample> customers = new ArrayList<>(Arrays.asList(
                new usuariosexample(001L,"Juan","halo1222@gmail.com","uswe1"),
                new usuariosexample(002L,"Arturo","system@gmail.com","admin"),
                new usuariosexample(003L,"Abraham","truxxitos@gmail.com","admin2"),
                new usuariosexample(004L,"Yomara", "system@gmail.com","adds")
        ));

        @GetMapping()
        public ResponseEntity<List<usuariosexample>> getCustomers(){
            return ResponseEntity.ok(customers);
        }
}
