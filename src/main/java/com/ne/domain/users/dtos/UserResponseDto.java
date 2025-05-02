package com.ne.domain.users.dtos;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
