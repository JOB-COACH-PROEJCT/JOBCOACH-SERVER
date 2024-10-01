package org.v1.job_coach.dto.user;

import org.v1.job_coach.entity.User;

public record UserResponseDto(
        Long id,
        String username,
        String email
) {
    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getPid(), user.getUsername(), user.getEmail());
    }
}
