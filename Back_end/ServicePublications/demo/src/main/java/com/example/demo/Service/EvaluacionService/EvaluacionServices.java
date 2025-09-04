package com.example.demo.Service.EvaluacionService;

import com.example.demo.DTO.EvaluacionDTO;
import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Repository.EvaluacionRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.VoteRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.PublicacionService.PublicationService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.Service.VotosService.VotesService;
import com.example.demo.models.Enum.TipoVeredicto;
import com.example.demo.models.EvaluacionModel;
import com.example.demo.models.PublicationsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluacionServices implements EvaluacionService {
    private final PublicationRepository publicationsRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final UsuarioService usuarioService;
    private final NotificationsService notificationsService;
    private final PublicationService publicationService;
    private final VoteRepository VoteRepository;
    private final PublicationRepository publicationRepository;

    public EvaluacionDTO crearEvaluacion(EvaluacionDTO dto) {
        PublicationsModel publicacion = publicationsRepository.findById(dto.getIdpublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + dto.getIdpublicacion()));


        TipoVeredicto tipo = TipoVeredicto.fromString(dto.getVeredicto());


        EvaluacionModel EvaluacionGuardado = evaluacionRepository.save(
                EvaluacionModel.builder()
                        .veredicto(tipo)
                        .comentario_final(dto.getComentario_final())
                        .fecha_evaluacion(LocalDateTime.now())
                        .publicacion(publicacion)
                        .idUsuario(dto.getIdUsuario())
                        .build()
        );
        String [] Datos = usuarioService.obtenerNombrePorId(EvaluacionGuardado.getIdUsuario());//obtiene datos del usuario nombre y sun foto de perfil

        Long id= publicationsRepository.findUserIdByPublicationId(dto.getIdpublicacion());
        if (dto.getVeredicto()=="MODIFICACION"){
            notificationsService.enviarNotificacion(
                    new NotificationsDTO(
                            "PublicacionModificacion",
                            "Tu publicaion requiere Modificaciones",
                            LocalDateTime.now(),
                            id,
                            dto.getIdpublicacion()

                    )
            );
        }
        else {
            notificationsService.enviarNotificacion(
                    new NotificationsDTO(
                            "Publicacion_"+dto.getVeredicto(),
                            "Tu publicacion fue "+dto.getVeredicto(),
                            LocalDateTime.now(),
                            id,
                            dto.getIdpublicacion()
                    )
            );
        }

        publicationService.actualizarEstadoPublicacion(dto.getIdpublicacion(), dto.getVeredicto());

        return EvaluacionDTO.builder()
                .veredicto(dto.getVeredicto())
                .comentario_final(dto.getComentario_final())
                .fecha_evaluacion(dto.getFecha_evaluacion())
                .idpublicacion(dto.getIdpublicacion())
                .idUsuario(dto.getIdUsuario())
                .nombreAutor(Datos[0])
                .fotoPerfil(Datos[1])
                .build();
    }

    @Override
    public List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual) {
        // 1. Obtener publicaciones en estado PENDIENTE
        List<PublicationsModel> publicaciones = publicationRepository.findByEstadoOrderByFechaCreacionDesc(estado);

        // 2. Filtrar: que el usuario no haya votado y tengan más de 5 votos
        List<PublicationsModel> filtradas = publicaciones.stream()
                .filter(pub -> {

                    long cantidadVotos = VoteRepository.contarVotosPorPublicacion(pub.getIdPublicacion());
                    return cantidadVotos >= 3;
                })
                .collect(Collectors.toList());

        // 3. Convertimos a DTO
        return filtradas.stream()
                .map(pub -> {
                    PublicacionDTO dto = publicationService.mapToDTO(pub);
                    String[] datos = usuarioService.obtenerNombrePorId(dto.getIdUsuario());
                    dto.setNombreAutor(datos[0]);
                    dto.setFotoPerfil(datos[1]);
                    return dto;
                })
                .collect(Collectors.toList());
    }


}
