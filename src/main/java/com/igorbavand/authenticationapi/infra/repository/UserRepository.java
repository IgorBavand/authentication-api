package com.igorbavand.authenticationapi.infra.repository;

import com.igorbavand.authenticationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLoginIgnoreCase(String login);
}
