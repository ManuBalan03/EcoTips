package com.example.demo.Service;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.models.PublicationsModel;

import java.util.List;
import java.util.Optional;

public interface PublicationsService {
    PublicacionDTO crear(PublicacionDTO dto);

    List<PublicacionDTO> listarTodas();
    List<PublicacionDTO> listarPorUsuario(Long idUsuario);
    Optional<PublicacionDTO> obtenerPorId(Long id);
    void eliminar(Long id);
}
