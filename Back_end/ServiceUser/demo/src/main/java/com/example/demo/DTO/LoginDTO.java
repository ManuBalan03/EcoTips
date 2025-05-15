package com.example.demo.DTO;

public class LoginDTO {
    private String email;      // o username, según tu modelo
    private String password;

    // Constructor vacío
    public LoginDTO() {}

    // Constructor con campos
    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
