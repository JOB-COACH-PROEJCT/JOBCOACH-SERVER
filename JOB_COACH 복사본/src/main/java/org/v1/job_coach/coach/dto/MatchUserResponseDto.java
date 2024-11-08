package org.v1.job_coach.coach.dto;

import org.v1.job_coach.coach.domain.*;
import org.v1.job_coach.global.util.DateFormatter;

public record MatchUserResponseDto(
        Long matchingId,
        Long coachId,
        String coachName,
        String profile,
        String requestAt,
        MatchingStatus status,
        Expertise expertise,
        AvailableTimes availableTimes
) {

    public static MatchUserResponseDto toDto(Matching matching) {
        Coach coach = matching.getCoach();
        return new MatchUserResponseDto(
                matching.getId(),
                coach.getPid(),
                coach.getName(),
                coach.getProfile(),
                DateFormatter.getDateNow(matching.getRequestedAt()),
                matching.getStatus(),
                coach.getExpertise(),
                coach.getAvailableTimes()
        );
    }
}
