package com.example.demo.Service;
import java.util.List;
import java.util.Optional;

import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.models.UserModel;
import com.example.demo.DTO.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    UserModel crearUsuario(UserModel usuario);
    List<UserDTO> obtenerTodos();
    Optional<UserDTO> obtenerPorId(Long id);
    void eliminarUsuario(Long id);
    UserDTO sumarPuntosUsuario(Long idUsuario, UpdateUserDTO dto);
    Integer obtenerpuntos (Long id);
    UserDTO actualizarUsuario(Long idUsuario, UserDTO dto);
}
