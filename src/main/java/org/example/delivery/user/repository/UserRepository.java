package org.example.delivery.user.repository;

import java.util.List;
import java.util.Optional;
import org.example.delivery.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  default User findByEmailOrElesThrow(String email) {
    return findByEmail(email).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  boolean existsByEmail(String email);
}
