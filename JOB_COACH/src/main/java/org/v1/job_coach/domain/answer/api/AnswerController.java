package org.v1.job_coach.domain.answer.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.v1.job_coach.domain.answer.dto.response.AnswerResponseDto;
import org.v1.job_coach.domain.answer.application.AnswerService;

import java.util.List;

@Slf4j
@Tag(name = "Answer", description = "답변 스크랩 API")
@RestController
@RequestMapping("api/v1/answers")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getAnswers(
            @PathVariable Long questionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<AnswerResponseDto> answers = answerService.getAnswers(questionId, page);
        return ResponseEntity.ok(answers);
    }
}
