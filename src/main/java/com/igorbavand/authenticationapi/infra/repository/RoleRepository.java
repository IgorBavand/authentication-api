package com.igorbavand.authenticationapi.infra.repository;

import com.igorbavand.authenticationapi.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}