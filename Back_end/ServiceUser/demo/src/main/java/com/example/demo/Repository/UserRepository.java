package com.example.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long>{
    Optional<UserModel> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM UserModel u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // Alternativa más eficiente para solo verificar existencia
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserModel u WHERE u.email = :email")
    boolean existsByEmailOptimized(@Param("email") String email);
}

