package org.v1.job_coach.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignUpRequestDto(
        @Schema(description = "사용자 이름", defaultValue = "서해윤")
        String name,
        @Schema(description = "사용자 이메일", defaultValue = "user@naver.com")
        String email,
        @Schema(description = "사용자 전화번호", defaultValue = "010-5363-6177")
        String number,
        @Schema(description = "사용자 비밀번호", defaultValue = "user")
        String password,
        @Schema(description = "사용자 프로필", defaultValue = "https://i.namu.wiki/i/c1GTTKMxSQJhdu1ro8bu9KxQqe6csuMTxAA_V-TkxKS2D6CPzXFHXG8pG9PnAYeLFPOT-1vFSVDWmcEuT2fYTw.webp")
        String profile,
        @Schema(description = "사용자 권한", defaultValue = "USER", allowableValues = {"ROLE_USER", "ROLE_COACH"})
        String roles
){
}