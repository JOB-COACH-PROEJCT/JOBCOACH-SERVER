package org.v1.job_coach.domain.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "유저 정보 업데이트 DTO")
public record UserUpdateRequestDto(
        @Schema(description = "회원 비밀번호", defaultValue = "user")
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password,
        @Schema(description = "회원 새 비밀번호", defaultValue = "1234")
        @NotBlank(message = "새 비밀번호는 필수 입력 사항입니다.")
        String updatePassword,
        @Schema(description = "회원 전화번호", defaultValue = "01055556177")
        @NotBlank(message = "전화번호를 입력해주세요.")
        String phoneNumber,
        @Schema(description = "회원 프로필 사진 url", defaultValue = "https://i.pinimg.com/222x/9e/06/9a/9e069a636dd2bb4eef3290a546648379.jpg")
        String profileUrl
) {
}
