package org.v1.job_coach.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public record UserInfoResponse(
        @Schema(description = "회원 이메일", example = "colabear754")
        String email,
        @Schema(description = "회원 이름", example = "콜라곰")
        String name,
        @Schema(description = "회원 타입", example = "USER")
        String role,
        @Schema(description = "회원 생성일", example = "2023-05-11T15:00:00")
        Timestamp createdAt
) {
/*    public static UserInfoResponse toDto(User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getCreated_at()
        );
    }*/
}
