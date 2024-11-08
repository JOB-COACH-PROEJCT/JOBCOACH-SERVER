package org.v1.job_coach.domain.answer.application;

import org.v1.job_coach.domain.answer.dto.response.AnswerResponseDto;
import org.v1.job_coach.global.dto.response.ResultResponseDto;

import java.util.List;

public interface AnswerService {
    ResultResponseDto<List<AnswerResponseDto>> getAnswers(Long questionId, int page);
}
