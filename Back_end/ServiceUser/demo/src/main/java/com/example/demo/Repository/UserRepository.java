package com.example.demo.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.UserModel;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long>{
    Optional<UserModel> findByEmail(String email);

    boolean existsByEmail(String email);
}
