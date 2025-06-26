package com.example.demo.Service;
import com.example.demo.DTO.UpdateUserDTO;
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
                u.getContrasenia()
        );
    }

    @Override
    public UserDTO sumarPuntosUsuario(Long idUsuario, UpdateUserDTO dto) {
        UserModel usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        System.out.println("entroo"+ dto.getPuntosTotales());
        int puntosASumar= dto.getPuntosTotales();
        int puntosActuales = usuario.getPuntosTotales();
        int puntostotales = puntosActuales+puntosASumar;

        if (puntostotales>=100){
            usuario= SubirNivel(usuario);
            puntostotales=puntostotales-100;
        }
        usuario.setPuntosTotales(puntostotales);
        System.out.println(usuario.getNivel());
        UserModel actualizado = repo.save(usuario);

        return mapToDTO(actualizado);
    }


    public UserModel SubirNivel(UserModel usuario ){
        String nivel =usuario.getNivel();
        System.out.println(usuario.getNivel());
        if (nivel.equals("nivel 0")){
            usuario.setNivel("nivel 1");
        }
        if (nivel.equals("nivel 1")){
            usuario.setNivel("nivel 2");
        }
        return usuario;
    }

}
