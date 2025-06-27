package com.example.demo.Service.PublicacionService;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.VoteRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.UsuarioService;
import org.aspectj.weaver.patterns.IfPointcut;
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



    private final UsuarioService usuarioService;

    private final NotificationsService notificationsService;

    private final PublicationRepository repo;
    private final VoteRepository VoteRepository;


    @Autowired
    public PublicationService(UsuarioService usuarioService, NotificationsService notificationsService, PublicationRepository repo, VoteRepository voteRepository) {
        this.usuarioService = usuarioService;
        this.notificationsService = notificationsService;
        this.repo = repo;
        VoteRepository = voteRepository;
    }
    @Override
    public PublicacionDTO crear(PublicacionDTO dto) {
        PublicationsModel entidad = mapToEntity(dto);
        entidad.setFechaCreacion(LocalDateTime.now());
        entidad.setEstado("PENDIENTE");
        entidad.setPuntos(0);
        PublicationsModel guardada = repo.save(entidad);
        PublicacionDTO resultado = mapToDTO(guardada);
        // Aquí inyectas el nombre del
        String [] Datos= usuarioService.obtenerNombrePorId(resultado.getIdUsuario());
        String nombreAutor = Datos[0];
        String foto =Datos[1];
        resultado.setNombreAutor(nombreAutor);
        resultado.setFotoPerfil(foto);




        notificationsService.enviarNotificacion(
                new NotificationsDTO(
                        "Solicitud Publicacion", "Tu publicacion esta en espera de alta",
                        LocalDateTime.now(),
                        resultado.getIdUsuario()
                )
        );
        return resultado;
    }



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

    public PublicacionDTO mapToDTO(PublicationsModel model) {
        return new PublicacionDTO(
                model.getIdPublicacion(),
                model.getContenido(),
                model.getTitulo(),
                model.getIdUsuario(),
                model.getDescripcion(),
                model.getFechaCreacion(),
                model.getPuntos()
        );
    }

    public PublicationsModel mapToEntity(PublicacionDTO dto) {
        PublicationsModel m = new PublicationsModel();
        m.setTitulo(dto.getTitulo());
        m.setContenido(dto.getContenido());
        m.setDescripcion(dto.getDescripcion());
        m.setIdUsuario(dto.getIdUsuario());
        return m;
    }

    @Override
    public PublicacionDTO actualizarPublicacion(Long id, PublicacionDTO dto) {
        PublicationsModel existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));

        // Solo actualizamos campos permitidos (contenido, título, descripción)
        existente.setTitulo(dto.getTitulo());
        existente.setContenido(dto.getContenido());
        existente.setDescripcion(dto.getDescripcion());


        PublicationsModel actualizada = repo.save(existente);

        PublicacionDTO resultado = mapToDTO(actualizada);
        String[] datos = usuarioService.obtenerNombrePorId(resultado.getIdUsuario());
        resultado.setNombreAutor(datos[0]);
        resultado.setFotoPerfil(datos[1]);

        return resultado;
    }

    @Override
    public PublicacionDTO actualizarEstadoPublicacion(Long id, String nuevoEstado) {
        PublicationsModel publicacion = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));

        publicacion.setEstado(nuevoEstado);
        if (nuevoEstado.equals("APROBADA")){
            publicacion.setPuntos(10);
            usuarioService.actualizarUsuarioRemoto(publicacion.getIdUsuario(),
                    UpdateUserDTO.builder()
                            .puntosTotales(10)
                            .build());
        }


        PublicationsModel actualizada = repo.save(publicacion);

        PublicacionDTO resultado = mapToDTO(actualizada);
        String[] datos = usuarioService.obtenerNombrePorId(resultado.getIdUsuario());
        resultado.setNombreAutor(datos[0]);
        resultado.setFotoPerfil(datos[1]);

        return resultado;
    }

}
