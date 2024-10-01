package org.v1.job_coach.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ReviewRequestDto(
        @NotBlank(message = "제목은 필수 항목입니다.")
        @Size(min = 2, max = 20, message = "2자 이상, 20자 이하로 입력해주세요.")
        String title,
        @NotBlank(message = "기업명은 필수 항목입니다.")
        String companyName,
        @NotNull(message = "평가는 필수 항목입니다.")
        String evaluation,
        @NotNull(message = "결과는 필수 항목입니다.")
        String result,
        @NotBlank(message = "내용을 입력해주세요.")
        String process,
        @NotEmpty(message = "면접 질문을 하나 이상 입력해주세요.")
        List<String> interviewQuestions,
        @NotBlank(message = "내용을 입력해주세요.")
        String tips
) {
}

