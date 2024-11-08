package org.v1.job_coach.domain.chat.dto;

public record ChattingRoomCreateResponseDto(
        Long userId,
        Long coachId,
        String chatRoomId
){
    public ChattingRoomCreateResponseDto toDto(Long userId, Long coachId, String chatRoomId) {
        return new ChattingRoomCreateResponseDto(userId, coachId, chatRoomId);
    }
}
