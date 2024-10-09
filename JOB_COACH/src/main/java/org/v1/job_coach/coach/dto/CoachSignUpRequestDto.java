package org.v1.job_coach.coach.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public record CoachSignUpRequestDto(
        String university,
        String career,
        String introduction,
        String expertise,
        String availableTimes
) {
}
