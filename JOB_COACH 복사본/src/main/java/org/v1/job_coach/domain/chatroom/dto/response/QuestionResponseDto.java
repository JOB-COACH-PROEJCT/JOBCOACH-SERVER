package org.v1.job_coach.domain.chatroom.dto.response;

import org.v1.job_coach.domain.chatroom.domain.Question;

public record QuestionResponseDto(
        Long id,
        String content
) {
    public static QuestionResponseDto toDto(Question question) {
        return new QuestionResponseDto(
                question.getId(),
                question.getContent()
        );
    }
}