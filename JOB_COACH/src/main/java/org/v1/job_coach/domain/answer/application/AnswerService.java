package org.v1.job_coach.domain.answer.application;

import org.v1.job_coach.domain.answer.dto.response.AnswerResponseDto;

import java.util.List;

public interface AnswerService {
    List<AnswerResponseDto> getAnswers(Long questionId, int page);
}
