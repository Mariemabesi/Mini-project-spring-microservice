package tn.gl.authservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.gl.authservice.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
