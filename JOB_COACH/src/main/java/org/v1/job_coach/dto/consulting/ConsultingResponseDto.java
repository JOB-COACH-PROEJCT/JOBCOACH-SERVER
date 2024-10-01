package org.v1.job_coach.dto.consulting;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import org.v1.job_coach.entity.chat.Answer;
import org.v1.job_coach.entity.consulting.Consulting;
import org.v1.job_coach.entity.chat.Question;

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
