package com.example.demo.Service.ComentariosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.models.ComentariosModel;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ComentariosService {

    ComentarioDTO crearComentario(ComentarioDTO dto);

    List<ComentarioDTO> listarComentariosPorPublicacion(Long idPublicacion);
}
