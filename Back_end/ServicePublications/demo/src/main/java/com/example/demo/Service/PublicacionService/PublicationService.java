package com.example.demo.Service.PublicacionService;

import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.VoteRepository;
import com.example.demo.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
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
//@RequiredArgsConstructor
public class PublicationService implements PublicationsService {


    @Lazy
    @Autowired
    private UsuarioService usuarioService;

    private final PublicationRepository repo;
    private final VoteRepository VoteRepository;


    @Autowired
    public PublicationService(PublicationRepository repo, VoteRepository voteRepository) {
        this.repo = repo;
        VoteRepository = voteRepository;
    }
    @Override
    public PublicacionDTO crear(PublicacionDTO dto) {
        PublicationsModel entidad = mapToEntity(dto);
        entidad.setFechaCreacion(LocalDateTime.now());
        entidad.setEstado("PENDIENTE");
        PublicationsModel guardada = repo.save(entidad);
        PublicacionDTO resultado = mapToDTO(guardada);
        // Aquí inyectas el nombre del
        String [] Datos= usuarioService.obtenerNombrePorId(resultado.getIdUsuario());
        String nombreAutor = Datos[0];
        String foto =Datos[1];
        resultado.setNombreAutor(nombreAutor);
        resultado.setFotoPerfil(foto);
        return resultado;
    }


//    @Override
//    public List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual) {
//
//        return repo.findByEstadoOrderByFechaCreacionDesc(estado).stream()
//                .map(pub -> {
//                    PublicacionDTO dto = mapToDTO(pub);
//                    String [] Datos= usuarioService.obtenerNombrePorId(dto.getIdUsuario());
//                    String nombreAutor = Datos[0];
//                    String foto =Datos[1];
//                    dto.setNombreAutor(nombreAutor);
//                    dto.setFotoPerfil(foto);
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }

    @Override
    public List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual) {

        List<PublicationsModel> publicaciones = repo.findByEstadoOrderByFechaCreacionDesc(estado);

        // Si hay un usuario actual, filtramos publicaciones que ya haya votado
        if (idUsuarioActual != null) {
            publicaciones = publicaciones.stream()
                    .filter(pub -> ! VoteRepository.existsByPublicacionIdPublicacionAndIdUsuario(
                            pub.getIdPublicacion(),
                            idUsuarioActual
                    ))
                    .collect(Collectors.toList());
        }

        // Convertimos a DTO
        return publicaciones.stream()
                .map(pub -> {
                    PublicacionDTO dto = mapToDTO(pub);
                    String[] datos = usuarioService.obtenerNombrePorId(dto.getIdUsuario());
                    dto.setNombreAutor(datos[0]);
                    dto.setFotoPerfil(datos[1]);
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
