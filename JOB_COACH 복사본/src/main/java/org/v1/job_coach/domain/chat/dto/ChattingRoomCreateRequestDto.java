package org.v1.job_coach.domain.chat.dto;

public record ChattingRoomCreateRequestDto(
        Long userId,
        Long coachId
) {
}
