package com.igorbavand.authenticationapi.infra.repository;

import com.igorbavand.authenticationapi.domain.RefreshToken;
import com.igorbavand.authenticationapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}