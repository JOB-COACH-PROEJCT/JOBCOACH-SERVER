package org.v1.job_coach.domain.consulting.dto.response;

import jakarta.persistence.Column;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.domain.chatroom.domain.Question;

public record ConsultingResponseDto(
        String question,
        String answer,
        @Column(nullable = false, columnDefinition = "TEXT")
        String feedback
) {
    public static ConsultingResponseDto toDto(Question question, Answer answer, Consulting consulting) {
        return new ConsultingResponseDto(
                question.getContent(),
                answer.getContent(),
                consulting.getFeedback()
        );
    }
}
