package com.example.demo.Service.PublicacionService;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.VoteRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.IfPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.example.demo.models.PublicationsModel;
import com.example.demo.DTO.PublicacionDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PublicationService implements PublicationsService {
    private final UsuarioService usuarioService;
    private final NotificationsService notificationsService;
    private final PublicationRepository repo;
    private final VoteRepository voteRepository;

        @Override
        @Transactional
        public PublicacionDTO crear(PublicacionDTO dto) {
            PublicationsModel entidad = mapToEntity(dto);
            entidad.setFechaCreacion(LocalDateTime.now());
            entidad.setEstado("PENDIENTE");
            entidad.setPuntos(0);

            PublicationsModel guardada = repo.save(entidad);
            PublicacionDTO resultado = mapToDTO(guardada);
            resultado = injectarDatosUsuario(resultado);


            // ✅ Notificación asíncrona para mejor performance
            enviarNotificacionAsync(resultado);

            return resultado;
    }
    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual) {
        List<PublicationsModel> publicaciones = repo.findByEstadoOrderByFechaCreacionDesc(estado);

        if (idUsuarioActual != null) {
            // ✅ Optimización: Consulta directa en lugar de stream filter
            publicaciones = filtrarPublicacionesNoVotadas(publicaciones, idUsuarioActual);
        }

        // ✅ Batch processing para obtener datos de usuario
        return mapearConDatosUsuario(publicaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PublicacionDTO> obtenerPorId(Long id) {
        return repo.findById(id)
                .map(this::mapToDTO)
                .map(this::injectarDatosUsuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Publicación no encontrada con ID: " + id);
        }
        repo.deleteById(id);
    }

        @Override
        @Transactional
    public PublicacionDTO actualizarPublicacion(Long id, PublicacionDTO dto) {
        PublicationsModel existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + id));

        boolean needsUpdate = false;
        needsUpdate = actualizarSiEsDiferente(dto.getTitulo(), existente.getTitulo(), existente::setTitulo) || needsUpdate;
        needsUpdate = actualizarSiEsDiferente(dto.getContenido(), existente.getContenido(), existente::setContenido) || needsUpdate;
        needsUpdate = actualizarSiEsDiferente(dto.getDescripcion(), existente.getDescripcion(), existente::setDescripcion) || needsUpdate;

        if (needsUpdate) {
            existente = repo.save(existente);
        }
        return injectarDatosUsuario(mapToDTO(existente));
    }

    @Override
    @Transactional
    public PublicacionDTO actualizarEstadoPublicacion(Long id, String nuevoEstado) {
        PublicationsModel publicacion = repo.findById(id).orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        publicacion.setEstado(nuevoEstado);

            if ("APROBADA".equals(nuevoEstado)) {
                publicacion.setPuntos(10);
                // ✅ Actualización asíncrona para mejor performance
                actualizarPuntosUsuarioAsync(publicacion.getIdUsuario(), 10);
            }

            PublicationsModel actualizada = repo.save(publicacion);
            return injectarDatosUsuario(mapToDTO(actualizada));
        }

    @Override
    @Transactional(readOnly = true)
    public PublicacionDTO obtenerPublicacionPorId(Long idPublicacion) {
        PublicationsModel publicacion = repo.findById(idPublicacion)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        return injectarDatosUsuario(mapToDTO(publicacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDTO> listarPorUsuario(Long idUsuario) {
        List<PublicationsModel> publicaciones = repo.findByIdUsuario(idUsuario);
        return mapearConDatosUsuario(publicaciones);
    }

    // ✅ Métodos optimizados de mapeo
    public PublicacionDTO mapToDTO(PublicationsModel model) {
        return new PublicacionDTO(
                model.getIdPublicacion(),
                model.getContenido(),
                model.getTitulo(),
                model.getIdUsuario(),
                model.getDescripcion(),
                model.getFechaCreacion(),
                model.getPuntos());
    }

    public PublicationsModel mapToEntity(PublicacionDTO dto) {
        PublicationsModel m = new PublicationsModel();
        m.setTitulo(dto.getTitulo());
        m.setContenido(dto.getContenido());
        m.setDescripcion(dto.getDescripcion());
        m.setIdUsuario(dto.getIdUsuario());
        return m;
    }

    // ✅ Métodos privados optimizados
    private PublicacionDTO injectarDatosUsuario(PublicacionDTO dto) {
        String[] datos = usuarioService.obtenerNombrePorId(dto.getIdUsuario());
        dto.setNombreAutor(datos[0]);
        dto.setFotoPerfil(datos[1]);
        return dto;
    }

    private List<PublicacionDTO> mapearConDatosUsuario(List<PublicationsModel> publicaciones) {
        // ✅ Batch processing para reducir llamadas al servicio
        Map<Long, String[]> datosUsuarios = obtenerDatosUsuariosBatch(
                publicaciones.stream()
                        .map(PublicationsModel::getIdUsuario)
                        .distinct()
                        .collect(Collectors.toList())
        );

        return publicaciones.stream()
                .map(this::mapToDTO)
                .map(dto -> {
                    String[] datos = datosUsuarios.get(dto.getIdUsuario());
                    if (datos != null) {
                        dto.setNombreAutor(datos[0]);
                        dto.setFotoPerfil(datos[1]);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Map<Long, String[]> obtenerDatosUsuariosBatch(List<Long> idsUsuarios) {
        Map<Long, String[]> resultados = new HashMap<>();
        for (Long id : idsUsuarios) {
            resultados.put(id, usuarioService.obtenerNombrePorId(id));
        }
        return resultados;
    }

    private List<PublicationsModel> filtrarPublicacionesNoVotadas(List<PublicationsModel> publicaciones, Long idUsuario) {
        // ✅ Consulta optimizada para evitar N+1
        List<Long> idsPublicaciones = publicaciones.stream()
                .map(PublicationsModel::getIdPublicacion)
                .collect(Collectors.toList());

        Set<Long> publicacionesVotadas = voteRepository.findVotedPublicationIds(idUsuario, idsPublicaciones);

        return publicaciones.stream()
                .filter(pub -> !publicacionesVotadas.contains(pub.getIdPublicacion()))
                .collect(Collectors.toList());
    }

    private boolean actualizarSiEsDiferente(String newValue, String currentValue, Consumer<String> setter) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    // ✅ Métodos asíncronos para mejor performance
    @Async
    public void enviarNotificacionAsync(PublicacionDTO publicacion) {
        try {
            notificationsService.enviarNotificacion(
                    new NotificationsDTO(
                            "Solicitud Publicacion",
                            "Tu publicación está en espera de aprobación",
                            LocalDateTime.now(),
                            publicacion.getIdUsuario(),
                            publicacion.getId()
                    )
            );
        } catch (Exception e) {
            // Log the error but don't fail the main operation
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }

    @Async
    public void actualizarPuntosUsuarioAsync(Long idUsuario, int puntos) {
        try {
            usuarioService.actualizarUsuarioRemoto(
                    idUsuario,
                    UpdateUserDTO.builder()
                            .puntosTotales(puntos)
                            .build()
            );
        } catch (Exception e) {
            System.err.println("Error actualizando puntos: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionDTO> listarTodasPaginadas(String estado, Long idUsuarioActual, Pageable pageable) {
        Page<PublicationsModel> publicacionesPage = repo.findByEstadoPaginado(estado, pageable);

        if (idUsuarioActual != null) {
            // Filtrar publicaciones ya votadas por el usuario
            List<PublicationsModel> publicacionesFiltradas = filtrarPublicacionesNoVotadas(
                    publicacionesPage.getContent(), idUsuarioActual);

            // Crear una nueva página con los resultados filtrados
            return new PageImpl<>(
                    mapearConDatosUsuario(publicacionesFiltradas),
                    pageable,
                    publicacionesFiltradas.size()
            );
        }

        return publicacionesPage.map(this::mapToDTO).map(this::injectarDatosUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionDTO> listarPorUsuarioPaginado(Long idUsuario, Pageable pageable) {
        Page<PublicationsModel> publicacionesPage = repo.findByIdUsuarioPaginado(idUsuario, pageable);
        return publicacionesPage.map(this::mapToDTO).map(this::injectarDatosUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionDTO> listarPorUsuarioYEstadoPaginado(Long idUsuario, String estado, Pageable pageable) {
        Page<PublicationsModel> publicacionesPage = repo.findByUsuarioAndEstadoPaginado(idUsuario, estado, pageable);
        return publicacionesPage.map(this::mapToDTO).map(this::injectarDatosUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDTO> listarPorUsuarioYEstado(Long idUsuario, String estado) {
        List<PublicationsModel> publicaciones = repo.findByUsuarioAndEstado(idUsuario, estado);
        return mapearConDatosUsuario(publicaciones);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasUsuario(Long idUsuario) {
        Map<String, Object> estadisticas = new HashMap<>();

        estadisticas.put("totalPublicaciones", repo.countByUsuario(idUsuario));
        estadisticas.put("publicacionesAprobadas", repo.countAprobadasByUsuario(idUsuario));
        estadisticas.put("publicacionesPendientes", repo.countPendientesByUsuario(idUsuario));
        estadisticas.put("publicacionesRechazadas", repo.countRechazadasByUsuario(idUsuario));

        return estadisticas;
    }
}


