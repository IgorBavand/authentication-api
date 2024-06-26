package com.igorbavand.authenticationapi.application.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
