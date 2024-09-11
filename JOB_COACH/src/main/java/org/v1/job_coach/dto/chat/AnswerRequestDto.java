package org.v1.job_coach.dto.chat;

import lombok.Getter;
import org.v1.job_coach.entity.chat.Answer;

public record AnswerRequestDto(
        Long questionId,
        String answerContent
) {
}
