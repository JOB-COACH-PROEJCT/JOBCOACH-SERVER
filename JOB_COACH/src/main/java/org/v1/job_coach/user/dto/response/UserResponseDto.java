package org.v1.job_coach.user.dto.response;

import org.v1.job_coach.user.domain.User;

public record UserResponseDto(
        Long id,
        String username,
        String email
) {
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getPid(), user.getName(), user.getEmail());
    }
}
