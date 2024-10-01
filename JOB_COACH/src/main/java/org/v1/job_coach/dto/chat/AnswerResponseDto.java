package org.v1.job_coach.dto.chat;

import org.v1.job_coach.entity.chat.Answer;

import java.time.LocalDateTime;

public record AnswerResponseDto(
        Long id,
        String content,
        Long userId
) {
    public static AnswerResponseDto toDto(Answer answer) {
        return new AnswerResponseDto(
                answer.getId(),
                answer.getContent(),
                answer.getUser().getPid()
        );
    }
}