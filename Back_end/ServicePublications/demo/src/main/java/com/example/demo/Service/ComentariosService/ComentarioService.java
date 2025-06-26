package com.example.demo.Service.ComentariosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Repository.ComentariosRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.ComentariosModel;
import com.example.demo.models.PublicationsModel;
import jakarta.servlet.http.HttpServletRequest;
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

    @Lazy
    @Autowired
    private UsuarioService usuarioService;

    @Lazy
    @Autowired
    private NotificationsService notificationsService;

    private final ComentariosRepository comentariosRepository;
    private final PublicationRepository publicationsRepository;

    public ComentarioDTO crearComentario(ComentarioDTO dto) {
        PublicationsModel publicacion = publicationsRepository.findById(dto.getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + dto.getIdPublicacion()));

        ComentariosModel comentarioGuardado = comentariosRepository.save(
                ComentariosModel.builder()
                        .contenido(dto.getContenido())
                        .fechaCreacion(LocalDateTime.now())
                        .publicacion(publicacion)
                        .idUsuario(dto.getIdUsuario())
                        .build()
        );
        String [] Datos = usuarioService.obtenerNombrePorId(comentarioGuardado.getIdUsuario());//obtiene datos del usuario nombre y sun foto de perfil

        Long id= publicationsRepository.findUserIdByPublicationId(dto.getIdPublicacion());
        notificationsService.enviarNotificacion(
                new NotificationsDTO(
                        "Comentario",
                        Datos[0]+" Comento en tu Publicacion",
                        LocalDateTime.now(),
                        id
                )
        );

        return ComentarioDTO.builder()
                .idcomentario(comentarioGuardado.getIdcomentario())
                .idPublicacion(comentarioGuardado.getPublicacion().getIdPublicacion())
                .contenido(comentarioGuardado.getContenido())
                .idUsuario(comentarioGuardado.getIdUsuario())
                .fechaCreacion(comentarioGuardado.getFechaCreacion())
                .nombreAutor(Datos[0])
                .fotoPerfil(Datos[1])
                .build();
    }

    public List<ComentarioDTO> listarComentariosPorPublicacion(Long idPublicacion) {
        List<ComentariosModel> comentarios = comentariosRepository.findByPublicacionIdPublicacion(idPublicacion);

        return comentarios.stream().map(comentario -> new ComentarioDTO(
                comentario.getIdcomentario(),
                comentario.getPublicacion().getIdPublicacion(),
                comentario.getContenido(),
                comentario.getIdUsuario(),
                comentario.getFechaCreacion(),
                usuarioService.obtenerNombrePorId(comentario.getIdUsuario())[0],
                usuarioService.obtenerNombrePorId(comentario.getIdUsuario())[1]
        )).toList();
    }


}
