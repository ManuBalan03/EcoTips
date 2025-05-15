package com.example.demo.Service;
import com.example.demo.DTO.UserDTO;
import com.example.demo.models.UserModel;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Override
    public UserModel crearUsuario(UserModel usuario) {
        return repo.save(usuario);
    }

    @Override
    public List<UserDTO> obtenerTodos() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> obtenerPorId(Long id) {
        return repo.findById(id).map(this::mapToDTO);
    }

    @Override
    public void eliminarUsuario(Long id) {
        repo.deleteById(id);
    }

    private UserDTO mapToDTO(UserModel u) {
        return new UserDTO(
                u.getIdUsuario(),
                u.getNombre(),
                u.getEmail(),
                u.getTelefono(),
                u.getFotoPerfil(),
                u.getNivel(),
                u.getPuntosTotales(),
                u.getContraseña()
        );
    }
}
