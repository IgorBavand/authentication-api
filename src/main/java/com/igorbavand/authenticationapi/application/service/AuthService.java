package com.igorbavand.authenticationapi.application.service;

import com.igorbavand.authenticationapi.application.dto.AuthenticationRequest;
import com.igorbavand.authenticationapi.application.dto.AuthenticationResponse;
import com.igorbavand.authenticationapi.application.dto.TokenRefreshRequest;
import com.igorbavand.authenticationapi.application.dto.TokenRefreshResponse;
import com.igorbavand.authenticationapi.domain.RefreshToken;
import com.igorbavand.authenticationapi.domain.User;
import com.igorbavand.authenticationapi.infrastructure.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserService userService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    public ResponseEntity<?> getToken(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        User user = userService.findByUsername(authenticationRequest.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, refreshToken.getToken()));
    }

    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

}
