package com.example.demo.Service;

import com.example.demo.Repository.PublicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.models.PublicationsModel;
import com.example.demo.DTO.PublicacionDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService implements PublicationsService {

    private final PublicationRepository repo;

    @Override
    public PublicacionDTO crear(PublicacionDTO dto) {
        PublicationsModel entidad = mapToEntity(dto);
        entidad.setFechaCreacion(LocalDateTime.now());
        entidad.setEstado("PENDIENTE");
        PublicationsModel guardada = repo.save(entidad);
        return mapToDTO(guardada);
    }

    @Override
    public List<PublicacionDTO> listarTodas() {
        return repo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicacionDTO> listarPorUsuario(Long idUsuario) {
        return repo.findByIdUsuario(idUsuario).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PublicacionDTO> obtenerPorId(Long id) {
        return repo.findById(id).map(this::mapToDTO);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ✅ Métodos de conversión
    private PublicacionDTO mapToDTO(PublicationsModel model) {
        return new PublicacionDTO(
                model.getIdPublicacion(),
                model.getContenido(),
                model.getTitulo(),
                model.getIdUsuario(),
                model.getDescripcion(),
                model.getFechaCreacion()
        );
    }

    private PublicationsModel mapToEntity(PublicacionDTO dto) {
        PublicationsModel m = new PublicationsModel();
        m.setTitulo(dto.getTitulo());
        m.setContenido(dto.getContenido());
        m.setIdUsuario(dto.getIdUsuario());
        return m;
    }

}
