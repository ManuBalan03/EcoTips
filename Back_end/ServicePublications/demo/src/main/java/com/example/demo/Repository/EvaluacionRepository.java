package com.example.demo.Repository;

import com.example.demo.models.ComentariosModel;
import com.example.demo.models.EvaluacionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluacionRepository extends JpaRepository<EvaluacionModel, Long> {
}
