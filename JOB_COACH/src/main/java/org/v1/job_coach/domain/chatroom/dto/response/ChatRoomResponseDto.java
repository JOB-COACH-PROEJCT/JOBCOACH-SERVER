package org.v1.job_coach.domain.chatroom.dto.response;

import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.chatroom.domain.ChatRoomStatus;

public record ChatRoomResponseDto(
        Long id,
        String roomName,
        ChatRoomStatus status
) {
    public static ChatRoomResponseDto toDto(ChatRoom chatroom) {
        return new ChatRoomResponseDto(chatroom.getId(), chatroom.getRoomName(), chatroom.getStatus());
    }
}
