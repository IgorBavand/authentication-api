package com.igorbavand.authenticationapi.domain.service.user;

import com.igorbavand.authenticationapi.api.dto.user.RegisterDto;
import com.igorbavand.authenticationapi.domain.User;
import com.igorbavand.authenticationapi.domain.exception.exception.BadRequestException;
import com.igorbavand.authenticationapi.domain.exception.exception.NotFoundException;
import com.igorbavand.authenticationapi.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public ResponseEntity register(RegisterDto registerDto) {
        validateExistingUser(registerDto.login());
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.password());
        User newUser = new User(registerDto.login(), encryptedPassword, registerDto.userRole());
        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }

    public User findByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new NotFoundException("User not found."));
    }

    private void validateExistingUser(String login) {
        if (this.repository.findByLogin(login).isPresent()) {
            throw new BadRequestException("User already registred.");
        }
    }
}
