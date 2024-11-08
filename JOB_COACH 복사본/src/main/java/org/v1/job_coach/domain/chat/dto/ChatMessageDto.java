package org.v1.job_coach.domain.chat.dto;

import org.v1.job_coach.domain.chat.domain.ChatMessage;
import org.v1.job_coach.global.error.CustomException;
import org.v1.job_coach.global.error.Error;
import org.v1.job_coach.user.domain.User;

public record ChatMessageDto(
        String roomId,
        String authorId,
        String message
) {
    /* Dto -> Entity */
    public ChatMessage toEntity(String authorName, String profileImageUrl) {
        return new ChatMessage(roomId(), authorId(), authorName, profileImageUrl, message());
    }
}
