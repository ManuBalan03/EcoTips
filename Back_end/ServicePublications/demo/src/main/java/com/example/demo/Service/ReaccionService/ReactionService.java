package com.example.demo.Service.ReaccionService;

import com.example.demo.DTO.ReactionsDTO;
import com.example.demo.Repository.ComentariosRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.ReactionsRepository;
import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.models.PublicationsModel;
import com.example.demo.models.ReactionsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionService implements  ReactionsService{


    private final ReactionsRepository  ReactionRepository;
    private final PublicationRepository publicationsRepository;

    public ReactionsDTO crearReaccion(ReactionsDTO dto) {
        // Buscar la publicación por ID
        PublicationsModel publicacion = publicationsRepository.findById(dto.getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + dto.getIdPublicacion()));

        // Buscar si ya existe una reacción del usuario
        Optional<ReactionsModel> existente = ReactionRepository
                .findByPublicacionIdPublicacionAndIdUsuario(dto.getIdPublicacion(), dto.getIdUsuario());

        ReactionsModel reaccion;

        if (existente.isPresent()) {
            // Si ya existe, actualizamos la reacción
            reaccion = existente.get();
            reaccion.setTipo(dto.getTipo());
            reaccion.setFechaCreacion(LocalDateTime.now());
        } else {
            // Si no existe, la creamos
            reaccion = ReactionsModel.builder()
                    .Tipo(dto.getTipo())
                    .fechaCreacion(LocalDateTime.now())
                    .publicacion(publicacion)
                    .idUsuario(dto.getIdUsuario())
                    .build();
        }

        // Guardamos la reacción (creada o actualizada)
        ReactionsModel reaccionGuardada = ReactionRepository.save(reaccion);

        // Retornamos el DTO
        return new ReactionsDTO(
                reaccionGuardada.getIdReaciones(),
                reaccionGuardada.getPublicacion().getIdPublicacion(),
                reaccionGuardada.getTipo(),
                reaccionGuardada.getIdUsuario(),
                reaccionGuardada.getFechaCreacion()
        );
    }


    public List<ReactionsDTO> listarReaccionesPorPublicacion(Long idPublicacion) {
        List<ReactionsModel> Reacciones = ReactionRepository.findByPublicacionIdPublicacion(idPublicacion);
        return Reacciones.stream().map(Reaccion -> new ReactionsDTO(
                Reaccion.getIdReaciones(),
                Reaccion.getPublicacion().getIdPublicacion(),
                Reaccion.getTipo(),
                Reaccion.getIdUsuario(),
                Reaccion.getFechaCreacion()
        )).toList();
    }

    public Map<String, Long> contarReaccionesPorTipo(Long idPublicacion) {
        List<ReactionsModel> reacciones = ReactionRepository.findByPublicacionIdPublicacion(idPublicacion);
        return reacciones.stream()
                .collect(Collectors.groupingBy(ReactionsModel::getTipo, Collectors.counting()));
    }


}
