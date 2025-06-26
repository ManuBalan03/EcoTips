package com.example.demo.Service.EvaluacionService;


import com.example.demo.DTO.EvaluacionDTO;
import com.example.demo.DTO.PublicacionDTO;

import java.util.List;

public interface EvaluacionService {
    EvaluacionDTO crearEvaluacion(EvaluacionDTO dto);

    List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual);
}
