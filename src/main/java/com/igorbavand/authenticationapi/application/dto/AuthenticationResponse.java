package com.igorbavand.authenticationapi.application.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String accessToken;
    private final String refreshToken;
    private final UserResponse user;

    public AuthenticationResponse(String jwt, String refreshToken, UserResponse user) {
        this.accessToken = jwt;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}
