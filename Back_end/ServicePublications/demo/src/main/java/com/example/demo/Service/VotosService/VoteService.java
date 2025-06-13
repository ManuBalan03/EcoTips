package com.example.demo.Service.VotosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.VotosDTO;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.VoteRepository;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.ComentariosModel;
import com.example.demo.models.Enum.TipoVoto;
import com.example.demo.models.PublicationsModel;
import com.example.demo.models.VotosModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService implements VotesService{

    private final VoteRepository VoteRepository;
    private final PublicationRepository publicationsRepository;
    private final UsuarioService usuarioService;

    public VotosDTO crearVoto(VotosDTO dto) {
        try {

            TipoVoto tipo = TipoVoto.fromString(dto.getVoto());

            PublicationsModel publicacion = publicationsRepository.findById(dto.getIdPublicacion())
                    .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

            Optional<VotosModel> existente = VoteRepository
                    .findByPublicacionIdPublicacionAndIdUsuario(dto.getIdPublicacion(), dto.getIdUsuario());

            VotosModel votos;

            if (existente.isPresent()) {
                votos = existente.get();
                votos.setVoto(tipo);
            } else {
                votos = VotosModel.builder()
                        .comentario(dto.getComentario())
                        .voto(tipo)
                        .publicacion(publicacion)
                        .idUsuario(dto.getIdUsuario())
                        .build();
            }

            votos.setFechaVoto((LocalDateTime.now()));
            System.out.println("DTO recibido: " + dto.getVoto());

            VotosModel reaccionGuardada = VoteRepository.save(votos);

            return new VotosDTO(
                    reaccionGuardada.getIdVotos(),
                    reaccionGuardada.getComentario(),
                    reaccionGuardada.getVoto().getValue(),
                    reaccionGuardada.getFechaVoto(),
                    reaccionGuardada.getPublicacion().getIdPublicacion(),
                    reaccionGuardada.getIdUsuario()
            );

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de reacción inválido: " + dto.getVoto() +
                    ". Tipos válidos: " + Arrays.toString(TipoVoto.values()));
        }
    }

    public List<VotosDTO> listarVotosPorPublicacion(Long idPublicacion) {
        List<VotosModel> Votos = VoteRepository.findByPublicacionIdPublicacion(idPublicacion);

        return Votos.stream().map(Voto -> new VotosDTO(
                Voto.getIdVotos(),
                Voto.getComentario(),
                Voto.getVoto().name(),
                Voto.getFechaVoto(),
                Voto.getIdUsuario(),
                Voto.getPublicacion().getIdPublicacion(),
                usuarioService.obtenerNombrePorId(Voto.getIdUsuario())[0],//nombre
                usuarioService.obtenerNombrePorId(Voto.getIdUsuario())[1]//foto
        )).toList();
    }


}
