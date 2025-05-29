package com.example.demo.Repository;

import com.example.demo.models.ComentariosModel;
import com.example.demo.models.PublicationsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentariosRepository extends JpaRepository<ComentariosModel, Long> {
    List<ComentariosModel> findAllByOrderByFechaCreacionDesc();
    List<ComentariosModel> findByPublicacionIdPublicacion(Long idPublicacion);
}
