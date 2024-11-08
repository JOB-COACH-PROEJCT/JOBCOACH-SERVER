package org.v1.job_coach.domain.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.v1.job_coach.user.domain.User;
@Schema(description = "유저 정보 업데이트 DTO")
public record UserUpdateResponseDto(
        @Schema(description = "회원 정보 수정 성공 여부", example = "true")
        boolean result,
        @Schema(description = "회원 이름", example = "서이름")
        String name,
        @Schema(description = "회원 이메일", example = "ggowater112@naver.com")
        String email,
        @Schema(description = "회원 전화번호", example = "01053636177")
        String phoneNumber,
        @Schema(description = "회원 프로필 Url", example = "https://i.pinimg.com/222x/9e/06/9a/9e069a636dd2bb4eef3290a546648379.jpg")
        String profileUrl
) {
        public static UserUpdateResponseDto toDto(User user, boolean isUpdate) {
                return new UserUpdateResponseDto(
                        isUpdate,
                        user.getName(),
                        user.getEmail(),
                        user.getNumber(),
                        user.getProfile());
        }
}

