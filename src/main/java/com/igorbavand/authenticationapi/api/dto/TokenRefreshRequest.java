package com.igorbavand.authenticationapi.api.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
