package pe.edu.upeu.msauth.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.msauth.entity.AuthUser;

import java.util.Optional;


public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByUserName(String username);
}
