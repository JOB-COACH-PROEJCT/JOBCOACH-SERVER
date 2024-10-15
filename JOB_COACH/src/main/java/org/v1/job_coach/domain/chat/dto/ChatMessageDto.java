package org.v1.job_coach.domain.chat.dto;

import org.v1.job_coach.domain.chat.domain.ChatMessage;

public record ChatMessageDto(
        String roomId,
        String authorId,
        String message
) {
    /* Dto -> Entity */
    public ChatMessage toEntity() {
        return new ChatMessage(roomId(), authorId(), message());
    }
}
