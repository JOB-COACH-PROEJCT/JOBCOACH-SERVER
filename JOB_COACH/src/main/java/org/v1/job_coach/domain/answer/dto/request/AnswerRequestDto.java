package org.v1.job_coach.domain.answer.dto.request;

public record AnswerRequestDto(
        Long questionId,
        String answerContent
) {
}
