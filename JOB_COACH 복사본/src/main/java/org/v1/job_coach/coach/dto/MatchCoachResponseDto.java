package org.v1.job_coach.coach.dto;

import org.v1.job_coach.coach.domain.*;
import org.v1.job_coach.global.util.DateFormatter;
import org.v1.job_coach.user.domain.User;

public record MatchCoachResponseDto(
        Long matchingId,
        Long userId,
        String userName,
        String profile,
        String requestAt,
        MatchingStatus status
) {
    public static MatchCoachResponseDto toDto(Matching matching){
        User user = matching.getUser();
        return new MatchCoachResponseDto(
                matching.getId(),
                user.getPid(),
                user.getName(),
                user.getProfile(),
                DateFormatter.getDateNow(matching.getRequestedAt()),
                matching.getStatus()
        );
    }
}
