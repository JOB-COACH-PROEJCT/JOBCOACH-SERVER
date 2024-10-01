package org.v1.job_coach.domain.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "유저 회원탈퇴 DTO")
public record UserDeleteResponseDto(
        @Schema(description = "회원 삭제 성공 여부", example = "true")
        boolean result
) {
        public static UserDeleteResponseDto toDto() {
                return new UserDeleteResponseDto(true);

        }
}