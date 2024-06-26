package com.igorbavand.authenticationapi.application.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
    private final String refreshToken;

    public AuthenticationResponse(String jwt, String refreshToken) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }
}
