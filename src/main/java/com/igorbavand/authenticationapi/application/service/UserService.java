package com.igorbavand.authenticationapi.application.service;

import com.igorbavand.authenticationapi.domain.Role;
import com.igorbavand.authenticationapi.domain.User;
import com.igorbavand.authenticationapi.shared.exception.exception.NotFoundException;
import com.igorbavand.authenticationapi.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User assignRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleService.findByName(roleName);

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("Usuário não encontrado.")
        );
    }
}

