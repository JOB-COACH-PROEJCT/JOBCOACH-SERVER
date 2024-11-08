package org.v1.job_coach.domain.answer.dto.response;

import org.v1.job_coach.domain.answer.domain.Answer;

public record AnswerResponseDto(
        Long id,
        String content,
        String userName
) {
    public static AnswerResponseDto toDto(Answer answer) {
        return new AnswerResponseDto(
                answer.getId(),
                answer.getContent(),
                answer.getUser().getUsername()
        );
    }
}