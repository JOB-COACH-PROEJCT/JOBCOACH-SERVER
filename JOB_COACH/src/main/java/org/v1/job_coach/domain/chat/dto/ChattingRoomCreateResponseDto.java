package org.v1.job_coach.domain.chat.dto;

public record ChattingRoomCreateResponseDto(
        Long roomMakerId,
        Long guestId,
        String chatRoomId
){
    public ChattingRoomCreateResponseDto toDto(Long roomMakerId, Long guestId, String chatRoomId) {
        return new ChattingRoomCreateResponseDto(roomMakerId, guestId, chatRoomId);
    }
}
