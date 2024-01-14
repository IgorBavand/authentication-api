package com.igorbavand.authenticationapi.api.dto.user;

import com.igorbavand.authenticationapi.domain.enums.EUserRole;

public record RegisterDto(String login, String password, EUserRole userRole) { }
