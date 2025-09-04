package com.example.demo.Service.NotificationsService;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.Repository.NotificationRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.models.NotificacionesModel;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacionService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationsDTO crear(NotificationsDTO dto) {
        System.out.println(dto.getTipo() + " " + dto.getMensaje());

        // Buscar el usuario por ID
        UserModel usuario = userRepository.findById(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuario()));

        NotificacionesModel notification = NotificacionesModel.builder()
                .tipo(dto.getTipo())
                .mensaje(dto.getMensaje())
                .fechaEnvio(dto.getFechaEnvio())
                .leido(false)
                .idPublicacion(dto.getIdPublicacion())
                .usuario(usuario) // ← ahora sí es un UserModel
                .build();

        NotificacionesModel guardada = notificationRepository.save(notification);

        return new NotificationsDTO(
                guardada.getIdNotificacion(),
                guardada.getTipo(),
                guardada.getMensaje(),
                guardada.getFechaEnvio(),
                guardada.getUsuario().getIdUsuario(), // ← para regresar el ID
                guardada.isLeido(),
                guardada.getIdPublicacion()
        );
    }

    public List<NotificationsDTO> obtenerNotificacionesPorUsuario(Long idUsuario) {
        if (!userRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        List<NotificacionesModel> notificaciones = notificationRepository.findByUsuarioIdUsuario(idUsuario);

        // Convertir a DTOs
        return notificaciones.stream()
                .map(n -> new NotificationsDTO(
                        n.getIdNotificacion(),
                        n.getTipo(),
                        n.getMensaje(),
                        n.getFechaEnvio(),
                        n.getUsuario().getIdUsuario(),
                        n.isLeido(),
                        n.getIdPublicacion()
                ))
                .collect(Collectors.toList());
    }

    public void marcarComoLeida(Long idNotificacion) {
        NotificacionesModel notificacion = notificationRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setLeido(true);
        notificationRepository.save(notificacion);
    }





}
