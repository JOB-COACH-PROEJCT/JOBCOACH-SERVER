package org.v1.job_coach.coach.dto;

import org.v1.job_coach.coach.domain.AvailableTimes;
import org.v1.job_coach.coach.domain.Coach;
import org.v1.job_coach.coach.domain.Expertise;

public record CoachResponseDto(
        Long id,
        String name,
        String profile,
        String introduction,
        Expertise expertise,
        AvailableTimes availableTimes
) {

    public static CoachResponseDto toDto(Coach coach){
        return new CoachResponseDto(
                coach.getPid(),
                coach.getName(),
                coach.getProfile(),
                coach.getIntroduction(),
                coach.getExpertise(),
                coach.getAvailableTimes()
        );
    }
}
