package org.v1.job_coach.user.dto.request;

public record SignInRequestDto(
        String email,
        String password
) {
}
