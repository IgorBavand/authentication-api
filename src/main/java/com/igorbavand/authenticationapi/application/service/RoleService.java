package com.igorbavand.authenticationapi.application.service;

import com.igorbavand.authenticationapi.domain.Role;
import com.igorbavand.authenticationapi.shared.exception.exception.NotFoundException;
import com.igorbavand.authenticationapi.infrastructure.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository roleRepository) {
        this.repository = roleRepository;
    }

    public Role findByName(String name) {
        return repository.findByName(name).orElseThrow(
            () -> new NotFoundException("Permissão não exitente.")
        );
    }

}
