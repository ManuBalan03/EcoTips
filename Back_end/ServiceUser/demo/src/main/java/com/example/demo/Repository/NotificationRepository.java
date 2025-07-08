package com.example.demo.Repository;

import com.example.demo.models.NotificaionesModel;
import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificaionesModel, Long> {

    List<NotificaionesModel> findByUsuarioIdUsuario(Long idUsuario);


}
