package com.example.demo.Repository;

import com.example.demo.models.NotificaionesModel;
import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificaionesModel, Long> {


}
