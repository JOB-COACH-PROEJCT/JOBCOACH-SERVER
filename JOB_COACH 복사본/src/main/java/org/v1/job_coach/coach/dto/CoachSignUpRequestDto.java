package org.v1.job_coach.coach.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public record CoachSignUpRequestDto(
        @Schema(description = "Coach 대학", defaultValue = "Kangnam University")
        String university,
        @Schema(description = "Coach 경력", defaultValue = "밥 많이먹기 경력 있습니다.")
        String career,
        @Schema(description = "Coach 소개", defaultValue = "안뇽하세요 저 돈벌고싶습니다.")
        String introduction,
        @Schema(description = "Coach 분야", defaultValue = "SOFTWARE_ENGINEERING")
        String expertise,
        @Schema(description = "Coach 코칭 가능 시간대", defaultValue = "WEEKEND_MORNING")
        String availableTimes
) {
}
