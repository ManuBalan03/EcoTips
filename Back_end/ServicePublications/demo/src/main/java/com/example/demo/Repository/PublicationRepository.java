package com.example.demo.Repository;
import com.example.demo.models.PublicationsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicationRepository extends JpaRepository<PublicationsModel, Long> {
    List<PublicationsModel> findByIdUsuario(Long idUsuario);
    List<PublicationsModel> findAllByOrderByFechaCreacionDesc();
    List<PublicationsModel> findByEstadoOrderByFechaCreacionDesc(String estado);
    // Nuevo método para obtener solo el idUsuario por id de publicación
    @Query("SELECT p.idUsuario FROM PublicationsModel p WHERE p.id = :publicationId")
    Long findUserIdByPublicationId(@Param("publicationId") Long publicationId);

}
