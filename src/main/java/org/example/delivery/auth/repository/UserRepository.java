package org.example.delivery.auth.repository;

import java.util.Optional;
import org.example.delivery.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  Optional<User> findUsersByEmail(String email);
}
