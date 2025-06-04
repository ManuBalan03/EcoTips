package com.example.demo.Service.VotosService;

import com.example.demo.DTO.ReactionsDTO;
import com.example.demo.DTO.VotosDTO;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.ReactionsRepository;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.Enum.TipoReacciones;
import com.example.demo.models.PublicationsModel;
import com.example.demo.models.ReactionsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService implements  VotesService{

    private final ReactionsRepository ReactionRepository;
    private final PublicationRepository publicationsRepository;
    private UsuarioService usuarioService;

    public VotosDTO crearVoto(VotosDTO dto) {
        try {
            PublicationsModel publicacion = publicationsRepository.findById(dto.getIdPublicacion())
                    .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

            Optional<ReactionsModel> existente = ReactionRepository
                    .findByPublicacionIdPublicacionAndIdUsuario(dto.getIdPublicacion(), dto.getIdUsuario());

            ReactionsModel reaccion;

            if (existente.isPresent()) {
                reaccion = existente.get();
                reaccion.setTipo(tipo);
            } else {
                reaccion = ReactionsModel.builder()
                        .Tipo(tipo)
                        .publicacion(publicacion)
                        .idUsuario(dto.getIdUsuario())
                        .build();
            }

            reaccion.setFechaCreacion(LocalDateTime.now());
            ReactionsModel reaccionGuardada = ReactionRepository.save(reaccion);

            return new VotosDTO(
                    reaccionGuardada.getIdReaciones(),
                    reaccionGuardada.getPublicacion().getIdPublicacion(),
                    reaccionGuardada.getTipo().getValue(),
                    reaccionGuardada.getIdUsuario(),
                    reaccionGuardada.getFechaCreacion()
            );

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de reacción inválido: " + dto.getTipo() +
                    ". Tipos válidos: " + Arrays.toString(TipoReacciones.values()));
        }
    }
}
