package com.example.demo.Service;
import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.models.UserModel;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        UserModel usuario = obtenerid(idUsuario);

        int puntosASumar= dto.getPuntosTotales();
        int puntosActuales = usuario.getPuntosTotales();
        int puntostotales = puntosActuales+puntosASumar;

        if (puntostotales>=100){
            usuario= SubirNivel(usuario);
            puntostotales=puntostotales-100;
        }
        usuario.setPuntosTotales(puntostotales);
        UserModel actualizado = repo.save(usuario);

        return mapToDTO(actualizado);
    }


    public UserModel SubirNivel(UserModel usuario ){
        String nivel =usuario.getNivel();
        if (nivel.equals("nivel 0")){
            usuario.setNivel("nivel 1");
        }
        if (nivel.equals("nivel 1")){
            usuario.setNivel("nivel 2");
        }
        return usuario;
    }

    public Integer obtenerpuntos (Long id){
        Optional<UserDTO> datos = obtenerPorId(id);
        return datos.get().getPuntosTotales();
    }

    public UserDTO actualizarUsuario(Long idUsuario, UserDTO dto) {
        UserModel usuario = obtenerid(idUsuario);

        updateIfNotEmpty(dto.getNivel(), usuario::setNivel, usuario.getNivel());
        updateIfNotEmpty(dto.getEmail(), usuario::setEmail, usuario.getEmail());
        updateIfNotEmpty(dto.getContraseña(), usuario::setContrasenia, usuario.getContrasenia());
        updateIfNotEmpty(dto.getNombre(), usuario::setNombre, usuario.getNombre());
        updateIfNotEmpty(dto.getTelefono(), usuario::setTelefono, usuario.getTelefono());
        updateIfNotEmpty(dto.getFotoPerfil(), usuario::setFotoPerfil, usuario.getFotoPerfil());

        if (dto.getPuntosTotales() != null) {
            usuario.setPuntosTotales(dto.getPuntosTotales());
        }

        UserModel actualizado = repo.save(usuario);
        return mapToDTO(actualizado);
    }


    public UserModel obtenerid(Long idUsuario){
        UserModel usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        return usuario;
    }

    private void updateIfNotEmpty(String newValue, Consumer<String> setter, String currentValue) {
        if (newValue != null && !newValue.trim().isEmpty()) {
            setter.accept(newValue);
        }
    }


}
