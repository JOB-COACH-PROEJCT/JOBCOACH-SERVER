package org.v1.job_coach.dto.chat;

import org.v1.job_coach.entity.chat.ChatRoom;
import org.v1.job_coach.entity.chat.ChatRoomStatus;

public record ChatRoomResponseDto(
        Long id,
        String roomName,
        ChatRoomStatus status
) {
    public static ChatRoomResponseDto toDto(ChatRoom chatroom) {
        return new ChatRoomResponseDto(chatroom.getId(), chatroom.getRoomName(), chatroom.getStatus());
    }
}
