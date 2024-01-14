package com.igorbavand.authenticationapi.api.dto.user;

import com.igorbavand.authenticationapi.domain.enums.EUserRole;

import java.time.LocalDateTime;

public record UserResponseDto(String id, String login, EUserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) { }
