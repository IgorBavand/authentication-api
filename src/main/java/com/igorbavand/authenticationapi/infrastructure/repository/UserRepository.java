package com.igorbavand.authenticationapi.infrastructure.repository;

import com.igorbavand.authenticationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
