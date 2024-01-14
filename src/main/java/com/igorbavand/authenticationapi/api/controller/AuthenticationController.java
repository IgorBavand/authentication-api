package com.igorbavand.authenticationapi.api.controller;

import com.igorbavand.authenticationapi.api.dto.authentication.AuthenticationDto;
import com.igorbavand.authenticationapi.api.dto.authentication.TokenResponseDto;
import com.igorbavand.authenticationapi.api.dto.user.RegisterDto;
import com.igorbavand.authenticationapi.domain.User;
import com.igorbavand.authenticationapi.domain.service.TokenService;
import com.igorbavand.authenticationapi.domain.service.authentication.AuthenticationService;
import com.igorbavand.authenticationapi.domain.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthenticationService service;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto authenticationDto) throws ChangeSetPersister.NotFoundException {

        userService.findByLogin(authenticationDto.login());
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDto.login(), authenticationDto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDto(token));
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @GetMapping("check-user")
    public ResponseEntity checkUser() {
        return ResponseEntity.ok().body(service.getAuthenticatedUser());
    }
}
