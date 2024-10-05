package org.v1.job_coach.domain.answer.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.v1.job_coach.domain.answer.dto.response.AnswerResponseDto;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.answer.dao.AnswerRepository;
import org.v1.job_coach.domain.answer.application.AnswerService;
import org.v1.job_coach.global.dto.response.ResultResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public ResultResponseDto<List<AnswerResponseDto>> getAnswers(Long questionId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Answer> answers = answerRepository.findByQuestionId(questionId, pageable);

        List<AnswerResponseDto> collect = answers.stream()
                .map(AnswerResponseDto::toDto)
                .toList();

        return ResultResponseDto.toDataResponseDto(200, "사용자들의 답변을 모두 반환합니다.", collect);

    }
}
