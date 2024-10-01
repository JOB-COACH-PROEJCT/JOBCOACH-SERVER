package org.v1.job_coach.service.interview;

import org.v1.job_coach.dto.chat.AnswerResponseDto;

import java.util.List;

public interface AnswerService {
    List<AnswerResponseDto> getAnswers(Long questionId, int page);
}
