package com.example.demo.Service.PublicacionService;

import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Service.UsuarioService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.models.PublicationsModel;
import com.example.demo.DTO.PublicacionDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicationService implements PublicationsService {


    @Lazy
    @Autowired
    private UsuarioService usuarioService;

    private final PublicationRepository repo;


    @Autowired
    public PublicationService(PublicationRepository repo) {
        this.repo = repo;
    }
    @Override
    public PublicacionDTO crear(PublicacionDTO dto) {
        PublicationsModel entidad = mapToEntity(dto);
        entidad.setFechaCreacion(LocalDateTime.now());
        entidad.setEstado("PENDIENTE");
        PublicationsModel guardada = repo.save(entidad);
        PublicacionDTO resultado = mapToDTO(guardada);
        // Aquí inyectas el nombre del autor
        String nombreAutor = usuarioService.obtenerNombrePorId(resultado.getIdUsuario());
        resultado.setNombreAutor(nombreAutor);

        return resultado;
    }

    @Override
    public List<PublicacionDTO> listarTodas() {
        return repo.findAllByOrderByFechaCreacionDesc().stream()
                .map(pub -> {
                    PublicacionDTO dto = mapToDTO(pub);
                    String nombreAutor = usuarioService.obtenerNombrePorId(dto.getIdUsuario());
                    dto.setNombreAutor(nombreAutor);
                    return dto;
                })
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
        m.setDescripcion(dto.getDescripcion());
        m.setIdUsuario(dto.getIdUsuario());
        return m;
    }

}
