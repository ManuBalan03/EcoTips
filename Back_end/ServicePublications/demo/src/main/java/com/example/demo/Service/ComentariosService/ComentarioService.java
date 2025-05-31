package com.example.demo.Service.ComentariosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Repository.ComentariosRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.ComentariosModel;
import com.example.demo.models.PublicationsModel;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ComentarioService implements  ComentariosService{

    private final ComentariosRepository comentariosRepository;
    private final PublicationRepository publicationsRepository;

    public ComentarioDTO crearComentario(ComentarioDTO dto) {
        // Buscar la publicación por ID
        PublicationsModel publicacion = publicationsRepository.findById(dto.getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + dto.getIdPublicacion()));

        // Crear el modelo
        ComentariosModel comentario = ComentariosModel.builder()
                .contenido(dto.getContenido())
                .fechaCreacion(LocalDateTime.now())
                .publicacion(publicacion)
                .idUsuario(dto.getIdUsuario())
                .build();

        // Guardar el comentario
        ComentariosModel comentarioGuardado = comentariosRepository.save(comentario);

        // Retornar DTO
        return new ComentarioDTO(
                comentarioGuardado.getIdcomentario(),
                comentarioGuardado.getPublicacion().getIdPublicacion(),
                comentarioGuardado.getContenido(),
                comentarioGuardado.getIdUsuario(),
                comentarioGuardado.getFechaCreacion()
        );
    }

    public List<ComentarioDTO> listarComentariosPorPublicacion(Long idPublicacion) {
        List<ComentariosModel> comentarios = comentariosRepository.findByPublicacionIdPublicacion(idPublicacion);
        return comentarios.stream().map(comentario -> new ComentarioDTO(
                comentario.getIdcomentario(),
                comentario.getPublicacion().getIdPublicacion(),
                comentario.getContenido(),
                comentario.getIdUsuario(),
                comentario.getFechaCreacion()
        )).toList();
    }


}
