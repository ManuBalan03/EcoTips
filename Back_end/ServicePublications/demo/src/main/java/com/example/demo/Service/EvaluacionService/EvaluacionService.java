package com.example.demo.Service.EvaluacionService;


import com.example.demo.DTO.EvaluacionDTO;
import com.example.demo.DTO.PublicacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EvaluacionService {
    EvaluacionDTO crearEvaluacion(EvaluacionDTO dto);
    List<EvaluacionDTO> obtenerEvaluacionesPorPublicacion(Long idPublicacion);
    List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual);
    Page<PublicacionDTO> listarTodasPaginadas(String estado, Long idUsuarioActual, Pageable pageable);
    }
