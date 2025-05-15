package com.example.demo.Service;
import java.util.List;
import java.util.Optional;
import com.example.demo.models.UserModel;
import com.example.demo.DTO.UserDTO;

public interface UserService {
    UserModel crearUsuario(UserModel usuario);
    List<UserDTO> obtenerTodos();
    Optional<UserDTO> obtenerPorId(Long id);
    void eliminarUsuario(Long id);
}
