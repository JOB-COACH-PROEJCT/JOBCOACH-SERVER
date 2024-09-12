package org.v1.job_coach.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.v1.job_coach.entity.User;

public record UserUpdateResponse(
        @Schema(description = "회원 정보 수정 성공 여부", example = "true")
        boolean result,
        @Schema(description = "회원 비밀번호", example = "1234")
        String password,
        @Schema(description = "회원 이메일", example = "ggowater112@naver.com")
        String email
) {
    public static UserUpdateResponse toDto(boolean result, User user) {
        return new UserUpdateResponse(result, user.getPassword(), user.getEmail());
    }
}

