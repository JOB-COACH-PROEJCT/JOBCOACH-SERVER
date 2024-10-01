package org.v1.job_coach.domain.answer.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.v1.job_coach.domain.answer.dto.response.AnswerResponseDto;
import org.v1.job_coach.domain.answer.domain.Answer;
import org.v1.job_coach.domain.answer.dao.AnswerRepository;
import org.v1.job_coach.domain.answer.application.AnswerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<AnswerResponseDto> getAnswers(Long questionId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Answer> answers = answerRepository.findByQuestionId(questionId, pageable);

        return answers.stream()
                .map(AnswerResponseDto::toDto)
                .collect(Collectors.toList());
    }
}