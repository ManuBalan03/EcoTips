package com.example.demo.Service.ComentariosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.models.ComentariosModel;

import java.util.List;

public interface ComentariosService {

    ComentariosModel guardarComentario(ComentarioDTO dto);

    List<ComentarioDTO> listarTodas();
}
