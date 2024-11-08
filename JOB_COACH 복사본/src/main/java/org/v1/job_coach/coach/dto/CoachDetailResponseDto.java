package org.v1.job_coach.coach.dto;

import org.v1.job_coach.coach.domain.AvailableTimes;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.domain.Expertise;

public record CoachDetailResponseDto(
        Long id,
        String name,
        String profile,
        String introduction,
        Expertise expertise,
        AvailableTimes availableTimes
) {
    public static org.v1.job_coach.coach.dto.CoachDetailResponseDto toDto(Coach coach){
        return new org.v1.job_coach.coach.dto.CoachDetailResponseDto(
                coach.getPid(),
                coach.getName(),
                coach.getProfile(),
                coach.getIntroduction(),
                coach.getExpertise(),
                coach.getAvailableTimes()
        );
    }

}
