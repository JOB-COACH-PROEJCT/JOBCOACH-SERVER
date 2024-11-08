package org.v1.job_coach.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInRequestDto(
        @Schema(description = "로그인 email", defaultValue = "user@naver.com")
        String email,
        @Schema(description = "로그인 password", defaultValue = "user")
        String password
) {
}
