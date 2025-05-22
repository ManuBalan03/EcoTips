package com.example.demo.Repository;
import com.example.demo.models.PublicationsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<PublicationsModel, Long> {
    List<PublicationsModel> findByIdUsuario(Long idUsuario);
    List<PublicationsModel> findAllByOrderByFechaCreacionDesc();

}
