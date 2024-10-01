package org.v1.job_coach.dto.chat;

import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.chat.Question;

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