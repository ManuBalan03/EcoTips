package com.example.demo.Repository;

import com.example.demo.models.NotificacionesModel;
import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificacionesModel, Long> {

    List<NotificacionesModel> findByUsuarioIdUsuario(Long idUsuario);


}
