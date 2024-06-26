package com.igorbavand.authenticationapi.application.controller;

import com.igorbavand.authenticationapi.application.dto.AuthenticationRequest;
import com.igorbavand.authenticationapi.application.dto.TokenRefreshRequest;
import com.igorbavand.authenticationapi.domain.User;
import com.igorbavand.authenticationapi.application.service.AuthService;
import com.igorbavand.authenticationapi.application.service.RefreshTokenService;
import com.igorbavand.authenticationapi.application.service.UserService;
import com.igorbavand.authenticationapi.infrastructure.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          UserService userService,
                          RefreshTokenService refreshTokenService,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return authService.getToken(authenticationRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<?> assignRoleToUser(@RequestParam String username, @RequestParam String roleName) {
        User user = userService.assignRoleToUser(username, roleName);
        return ResponseEntity.ok(user);
    }
}
