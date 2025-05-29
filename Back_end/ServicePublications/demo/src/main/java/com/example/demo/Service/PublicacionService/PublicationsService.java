package com.example.demo.Service.PublicacionService;
import com.example.demo.DTO.PublicacionDTO;

import java.util.List;
import java.util.Optional;

public interface PublicationsService {
    PublicacionDTO crear(PublicacionDTO dto);

    List<PublicacionDTO> listarTodas();
    List<PublicacionDTO> listarPorUsuario(Long idUsuario);
    Optional<PublicacionDTO> obtenerPorId(Long id);
    void eliminar(Long id);
}
